package core;

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

        Math.Vec4<Float> cb1 = new Math.Vec4<>( (float)this.x + (this.width / 2) ,
                                                (float)this.x - (this.width / 2) ,
                                               (float)this.y + (this.height / 2) ,
                                               (float)this.y - (this.height / 2));


        Math.Vec4<Float> cb2 = new Math.Vec4<>( (float)Box_to_check.x + (Box_to_check.width / 2) ,
                                                (float)Box_to_check.x - (Box_to_check.width / 2) ,
                                                (float)Box_to_check.y + (Box_to_check.height / 2) ,
                                                (float)Box_to_check.y - (Box_to_check.height / 2));


        if ((cb1.x >= cb2.y && cb1.x <= cb2.x) || (cb1.y >= cb2.y && cb1.y <= cb2.x) || (cb2.x >= cb1.y && cb2.x <= cb1.x) || (cb2.y >= cb1.y && cb2.y <= cb1.x))
        {
            if ((cb1.z >= cb2.w && cb1.z <= cb2.w) || (cb1.w >= cb2.w && cb1.w <= cb2.z) || (cb2.z >= cb1.w && cb2.z <= cb1.z) || (cb2.w >= cb1.w && cb2.w <= cb1.z))
            {

                return true;

            }

        }
        return false;
    }

    boolean CheckCollisionBoxMouse(Math.Vec2<Double> MousePos)
    {

        if ((MousePos.x < (this.x + (this.width) / 2)) && (MousePos.x > (this.x - (this.width) / 2)))
        {
            return (MousePos.y < (this.y + (this.height) / 2)) && (MousePos.y > (this.y - (this.height) / 2));
        }
        return false;
    }


}
