import java.util.Vector;

public class Knight extends piece
{

    public static final int UP_LEFT = 1;
    public static final int UP_RIGHT = 2;
    public static final int DOWN_LEFT = 3;
    public static final int DOWN_RIGHT = 4;
    public static final int LEFT_UP = 5;
    public static final int LEFT_DOWN = 6;
    public static final int RIGHT_UP = 7;
    public static final int RIGHT_DOWN = 8;


    Knight(int x_cord , int y_cord , int color)
    {
        super(x_cord, y_cord, color);
    }

    Knight(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , String file_path)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard , file_path);
    }


    @Override
    public void Move(int newX, int newY) {
        this.Coordinates.SetValues(newX,newY);
    }

    @Override
    public void capture() {

    }

    boolean SwitchSide = false;

    Math.Vec2<Integer> tileTracer = new Math.Vec2<>();
    int Side = UP_LEFT;

    @Override
    public Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty, Math.Vec2<Integer> input_Coordinates) {

        input_Coordinates = this.Coordinates;

        if(!this.Possible_Moves.isEmpty() && !isTileEmpty || this.ClearPossibleMoves)
        {
            this.ClearPossibleMoves = false;
            this.Possible_Moves.clear();
        }

        if(this.SwitchSide)
        {
            this.Side++;
            this.SwitchSide = false;
        }

        if (this.Side == UP_LEFT && input_Coordinates.y < 7 && input_Coordinates.x > 1)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y += 2;
            tileTracer.x--;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == UP_RIGHT  && input_Coordinates.y < 7 && input_Coordinates.x < 8)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y += 2;
            tileTracer.x++;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == DOWN_LEFT  && input_Coordinates.y > 2 && input_Coordinates.x > 1)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y -= 2;
            tileTracer.x--;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == DOWN_RIGHT  && input_Coordinates.y > 2 && input_Coordinates.x < 8)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y -= 2;
            tileTracer.x++;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == LEFT_UP  && input_Coordinates.y < 8  && input_Coordinates.x > 2)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x -= 2;
            tileTracer.y++;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == LEFT_DOWN  && input_Coordinates.y > 1  && input_Coordinates.x > 2)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x -= 2;
            tileTracer.y--;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == RIGHT_UP && input_Coordinates.y < 8  && input_Coordinates.x < 8)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x += 2;
            tileTracer.y++;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == RIGHT_DOWN && input_Coordinates.y > 1  && input_Coordinates.x < 8)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x += 2;
            tileTracer.y--;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if(this.Side <= RIGHT_DOWN)
        {
            this.SwitchSide = true;
            tileTracer.SetValues(this.Coordinates);
            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);

        }

        if(!isTileEmpty || this.Side > RIGHT_DOWN)
        {
            System.out.println("QUEEN POSSIBLE MOVES RETURN THE VALUE: ");
            this.Side = UP_LEFT;
            this.ClearPossibleMoves = true;
            return this.Possible_Moves;
        }
        return null;
    }
}
