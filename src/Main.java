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

        Math.Vec2<Integer> temp = new Math.Vec2<>(6,2);

        Math.UV_Tools.Invert_Y_Axis(temp,Tile.BLACK);

        Pawn newPawn = new Pawn(1,2,Tile.WHITE);

        System.out.println(String.valueOf(temp.x) + " " + String.valueOf(temp.y));

    }

}
