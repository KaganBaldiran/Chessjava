import javax.swing.*;

class Chess extends JFrame
{
    public static void main(String[] args) {
       new Thread(new Game()).start();
    }
}
