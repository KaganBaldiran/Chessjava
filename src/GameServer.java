import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.xml.sax.SAXException;

import javax.naming.spi.Resolver;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.*;
import java.util.Map;


public class GameServer extends Thread
{

    private DatagramSocket socket;
    private Game game;

    public Boolean CLIENT1_STATE = false;
    public Boolean CLIENT2_STATE = false;

    int internalPort = 8080;
    int externalPort = 8080;
    String protocol = "UDP";

    boolean success = false;

    public GameServer(Game game , int port) throws UnknownHostException, SocketException
    {
        this.game = game;

        try
        {
            this.socket = new DatagramSocket(port,InetAddress.getByName("192.168.0.107"));
            PortMapping();
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




            /*byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data , data.length);

            try
            {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String message = new String(packet.getData());
            System.out.println("CLIENT [" + packet.getAddress().getHostAddress() + " port: " + packet.getPort() +  "] > " + message);
            if(!message.isEmpty())
            {
                SendData("Pong".getBytes(),packet.getAddress(), packet.getPort());
            }*/
/*
            try {
                HolePunching();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
*/
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
