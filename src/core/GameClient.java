package core;

import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import java.net.InetAddress;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.javawi.jstun.attribute.*;
import de.javawi.jstun.header.MessageHeader;
import de.javawi.jstun.util.UtilityException;
import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;


public class GameClient extends Thread
{

    InetAddress STUNip;
    int Stunport;



    private InetAddress ipAddress;
    private InetAddress otherclient_ipAddress;
    private DatagramSocket clientsocket;
    private Game game;

    Boolean STATEINFORM = false;

    String DataTosend = new String("Is it coming?");

    int port;
    int internalPort = 50100;
    int externalPort = 50100;
    String protocol = "UDP";

    boolean success = false;

    Client kryoclient;

    public GameClient(Game game , String ipAddress , int port) throws IOException {
        this.game = game;
        this.port = port;
        this.externalPort = port;
        this.internalPort = port;

        Client kryoclient = new Client();
        kryoclient.start();
        kryoclient.connect(5000, "192.168.0.107", 54555, 54777);

        kryoclient.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof String) {
                    String message = (String)object;
                    System.out.println("Received message from server: " + message);
                }
            }
        });






        try
        {
            this.clientsocket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(GameServer.DeConstructLink(ipAddress));
        }
        catch (SocketException | NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

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
    public void run()
    {
        while(true)
        {
            if (!STATEINFORM) {
                DatagramSocket socket;
                try {
                    socket = new DatagramSocket();
                } catch (SocketException e) {
                    throw new RuntimeException(e);
                }

                byte[] senddata = "ONLINE".getBytes();
                DatagramPacket sendpacket;

                try {
                    sendpacket = new DatagramPacket(senddata, senddata.length,ipAddress, externalPort);
                    this.clientsocket.send(sendpacket);
                } catch (RuntimeException | IOException e) {
                    e.printStackTrace();
                }

                DatagramPacket recievepacket;
                try {
                    recievepacket = new DatagramPacket(new byte[1024], 1024);
                    this.clientsocket.receive(recievepacket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String message = new String(recievepacket.getData()).trim();

                if (message.equalsIgnoreCase("RECEIVED")) {

                    System.out.println("SERVER> " + message);
                    STATEINFORM = true;
                }

                socket.close();
            }
            else
            {
                DatagramPacket sendpacket;

                /*try {
                    sendpacket = new DatagramPacket(this.DataTosend.getBytes(), this.DataTosend.getBytes().length,ipAddress, externalPort);
                    this.clientsocket.send(sendpacket);
                } catch (RuntimeException | IOException e) {
                    e.printStackTrace();
                }*/
            }




        }
    }

    public GatewayDevice PortMapping() throws IOException, ParserConfigurationException, SAXException {
        // Discover the IGD on the network
        GatewayDiscover discover = new GatewayDiscover();
        discover.discover();
        GatewayDevice d = discover.getValidGateway();
        if (d == null) {
            System.err.println("No IGD found");
            System.exit(1);
        }
        System.out.println("Found IGD: " + d.getFriendlyName());

        // Get the external IP address of the IGD
        String externalIpAddress = d.getExternalIPAddress();
        System.out.println("External IP address: " + externalIpAddress);

        // Add a port mapping to the IGD
        String description = "My Port Forwarding Rule";
        InetAddress localAddress = InetAddress.getLocalHost();
        success = d.addPortMapping(externalPort, internalPort, localAddress.getHostAddress(), protocol, description);
        if (success) {
            System.out.println("Port mapping added: " + externalIpAddress + ":" + externalPort + " -> " + localAddress.getHostAddress() + ":" + internalPort);
        } else {
            System.err.println("Failed to add port mapping");
        }

        return d;
    }

    public void DeletePortMapping(GatewayDevice d , int externalPort , String protocol)
    {
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

    public void SendInfoToServer() throws IOException {

        DatagramSocket socket;
        try {
           socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        byte[] senddata = "Hello".getBytes();
        DatagramPacket sendpacket;

        try {
           sendpacket = new DatagramPacket(senddata, senddata.length, InetAddress.getByName(Objects.requireNonNull(GameServer.getIPv4Addresses(InetAddress.getAllByName("LocalHost"))).getHostAddress()), 7070);
           this.clientsocket.send(sendpacket);
        }
        catch (RuntimeException | IOException e)
        {
            e.printStackTrace();
        }

        DatagramPacket recievepacket;
        try {
            recievepacket = new DatagramPacket(new byte[1024], 1024);
            this.clientsocket.receive(recievepacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String response = new String(recievepacket.getData());
        String[] splitResponse = response.split("-");
        InetAddress ip = InetAddress.getByName(splitResponse[0].substring(1));

        int port = Integer.parseInt(splitResponse[1]);
        System.out.println("IP: " + ip + "PORT: " + port);

        int localPort = clientsocket.getLocalPort();
        clientsocket.close();
        try {
            clientsocket = new DatagramSocket(localPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        clientsocket.setSoTimeout(1000);

        for (int i = 0; i < 5000; i++) {

            senddata = ("Datapacket(" + i + ")").getBytes();
            sendpacket = new DatagramPacket(senddata, senddata.length,ip , port);
            clientsocket.send(sendpacket);

            try
            {
                recievepacket.setData(new byte[1024]);
                clientsocket.receive(recievepacket);
                System.out.println("REC: " + new String(recievepacket.getData()));
            } catch (IOException e) {
                System.out.println("SERVER TIMED OUT");
            }

        }
        clientsocket.close();
    }

    public void SendData(byte[] data)
    {
        DatagramPacket packet = new DatagramPacket(data , data.length , ipAddress , port);
        try
        {
            clientsocket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setDataTosend(String dataTosend) {
        DataTosend = dataTosend;
    }
}
