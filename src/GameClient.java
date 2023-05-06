import java.io.IOException;
import java.net.*;
import java.util.Objects;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;


public class GameClient extends Thread
{

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

    public GameClient(Game game , InetAddress ipAddress , int port) throws UnknownHostException, SocketException
    {
        this.game = game;
        this.port = port;

        try
        {
            this.clientsocket = new DatagramSocket();
            this.ipAddress = ipAddress;
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

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
