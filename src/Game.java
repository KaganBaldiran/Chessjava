import de.javawi.jstun.attribute.MappedAddress;
import de.javawi.jstun.attribute.MessageAttributeParsingException;
import de.javawi.jstun.util.UtilityException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

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

    UI.UIcomponents leftComponent;



    JButton button = new JButton("random button");
    UI ui;


    GameEventHandler Games;

    Color LeftComponentColor = GraphicHandler.HexToRgba("#787F8F");


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



        this.input_handler = new InputHandler();

        this.isRunning = true;

        this.allow_click = new boolean[4];


        frame.setFocusable(true);

        this.ui = new UI(frame,mouseListener);

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

        this.leftComponent = new UI.UIcomponents((int) (ScreenSize.getWidth() * 0.82f), (int) (ScreenSize.getWidth() * 0.82f),BufferedImage.TYPE_INT_ARGB);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                //server.DeletePortMapping(server.device,server.externalPort,server.protocol);
                isRunning = false;

            }
        });

        Games = new GameEventHandler(mouseListener);
        Games.AddGameEvent(GameEventHandler.LAN_GAME_EVENT);
    }

    boolean AddNewGame = false;
    
    @Override
    public void run()
    {

        while (isRunning)
        {

            if(this.mouseListener.isClicked(MouseEvent.BUTTON2))
            {
                AddNewGame = false;
                Games.AddGameEvent(GameEventHandler.LAN_GAME_EVENT);
                System.out.println("GAME ADDED!");
            }

            Games.GetGameEvent(0).GameLoop();


            if(this.input_handler.isPressed(KeyEvent.VK_S))
            {
                //krclient.sendTCP("FUCK YOU");
            }

            BufferStrategy bufferstrategy = current_canvas.getBufferStrategy();
            Graphics graphics = bufferstrategy.getDrawGraphics();

            Graphics2D bufferedGraphics = bufferedImage.createGraphics();

            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0 , current_canvas.getWidth(), current_canvas.getHeight());


            float final_scale_coeffi = GraphicHandler.GetScreenScaleCoefficient(frame ,this.current_canvas, this.ScreenSize);


            bufferedGraphics.setColor(Color.GRAY.darker());
            bufferedGraphics.fillRect((int) (ui.UIsliderBar.UnchangingComponentSizes.x), 0, ui.UIsliderBar.UnchangingComponentSizes.y, (int) ScreenSize.getHeight());

            bufferedGraphics.setColor(Color.GRAY);
            bufferedGraphics.fillRoundRect((int) (ui.UIsliderBar.UnchangingComponentSizes.x + ((ui.UIsliderBar.UnchangingComponentSizes.y * 0.10f) / 2))  , 10, (int) (ui.UIsliderBar.UnchangingComponentSizes.y * 0.90f), (int) (bufferedImage.getHeight() * 0.980f),30,30);

            
            Games.GetGameEvent(0).DrawGame(leftComponent.bufferedGraphics);



            float scaledWidth = bufferedImage.getWidth() * final_scale_coeffi;
            float scaledHeight = bufferedImage.getHeight() * final_scale_coeffi;


            leftComponent.drawUIcomponent(bufferedGraphics,
                    0,
                    (int)((bufferedImage.getHeight() -ui.UIsliderBar.UnchangingComponentSizes.x)/2) ,
                    ui.UIsliderBar.UnchangingComponentSizes.x,
                    ui.UIsliderBar.UnchangingComponentSizes.x,
                    current_canvas);



            FBO_position.SetValues((current_canvas.getWidth()/2) - (scaledWidth/2) , current_canvas.getHeight()/2 - (float)(ScreenSize.getHeight() * final_scale_coeffi)/2);


            Games.GetGameEvent(0).UpdateBoardUtilities(ScreenSize,final_scale_coeffi,FBO_position,bufferedImage,ui);

            ui.setUIsize(((int) scaledWidth), (int) scaledHeight,ScreenSize.height * final_scale_coeffi,ScreenSize.height * final_scale_coeffi);



            graphics.drawImage(bufferedImage, FBO_position.x.intValue(), FBO_position.y.intValue(), (int)scaledWidth, (int)scaledHeight, current_canvas);


            for(piece piece : whiteplayer.pieces)
            {
                if(piece.Selected)
                {
                   // this.client.setDataTosend(String.valueOf(piece.Coordinates.x) +" "+ String.valueOf(piece.Coordinates.y));
                }

            }

            ui.UpdateBoardAttribs(FBO_position, final_scale_coeffi , ScreenSize);
            ui.Update();


            ui.paintComponent(graphics);


            bufferedGraphics.setBackground(LeftComponentColor);
            bufferedGraphics.clearRect(0,0, 3000, 4000);

            frame.revalidate();
            frame.repaint();

            graphics.dispose();
            bufferstrategy.show();

        }

            System.out.println("Game Terminated");
            frame.dispose();
            exit(1);
    }

}
