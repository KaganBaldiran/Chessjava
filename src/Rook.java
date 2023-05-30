import java.awt.event.MouseEvent;
import java.util.Vector;

public class Rook extends piece{

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;


    Rook(int x_cord,int y_cord , int color)
    {
        super(x_cord, y_cord, color);
    }
    Rook(int x_cord,int y_cord , int color, Tile TilePieceStandingOn)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn);
        this.CurrentMouseListenerReference = null;
    }
    Rook(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , String file_path , MouseInputListener current_mouse_listener, Player player_this_piece_belongs)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard,file_path,current_mouse_listener,player_this_piece_belongs);
        this.TilePieceStandingOn.SetEmptinessState(false);
    }

    Rook(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , Texture existing_texture , MouseInputListener current_mouse_listener, Player player_this_piece_belongs)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard, existing_texture, current_mouse_listener,player_this_piece_belongs);
        this.TilePieceStandingOn.SetEmptinessState(false);
    }


    @Override
    public void capture()
    {

    }

    boolean SwitchSide = false;

    Math.Vec2<Integer> tileTracer = new Math.Vec2<>();
    int Side = UP;

    @Override
    public Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty, Math.Vec2<Integer> input_Coordinates)
    {

        if(!this.Possible_Moves.isEmpty() && this.ClearPossibleMoves)
        {
            this.ClearPossibleMoves = false;
            this.Possible_Moves.clear();
        }

        if(this.SwitchSide)
        {
            this.Side++;
            this.SwitchSide = false;
        }

        isTileEmpty = CheckTileEmptiness(Side , input_Coordinates);

        if (this.Side == UP && input_Coordinates.y < 8 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y++;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);

        }
        else if (this.Side == DOWN  && input_Coordinates.y > 1 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y--;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == LEFT  && input_Coordinates.x > 1 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x--;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == RIGHT  && input_Coordinates.x < 8  && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x++;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if(this.Side <= RIGHT)
        {
            this.SwitchSide = true;
            tileTracer.SetValues(this.Coordinates);

            GetPossibleMoves(true,tileTracer);

        }

        if(this.Side > RIGHT)
        {
            this.Side = UP;
            this.ClearPossibleMoves = true;
            return this.Possible_Moves;
        }
        return null;
    }

    public boolean CheckTileEmptiness(int Side , Math.Vec2<Integer> Coordinates)
    {
        boolean result = false;
        Math.Vec2<Integer> input_Coordinates = new Math.Vec2<>(Coordinates);

        if(Side <= RIGHT)
        {
            if (this.Side == UP && input_Coordinates.y < 8)
            {
                input_Coordinates.y++;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (this.Side == DOWN  && input_Coordinates.y > 1)
            {
                input_Coordinates.y--;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (this.Side == LEFT  && input_Coordinates.x > 1)
            {
                input_Coordinates.x--;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (this.Side == RIGHT  && input_Coordinates.x < 8)
            {
                input_Coordinates.x++;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }

            System.out.println("IS IT EMPTY SIDE "+ Side + ": " + result);

        }
        return result;
    }

}
