package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.javawi.jstun.attribute.ChangeRequest;
import de.javawi.jstun.attribute.MappedAddress;
import de.javawi.jstun.attribute.MessageAttribute;
import de.javawi.jstun.attribute.MessageAttributeParsingException;
import de.javawi.jstun.header.MessageHeader;
import de.javawi.jstun.util.UtilityException;
import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
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

    String GameLinkExternal;

    String GameLinkInternal;
    String externalIpAddress;

    GatewayDevice PortMappingDevice;

    Math.Vec2<Integer> Ports = new Math.Vec2<>();
    Math.Vec2<String> PlayerNames = new Math.Vec2<>();


    KryonetServer(int tcp_port , int udp_port , String ExternalIP) throws IOException {
        super();
        start();

        String ipaddress = InetAddress.getLocalHost().getHostAddress();

        Ports.SetValues(tcp_port , udp_port);

        if(ExternalIP != null)
        {
            GameLinkExternal = ConstructLink(ExternalIP);
        }

        GameLinkInternal = ConstructLink(ipaddress);

        System.out.println("GameLink: "+ GameLinkExternal + " " + ExternalIP + " internal: " + GameLinkInternal);

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
            if(!message.isEmpty()) {
                if (message.substring(0, 6).trim().equals("ONLINE")) {

                    PlayerNames.x = message.substring(7);

                    System.out.println("CLIENT1> " + message.trim() + " NAME: " + PlayerNames.x);
                    CLIENT1_STATE = true;

                    clientConnection1 = connection;
                    connection.sendTCP("RECEIVED");

                    System.out.println("CLIENT1 CONNECTION> " + clientConnection1.getRemoteAddressTCP());

                    message = "";
                }
            }
        }
        if(!CLIENT2_STATE)
        {
            if(!message.isEmpty()) {
                if (message.substring(0, 6).trim().equals("ONLINE")) {

                    PlayerNames.y = message.substring(7);

                    System.out.println("CLIENT2> " + message.trim() + " NAME: " + PlayerNames.y);
                    CLIENT2_STATE = true;

                    clientConnection2 = connection;
                    connection.sendTCP("RECEIVED");
                    System.out.println("CLIENT2 CONNECTION> " + clientConnection2.getRemoteAddressTCP());
                    message = "";

                    if (AreBothPlayersOnline()) {
                        clientConnection1.sendTCP("BOTH ONLINE " + PlayerNames.y);
                        clientConnection2.sendTCP("BOTH ONLINE " + PlayerNames.x);
                    }
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

    public static Math.Pair<GatewayDevice , String> PortMapping(int externalPort , int internalPort , String protocol , boolean DeleteGameEvent) throws IOException, ParserConfigurationException, SAXException {

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
