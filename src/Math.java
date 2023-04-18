public class Math {

    public static class Vec2<type>
    {
        type x;
        type y;

        void SetValues(type x , type y)
        {
            this.x = x;
            this.y = y;
        }
    }
    public static class Vec3<type>
    {
        type x;
        type y;
        type z;

        void SetValues(type x , type y , type z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    public static class Vec4<type>
    {
        type x;
        type y;
        type z;
        type w;

        void SetValues(type x , type y , type z ,type w)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

    }


}
