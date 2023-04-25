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


    //JFrame frame;

    Canvas current_canvas;

    Board chessBoard;

    GraphicHandler gh;

    InputHandler input_handler;

    BufferedImage bufferedImage;


    boolean IsPieceHovering = false;
    boolean IsPieceHoveringClick = false;

    boolean isRunning;

    Label label;

    BufferedImage buffer = new BufferedImage(1025, 1025, BufferedImage.TYPE_INT_ARGB);

    //MouseInputListener mouseListener = new MouseInputListener();
    MouseInputListener mouseListener;

    Player whiteplayer;
    Player blackplayer;

    boolean allow_click[];


    Game()
    {



        chessBoard = new Board();


        javax.swing.JFrame frame = new javax.swing.JFrame("Chess");
        MouseInputListener mouseListener = new MouseInputListener(frame);
        this.mouseListener = mouseListener;

        frame.addMouseListener(mouseListener);
        frame.setSize(1300, 900);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

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
        current_canvas.setPreferredSize(new Dimension(1300, 900));
        current_canvas.setFocusable(true);
        current_canvas.requestFocus();




        frame.addKeyListener(input_handler);


        gh.add(current_canvas);

        frame.add(current_canvas);


        current_canvas.addMouseListener(mouseListener);


        frame.pack();

        current_canvas.createBufferStrategy(3);


        bufferedImage = new BufferedImage(this.current_canvas.getWidth(), this.current_canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
    
    @Override
    public void run()
    {

        while (isRunning) {


            whiteplayer.GetPosssibleMoves();


            whiteplayer.MovePlayerPieces();


            System.out.println("ENTITY 1 X: "+whiteplayer.pieces.get(0).Coordinates.x + " Y: " + whiteplayer.pieces.get(0).Coordinates.y);



            BufferStrategy bufferstrategy = current_canvas.getBufferStrategy();
            Graphics graphics = bufferstrategy.getDrawGraphics();

            Graphics2D bufferedGraphics = bufferedImage.createGraphics();

            gh.paintComponent(bufferedGraphics);

            int scaledWidth = (int)(bufferedImage.getWidth() * 1.0f);
            int scaledHeight = (int)(bufferedImage.getHeight() * 1.0f);

            graphics.drawImage(bufferedImage, 0, 0, scaledWidth, scaledHeight, current_canvas);


            graphics.dispose();
            bufferstrategy.show();






        }


    }

}
