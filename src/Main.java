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
        JFrame frame = new JFrame("Chess Board");
        Board chessBoard = new Board();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1025, 1025);
        frame.add(chessBoard);
        frame.setVisible(true);

        Math.Vec2<Integer> temp = new Math.Vec2<>(6,2);

        Math.UV_Tools.Invert_Y_Axis(temp,Tile.BLACK);

        Pawn newPawn = new Pawn(3,5,Tile.WHITE);

        Board newboard = new Board();

        Player newplayer = new Player(Tile.WHITE,newboard);

        System.out.println("SIZE OF PLAYER PIECES: " + newplayer.pieces.get(0).Coordinates.x);

        newPawn.GetPossibleMoves(true);

        for (int i = 0; i < newPawn.Possible_Moves.size(); i++)
        {
            System.out.println(String.valueOf(newPawn.Possible_Moves.get(i).x) + " " + String.valueOf(newPawn.Possible_Moves.get(i).y));
        }

    }

}
