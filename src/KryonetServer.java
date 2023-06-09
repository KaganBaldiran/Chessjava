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

    Connection clientConnection1;
    Connection clientConnection2;

    String GameLink;

    KryonetServer(int tcp_port , int udp_port) throws IOException {
        super();
        start();

        try {
            StunAddress = SendRequestToSTUNserver();
        } catch (UtilityException | MessageAttributeParsingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("STUN SERVER: "+ StunAddress.getAddress() + " " + StunAddress.getPort());

        String ipaddress = InetAddress.getLocalHost().getHostAddress();


        GameLink = ConstructLink(ipaddress);

        System.out.println("GameLink: "+ GameLink + " " + ipaddress);

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

                clientConnection1 = connection;
                connection.sendTCP("RECEIVED");

                System.out.println("CLIENT1 CONNECTION> " + clientConnection1.getRemoteAddressTCP());

                message = "";
            }
        }
        if(!CLIENT2_STATE)
        {
            if (message.trim().equals("ONLINE")) {
                System.out.println("CLIENT2> " + message.trim());
                CLIENT2_STATE = true;

                clientConnection2 = connection;
                connection.sendTCP("RECEIVED");
                System.out.println("CLIENT2 CONNECTION> " + clientConnection2.getRemoteAddressTCP());
                message = "";

                if (AreBothPlayersOnline())
                {
                    clientConnection1.sendTCP("BOTH ONLINE");
                    clientConnection2.sendTCP("BOTH ONLINE");
                }
            }

        }
        else if(AreBothPlayersOnline())
        {
            if(message.substring(0,4).trim().equals("MOVE"))
            {
                System.out.println("MOVE> " + message);

                SendTheOtherPlayer(connection , message);
            }
            if (message.equalsIgnoreCase("DISCONNECT"))
            {
                System.out.println("CLIENT1> " + message);

                SendTheOtherPlayer(connection , message);

                close();

            }
            if (message.substring(0,8).trim().equalsIgnoreCase("CAPTURED"))
            {
                System.out.println("CLIENT1> " + message);
                SendTheOtherPlayer(connection , message);
                close();

            }

        }
    }

    public boolean AreBothPlayersOnline()
    {
        return CLIENT1_STATE & CLIENT2_STATE;
    }

    public void SendTheOtherPlayer(Connection connection , String message)
    {
        if (connection.getRemoteAddressTCP().equals(clientConnection1.getRemoteAddressTCP())) {
            clientConnection2.sendTCP(message);
        }
        if (connection.getRemoteAddressTCP().equals(clientConnection2.getRemoteAddressTCP())) {
            clientConnection1.sendTCP(message);
        }
    }

    private static final String KEY = "Prty3Whjvnmd458l";

    public static String ConstructLink(String ip_address) {

        StringBuilder FinalString = new StringBuilder();

        for (int i = 0; i < ip_address.length(); i++) {

            String substring = ip_address.trim().substring(i, i + 1);
            if(!substring.equalsIgnoreCase("."))
            {
                FinalString.append(KEY.charAt(Integer.parseInt(substring)
                ));

            }
            else
            {
                FinalString.append("/");
            }
        }
        FinalString = new StringBuilder("www.chessjava/" + FinalString + ".com");

        System.out.println("CONSTRUCTED: "+ FinalString);

        return FinalString.toString();

    }

    public static String DeConstructLink(String Link) {

        StringBuilder FinalString = new StringBuilder();

        for (int i = "www.chessjava/".length(); i < Link.length() - ".com".length(); i++) {

            String substring = Link.trim().substring(i, i + 1);
            if(!substring.equalsIgnoreCase("/"))
            {
                for (int j = 0; j < KEY.length(); j++)
                {
                    if(KEY.substring(j,j+1).equals(substring))
                    {
                        FinalString.append(j);
                    }
                }
            }
            else
            {
                FinalString.append(".");
            }
        }
        System.out.println("DECONSTRUCTED: "+ FinalString);
        return FinalString.toString();

    }

    @Override
    public void run() {
        super.run();




    }
}
