import java.io.IOException;
import java.net.*;
import java.util.Objects;

public class GameClient extends Thread
{

    private InetAddress ipAddress;
    private InetAddress otherclient_ipAddress;
    private DatagramSocket clientsocket;
    private Game game;

    Boolean STATEINFORM = false;

    String DataTosend = new String("Is it coming?");

    int port;

    public GameClient(Game game , InetAddress ipAddress , int port) throws UnknownHostException, SocketException {
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
                    sendpacket = new DatagramPacket(senddata, senddata.length,ipAddress, 7070);
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
            }

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
