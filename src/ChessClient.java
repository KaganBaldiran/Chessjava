import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ChessClient extends Thread {

    private Socket clientsocket;
    private BufferedReader in;
    private PrintWriter out;

    String DataTosend = new String("Is it coming?");

    ChessClient(String ipAddress , int port) throws IOException {


        this.clientsocket = new Socket(ipAddress,port);
        this.in = new BufferedReader(new InputStreamReader(this.clientsocket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(this.clientsocket.getOutputStream()),true);
        //out.println("IM IN THE CLIENT");
        //start();

    }

    public void run()
    {
        while(true)
        {
            try {
                SendData(this.DataTosend);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void setDataTosend(String dataTosend) {
        DataTosend = dataTosend;
    }

    public void SendData(String data) throws IOException {

        out.println(data);

        //return in.readLine();
    }

    public void close() throws IOException {
        this.clientsocket.close();
        this.in.close();
        this.out.close();
    }



}
