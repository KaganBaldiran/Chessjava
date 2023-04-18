import javax.swing.*;


class MyGUI extends JFrame {

    public MyGUI() {

        JButton button = new JButton("Click me!");
        add(button);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }

    public static void main(String[] args)
    {
        new MyGUI();
    }

}
