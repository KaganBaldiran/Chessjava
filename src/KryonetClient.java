import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import javax.swing.*;
import java.io.IOException;

public class KryonetClient extends Client {

    Boolean STATEINFORM = false;
    Math.Vec2<Integer> OtherPlayerMove = new Math.Vec2<>(0,0);
    int OtherPlayerPieceIndex = 0;
    Semaphore MoveOpponentPlayer;

   KryonetClient(int timeout, String host, int tcpPort, int udpPort , Semaphore MoveOpponentPlayer) throws IOException
   {
       super();
       this.MoveOpponentPlayer = MoveOpponentPlayer;
       start();

       String ipAddress = KryonetServer.DeConstructLink(host);

       connect(timeout,ipAddress,tcpPort,udpPort);

       addListener(new Listener() {
           public void received(Connection connection, Object object) {

               if (object instanceof String) {
                   String message = (String)object;

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
                       close();
                   }

                   System.out.println("Received message from server: " + message);
               }

           }
       });

       sendTCP("Hello, server!");

   }

    public void loop(Player CurrentPlayer)
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
                    sendTCP("MOVE "+ String.valueOf(piece.LastPlayedMove.x) +" "+ String.valueOf(piece.LastPlayedMove.y) + " " + i);
                    piece.LastPlayedMove.SetValues(0,0);
                }

            }
        }


    }

    public void Disconnect()
    {
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
}
