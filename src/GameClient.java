

/*import org.fourthline.cling.*;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.NetworkAddress;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.*;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceId;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDAServiceId;
import org.fourthline.cling.protocol.ProtocolFactory;
import org.fourthline.cling.registry.event.RemoteDeviceDiscovery;
import org.fourthline.cling.support.igd.callback.PortMappingAdd;
import org.fourthline.cling.support.model.PortMapping;
import org.fourthline.cling.transport.Router;
import org.fourthline.cling.transport.RouterException;
import org.fourthline.cling.transport.spi.InitializationException;
import org.fourthline.cling.transport.spi.UpnpStream;


import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.support.model.PortMapping;*/


import java.io.IOException;
import java.net.*;
import java.security.Provider;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

//import static org.fourthline.cling.support.igd.PortMappingListener.IGD_DEVICE_TYPE;





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

                socket.close();
            }

        }
    }
    //private static final UDADeviceType IGD_DEVICE_TYPE = new UDADeviceType("InternetGatewayDevice", 1);

   /* public void doPortForwarding() {
        // Create a new instance of UpnpServiceImpl
        UpnpServiceImpl upnpService = new UpnpServiceImpl();

        // Get the ControlPoint from the UPnP service
        ControlPoint controlPoint = upnpService.getControlPoint();

        // Discover devices and find the IGD device
        Collection<RemoteDevice> devices = controlPoint.getRegistry().getRemoteDevices();
        RemoteDevice igdDevice = null;
        for (RemoteDevice device : devices) {
            if (device.getType().equals(IGD_DEVICE_TYPE)) {
                igdDevice = device;
                break;
            }
        }

        // Make sure we found the IGD device
        if (igdDevice == null) {
            throw new RuntimeException("IGD device not found");
        }

        // Find the WAN IP connection service of the router
        RemoteService wanService = igdDevice.findService(new UDAServiceId("WANIPConnection"));

        // Get the WAN IP address of the router
        String wanIP = wanService.getAction("GetExternalIPAddress")
                .getOutputArgument("NewExternalIPAddress").toString();

        System.out.println("WANIP: "+ wanIP);


        // Request a port mapping for the chess game
        PortMapping portMapping = new PortMapping(1220, wanIP, PortMapping.Protocol.TCP, "Chess Game");
        controlPoint.execute(new PortMappingAdd(wanService, portMapping) {
            @Override
            public void success(ActionInvocation actionInvocation) {
                // Handle success
            }

            @Override
            public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {
                // Handle failure
            }
        });

        // Stop the UPnP service when done
        upnpService.shutdown();
    }*/

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
