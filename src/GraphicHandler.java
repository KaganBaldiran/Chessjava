import javax.swing.*;
import java.awt.*;

public class GraphicHandler extends JPanel
{
    Board current_board;
    Queen newqueen;

    Knight newknight;

    Pawn newpawn;

    Rook newrook;

    King newking;

    Bishop newbishop;

    Texture knighttexture;

    Player player;

    GraphicHandler(Board br , Queen qn , Knight k , Pawn pawn , Rook r , King king , Bishop b)
    {
        this.current_board = br;
        this.newqueen = qn;
        this.newknight = k;
        this.newpawn = pawn;
        this.newrook = r;
        this.newking = king;
        this.newbishop = b;
        knighttexture = new Texture("C:\\Users\\kbald\\Desktop\\Chess_cdt45.png");
        knighttexture.Position.SetValues(100,100);
        knighttexture.setScale(0.7f);
        this.player = new Player(Tile.WHITE,current_board);

    }

    @Override
    protected void paintComponent(Graphics g)
    {

       this.current_board.paintComponent(g);
       //this.newqueen.paintComponent(g);

         player.paintComponent(g);
        //this.newknight.paintComponent(g);
        //this.newbishop.paintComponent(g);
        //this.newrook.paintComponent(g);
        //this.newpawn.paintComponent(g);
        //this.newking.paintComponent(g);
        //knighttexture.paintComponent(g);

        g.dispose();
    }




}
