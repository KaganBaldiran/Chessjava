import java.util.Vector;

public class Pawn extends piece{

    boolean First_turn;
    
    Math.Vec2<Integer> tileTracer;

    Pawn(int x_cord , int y_cord , int color)
    {
        super(x_cord, y_cord, color);
        this.First_turn = true;
        this.tileTracer = new Math.Vec2<>();
    }
    Pawn(int x_cord, int y_cord , int color , Tile TilePieceStandingOn)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn);
    }
    Pawn(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard);
    }

        @Override
    public void Move(int newX, int newY) {
        this.Coordinates.SetValues(newX,newY);
    }

    @Override
    public void capture()
    {

    }

    @Override
    public Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty) {

        if (isTileEmpty)
        {
            tileTracer.SetValues(this.Coordinates);
            if (this.First_turn)
            {
                tileTracer.y += 2;
                this.First_turn = false;

                GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty());

            }
            else if (!this.First_turn)
            {
                tileTracer.y += 1;
                System.out.println(String.valueOf(tileTracer.x) + " " + String.valueOf(tileTracer.y));
                GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty());
            }

            if (this.Color == Tile.BLACK)
            {
                Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

        }
        else if(!isTileEmpty)
        {

            if(!this.Possible_Moves.isEmpty())
            {
                this.Possible_Moves.clear();
            }

            Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y-1));

            return this.Possible_Moves;
        }
        return null;
    }
}
