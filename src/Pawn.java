import java.util.Vector;

public class Pawn extends piece{

    boolean First_turn;
    
    Math.Vec2<Integer> tileTracer;

    Pawn(int x_cord , int y_cord , int color){

        super(x_cord, y_cord, color);
        this.First_turn = true;
        Math.Vec2<Integer> tileTracer = new Math.Vec2<>();

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

            if (this.Color == Tile.WHITE)
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
                    GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty());
                }
            }
            else if (this.Color == Tile.BLACK)
            {
                tileTracer.SetValues(this.Coordinates);
                if (this.First_turn)
                {
                    tileTracer.y -= 2;
                    this.First_turn = false;
                    GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty());

                }
                else if (!this.First_turn)
                {
                    tileTracer.y -= 1;
                    GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty());

                }
            }

        }
        else if(!isTileEmpty)
        {

            if(!this.Possible_Moves.isEmpty())
            {
                this.Possible_Moves.clear();
            }

            if (this.Color == Tile.WHITE)
            {
                Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y-1));
            }
            else if (this.Color == Tile.BLACK)
            {
                Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y+1));
            }

            return this.Possible_Moves;
        }
        return null;
    }
}
