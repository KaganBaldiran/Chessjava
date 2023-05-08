import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class KryonetServer extends Server {

    KryonetServer(int tcp_port , int udp_port) throws IOException {
        super();
        start();
        bind(tcp_port, udp_port);
        //54555, 54777
        addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof String message) {
                    System.out.println("Received message from client: " + message);
                    connection.sendTCP("Server received message: " + message);
                }
            }
        });

    }

    @Override
    public void run() {
        super.run();




    }
}
