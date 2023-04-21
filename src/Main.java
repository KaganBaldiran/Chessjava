import javax.swing.*;
import java.util.Vector;


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

        Pawn newPawn = new Pawn(1,1,Tile.WHITE);

        Bishop newBishop = new Bishop(4,5,Tile.WHITE);

        Board newboard = new Board();

        King newking = new King(5,5,Tile.WHITE);

        Player newplayer = new Player(Tile.WHITE,newboard);

        System.out.println("SIZE OF PLAYER PIECES: " + newplayer.pieces.get(0).Coordinates.x);

        Vector<Math.Vec2<Integer>> result = newPawn.GetPossibleMoves(true, newPawn.Coordinates);
       // result = newPawn.GetPossibleMoves(true, newPawn.Coordinates);

        newBishop.GetPossibleMoves(true,newBishop.Coordinates);

        Rook newrook = new Rook(8,8,Tile.WHITE);

        newrook.GetPossibleMoves(true , newrook.Coordinates);

        newrook.GetPossibleMoves(true , newrook.Coordinates);

        newking.GetPossibleMoves(true , newking.Coordinates);

        Queen newqueen = new Queen(1,1,Tile.WHITE);

        newqueen.GetPossibleMoves(true,newqueen.Coordinates);

        Knight newknight = new Knight(5,5,Tile.WHITE);

        newknight.GetPossibleMoves(true,newknight.Coordinates);


        for (int i = 0; i < newPawn.Possible_Moves.size(); i++)
        {
            System.out.println("PAWN Possible moves: " + newPawn.Possible_Moves.size() + " " + String.valueOf(newPawn.Possible_Moves.get(i).x) + " " + String.valueOf(newPawn.Possible_Moves.get(i).y));
        }

        for (int i = 0; i < newBishop.Possible_Moves.size(); i++)
        {
            System.out.println("BISHOP Possible moves: " + newBishop.Possible_Moves.size() + " " + String.valueOf(newBishop.Possible_Moves.get(i).x) + " " + String.valueOf(newBishop.Possible_Moves.get(i).y));
        }

        for (int i = 0; i < newrook.Possible_Moves.size(); i++)
        {
            System.out.println(" ROOK Possible moves: " + newrook.Possible_Moves.size() + " " + String.valueOf(newrook.Possible_Moves.get(i).x) + " " + String.valueOf(newrook.Possible_Moves.get(i).y));
        }

        for (int i = 0; i < newqueen.Possible_Moves.size(); i++)
        {
            System.out.println(" QUEEN Possible moves: " + newqueen.Possible_Moves.size() + " " + String.valueOf(newqueen.Possible_Moves.get(i).x) + " " + String.valueOf(newqueen.Possible_Moves.get(i).y));
        }

        for (int i = 0; i < newking.Possible_Moves.size(); i++)
        {
            System.out.println(" KING Possible moves: " + newking.Possible_Moves.size() + " " + String.valueOf(newking.Possible_Moves.get(i).x) + " " + String.valueOf(newking.Possible_Moves.get(i).y));
        }

        for (int i = 0; i < newknight.Possible_Moves.size(); i++)
        {
            System.out.println(" KNIGHT Possible moves: " + newknight.Possible_Moves.size() + " " + String.valueOf(newknight.Possible_Moves.get(i).x) + " " + String.valueOf(newknight.Possible_Moves.get(i).y));
        }

    }

}
