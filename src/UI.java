import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class UI extends JPanel
{

    JButton button = new JButton("CREATE GAME");
    JFrame frame_reference;
    Math.Vec2<Float> BoardSize = new Math.Vec2<>(0.0f,0.0f);
    Math.Vec2<Float> BoardLocation = new Math.Vec2<>(0.0f,0.0f);

    Math.Vec2<Float> UIsize = new Math.Vec2<>(0.0f,0.0f);

    public static class UIcomponents
    {
        public BufferedImage componentImage;
        public Graphics2D bufferedGraphics;
        UIcomponents(int width , int height , int imagetype)
        {
            this.componentImage = new BufferedImage(width,height,imagetype);
            bufferedGraphics = componentImage.createGraphics();
        }
        public void drawUIcomponent(Graphics g , int x , int y , int width , int height , ImageObserver observer)
        {
            bufferedGraphics = componentImage.createGraphics();
            g.drawImage(this.componentImage,x,y,width,height,observer);

        }
    }

    public static class SliderBar extends JPanel
    {
        public CollisionBox collisionBox;
        Math.Vec4<Integer> Drawingattrib = new Math.Vec4<>();
        Color color;
        public int Shape;
        public final static int ROUND = 10000;
        int roundness = 0;
        public final static int SHARP = 20000;
        MouseInputListener mouselistenerReference;

        Math.Vec2<Double> ClickkedInitialPosition = new Math.Vec2<>();

        boolean initialized = false;

        Math.Vec2<Integer> initialXvalues = new Math.Vec2<>(0,0);

        Math.Vec4<Integer> ComponentSizes = new Math.Vec4<>(0,0,0,0);

        SliderBar(int Shape , MouseInputListener Mouselistener)
        {
            collisionBox = new CollisionBox();
            this.Shape = Shape;
            this.color = new Color(0);
            this.mouselistenerReference = Mouselistener;
        }
        SliderBar(int Shape , int roundness , MouseInputListener Mouselistener)
        {
            collisionBox = new CollisionBox();
            this.Shape = Shape;
            this.roundness = roundness;
            this.color = new Color(0);
            this.mouselistenerReference = Mouselistener;
        }

        int slided_x_position = 0;
        int permanent_slide_position = 0;
        public void SetCollisionAttribs(int x , int y , int width , int height )
        {
            this.collisionBox.SetValues(x, y, width, height);
            initialXvalues.x = x;
        }
        public void SetDrawingAttribs(int x , int y , int width , int height , Color color )
        {
            this.Drawingattrib.SetValues(x ,y,width,height);
            initialXvalues.y = x;
            this.color = color;
        }

        public void Update(float scalecoeff , int BoarddSizeX , )
        {



            if(SlideAmount() != 0.0)
            {
                slided_x_position = SlideAmount().intValue();

            }

            float coefficent = (float) slided_x_position / (BoarddSizeX * scalecoeff);

            collisionBox.x = collisionBox.x + (int)(BoarddSizeX * coefficent);
            this.Drawingattrib.x = Drawingattrib.x + (int)(BoarddSizeX * coefficent);


            //ReverseUpdate();

            System.out.println("SLIDE AMOUNT: " + BoarddSizeX);

        }

        float ScaleCoefBoardSizeLeftCom = 0.0f;

        public void CalculateComponentSizes(int BoardSize_x , int BoardSize_y)
        {
            ComponentSizes.x = BoardSize_x - Drawingattrib.x;
            ComponentSizes.z = (Drawingattrib.x-BoardSize_y);

            ScaleCoefBoardSizeLeftCom = ComponentSizes.z.floatValue() / BoardSize_x;

            System.out.println("COMPONENT 1 SIZE X: " + ComponentSizes.x);
            System.out.println("COMPONENT 2 SIZE X: " + ComponentSizes.z);

        }

        public void ReverseUpdate()
        {
            collisionBox.x = collisionBox.x - slided_x_position;
            this.Drawingattrib.x = this.Drawingattrib.x - slided_x_position;

        }

        public boolean IsClicked()
        {
            return this.collisionBox.CheckCollisionBoxMouse(this.mouselistenerReference.GetMousePos());
        }

        boolean pressed = false;
        boolean released = false;

        double last_size = 0.0;


        public Double SlideAmount()
        {

            System.out.println("IS CLICKED : " + IsClicked());

            if(this.mouselistenerReference.isReleased(MouseEvent.BUTTON1))
            {
                released = true;
                pressed = false;
            }

            if(IsClicked() && this.mouselistenerReference.isClicked(MouseEvent.BUTTON1) && released)
            {
                ClickkedInitialPosition.SetValues(this.mouselistenerReference.GetMousePos());
                pressed = true;
                released = false;
            }

            if (ClickkedInitialPosition.x != null && pressed && !released)
            {

                if (this.mouselistenerReference.GetMousePos().x - ClickkedInitialPosition.x != 0.0)
                {
                    last_size = this.mouselistenerReference.GetMousePos().x - ClickkedInitialPosition.x;
                    return this.mouselistenerReference.GetMousePos().x - ClickkedInitialPosition.x;
                }

            }

            return 0.0;

        }
        @Override
        protected void paintComponent(Graphics g) {

            if(!IsClicked())
            {
                g.setColor(color);
            }
            else
            {
                g.setColor(Color.darkGray);
            }

            if(Shape == ROUND)
            {
                if(roundness <= 0)
                {
                    throw new RuntimeException("Roundness value is too small :: SliderBar");
                }

                g.fillRoundRect(Drawingattrib.x ,Drawingattrib.y , Drawingattrib.z, Drawingattrib.w, roundness , roundness);
            }
            else if(Shape == SHARP)
            {
                g.fillRect(Drawingattrib.x ,Drawingattrib.y , Drawingattrib.z, Drawingattrib.w);

            }
        }
    }

    SliderBar UIsliderBar;


    float scale_coeffic = 0;

    UI(JFrame frame ,MouseInputListener Mouselistener) {
        button.setSize(100, 50);
        button.setFont(new Font("Arial", Font.PLAIN, 9));
        button.setLocation(0, 0);
        frame_reference = frame;
        frame_reference.add(button);
        UIsliderBar = new SliderBar(SliderBar.ROUND , 7,Mouselistener);
        //this.add(button);
    }

    public void UpdateBoardAttribs(Math.Vec2<Float> Location , float scale_coeffi)
    {
        this.BoardSize.SetValues((float) (Board.SQUARE_SIZE_non_GUI * 8), (float) (Board.SQUARE_SIZE_non_GUI * 8));
        this.BoardLocation = Location;
        this.scale_coeffic = scale_coeffi;
    }

    public void setUIsize(int FrameWidth , int FrameHeight , float BoardWidth , float BoardHeight) {
        this.UIsize.SetValues(FrameWidth - BoardWidth, BoardHeight);
    }



    public void Update()
    {


            UIsliderBar.SetDrawingAttribs((int) (BoardLocation.x.intValue() + BoardSize.x ) ,
                    BoardLocation.y.intValue(),(int)(UIsize.x * 0.10f),
                    UIsize.y.intValue(),
                    Color.BLACK);

            UIsliderBar.SetCollisionAttribs((int) ((BoardLocation.x.intValue() + BoardSize.x ) + ((UIsize.x * 0.10f)/2)) ,
                    (int) ((BoardLocation.y.intValue() + BoardSize.y ) + ((UIsize.y * 0.10f)/2)),
                    (int)(UIsize.x * 0.10f),
                    2* UIsize.y.intValue());




        button.setLocation((int) (BoardLocation.x.intValue() + BoardSize.x ), (int) (BoardLocation.y + BoardSize.y / 2));

        button.setPreferredSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));
        button.setSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));

        UIsliderBar.Update(scale_coeffic , BoardSize.x.intValue() + (int)((BoardSize.x/9)*2));

        UIsliderBar.CalculateComponentSizes(BoardSize.x.intValue() + (int)((BoardSize.x/9)*2) + BoardLocation.x.intValue() , BoardLocation.x.intValue() );


    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        UIsliderBar.paintComponent(g);
    }
}
