import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class KryonetClient extends Client {

    Boolean STATEINFORM = false;

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
                if(piece.Selected)
                {
                    sendTCP(String.valueOf(piece.Coordinates.x) +" "+ String.valueOf(piece.Coordinates.y));
                }

            }
        }


    }

    @Override
    public void run() {

        super.run();

    }
}
