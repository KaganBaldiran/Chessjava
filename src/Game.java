import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;


public class Game extends JPanel implements Runnable
{

    Queen Selected_piece;
    Knight newknight;
    Pawn newPawn;
    Bishop newBishop;
    Rook newrook;
    King newking;
    Queen newqueen;
    JFrame frame;

    Canvas current_canvas;

    Board chessBoard;

    GraphicHandler gh;

    InputHandler input_handler;

    boolean isRunning;

    BufferedImage buffer = new BufferedImage(1025, 1025, BufferedImage.TYPE_INT_ARGB);

    boolean allow_click[];


    Game()
    {
        frame = new JFrame("Chess Board");
        chessBoard = new Board();
        newqueen = new Queen(7,5,Tile.WHITE);


        newknight = new Knight(5,5,Tile.WHITE);

        newPawn = new Pawn(1,1,Tile.WHITE);
        newBishop = new Bishop(1,1,Tile.WHITE);
        newrook = new Rook(8,8,Tile.WHITE);
        newking = new King(5,5,Tile.WHITE);

        this.input_handler = new InputHandler();

        this.isRunning = true;

        this.allow_click = new boolean[4];



        gh = new GraphicHandler(chessBoard, newqueen, newknight, newPawn, newrook, newking, newBishop);


        frame = new JFrame("My Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        current_canvas = new Canvas();
        current_canvas.setPreferredSize(new Dimension(1025, 1025));
        current_canvas.setFocusable(false);
        current_canvas.requestFocus();

        frame.addKeyListener(input_handler);

        gh.add(current_canvas);
        frame.add(current_canvas);


        frame.pack();


        current_canvas.createBufferStrategy(3);
    }



    @Override
    public void run()
    {

        newrook.GetPossibleMoves(true , newrook.Coordinates);

        newking.GetPossibleMoves(true , newking.Coordinates);

        newPawn.GetPossibleMoves(true, newPawn.Coordinates);

        newknight.GetPossibleMoves(true,newknight.Coordinates);



        while (isRunning) {



            if (input_handler.isReleased(KeyEvent.VK_DOWN))
            {
                allow_click[0] = true;
            }
            if (input_handler.isReleased(KeyEvent.VK_UP))
            {
                allow_click[1] = true;
            }
            if (input_handler.isReleased(KeyEvent.VK_LEFT))
            {
                allow_click[2] = true;
            }
            if (input_handler.isReleased(KeyEvent.VK_RIGHT))
            {
                allow_click[3] = true;
            }


                if (input_handler.isPressed(KeyEvent.VK_DOWN) && this.newBishop.Coordinates.y < 8 && allow_click[0]) {
                    //this.newqueen.Coordinates.y++;
                    this.newBishop.Coordinates.y++;
                    allow_click[0] = false;
                }

                if (input_handler.isPressed(KeyEvent.VK_UP) && this.newBishop.Coordinates.y > 1 & allow_click[1]) {
                    //this.newqueen.Coordinates.y--;
                    this.newBishop.Coordinates.y--;
                    allow_click[1] = false;
                }

                if (input_handler.isPressed(KeyEvent.VK_LEFT) && this.newBishop.Coordinates.x > 1 && allow_click[2]) {
                    //this.newqueen.Coordinates.x--;
                    this.newBishop.Coordinates.x--;
                    allow_click[2] = false;
                }

                if (input_handler.isPressed(KeyEvent.VK_RIGHT) && this.newBishop.Coordinates.x < 8 && allow_click[3]) {
                    this.newBishop.Coordinates.x++;
                    allow_click[3] = false;
                }





            //newqueen.GetPossibleMoves(true,newqueen.Coordinates);
            newBishop.GetPossibleMoves(true,newBishop.Coordinates);


            // Draw the BufferedImage to the Canvas


            System.out.println(" QUEEN COORDINATES: " + String.valueOf(newqueen.Coordinates.x) + " " + String.valueOf(newqueen.Coordinates.y));

            BufferStrategy bufferstrategy = current_canvas.getBufferStrategy();
            Graphics graphics = bufferstrategy.getDrawGraphics();

            gh.paintComponent(graphics);

            graphics.dispose();
            bufferstrategy.show();




        }


    }
}
