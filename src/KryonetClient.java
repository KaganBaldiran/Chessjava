import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class KryonetClient extends Client {

    Boolean STATEINFORM = false;
    Math.Vec2<Integer> OtherPlayerMove = new Math.Vec2<>(0,0);


   KryonetClient(int timeout, String host, int tcpPort, int udpPort) throws IOException {
       super();
       start();
       connect(timeout,host,tcpPort,udpPort);

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

                       System.out.println("OtherPlayerMove> " + OtherPlayerMove.x +" "+ OtherPlayerMove.y);
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
            for(piece piece : CurrentPlayer.pieces)
            {
                if(piece.LastPlayedMove.x != 0 && piece.LastPlayedMove.y != 0)
                {
                    sendTCP("MOVE "+ String.valueOf(piece.LastPlayedMove.x) +" "+ String.valueOf(piece.LastPlayedMove.y) + " " + piece.GetPieceType());
                    piece.LastPlayedMove.SetValues(0,0);
                }

            }
        }


    }

    @Override
    public void run() {

        super.run();

    }
}
