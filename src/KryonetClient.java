import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


import java.io.IOException;

public class KryonetClient extends Client {

   KryonetClient(int timeout, String host, int tcpPort, int udpPort) throws IOException {
       super();
       start();
       connect(timeout,host,tcpPort,udpPort);

       addListener(new Listener() {
           public void received(Connection connection, Object object) {
               if (object instanceof String) {
                   String message = (String)object;
                   System.out.println("Received message from server: " + message);
               }
           }
       });

       sendTCP("Hello, server!");



   }

    @Override
    public void run() {

        super.run();

       while(true)
       {
           sendTCP("Hello, server!");
       }



    }
}
