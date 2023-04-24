public class CollisionBox
{
    int x;
    int y;
    int width;
    int height;

    CollisionBox()
    {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;

    }
    CollisionBox(int x , int y ,int width , int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }
    void SetValues(int x , int y ,int width , int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void SetValues(CollisionBox Box_to_initialize_with)
    {
        this.x = Box_to_initialize_with.x;
        this.y = Box_to_initialize_with.y;
        this.width = Box_to_initialize_with.width;
        this.height = Box_to_initialize_with.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    boolean CheckCollisionBoxBox(CollisionBox Box_to_check)
    {

        return false;
    }

    boolean CheckCollisionBoxMouse(Math.Vec2<Double> MousePos)
    {

        return false;
    }


}
