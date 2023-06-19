package core;

import de.javawi.jstun.util.UtilityException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public abstract class GameEvent
{
    public final static int FIRST_PLAYER = 1;
    public final static int SECOND_PLAYER = 2;
    int Winner = 0;
    Board GameBoard;
    Player player1;
    Player player2;
    GraphicHandler graphicHandler;
    Math.Chrono timer;

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

        LANGameEvent(MouseInputListener mouseListener , boolean GameUsage , UI currentUI , String ExternalIP)
        {
            super();
            this.GameBoard = new Board();
            MoveTheOpponent.SetMutexFalse();
            timer = new Math.Chrono();

            if (GameUsage)
            {
                try {
                    InitServer(ExternalIP);
                } catch (IOException | UtilityException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                try {
                    InitClient(currentUI.ReadOnlyField.GetText());
                } catch (IOException | UtilityException e) {
                    e.printStackTrace();
                    DeleteGameEvent.SetMutexTrue();
                    JOptionPane.showMessageDialog(null , "Unable to connect to the target server!" , "Connection Error" , JOptionPane.ERROR_MESSAGE);
                }

                if (ConnectingThread != null)
                {
                    ConnectingThread.interrupt();
                }

            }

            if (Server != null)
            {
                player2 = new Player(Tile.BLACK , GameBoard,mouseListener , true);
                player1 = new Player(Tile.WHITE , GameBoard,mouseListener , false);

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

            if (!DeleteGameEvent.IsMutexTrue())
            {
                timer.start();
            }
        }

        Semaphore DeleteGameEvent = new Semaphore(2);

        public synchronized void InitServer(String ExternalIP) throws IOException, UtilityException
        {
            if (JOptionPane.showConfirmDialog(null, "Are you sure to create a LAN game server?") == 0)
            {
                Server = new KryonetServer(54555, 54777 , ExternalIP);
                Client = new KryonetClient(5000, Server.GameLinkInternal, 54555, 54777 , MoveTheOpponent);
            }
            else
            {
                JOptionPane.showMessageDialog(null , "LAN server isn't created" , "Server Error" , JOptionPane.ERROR_MESSAGE);
                DeleteGameEvent.SetMutexTrue();
            }
        }

        Semaphore LinkProvided = new Semaphore(2);

        public synchronized void InitClient(String Link) throws IOException, UtilityException
        {
            if (JOptionPane.showConfirmDialog(null, "Are you sure to create a LAN game client?") == 0)
            {
                if(Link.isEmpty())
                {
                    JOptionPane.showMessageDialog(null , "No game link is provided!" , "Connection Error" , JOptionPane.ERROR_MESSAGE);
                    DeleteGameEvent.SetMutexTrue();
                }
                else
                {
                    if(Link.length() >= 28)
                    {
                        if(Link.substring(0 , 13).equalsIgnoreCase("www.chessjava") && Link.substring(Link.length() - 4).equalsIgnoreCase(".com"))
                        {
                            ConnectingThread = new Thread(new LoadingDialog("                           CONNECTING..."));
                            ConnectingThread.start();
                            Client = new KryonetClient(5000, Link, 54555, 54777 , MoveTheOpponent);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null , "No game link is provided!", "Connection Error" , JOptionPane.ERROR_MESSAGE);
                            DeleteGameEvent.SetMutexTrue();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null , "No game link is provided!", "Connection Error" , JOptionPane.ERROR_MESSAGE);
                        DeleteGameEvent.SetMutexTrue();
                    }

                }
            }
            else
            {
                JOptionPane.showMessageDialog(null , "Client isn't created" , "Client Error" , JOptionPane.ERROR_MESSAGE);
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
                    PlayerOnThisMachine.Capture(PlayerOnTheOpponentMachine);
                }
            }
            else
            {
                //timer.interrupt();
            }

            Client.loop(PlayerOnThisMachine , PlayerOnTheOpponentMachine);

            if (MoveTheOpponent.IsMutexTrue())
            {
                PlayerOnTheOpponentMachine.pieces.get(Client.getOtherPlayerPieceIndex()).TilePieceStandingOn.SetEmptinessState(true);
                Math.Vec2<Integer> NewTilePosition = Math.UV_Tools.Invert_Y_Axis(Client.getOtherPlayerMove() , Tile.WHITE);
                PlayerOnTheOpponentMachine.pieces.get(Client.getOtherPlayerPieceIndex()).Coordinates.SetValues(NewTilePosition);
                PlayerOnTheOpponentMachine.pieces.get(Client.getOtherPlayerPieceIndex()).TilePieceStandingOn = this.GameBoard.FetchTile(NewTilePosition.x , NewTilePosition.y);
                PlayerOnTheOpponentMachine.pieces.get(Client.getOtherPlayerPieceIndex()).TilePieceStandingOn.setPieceThatStandsOnThisTile(PlayerOnTheOpponentMachine.pieces.get(Client.getOtherPlayerPieceIndex()));
                PlayerOnTheOpponentMachine.pieces.get(Client.getOtherPlayerPieceIndex()).TilePieceStandingOn.SetEmptinessState(false);
                MoveTheOpponent.SetMutexFalse();
                System.out.println("MOVED THE OPPONENT TO: " + Client.getOtherPlayerMove().x + " " + Client.OtherPlayerMove.y);

                if(this.Client.CapturedPieceIndex > 0)
                {
                    PlayerOnThisMachine.pieces.remove(Client.CapturedPieceIndex);

                    for (int i = 0; i < PlayerOnThisMachine.pieces.size(); i++)
                    {
                        PlayerOnThisMachine.pieces.get(i).PieceIndexInPlayer = i;
                    }

                    Client.CapturedPieceIndex = -1;
                }
            }
        }

    }

    public static class LoadingDialog implements Runnable {
        String Text;
        LoadingDialog(String text)
        {
            super();
            Text = text;
        }
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null ,Text ,"Process",JOptionPane.PLAIN_MESSAGE);
        }
    }
}
