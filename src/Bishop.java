import java.util.Vector;

public class Bishop extends piece{


    public static final int RIGHT_UP = 1;
    public static final int RIGHT_DOWN = 2;
    public static final int LEFT_UP = 3;
    public static final int LEFT_DOWN = 4;

    Bishop(int x_cord , int y_cord , int color)
    {
        super(x_cord, y_cord, color);
    }

    Bishop(int x_cord , int y_cord , int color, Tile TilePieceStandingOn)
    {
        super(x_cord, y_cord, color,TilePieceStandingOn);
    }

    Bishop(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , String file_path,  MouseInputListener current_mouse_listener)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard,file_path,current_mouse_listener);
    }


    @Override
    public void capture() {

    }


    Math.Vec2<Integer> tileTracer = new Math.Vec2<>();
    boolean SwitchSide = false;
    int Side = RIGHT_UP;

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

        if (this.Side == RIGHT_UP  && input_Coordinates.y < 8  && input_Coordinates.x < 8 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x++;
            tileTracer.y++;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == RIGHT_DOWN  && input_Coordinates.x < 8  && input_Coordinates.y > 1 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x++;
            tileTracer.y--;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == LEFT_UP && input_Coordinates.x > 1 && input_Coordinates.y < 8 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x--;
            tileTracer.y++;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == LEFT_DOWN && input_Coordinates.x > 1 && input_Coordinates.y > 1 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x--;
            tileTracer.y--;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if(this.Side <= LEFT_DOWN)
        {
            this.SwitchSide = true;
            tileTracer.SetValues(this.Coordinates);
            GetPossibleMoves(true,tileTracer);

        }

        if(this.Side > LEFT_DOWN)
        {
            //System.out.println("QUEEN POSSIBLE MOVES RETURN THE VALUE: ");
            this.Side = RIGHT_UP;
            this.ClearPossibleMoves = true;
            return this.Possible_Moves;
        }
        return null;
    }
}
