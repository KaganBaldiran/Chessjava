import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer extends Thread{

 private ServerSocket ChessServer;
 private Socket ClientSocket;
 private BufferedReader in;
 private PrintWriter out;

 ChessServer(int port) throws IOException {

     this.ChessServer = new ServerSocket(port);

 }


 public void AcceptClient() throws IOException
 {

 }
 public void run()
 {
     try {
         this.ClientSocket = ChessServer.accept();
         this.in = new BufferedReader(new InputStreamReader(this.ClientSocket.getInputStream()));
         this.out = new PrintWriter(new OutputStreamWriter(this.ClientSocket.getOutputStream()),true);
     } catch (IOException e) {
         throw new RuntimeException(e);
     }

     while(true)
     {
         AcceptData();
     }
 }


 public boolean IsConnected()
 {
     return ClientSocket.isConnected();
 }



    @Override
    public void start() {
        super.start();
    }

    public void AcceptData()
 {

     String data;

     try
     {
         data = in.readLine();
     } catch (IOException e) {
         e.printStackTrace();
         throw new RuntimeException(e);
     }

     if(!data.isEmpty())
     {
         System.out.println("Data is recieved :" + data);
         System.out.println("YES IT DOES");
     }

 }

 public void close()
 {
     try
     {
         this.ClientSocket.close();
         this.ChessServer.close();
         this.in.close();
         this.out.close();

     } catch (IOException e) {
         e.printStackTrace();
         throw new RuntimeException(e);
     }
 }
}
