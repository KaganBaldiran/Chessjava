import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.awt.event.MouseAdapter;


import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;


public class Game extends JPanel implements Runnable
{

    Canvas current_canvas;

    Board chessBoard;

    GraphicHandler gh;

    InputHandler input_handler;

    BufferedImage bufferedImage;

    boolean isRunning;

    //MouseInputListener mouseListener = new MouseInputListener();
    MouseInputListener mouseListener;

    Player whiteplayer;
    Player blackplayer;

    Dimension ScreenSize;

    boolean allow_click[];

    Math.Vec2<Float> FBO_position = new Math.Vec2<>();


    javax.swing.JFrame frame;


    Game()
    {



        chessBoard = new Board();


        frame = new javax.swing.JFrame("Chess");
        MouseInputListener mouseListener = new MouseInputListener(frame);
        this.mouseListener = mouseListener;

        frame.addMouseListener(mouseListener);

        ScreenSize = Toolkit.getDefaultToolkit().getScreenSize().getSize();

        ScreenSize.setSize(ScreenSize.getHeight() * 0.90f , ScreenSize.getHeight() * 0.90f);

        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        System.out.println("FRAME WIDTH: " + frame.getWidth() + " FRAME HEIGHT: " + frame.getHeight());
        System.out.println("SCREEN WIDTH: " + ScreenSize.getWidth() + " SCREEN HEIGHT: " + ScreenSize.getHeight());

        this.whiteplayer = new Player(Tile.WHITE,this.chessBoard,this.mouseListener);
        this.blackplayer = new Player(Tile.BLACK,this.chessBoard,this.mouseListener);



        this.input_handler = new InputHandler();

        this.isRunning = true;

        this.allow_click = new boolean[4];


        //gh = new GraphicHandler(chessBoard, newqueen, newknight, newPawn, newrook, newking, newBishop);
        gh = new GraphicHandler(chessBoard, this.whiteplayer,this.blackplayer);
        //this.newBishop = (Bishop) whiteplayer.pieces.get(1);


        frame.setFocusable(true);



        current_canvas = new Canvas();
        //current_canvas.setPreferredSize();
        current_canvas.setSize(new Dimension((int)ScreenSize.getWidth(), (int)ScreenSize.getHeight()));
        current_canvas.setFocusable(true);
        current_canvas.requestFocus();





        frame.addKeyListener(input_handler);

        gh.add(current_canvas);



        frame.add(current_canvas);


        current_canvas.addMouseListener(mouseListener);



        frame.pack();
        frame.setSize((int)ScreenSize.getWidth(), (int)ScreenSize.getHeight());


        current_canvas.createBufferStrategy(3);


        bufferedImage = new BufferedImage(this.current_canvas.getWidth(), this.current_canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);

        System.out.println("this.current_canvas.getWidth() X :  " + this.current_canvas.getWidth() + " Y: " + this.current_canvas.getHeight());

    }
    
    @Override
    public void run()
    {

        System.out.println("this.current_canvas.getWidth() X :  " + this.current_canvas.getWidth() + " Y: " + this.current_canvas.getHeight());

        while (isRunning) {



            whiteplayer.GetPosssibleMoves();


            whiteplayer.MovePlayerPieces();



            System.out.println("ENTITY 1 X: "+whiteplayer.pieces.get(0).Coordinates.x + " Y: " + whiteplayer.pieces.get(0).Coordinates.y);



            BufferStrategy bufferstrategy = current_canvas.getBufferStrategy();
            Graphics graphics = bufferstrategy.getDrawGraphics();

            Graphics2D bufferedGraphics = bufferedImage.createGraphics();

            gh.paintComponent(bufferedGraphics);




            double final_scale_coeffi = GraphicHandler.GetScreenScaleCoefficient(frame , this.ScreenSize);


            float scaledWidth = (int)(bufferedImage.getWidth() * final_scale_coeffi);
            float scaledHeight = (int)(bufferedImage.getHeight() * final_scale_coeffi);


            FBO_position.SetValues(frame.getWidth() - scaledWidth , frame.getHeight() - scaledHeight);

            System.out.println("bufferedImage.getWidth(): " + bufferedImage.getWidth()+ " bufferedImage.getHeight(): " + bufferedImage.getHeight());

            graphics.drawImage(bufferedImage, (int)(FBO_position.x / 2), (int)(FBO_position.y / 2), (int)scaledWidth, (int)scaledHeight, current_canvas);


            graphics.dispose();
            bufferstrategy.show();

        }


    }

}
