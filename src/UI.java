import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;

public class UI extends JPanel
{

    JButton button = new JButton("CREATE GAME");
    JFrame frame_reference;
    Math.Vec2<Float> BoardSize = new Math.Vec2<>(0.0f,0.0f);
    Math.Vec2<Float> BoardLocation = new Math.Vec2<>(0.0f,0.0f);

    Math.Vec2<Float> UIsize = new Math.Vec2<>(0.0f,0.0f);


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
        public void SetCollisionAttribs(int x , int y , int width , int height)
        {
            this.collisionBox.SetValues(x, y, width, height);
        }
        public void SetDrawingAttribs(int x , int y , int width , int height , Color color)
        {
            this.Drawingattrib.SetValues(x,y,width,height);
            this.color = color;
        }
        public boolean IsClicked()
        {
            return this.collisionBox.CheckCollisionBoxMouse(this.mouselistenerReference.GetMousePos());
        }

        public Double SlideAmount()
        {
            if(IsClicked())
            {
                ClickkedInitialPosition.SetValues(this.mouselistenerReference.GetMousePos());
            }

            if (ClickkedInitialPosition.x != null)
            {
                System.out.println("SLIDE AMOUNT: " + (this.mouselistenerReference.GetMousePos().x - ClickkedInitialPosition.x));

                return this.mouselistenerReference.GetMousePos().x - ClickkedInitialPosition.x;
            }

            return 1.0;

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
                g.fillRoundRect(Drawingattrib.x,Drawingattrib.y , Drawingattrib.z, Drawingattrib.w, roundness , roundness);
            }
            else if(Shape == SHARP)
            {
                g.fillRect(Drawingattrib.x,Drawingattrib.y , Drawingattrib.z, Drawingattrib.w);
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
        //System.out.println("UI SIZE x: " + UIsize.x + " Y: " + UIsize.y);
    }

    public void Update()
    {
        button.setLocation((int) (BoardLocation.x.intValue() + BoardSize.x ), (int) (BoardLocation.y + BoardSize.y / 2));
        //button.setLocation(BoardSize.x.intValue(), (int) (BoardLocation.y + BoardSize.y / 2));
        //button.setSize((int) (100 * scale_coeffic), (int) (50 * scale_coeffic));

        button.setPreferredSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));
        button.setSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));

        UIsliderBar.SetDrawingAttribs((int) (BoardLocation.x.intValue() + BoardSize.x ) ,BoardLocation.y.intValue(),(int)(UIsize.x * 0.10f),UIsize.y.intValue(), Color.BLACK);
        UIsliderBar.SetCollisionAttribs((int) ((BoardLocation.x.intValue() + BoardSize.x ) + ((UIsize.x * 0.10f)/2)),
                                              (int) ((BoardLocation.y.intValue() + BoardSize.y ) + ((UIsize.y * 0.10f)/2)),
                                               (int)(UIsize.x * 0.10f),
                                               2* UIsize.y.intValue());



       // UIsliderBar.SlideAmount();

        //System.out.println("BUTTON SIZE X: " + button.getSize().getWidth() + " Y: " + button.getSize().getHeight());
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        UIsliderBar.paintComponent(g);
        /*g.setColor(Color.GRAY.darker());
        g.fillRect((int) (BoardLocation.x.intValue() + BoardSize.x), BoardLocation.y.intValue() , UIsize.x.intValue(),UIsize.y.intValue());
        g.setColor(Color.GRAY);
        g.fillRoundRect((int) (BoardLocation.x.intValue() + BoardSize.x), BoardLocation.y.intValue() , (int) (UIsize.x * 0.90f),(int) (UIsize.y * 0.90f),100,100);*/


    }
}
