import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.security.MessageDigest;


public class GameServer extends Thread
{
    private DatagramSocket socket;
    private Game game;
    public Boolean CLIENT1_STATE = false;
    public Boolean CLIENT2_STATE = false;
    public int internalPort;
    public int externalPort;
    public String protocol = "UDP";

    String externalIpAddress;
    public GatewayDevice device;



    boolean success = false;
    boolean ServerisRequested = false;


    public GameServer(Game game , int port) throws UnknownHostException, SocketException
    {
        this.game = game;
        ServerisRequested = true;
        this.externalPort = port;
        this.internalPort = port;

        try
        {
            this.socket = new DatagramSocket(port,InetAddress.getByName("192.168.0.107"));
            device = PortMapping();
            //System.out.println("Waiting for client 1 on Port " + socket.getLocalPort());

        }
        catch (SocketException e)
        {
            e.printStackTrace();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

    }

    public void run()
    {
        while(true)
        {

                if (!CLIENT1_STATE)
                {
                    DatagramSocket serverSocket1 = null;

                    serverSocket1 = this.socket;


                    System.out.println("Waiting for Client 1 on Port "
                            + serverSocket1.getLocalPort());

                    // receive Data
                    DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                    try {
                        serverSocket1.receive(receivePacket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String message = new String(receivePacket.getData());

                    if(message.trim().equals("ONLINE"))
                    {
                        System.out.println("CLIENT1> " + message.trim());
                        CLIENT1_STATE = true;
                        SendData("RECEIVED".getBytes() ,receivePacket.getAddress(),receivePacket.getPort() );
                    }


                }
                else
                {

                    DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                    try {
                        this.socket.receive(receivePacket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String message = new String(receivePacket.getData());

                    if(!message.trim().isEmpty())
                    {
                        System.out.println("CLIENT1> " + message.trim());
                        //CLIENT1_STATE = true;
                        //SendData("RECEIVED".getBytes() ,receivePacket.getAddress(),receivePacket.getPort() );
                    }

                }

                if(!CLIENT2_STATE)
                {
                    DatagramSocket serverSocket2 = null;

                    serverSocket2 = this.socket;

                    //System.out.println("Waiting for Client 2 on Port "
                          // + serverSocket2.getLocalPort());

                    // receive Data
                    DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                    try {
                        serverSocket2.receive(receivePacket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String message = new String(receivePacket.getData()).trim();

                    if(message.trim().equals("ONLINE"))
                    {
                        System.out.println("CLIENT2> " + message);
                        CLIENT2_STATE = true;
                        SendData("RECEIVED".getBytes() ,receivePacket.getAddress(),receivePacket.getPort() );
                    }


                }
                else
                {

                    DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                    try {
                        this.socket.receive(receivePacket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String message = new String(receivePacket.getData());

                    if(!message.trim().isEmpty())
                    {
                        System.out.println("CLIENT2> " + message.trim());
                        //CLIENT1_STATE = true;
                        //SendData("RECEIVED".getBytes() ,receivePacket.getAddress(),receivePacket.getPort() );
                    }

                }


        }
    }

    private static final String KEY = "Prty3Whjvnmd458l";

    public String ConstructLink() throws NoSuchAlgorithmException {

        String FinalString = "";

        if (success)
        {
            for (int i = 0; i < externalIpAddress.length(); i++) {

                if(!this.externalIpAddress.trim().substring(i,i+1).equalsIgnoreCase("."))
                {
                    FinalString += KEY.substring(Integer.parseInt(this.externalIpAddress.trim().substring(i,i+1)),
                                                 Integer.parseInt(this.externalIpAddress.trim().substring(i,i+1))+1);

                }
                else
                {
                    FinalString += "/";
                }
            }
            FinalString = "www.chessjava/"+FinalString+".com";

            System.out.println("CONSTRUCTED: "+ FinalString);

            return FinalString;
        }

        return null;
    }

    public String DeConstructLink(String Link) throws NoSuchAlgorithmException {

        String FinalString = "";
        if (success)
        {
            for (int i = "www.chessjava/".length(); i < Link.length() - ".com".length(); i++) {

                String substring = Link.trim().substring(i, i + 1);
                if(!substring.equalsIgnoreCase("/"))
                {
                    for (int j = 0; j < KEY.length(); j++)
                    {
                        if(KEY.substring(j,j+1).equals(substring))
                        {
                            FinalString += String.valueOf(j);
                        }
                    }
                }
                else
                {
                    FinalString += ".";
                }
            }
            System.out.println("DECONSTRUCTED: "+ FinalString);
            return FinalString;
        }
        return null;
    }

    public String checkIps() {
        Enumeration<NetworkInterface> interfaces;
        Enumeration<InetAddress> addresses;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLinkLocalAddress() && !address.isLoopbackAddress() && address instanceof Inet4Address)
                        return address.getHostAddress();
                }
            }
        } catch (SocketException e) {
            System.out.println("Could not get network interfaces.");
            e.printStackTrace();
        }
        return null;
    }
    private static String getWirelessInterfaceName() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            if (iface.isUp() && iface.getName().startsWith("w")) {
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!addr.isLinkLocalAddress() && !addr.isLoopbackAddress() && addr.getAddress().length == 4) {
                        return iface.getName();
                    }
                }
            }
        }
        throw new SocketException("No wireless interface found");
    }


    public GatewayDevice PortMapping() throws IOException, ParserConfigurationException, SAXException {

        System.out.println("MY INTERFACE: " + this.checkIps());

        //System.out.println("MY WIFI INTERFACE: " + getWirelessInterfaceName());
        GatewayDiscover discover = new GatewayDiscover();
        discover.discover();

        GatewayDevice d = discover.getValidGateway();
        System.out.println("IS EMPTY?????: " + discover.getAllGateways().isEmpty());
        if (d == null) {
            System.err.println("No IGD found");
            System.err.println("Server cannot be initialized");
            //System.exit(1);
        }

        assert d != null;
        System.out.println("Found IGD: " + d.getFriendlyName());

        this.externalIpAddress = d.getExternalIPAddress();
        System.out.println("External IP address: " + this.externalIpAddress);

        // Add a port mapping to the IGD
        String description = "Chessjava gameport";

        InetAddress localAddress = getIPv4Addresses(InetAddress.getAllByName(InetAddress.getLocalHost().getHostName()));
        assert localAddress != null;
        success = d.addPortMapping(externalPort, internalPort, localAddress.getHostAddress(), protocol, description);
        if (success) {
            System.out.println("Port mapping added: " + this.externalIpAddress + ":" + externalPort + " -> " + localAddress.getHostAddress() + ":" + internalPort);
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

    public void SendData(byte[] data , InetAddress ipAddress , int port)
    {
        DatagramPacket packet = new DatagramPacket(data , data.length , ipAddress , port);
        try
        {
            this.socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void HolePunching() throws IOException {
        // Waiting for Connection of Client1 on Port 7070
        // ////////////////////////////////////////////////

        // open serverSocket on Port 7070
        DatagramSocket serverSocket1 = new DatagramSocket(7070);

        System.out.println("Waiting for Client 1 on Port "
                + serverSocket1.getLocalPort());

        // receive Data
        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
        serverSocket1.receive(receivePacket);

        // Get IP-Address and Port of Client1
        InetAddress IPAddress1 = receivePacket.getAddress();
        int port1 = receivePacket.getPort();
        String msgInfoOfClient1 = IPAddress1 + "-" + port1 + "-";

        System.out.println("Client1: " + msgInfoOfClient1);

        // Waiting for Connection of Client2 on Port 7071
        // ////////////////////////////////////////////////

        // open serverSocket on Port 7071
        DatagramSocket serverSocket2 = new DatagramSocket(7071);

        System.out.println("Waiting for Client 2 on Port "
                + serverSocket2.getLocalPort());

        // receive Data
        receivePacket = new DatagramPacket(new byte[1024], 1024);
        serverSocket2.receive(receivePacket);

        // GetIP-Address and Port of Client1
        InetAddress IPAddress2 = receivePacket.getAddress();
        int port2 = receivePacket.getPort();
        String msgInfoOfClient2 = IPAddress2 + "-" + port2 + "-";

        System.out.println("Client2:" + msgInfoOfClient2);

        // Send the Information to the other Client
        // /////////////////////////////////////////////////

        // Send Information of Client2 to Client1
        serverSocket1.send(new DatagramPacket(msgInfoOfClient2.getBytes(),
                msgInfoOfClient2.getBytes().length, IPAddress1, port1));

        // Send Infos of Client1 to Client2
        serverSocket2.send(new DatagramPacket(msgInfoOfClient1.getBytes(),
                msgInfoOfClient1.getBytes().length, IPAddress2, port2));

        //close Sockets
        serverSocket1.close();
        serverSocket2.close();

    }

    public static Inet6Address getIPv6Addresses(InetAddress[] addresses)
    {
        for(InetAddress addr : addresses)
        {
            if(addr instanceof Inet6Address)
            {
                return (Inet6Address) addr;
            }
        }
        return null;
    }

    public static Inet4Address getIPv4Addresses(InetAddress[] addresses)
    {
        for(InetAddress addr : addresses)
        {
            if(addr instanceof Inet4Address)
            {
                return (Inet4Address) addr;
            }
        }
        return null;
    }

    public static String ReverseDSN(String hostIP)
    {
        try
        {
            InetAddress inetaddres = InetAddress.getByName(hostIP);
            String hostname = inetaddres.getHostName();
            return hostname;
        } catch (UnknownHostException e) {
            System.out.println("Unable to resolve hostname for " + hostIP);
        }
        return null;
    }

}
