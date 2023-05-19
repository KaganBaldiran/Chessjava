import de.javawi.jstun.util.UtilityException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.System.exit;


public abstract class GameEvent
{
    public final static int FIRST_PLAYER = 1;
    public final static int SECOND_PLAYER = 2;
    int Winner = 0;
    Board GameBoard;
    Player player1;
    Player player2;
    GraphicHandler graphicHandler;

    GameEvent(MouseInputListener mouseListener)
    {
        this.GameBoard = new Board();
        player1 = new Player(Tile.WHITE , GameBoard,mouseListener , false);
        player2 = new Player(Tile.BLACK , GameBoard,mouseListener , true);
        this.graphicHandler = new GraphicHandler(GameBoard,player1,player2);
    }

    protected GameEvent()
    {
    }

    public void UpdateBoardUtilities(Dimension ScreenSize , float final_scale_coeffi , Math.Vec2<Float> FBO_position , BufferedImage bufferedImage , UI ui)
    {
        Board.UpdateSquareSize(ScreenSize.height * final_scale_coeffi);
        Board.UpdateSQUARESIZEUI(ui.UIsliderBar.ComponentSizes.z);
        this.GameBoard.UpdateCollisionBoxes(new Math.Vec2<>(FBO_position.x,(float)((bufferedImage.getHeight() -ui.UIsliderBar.UnchangingComponentSizes.x)/2)) );
    }
    public abstract void GameLoop();

    public void EndGame()
    {

    }
    public void DrawGame(Graphics graphics)
    {
        graphicHandler.paintComponent(graphics);
    }

    boolean DataSent = false;


    public static class LANGameEvent extends GameEvent
    {
        public KryonetServer Server;
        public KryonetClient Client;
        public Player PlayerOnThisMachine;
        public Player PlayerOnTheOpponentMachine;
        Thread ConnectingThread = null;

        LANGameEvent(MouseInputListener mouseListener , boolean GameUsage , String link)
        {
            super();
            this.GameBoard = new Board();
            MoveTheOpponent.SetMutexFalse();


            if (GameUsage)
            {
                try {
                    InitServer();
                } catch (IOException | UtilityException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                try {
                    InitClient(link);
                } catch (IOException | UtilityException e) {
                    e.printStackTrace();

                    if (ConnectingThread != null)
                    {
                        ConnectingThread.interrupt();
                    }

                    DeleteGameEvent.SetMutexTrue();
                    JOptionPane.showMessageDialog(null , "Unable to connect to the target server!");
                }
            }

            if (Server != null)
            {
                player1 = new Player(Tile.WHITE , GameBoard,mouseListener , false);
                player2 = new Player(Tile.BLACK , GameBoard,mouseListener , true);
                this.PlayerOnThisMachine = player1;
                PlayerOnTheOpponentMachine = player2;
            }
            else
            {
                player1 = new Player(Tile.WHITE , GameBoard,mouseListener , true);
                player2 = new Player(Tile.BLACK , GameBoard,mouseListener , false);
                this.PlayerOnThisMachine = player2;
                PlayerOnTheOpponentMachine = player1;
            }
            this.graphicHandler = new GraphicHandler(GameBoard,player1,player2);
        }

        Semaphore DeleteGameEvent = new Semaphore(2);

        public synchronized void InitServer() throws IOException, UtilityException
        {
            if (JOptionPane.showConfirmDialog(null, "Are you sure to create a LAN game server?") == 0)
            {
                Server = new KryonetServer(54555, 54777);
                Client = new KryonetClient(5000, "www.chessjava/rnt/rhv/P/rPj.com", 54555, 54777 , MoveTheOpponent);
            }
            else
            {
                JOptionPane.showMessageDialog(null , "LAN server isn't created");
                DeleteGameEvent.SetMutexTrue();
            }
        }

        public synchronized void InitClient(String Link) throws IOException, UtilityException
        {
            if (JOptionPane.showConfirmDialog(null, "Are you sure to create a LAN game client?") == 0)
            {
                if(Link.isEmpty())
                {
                    JOptionPane.showMessageDialog(null , "No game link is provided!");
                    DeleteGameEvent.SetMutexTrue();
                }
                else
                {
                    if(Link.length() >= 31)
                    {
                        System.out.println("LINK: " + Link.substring(0 , 13) + " " + Link.substring(27 , 31));
                        if(Link.substring(0 , 13).equalsIgnoreCase("www.chessjava") && Link.substring(27 , 31).equalsIgnoreCase(".com"))
                        {
                            ConnectingThread = new Thread(new LoadingDialog());
                            ConnectingThread.start();
                            Client = new KryonetClient(5000, Link, 54555, 54777 , MoveTheOpponent);
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null , "No game link is provided!");
                        DeleteGameEvent.SetMutexTrue();
                    }

                }
            }
            else
            {
                JOptionPane.showMessageDialog(null , "Client isn't created");
                DeleteGameEvent.SetMutexTrue();
            }

        }
        Semaphore MoveTheOpponent = new Semaphore(2);

        @Override
        public void GameLoop()
        {
            if(Client.ConnectionState.equalsIgnoreCase("CONNECTED"))
            {
                if (PlayerOnThisMachine.isPlayersTurn())
                {
                    PlayerOnThisMachine.GetPosssibleMoves();
                    PlayerOnThisMachine.MovePlayerPieces();
                }
            }

            Client.loop(PlayerOnThisMachine , PlayerOnTheOpponentMachine);

            if (MoveTheOpponent.IsMutexTrue())
            {
                PlayerOnTheOpponentMachine.pieces.get(Client.getOtherPlayerPieceIndex()).Coordinates.SetValues(Math.UV_Tools.Invert_Y_Axis(Client.getOtherPlayerMove() , Tile.WHITE));
                MoveTheOpponent.SetMutexFalse();
                System.out.println("MOVED THE OPPONENT TO: " + Client.getOtherPlayerMove().x + " " + Client.OtherPlayerMove.y);
            }


        }

    }

    public static class LoadingDialog implements Runnable {
        LoadingDialog()
        {
            super();
        }
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null , "CONNECTING...");
        }
    }
}
