import java.util.Vector;

public class Rook extends piece{

    Rook(int x_cord,int y_cord , int color)
    {
        super(x_cord, y_cord, color);
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
        return null;
    }
}