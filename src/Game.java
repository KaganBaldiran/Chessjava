import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;


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

    Math.Vec2<Float> Boundry_size = new Math.Vec2<>();


    GameServer server;
    GameClient client;

    ChessServer ChessServer;
    ChessClient ChessClient;




    javax.swing.JFrame frame;

    String Yourname = new String();



    JButton button = new JButton("random button");
    UI ui;


    Game() throws IOException {


        chessBoard = new Board();





        frame = new javax.swing.JFrame("Chess");
        MouseInputListener mouseListener = new MouseInputListener(frame);
        this.mouseListener = mouseListener;











        frame.addMouseListener(mouseListener);

        ScreenSize = Toolkit.getDefaultToolkit().getScreenSize().getSize();

        ScreenSize.setSize(ScreenSize.getHeight() * 0.90f + ScreenSize.getHeight() * 0.20f , ScreenSize.getHeight() * 0.90f);








        this.whiteplayer = new Player(Tile.WHITE,this.chessBoard,this.mouseListener);
        this.blackplayer = new Player(Tile.BLACK,this.chessBoard,this.mouseListener);






        gh = new GraphicHandler(chessBoard, this.whiteplayer,this.blackplayer , null);







        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        System.out.println("FRAME WIDTH: " + frame.getWidth() + " FRAME HEIGHT: " + frame.getHeight());
        System.out.println("SCREEN WIDTH: " + ScreenSize.getWidth() + " SCREEN HEIGHT: " + ScreenSize.getHeight());




        this.input_handler = new InputHandler();

        this.isRunning = true;

        this.allow_click = new boolean[4];


        //gh = new GraphicHandler(chessBoard, newqueen, newknight, newPawn, newrook, newking, newBishop);
        //this.newBishop = (Bishop) whiteplayer.pieces.get(1);

        frame.setFocusable(true);

        this.ui = new UI(frame);

        current_canvas = new Canvas();
        //current_canvas.setPreferredSize();
        current_canvas.setSize(new Dimension((int)ScreenSize.getWidth(), (int)ScreenSize.getHeight()));
        current_canvas.setFocusable(true);
        current_canvas.requestFocus();

        //button.setSize(100, 50); // set size to 100x50 pixels
        //button.setLocation((int) (ScreenSize.getHeight()), 0); // set location to (10, 10) pixels
        frame.add(button); // add button to GraphicHandler panel



        frame.addKeyListener(input_handler);

        gh.add(current_canvas);

        frame.add(current_canvas);



        current_canvas.addMouseListener(mouseListener);



        frame.pack();

        Boundry_size.SetValues((float) (frame.getWidth()- ScreenSize.getWidth()), (float) (frame.getHeight() - ScreenSize.getHeight()));

        current_canvas.setSize(new Dimension((int)frame.getContentPane().getWidth(), (int)frame.getContentPane().getHeight()));
        //frame.setSize((int)(frame.getWidth() * 0.95f), (int)(frame.getHeh() * 0.95f));


        current_canvas.createBufferStrategy(3);


        bufferedImage = new BufferedImage(this.current_canvas.getWidth(), this.current_canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);

        System.out.println("this.current_canvas.getWidth() X :  " + this.current_canvas.getWidth() + " Y: " + this.current_canvas.getHeight());

        InitNetworking();

    }

    public synchronized void InitNetworking() throws IOException {

        if (JOptionPane.showConfirmDialog(null, "Do you want to run the server?") == 0) {

            server = new GameServer(this,55516);
            server.start();
        }

        InetAddress[] inet = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
        System.out.println("HOST NAME: " + InetAddress.getLocalHost().getHostName() );

        assert GameServer.getIPv4Addresses(inet) != null;
        System.out.println(GameServer.getIPv4Addresses(inet).getHostAddress().trim());
        GameServer.ReverseDSN(GameServer.getIPv4Addresses(inet).getHostAddress().trim());

        //client = new GameClient(this, GameServer.getIPv4Addresses(inet),55516);
        client = new GameClient(this, InetAddress.getByName("192.168.0.107"),55516);
        client.start();
        //this.ChessClient = new ChessClient("192.168.56.1", 1331);
        //ChessClient.start();
    }
    
    @Override
    public void run()
    {

        //System.out.println("this.current_canvas.getWidth()rr X :  " + this.current_canvas.getWidth() + " Y: " + this.current_canvas.getHeight());

        while (isRunning) {

            
            whiteplayer.GetPosssibleMoves();


            whiteplayer.MovePlayerPieces();


            //blackplayer.GetPosssibleMoves();


            //blackplayer.MovePlayerPieces();





            BufferStrategy bufferstrategy = current_canvas.getBufferStrategy();
            Graphics graphics = bufferstrategy.getDrawGraphics();

            Graphics2D bufferedGraphics = bufferedImage.createGraphics();

            //gh.Update();
            gh.paintComponent(bufferedGraphics);
            ui.paintComponent(graphics);

            ///button.paint(bufferedGraphics);

            //button.paint(bufferedGraphics);




            float final_scale_coeffi = GraphicHandler.GetScreenScaleCoefficient(frame ,this.current_canvas, this.ScreenSize);



            float scaledWidth = bufferedImage.getWidth() * final_scale_coeffi;
            float scaledHeight = bufferedImage.getHeight() * final_scale_coeffi;

            System.out.println("FLOAT:  " + scaledHeight + "INT: " + (int)scaledHeight);



            //FBO_position.SetValues(frame.getWidth() - scaledWidth , frame.getHeight() - scaledHeight);
            //FBO_position.SetValues(current_canvas.getWidth() - scaledWidth , current_canvas.getHeight() - scaledHeight);
            FBO_position.SetValues((current_canvas.getWidth()/2) - (scaledWidth/2) , current_canvas.getHeight()/2 - (float)(ScreenSize.getHeight() * final_scale_coeffi)/2);

            ui.setUIsize(current_canvas.getWidth(),current_canvas.getHeight(),scaledWidth,scaledHeight);

            //System.out.println("FBO_position X: "+FBO_position.x + " Y: " + FBO_position.y);

            Board.UpdateSquareSize(ScreenSize.height * final_scale_coeffi);

            this.chessBoard.UpdateCollisionBoxes(FBO_position);

            //System.out.println("BOARD SQUARE_SIZE SCALE * 8: " + Board.SQUARE_SIZE * 8);
            //FBO_position.y = (float) 0;


            //System.out.println("scaledWidth: " +scaledWidth+ " scaledHeight: " + scaledHeight);




            System.out.println("CURRENT CANVAS X: " + current_canvas.getWidth() + " Y: " + current_canvas.getHeight());
            System.out.println("POSITION X: " + FBO_position.x + " Y: " + FBO_position.y);

            graphics.drawImage(bufferedImage, FBO_position.x.intValue(), FBO_position.y.intValue(), (int)scaledWidth, (int)scaledHeight, current_canvas);


            //graphics.drawImage(bufferedImage, 0, 0, (int)scaledWidth, (int)scaledHeight, current_canvas);
            //graphics.drawImage(bufferedImage, 0, 0, (int)scaledWidth, (int)scaledHeight, current_canvas);


            for(piece piece : whiteplayer.pieces)
            {
                if(piece.Selected)
                {
                    this.client.setDataTosend(String.valueOf(piece.Coordinates.x) +" "+ String.valueOf(piece.Coordinates.y));
                }

            }

            graphics.dispose();
            bufferstrategy.show();



            ui.UpdateBoardAttribs(FBO_position, final_scale_coeffi);
            ui.Update();

            frame.revalidate();
            frame.repaint();

        }


            this.ChessServer.close();
            try {
                this.ChessClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


    }

}
