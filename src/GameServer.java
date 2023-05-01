import javax.naming.spi.Resolver;
import java.io.IOException;
import java.net.*;



public class GameServer extends Thread
{

    private DatagramSocket socket;
    private Game game;

    public GameServer(Game game , int port) throws UnknownHostException, SocketException
    {
        this.game = game;

        try
        {
            this.socket = new DatagramSocket(port);

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
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data , data.length);

            try
            {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String message = new String(packet.getData());
            System.out.println("CLIENT [" + packet.getAddress().getHostAddress() + " port: " + packet.getPort() +  "] > " + message);
            if(message.trim().equalsIgnoreCase("ping") )
            {
                SendData("Pong".getBytes(),packet.getAddress(), packet.getPort());
            }

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
