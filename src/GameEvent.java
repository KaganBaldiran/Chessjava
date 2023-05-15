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

    GameEvent(MouseInputListener mouseListener)
    {
        this.GameBoard = new Board();
        player1 = new Player(Tile.WHITE , GameBoard,mouseListener);
        player2 = new Player(Tile.BLACK , GameBoard,mouseListener);
        this.graphicHandler = new GraphicHandler(GameBoard,player1,player2);
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

    public static class LANGameEvent extends GameEvent
    {
        KryonetServer Server;
        KryonetClient Client;
        Player PlayerOnThisMachine;

        LANGameEvent(MouseInputListener mouseListener)
        {
            super(mouseListener);

            try
            {
                InitNetworking();
            }
            catch (IOException | UtilityException e)
            {
                throw new RuntimeException(e);
            }

            if (Server != null)
            {
                this.PlayerOnThisMachine = player1;
            }
            else
            {
                this.PlayerOnThisMachine = player2;
            }
        }

        public synchronized void InitNetworking() throws IOException, UtilityException
        {
            if (JOptionPane.showConfirmDialog(null, "Do you want to run the server?") == 0)
            {
                Server = new KryonetServer(54555, 54777);
            }
            Client = new KryonetClient(5000, "localhost", 54555, 54777);
        }

        @Override
        public void GameLoop()
        {
            PlayerOnThisMachine.GetPosssibleMoves();
            PlayerOnThisMachine.MovePlayerPieces();
            Client.loop(PlayerOnThisMachine);
        }

    }
}
