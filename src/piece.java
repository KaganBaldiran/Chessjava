public abstract class piece {

    Math.Vec2<Integer> Coordinates;
    int Color;

    piece(int x_cord, int y_cord , int color)
    {
       this.Coordinates.SetValues(x_cord,y_cord);
       this.Color = color;
    }

    public abstract void Move(int newX,int newY);
    public abstract void capture();

}


