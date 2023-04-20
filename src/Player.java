import java.util.Vector;

public class Player
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

        for (int x = 0; x < 8; x++)
        {
            pieces.add(new Pawn(x,y_axis,this.Color,current_board.FetchTile(x,2),this.Current_Board_Reference));
        }

    }


}
