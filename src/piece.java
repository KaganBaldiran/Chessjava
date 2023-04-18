import java.util.Vector;

public abstract class piece {

    Math.Vec2<Integer> Coordinates;

    Vector<Math.Vec2<Integer>> Possible_Moves;

    int Color;

    piece(int x_cord, int y_cord , int color)
    {
       this.Coordinates.SetValues(x_cord,y_cord);
       this.Color = color;
    }

    public abstract void Move(int newX,int newY);
    public abstract void capture();
    public abstract Vector<Math.Vec2<Integer>> GetPossibleMoves();

}


