package core;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.InetAddress;

public class KryonetClient extends Client {

    Boolean STATEINFORM = false;
    Math.Vec2<Integer> OtherPlayerMove = new Math.Vec2<>(0,0);
    int OtherPlayerPieceIndex = 0;
    Semaphore MoveOpponentPlayer;
    String ConnectionState = "";

    int CapturedPieceIndex = -1;

    String externalIpAddress;

    GatewayDevice PortMappingDevice;

    Math.Vec2<Integer> Ports = new Math.Vec2<>();

   KryonetClient(int timeout, String host, int tcpPort, int udpPort , Semaphore MoveOpponentPlayer) throws IOException
   {
       super();
       this.MoveOpponentPlayer = MoveOpponentPlayer;
       start();

       String ipAddress = KryonetServer.DeConstructLink(host);

       Ports.SetValues(tcpPort , udpPort);

       connect(timeout,ipAddress,tcpPort,udpPort);
       ConnectionState = "DISCONNECTED";

       addListener(new Listener() {
           public void received(Connection connection, Object object) {

               if (object instanceof String message) {

                   if (message.equalsIgnoreCase("RECEIVED")) {

                       System.out.println("SERVER> " + message);
                       STATEINFORM = true;
                   }
                   if(message.substring(0,4).trim().equals("MOVE"))
                   {
                       OtherPlayerMove.SetValues(Integer.parseInt(String.valueOf(message.charAt(5))),
                                                 Integer.parseInt(String.valueOf(message.charAt(7))));

                       if(message.length() == 11)
                       {
                           OtherPlayerPieceIndex = Integer.parseInt(String.valueOf(message.charAt(9) + String.valueOf(message.charAt(10))));
                       }
                       else
                       {
                           OtherPlayerPieceIndex = Integer.parseInt(String.valueOf(message.charAt(9)));
                       }

                       MoveOpponentPlayer.SetMutexTrue();

                       System.out.println("OtherPlayerMove> " + OtherPlayerMove.x +" "+ OtherPlayerMove.y + " "+OtherPlayerPieceIndex);
                   }
                   if (message.equalsIgnoreCase("DISCONNECT"))
                   {
                       System.out.println("IS CONNECTED1: " + connection.isConnected());
                       JOptionPane.showMessageDialog(null,"The opponent has disconnected from the game!");
                       ConnectionState = "DISCONNECTED";
                       close();
                   }
                   if(message.equalsIgnoreCase("BOTH ONLINE"))
                   {
                       ConnectionState = "CONNECTED";
                   }
                   if(message.substring(0,8).trim().equalsIgnoreCase("CAPTURED"))
                   {
                       if(message.length() > 10)
                       {
                           CapturedPieceIndex = Integer.parseInt(String.valueOf(message.substring(message.length() - 2)));
                           System.out.println("CapturedPieceIndex STRING VALUE "+ " : " + String.valueOf(message.substring(message.length() - 2)));
                           System.out.println("CapturedPieceIndex MESSAGE "+ " : " + message);
                       }
                       else
                       {
                           CapturedPieceIndex = Integer.parseInt(String.valueOf(message.charAt(9)));
                       }
                       System.out.println("CapturedPieceIndex" + message.length() + " : " + CapturedPieceIndex);

                   }

                   System.out.println("Received message from server: " + message);
               }

           }
       });

       sendTCP("Hello, server!");

   }

    public void loop(Player CurrentPlayer , Player OpponentPlayer)
    {
        if (!STATEINFORM)
        {
            sendTCP("ONLINE");
        }
        else
        {
            for (int i = 0; i < CurrentPlayer.pieces.size(); i++)
            {
                piece piece = CurrentPlayer.pieces.get(i);

                if(piece.LastPlayedMove.x != 0 && piece.LastPlayedMove.y != 0)
                {

                    sendTCP("MOVE "+ String.valueOf(piece.LastPlayedMove.x) +" "+ String.valueOf(piece.LastPlayedMove.y) + " " + piece.PieceIndexInPlayer);
                    CurrentPlayer.SetTurnState(false);
                    OpponentPlayer.SetTurnState(true);
                    piece.LastPlayedMove.SetValues(0,0);


                    if(piece.NewTileHasEnemyPiece)
                    {
                        sendTCP("CAPTURED " + piece.NewTileOldEnemyPiece.PieceIndexInPlayer);
                        piece.NewTileHasEnemyPiece = false;
                    }

                    break;
                }

            }

            if(MoveOpponentPlayer.IsMutexTrue())
            {
                CurrentPlayer.SetTurnState(true);
                OpponentPlayer.SetTurnState(false);
            }
        }


    }

    public void Disconnect()
    {
        ConnectionState = "DISCONNECTED";
        sendTCP("DISCONNECT");
    }

    public int getOtherPlayerPieceIndex()
    {
        return OtherPlayerPieceIndex;
    }
    public Math.Vec2<Integer> getOtherPlayerMove()
    {
        return OtherPlayerMove;
    }

    @Override
    public void run() {

        super.run();

    }

    public static Math.Pair<GatewayDevice , String> PortMapping(int externalPort , int internalPort , String protocol, boolean DeleteGameEvent) throws IOException, ParserConfigurationException, SAXException {

        boolean success;
        String Error = "Requested port is already mapped!";
        String externalIpAddress = null;

        GatewayDiscover discover = new GatewayDiscover();
        discover.discover();

        GatewayDevice d = discover.getValidGateway();
        System.out.println("IS EMPTY?????: " + discover.getAllGateways().isEmpty());
        if (d == null) {
            System.err.println("No IGD found");
            System.err.println("Server cannot be initialized");
            Error = "No IGD found // Server cannot be initialized";
        }

        if(d != null) {
            System.out.println("Found IGD: " + d.getFriendlyName());

            externalIpAddress = d.getExternalIPAddress();
            System.out.println("External IP address: " + externalIpAddress);

            // Add a port mapping to the IGD
            String description = "Port mapping";

            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("LOCAL IP address: " + localAddress.getHostAddress());
            success = d.addPortMapping(externalPort, internalPort, localAddress.getHostAddress(), protocol, description);
            if (success) {
                System.out.println("Port mapping added: " + externalIpAddress + ":" + externalPort + " -> " + localAddress.getHostAddress() + ":" + internalPort);
            } else {
                System.err.println("Unable to execute Port Mapping! :: " + Error);
                DeleteGameEvent = true;
                new Thread(new GameEvent.LoadingDialog("Unable to execute Port Mapping! :: " + Error)).start();
            }
        }
        return new Math.Pair<>(d ,externalIpAddress);
    }

    public static void DeletePortMapping(GatewayDevice d , int externalPort , String protocol)
    {
        boolean success;

        try {
            success = d.deletePortMapping(externalPort, protocol);
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        if (success) {
            System.out.println("Port mapping removed");
        } else {
            System.err.println("Failed to remove port mapping");
        }

    }
}
