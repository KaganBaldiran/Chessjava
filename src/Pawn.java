import java.util.Vector;

public class Pawn extends piece{

    boolean First_turn;
    Vector<Math.Vec2<Integer>> PossibleMoves;

    Pawn(int x_cord , int y_cord , int color){

        super(x_cord, y_cord, color);
        this.First_turn = true;

    }
    @Override
    public void Move(int newX, int newY) {
        this.Coordinates.SetValues(newX,newY);
    }

    @Override
    public void capture() {

    }

    @Override
    public Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty) {

        if (isTileEmpty)
        {
            Math.Vec2<Integer> tileTracer = new Math.Vec2<>(this.Coordinates);
            if (this.Color == Tile.WHITE)
            {
                if (this.First_turn)
                {
                    tileTracer.y += 2;
                    GetPossibleMoves(this.IsTileEmpty(tileTracer.y));
                    this.First_turn = false;
                }
                else if (!this.First_turn)
                {
                    tileTracer.y += 1;
                    GetPossibleMoves(this.IsTileEmpty(tileTracer.y));
                }
            }
            else if (this.Color == Tile.BLACK)
            {
                if (this.First_turn)
                {
                    tileTracer.y -= 2;
                    GetPossibleMoves(this.IsTileEmpty(tileTracer.y));
                    this.First_turn = false;

                }
                else if (!this.First_turn)
                {
                    tileTracer.y -= 1;
                    GetPossibleMoves(this.IsTileEmpty(tileTracer.y));

                }
            }

        }
        return null;
    }
}
