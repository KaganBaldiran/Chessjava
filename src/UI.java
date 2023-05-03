import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UI extends JPanel
{

    JButton button = new JButton("random button");
    JFrame frame_reference;
    Math.Vec2<Float> BoardSize = new Math.Vec2<>(0.0f,0.0f);
    Math.Vec2<Float> BoardLocation = new Math.Vec2<>();

    Math.Vec2<Float> UIsize = new Math.Vec2<>();


    float scale_coeffic = 0;

    UI(JFrame frame) {
        button.setSize(100, 50); // set size to 100x50 pixels
        button.setLocation(0, 0);
        frame_reference = frame;
        frame_reference.add(button);
        //this.add(button);
    }

    public void UpdateBoardAttribs(Math.Vec2<Float> Location , float scale_coeffi)
    {
        this.BoardSize.SetValues((float) (Board.SQUARE_SIZE_non_GUI * 8), (float) (Board.SQUARE_SIZE_non_GUI * 8));
        this.BoardLocation = Location;
        this.scale_coeffic = scale_coeffi;
    }

    public void setUIsize(int FrameWidth , int FrameHeight , float BoardWidth , float BoardHeight) {
        this.UIsize.SetValues(FrameWidth - BoardWidth, FrameHeight - BoardHeight);
    }

    public void Update()
    {
        button.setLocation((int) (BoardLocation.x.intValue() + BoardSize.x ), (int) (BoardLocation.y + BoardSize.y / 2));
        //button.setLocation(BoardSize.x.intValue(), (int) (BoardLocation.y + BoardSize.y / 2));
        //button.setSize((int) (100 * scale_coeffic), (int) (50 * scale_coeffic));

        button.setPreferredSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));
        button.setSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));





        System.out.println("BUTTON SIZE X: " + button.getSize().getWidth() + " Y: " + button.getSize().getHeight());
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.fillRect(BoardSize.x.intValue() , 0 , 100,100);
    }
}
