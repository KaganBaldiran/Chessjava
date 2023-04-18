public class Math {

    public static class Vec2<type>
    {
        type x;
        type y;

        Vec2()
        {
            this.x = null;
            this.y = null;
        }

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

        Vec3()
        {
            this.x = null;
            this.y = null;
            this.z = null;
        }

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

        Vec4()
        {
            this.x = null;
            this.y = null;
            this.z = null;
            this.w = null;
        }

        void SetValues(type x , type y , type z , type w)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

    }


}
