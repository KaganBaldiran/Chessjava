import java.io.IOException;
import java.net.*;

public class GameClient extends Thread
{

    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;

    String DataTosend = new String("Is it coming?");

    int port;

    public GameClient(Game game , InetAddress ipAddress , int port) throws UnknownHostException, SocketException {
        this.game = game;
        this.port = port;

        try
        {
            this.socket = new DatagramSocket();
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

            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data , data.length);

            try
            {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String message = new String(packet.getData());
            System.out.println("SERVER [" + packet.getAddress().getHostAddress() + " port: " + packet.getPort() +  "] > " + message);
        }
    }

    public void SendData(byte[] data)
    {
        DatagramPacket packet = new DatagramPacket(data , data.length , ipAddress , port);
        try
        {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setDataTosend(String dataTosend) {
        DataTosend = dataTosend;
    }
}
