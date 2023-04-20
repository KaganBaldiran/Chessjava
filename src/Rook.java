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
    }
    Rook(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard);
    }

    @Override
    public void Move(int newX, int newY)
    {
        this.Coordinates.SetValues(newX,newY);
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

        if(!this.Possible_Moves.isEmpty() && !isTileEmpty)
        {
            this.Possible_Moves.clear();
        }

        if(this.SwitchSide)
        {
            this.Side++;
            this.SwitchSide = false;
        }

        if (isTileEmpty && !(this.CurrentGameBoard.MinMaxBoundaries.x >= input_Coordinates.x ||
                this.CurrentGameBoard.MinMaxBoundaries.y <= input_Coordinates.x ||
                this.CurrentGameBoard.MinMaxBoundaries.z >= input_Coordinates.y ||
                this.CurrentGameBoard.MinMaxBoundaries.w <= input_Coordinates.y))
        {

            if (this.Side == UP)
            {
                tileTracer.SetValues(input_Coordinates);

                tileTracer.y++;

                if (this.Color == Tile.BLACK)
                {
                    tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
                }

                this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

                GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
            }
            else if (this.Side == DOWN)
            {
                tileTracer.SetValues(input_Coordinates);

                tileTracer.y--;

                if (this.Color == Tile.BLACK)
                {
                    tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
                }

                this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

                GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
            }
            else if (this.Side == LEFT)
            {
                tileTracer.SetValues(input_Coordinates);

                tileTracer.x--;

                if (this.Color == Tile.BLACK)
                {
                    tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
                }

                this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

                GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
            }
            else if (this.Side == RIGHT)
            {
                tileTracer.SetValues(input_Coordinates);

                tileTracer.x++;

                if (this.Color == Tile.BLACK)
                {
                    tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
                }

                this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

                GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
            }

        }
        else if ((this.CurrentGameBoard.MinMaxBoundaries.x >= input_Coordinates.x ||
                this.CurrentGameBoard.MinMaxBoundaries.y <= input_Coordinates.x ||
                this.CurrentGameBoard.MinMaxBoundaries.z >= input_Coordinates.y ||
                this.CurrentGameBoard.MinMaxBoundaries.w <= input_Coordinates.y))
        {

            this.SwitchSide = true;
            tileTracer.SetValues(this.Coordinates);
            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);

        }
        else
        {
            this.Side = UP;
            return this.Possible_Moves;
        }
        return null;
    }
}
