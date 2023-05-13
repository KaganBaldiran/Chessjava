import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

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

    public void loop()
    {
        if (!STATEINFORM)
        {
            sendTCP("ONLINE");
        }

    }

    @Override
    public void run() {

        super.run();

    }
}
