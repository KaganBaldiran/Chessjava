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

    GraphicHandler(Board br , Queen qn , Knight k , Pawn pawn , Rook r , King king , Bishop b)
    {
        this.current_board = br;
        this.newqueen = qn;
        this.newknight = k;
        this.newpawn = pawn;
        this.newrook = r;
        this.newking = king;
        this.newbishop = b;

    }

    @Override
    protected void paintComponent(Graphics g)
    {

       this.current_board.paintComponent(g);
       //this.newqueen.paintComponent(g);


        //this.newknight.paintComponent(g);
        this.newbishop.paintComponent(g);
        //this.newrook.paintComponent(g);
        //this.newpawn.paintComponent(g);
        //this.newking.paintComponent(g);

        g.dispose();
    }




}
