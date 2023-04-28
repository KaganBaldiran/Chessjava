import javax.swing.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

class Chess extends JFrame
{

    public static void main(String[] args) throws IOException {
       new Thread(new Game()).start();
    }

}
