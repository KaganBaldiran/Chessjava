import java.io.IOException;
import java.net.*;

public class GameServer extends Thread
{

    private DatagramSocket socket;
    private Game game;

    public GameServer(Game game) throws UnknownHostException, SocketException
    {
        this.game = game;

        try
        {
            this.socket = new DatagramSocket(1331);

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
            if(message.trim().equalsIgnoreCase("AHMED") )
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

}
