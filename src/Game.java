import de.javawi.jstun.attribute.MappedAddress;
import de.javawi.jstun.attribute.MessageAttributeParsingException;
import de.javawi.jstun.util.UtilityException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

import static java.lang.System.exit;


public class Game extends JPanel implements Runnable
{

    Canvas current_canvas;

    Board chessBoard;

    GraphicHandler gh;

    InputHandler input_handler;

    BufferedImage bufferedImage;

    boolean isRunning;

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


        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);

        //System.out.println("FRAME WIDTH: " + frame.getWidth() + " FRAME HEIGHT: " + frame.getHeight());
        //System.out.println("SCREEN WIDTH: " + ScreenSize.getWidth() + " SCREEN HEIGHT: " + ScreenSize.getHeight());

        this.input_handler = new InputHandler();

        this.isRunning = true;

        this.allow_click = new boolean[4];


        frame.setFocusable(true);

        this.ui = new UI(frame);

        current_canvas = new Canvas();

        current_canvas.setSize(new Dimension((int)ScreenSize.getWidth(), (int)ScreenSize.getHeight()));
        current_canvas.setFocusable(true);
        current_canvas.requestFocus();

        frame.add(button);

        frame.addKeyListener(input_handler);

        gh.add(current_canvas);

        frame.add(current_canvas);


        current_canvas.addMouseListener(mouseListener);

        frame.pack();

        Boundry_size.SetValues((float) (frame.getWidth()- ScreenSize.getWidth()), (float) (frame.getHeight() - ScreenSize.getHeight()));

        current_canvas.setSize(new Dimension((int)frame.getContentPane().getWidth(), (int)frame.getContentPane().getHeight()));

        current_canvas.createBufferStrategy(3);

        bufferedImage = new BufferedImage(this.current_canvas.getWidth(), this.current_canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);

        System.out.println("this.current_canvas.getWidth() X :  " + this.current_canvas.getWidth() + " Y: " + this.current_canvas.getHeight());

        try {
            InitNetworking();
        } catch (UtilityException e) {
            throw new RuntimeException(e);
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                //server.DeletePortMapping(server.device,server.externalPort,server.protocol);
                isRunning = false;

            }
        });

    }

    public synchronized void InitNetworking() throws IOException, UtilityException {


        MappedAddress ma = new MappedAddress();
        try {
            ma = GameClient.SendRequestToSTUNserver();
        } catch (UtilityException | MessageAttributeParsingException e) {
            throw new RuntimeException(e);
        }

        if (JOptionPane.showConfirmDialog(null, "Do you want to run the server?") == 0) {

            server = new GameServer(this,ma.getPort());
            server.start();

        }
            InetAddress[] inet = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
            System.out.println("HOST NAME: " + InetAddress.getLocalHost().getHostName() );

            assert GameServer.getIPv4Addresses(inet) != null;
            System.out.println(GameServer.getIPv4Addresses(inet).getHostAddress().trim());
            GameServer.ReverseDSN(GameServer.getIPv4Addresses(inet).getHostAddress().trim());

            client = new GameClient(this, server.GameLink,ma.getPort());
            client.start();

    }
    
    @Override
    public void run()
    {

        while (isRunning) {

            
            whiteplayer.GetPosssibleMoves();


            whiteplayer.MovePlayerPieces();





            BufferStrategy bufferstrategy = current_canvas.getBufferStrategy();
            Graphics graphics = bufferstrategy.getDrawGraphics();

            Graphics2D bufferedGraphics = bufferedImage.createGraphics();

            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0 , current_canvas.getWidth(), current_canvas.getHeight());

            ui.paintComponent(bufferedGraphics);

            bufferedGraphics.setColor(Color.GRAY.darker());
            bufferedGraphics.fillRect(Board.SQUARE_SIZE * 8, 0, (int) (ScreenSize.getWidth() * 0.20f), (int) ScreenSize.getHeight());
            bufferedGraphics.setColor(Color.GRAY);
            bufferedGraphics.fillRoundRect((int) ((((ScreenSize.getWidth() * 0.20f) - (ScreenSize.getWidth() * 0.15f)) /2) + Board.SQUARE_SIZE * 8), 0, (int) (ScreenSize.getWidth() * 0.15f), (int) (ScreenSize.getHeight() * 0.90f),50,50);
            //bufferedGraphics.setColor(Color.GRAY);
            //bufferedGraphics.fillRoundRect((int) (BoardLocation.x.intValue() + BoardSize.x), BoardLocation.y.intValue() , (int) (UIsize.x * 0.90f),(int) (UIsize.y * 0.90f),100,100);
            gh.paintComponent(bufferedGraphics);




            float final_scale_coeffi = GraphicHandler.GetScreenScaleCoefficient(frame ,this.current_canvas, this.ScreenSize);



            float scaledWidth = bufferedImage.getWidth() * final_scale_coeffi;
            float scaledHeight = bufferedImage.getHeight() * final_scale_coeffi;


            FBO_position.SetValues((current_canvas.getWidth()/2) - (scaledWidth/2) , current_canvas.getHeight()/2 - (float)(ScreenSize.getHeight() * final_scale_coeffi)/2);





            Board.UpdateSquareSize(ScreenSize.height * final_scale_coeffi);

            this.chessBoard.UpdateCollisionBoxes(FBO_position);

            ui.setUIsize(((int) scaledWidth), (int) scaledHeight,ScreenSize.height * final_scale_coeffi,ScreenSize.height * final_scale_coeffi);





            graphics.drawImage(bufferedImage, FBO_position.x.intValue(), FBO_position.y.intValue(), (int)scaledWidth, (int)scaledHeight, current_canvas);


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

            System.out.println("Game Terminated");
            frame.dispose();
            exit(1);
    }

}
