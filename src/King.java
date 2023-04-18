public class King extends piece
{
    King(int x_cord , int y_cord , int color)
    {
        super(x_cord, y_cord, color);
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
}
