import javax.swing.*;
import java.util.Vector;

class Chess extends JFrame
{
    public static void main(String[] args)
    {
       new Thread(new Game()).start();
    }

}
