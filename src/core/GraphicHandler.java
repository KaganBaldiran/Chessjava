package core;

import javax.swing.*;
import java.awt.*;

public class GraphicHandler extends JPanel
{
    Board current_board;
    Player player1;
    Player player2;
    JPanel UIpanel;
    JButton button = new JButton("random button");

    GraphicHandler(Board br)
    {
        this.current_board = br;
    }


    public GraphicHandler(Board br, Player player1, Player player2)
    {
        this.current_board = br;
        this.player1 = player1;
        this.player2 = player2;
    }

    GraphicHandler(Board br ,Player player1 , Player player2, JPanel UIpanel)
    {
        this.current_board = br;
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    protected void paintComponent(Graphics g)
    {

        super.paintComponent(g);

        this.current_board.paintComponent(g);

        if(player1 != null) {
            player1.paintComponent(g);
        }

        if(player2 != null) {
            player2.paintComponent(g);
        }

       g.dispose();
    }

    public void Update()
    {



    }


        public static Color HexToRgba(String hexCode) {
            hexCode = hexCode.trim();

            if (!hexCode.matches("^#[0-9A-Fa-f]{6}$")) {
                throw new IllegalArgumentException("Invalid HEX code format");
            }

            int red = Integer.parseInt(hexCode.substring(1, 3), 16);
            int green = Integer.parseInt(hexCode.substring(3, 5), 16);
            int blue = Integer.parseInt(hexCode.substring(5, 7), 16);


            int alpha = 255;

            if (hexCode.length() == 9) {
                alpha = Integer.parseInt(hexCode.substring(7, 9), 16);
            }

            return new Color(red, green, blue, alpha);
        }

        public static float GetScreenScaleCoefficient(JFrame frame , Canvas canvas , Dimension ScreenSize)
        {
            //double scaledWidth = (float)(frame.getSize().getWidth() / ScreenSize.getWidth());
            //double scaledHeight = (float)(frame.getSize().getHeight() / ScreenSize.getHeight());

            float scaledWidth = (float)(frame.getContentPane().getWidth() / ScreenSize.getWidth());
            float scaledHeight = (float)(frame.getContentPane().getHeight() / ScreenSize.getHeight());

            return java.lang.Math.min(scaledHeight , scaledWidth);
        }







}
