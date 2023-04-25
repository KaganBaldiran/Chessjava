import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Player extends JPanel
{
    Vector<piece> pieces = new Vector<>();
    Vector<piece> TakenPieces = new Vector<>();
    Board Current_Board_Reference;

    MouseInputListener mouselistenerreference;

    int Color;

    Player(int color , Board current_board , MouseInputListener current_mouse_listener)
    {
        this.Color = color;

        this.Current_Board_Reference = current_board;

        int y_axis = 2;

        this.mouselistenerreference = current_mouse_listener;

        if (this.Color == Tile.BLACK)
        {
           y_axis = Math.UV_Tools.Invert_Y_Axis( Current_Board_Reference.FetchTile(1,y_axis).Tilecoordinates,Tile.BLACK).y;
        }

        for (int x = 1; x < 9; x++)
        {
            //pieces.add(new Pawn(x,y_axis,this.Color,current_board.FetchTile(x,y_axis),this.Current_Board_Reference,null));
        }

        //pieces.add(new King(x,y_axis,this.Color,current_board.FetchTile(x,2),this.Current_Board_Reference));


        pieces.add(new Knight(6,2,this.Color,current_board.FetchTile(6,2),this.Current_Board_Reference,"resources\\Chess_cdt45.png",current_mouse_listener));

        //pieces.add(new Bishop(7,2,this.Color,current_board.FetchTile(7,2),this.Current_Board_Reference,"resources\\Chess_tile_bd.png"));

        pieces.add(new Rook(8,8,this.Color,current_board.FetchTile(8,8),this.Current_Board_Reference,"resources\\rookpng.png",current_mouse_listener));
        pieces.add(new Rook(1,8,this.Color,current_board.FetchTile(1,8),this.Current_Board_Reference,"resources\\rookpng.png",current_mouse_listener));

        //pieces.add(new Bishop(x,y_axis,this.Color,current_board.FetchTile(x,2),this.Current_Board_Reference));
        //pieces.add(new Bishop(x,y_axis,this.Color,current_board.FetchTile(x,2),this.Current_Board_Reference));

        //pieces.add(new Bishop(x,y_axis,this.Color,current_board.FetchTile(x,2),this.Current_Board_Reference));

    }

    public void TakeOpponentPiece(piece takenPiece)
    {
        TakenPieces.add(takenPiece);
    }

    public void MovePlayerPieces()
    {
        for (piece piece : this.pieces)
        {
            piece.Move(0, 0);
        }
    }

    public void GetPosssibleMoves()
    {
        for (piece piece : this.pieces)
        {
            piece.GetPossibleMoves(true, piece.Coordinates);
        }

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for (int i = 0; i < this.pieces.size(); i++)
        {
            pieces.get(i).paintComponent(g);
        }

        g.dispose();
    }
}
