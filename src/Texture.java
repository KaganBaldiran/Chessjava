import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Texture extends JPanel
{

    Math.Vec2<Integer> Position = new Math.Vec2<>();
    private Image textureimage;
    Texture(String file_path)
    {
        try
        {
            textureimage = ImageIO.read(new File(file_path));
        } catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DrawTexture(g, Position.x,Position.y,getWidth(),getHeight());
    }

    public void DrawTexture(Graphics g, int x, int y, int width, int height)
    {
        g.drawImage(textureimage,x, y, width,height,null);

    }
}
