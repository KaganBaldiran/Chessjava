import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Player extends JPanel
{
    Vector<piece> pieces = new Vector<>();
    Vector<piece> TakenPieces = new Vector<>();
    Board Current_Board_Reference;

    int Color;

    Player(int color , Board current_board)
    {
        this.Color = color;

        this.Current_Board_Reference = current_board;

        int y_axis = 2;

        if (this.Color == Tile.BLACK)
        {
           y_axis = Math.UV_Tools.Invert_Y_Axis( Current_Board_Reference.FetchTile(1,y_axis).Tilecoordinates,Tile.BLACK).y;
        }

        for (int x = 1; x < 9; x++)
        {
            //pieces.add(new Pawn(x,y_axis,this.Color,current_board.FetchTile(x,y_axis),this.Current_Board_Reference,null));
        }

        //pieces.add(new King(x,y_axis,this.Color,current_board.FetchTile(x,2),this.Current_Board_Reference));


        pieces.add(new Knight(6,2,this.Color,current_board.FetchTile(6,2),this.Current_Board_Reference,"C:\\Users\\kbald\\Desktop\\Chess_cdt45.png"));

        //pieces.add(new Bishop(x,y_axis,this.Color,current_board.FetchTile(x,2),this.Current_Board_Reference));
        //pieces.add(new Bishop(x,y_axis,this.Color,current_board.FetchTile(x,2),this.Current_Board_Reference));

        //pieces.add(new Bishop(x,y_axis,this.Color,current_board.FetchTile(x,2),this.Current_Board_Reference));

    }

    public void TakeOpponentPiece(piece takenPiece)
    {
        TakenPieces.add(takenPiece);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        pieces.get(0).paintComponent(g);




        g.dispose();
    }
}
