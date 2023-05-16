import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static java.awt.MouseInfo.getNumberOfButtons;

public class MouseInputListener extends MouseAdapter {

    boolean clickked[];
    Math.Vec2<Double> MousePos = new Math.Vec2<>();
    JFrame frame;

    MouseInputListener(JFrame frame)
    {
        clickked = new boolean[getNumberOfButtons()];
        this.frame = frame;
    }

    public boolean isClicked(int keyCode) {
        return this.clickked[keyCode];
    }

    public boolean isReleased(int keyCode) {
        return !this.clickked[keyCode];
    }

    public Math.Vec2<Double> GetMousePos()
    {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point componentLocationOnScreen = frame.getLocationOnScreen();
        int x = mouseLocation.x - componentLocationOnScreen.x;
        int y = mouseLocation.y - componentLocationOnScreen.y;

        MousePos.SetValues((double)x,(double)y);
        return MousePos;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("Mouse Clicked: X=" + e.getX() + ", Y=" + e.getY());
        //this.clickked[e.getButton()] = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /*System.out.println("Mouse Pressed: X=" + e.getX() + ", Y=" + e.getY());
        if (e.getButton() == MouseEvent.BUTTON1) {
            System.out.println("Left Mouse Button Pressed");
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            System.out.println("Middle Mouse Button Pressed");
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("Right Mouse Button Pressed");
        }*/

        this.clickked[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.println("Mouse Released: X=" + e.getX() + ", Y=" + e.getY());
        this.clickked[e.getButton()] = false;
    }
}
