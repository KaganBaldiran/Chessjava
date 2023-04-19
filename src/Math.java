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
        Vec2(type x , type y)
        {
            this.x = x;
            this.y = y;
        }
        Vec2(Math.Vec2<type> Input)
        {
            this.x = Input.x;
            this.y = Input.y;
        }

        void SetValues(type x , type y)
        {
            this.x = x;
            this.y = y;
        }

        void SetValues(Vec2<type> Input)
        {
            this.x = Input.x;
            this.y = Input.y;
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

        Vec3(type x , type y , type z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        Vec3(Vec3<type> Input)
        {
            this.x = Input.x;
            this.y = Input.y;
            this.y = Input.y;
        }

        void SetValues(type x , type y , type z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        void SetValues(Vec3<type> Input)
        {
            this.x = Input.x;
            this.y = Input.y;
            this.y = Input.y;
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
        Vec4(type x , type y , type z , type w)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        Vec4(Vec4<type> Input)
        {
            this.x = Input.x;
            this.y = Input.y;
            this.y = Input.y;
            this.w = Input.w;
        }

        void SetValues(type x , type y , type z , type w)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        void SetValues(Vec4<type> Input)
        {
            this.x = Input.x;
            this.y = Input.y;
            this.y = Input.y;
            this.w = Input.w;
        }

    }


}
