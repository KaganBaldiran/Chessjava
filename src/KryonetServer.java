import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.javawi.jstun.attribute.ChangeRequest;
import de.javawi.jstun.attribute.MappedAddress;
import de.javawi.jstun.attribute.MessageAttribute;
import de.javawi.jstun.attribute.MessageAttributeParsingException;
import de.javawi.jstun.header.MessageHeader;
import de.javawi.jstun.util.UtilityException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class KryonetServer extends Server {

    MappedAddress StunAddress;
    public Boolean CLIENT1_STATE = false;
    public Boolean CLIENT2_STATE = false;

    KryonetServer(int tcp_port , int udp_port) throws IOException {
        super();
        start();

        try {
            StunAddress = SendRequestToSTUNserver();
        } catch (UtilityException | MessageAttributeParsingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("STUN SERVER: "+ StunAddress.getAddress() + " " + StunAddress.getPort());

        bind(tcp_port, udp_port);

        //54555, 54777
        addListener(new Listener() {
            public void received(Connection connection, Object object) {

                if (object instanceof String message) {
                    System.out.println("Received message from client: " + message);
                    connection.sendTCP("Server received message: " + message);

                    loop(message , connection);
                }
            }
        });


    }

    public static MappedAddress SendRequestToSTUNserver() throws UtilityException, IOException, MessageAttributeParsingException {

        MessageHeader sendMH = new MessageHeader(MessageHeader.MessageHeaderType.BindingRequest);

        ChangeRequest changeRequest = new ChangeRequest();
        sendMH.addMessageAttribute(changeRequest);

        byte[] data = sendMH.getBytes();

        DatagramSocket s = new DatagramSocket();
        s.setReuseAddress(true);

        DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getByName("stun.l.google.com"), 19302);
        s.send(p);

        DatagramPacket rp;

        rp = new DatagramPacket(new byte[32], 32);

        s.receive(rp);
        MessageHeader receiveMH = new MessageHeader(MessageHeader.MessageHeaderType.BindingResponse);

        receiveMH.parseAttributes(rp.getData());
        MappedAddress ma = (MappedAddress) receiveMH
                .getMessageAttribute(MessageAttribute.MessageAttributeType.MappedAddress);

        System.out.println(ma.getAddress()+" "+ma.getPort());

        s.close();

        return ma;

    }

    public void loop(String message , Connection connection)
    {
        if(!CLIENT1_STATE) {
            if (message.trim().equals("ONLINE")) {
                System.out.println("CLIENT1> " + message.trim());
                CLIENT1_STATE = true;

                connection.sendTCP("RECEIVED");

                message = "";
            }
        }
        else
        {
            if (!message.trim().isEmpty()) {
                System.out.println("CLIENT1> " + message.trim());
            }
        }
        if(!CLIENT2_STATE)
        {
            if (message.trim().equals("ONLINE")) {
                System.out.println("CLIENT2> " + message.trim());
                CLIENT1_STATE = true;

                connection.sendTCP("RECEIVED");
                message = "";
            }
        }
        else
        {
            if (!message.trim().isEmpty()) {
                System.out.println("CLIENT2> " + message.trim());
            }
        }
    }

    @Override
    public void run() {
        super.run();




    }
}
