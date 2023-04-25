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

    Queen Selected_piece;
    Knight newknight;
    Pawn newPawn;
    Bishop newBishop;
    Rook newrook;
    King newking;
    Queen newqueen;
    //JFrame frame;

    Canvas current_canvas;

    Board chessBoard;

    GraphicHandler gh;

    InputHandler input_handler;



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
        newqueen = new Queen(7,5,Tile.WHITE);

        javax.swing.JFrame frame = new javax.swing.JFrame("Chess");
        MouseInputListener mouseListener = new MouseInputListener(frame);
        this.mouseListener = mouseListener;

        frame.addMouseListener(mouseListener);
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        this.whiteplayer = new Player(Tile.WHITE,this.chessBoard,this.mouseListener);
        this.blackplayer = new Player(Tile.BLACK,this.chessBoard,this.mouseListener);

        newknight = new Knight(5,5,Tile.WHITE);

        newPawn = new Pawn(1,1,Tile.WHITE);
        newBishop = new Bishop(1,1,Tile.WHITE);
        newrook = new Rook(8,8,Tile.WHITE);
        newking = new King(5,5,Tile.WHITE);

        this.input_handler = new InputHandler();

        this.isRunning = true;

        this.allow_click = new boolean[4];


        //gh = new GraphicHandler(chessBoard, newqueen, newknight, newPawn, newrook, newking, newBishop);
        gh = new GraphicHandler(chessBoard, this.whiteplayer,this.blackplayer);
        //this.newBishop = (Bishop) whiteplayer.pieces.get(1);







        frame.setFocusable(true);



        current_canvas = new Canvas();
        current_canvas.setPreferredSize(new Dimension(1000, 1000));
        current_canvas.setFocusable(true);
        current_canvas.requestFocus();


        frame.addKeyListener(input_handler);


        gh.add(current_canvas);

        frame.add(current_canvas);


        current_canvas.addMouseListener(mouseListener);


        frame.pack();

        current_canvas.createBufferStrategy(3);
    }
    
    @Override
    public void run()
    {

        //newrook.GetPossibleMoves(true , newrook.Coordinates);

       //newking.GetPossibleMoves(true , newking.Coordinates);

        //newPawn.GetPossibleMoves(true, newPawn.Coordinates);

       /// newknight.GetPossibleMoves(true,newknight.Coordinates);



        while (isRunning) {

            newBishop.GetPossibleMoves(true,newBishop.Coordinates);


            whiteplayer.GetPosssibleMoves();


            if (input_handler.isReleased(KeyEvent.VK_DOWN))
            {
                allow_click[0] = true;
            }
            if (input_handler.isReleased(KeyEvent.VK_UP))
            {
                allow_click[1] = true;
            }
            if (input_handler.isReleased(KeyEvent.VK_LEFT))
            {
                allow_click[2] = true;
            }
            if (input_handler.isReleased(KeyEvent.VK_RIGHT))
            {
                allow_click[3] = true;
            }


                if (input_handler.isPressed(KeyEvent.VK_DOWN) && this.newBishop.Coordinates.y < 8 && allow_click[0]) {
                    //this.newqueen.Coordinates.y++;
                    this.newBishop.Coordinates.y++;
                    allow_click[0] = false;
                }

                if (input_handler.isPressed(KeyEvent.VK_UP) && this.newBishop.Coordinates.y > 1 & allow_click[1]) {
                    //this.newqueen.Coordinates.y--;
                    this.newBishop.Coordinates.y--;
                    allow_click[1] = false;
                }

                if (input_handler.isPressed(KeyEvent.VK_LEFT) && this.newBishop.Coordinates.x > 1 && allow_click[2]) {
                    //this.newqueen.Coordinates.x--;
                    this.newBishop.Coordinates.x--;
                    allow_click[2] = false;
                }

                if (input_handler.isPressed(KeyEvent.VK_RIGHT) && this.newBishop.Coordinates.x < 8 && allow_click[3]) {
                    this.newBishop.Coordinates.x++;
                    allow_click[3] = false;
                }


                if(this.mouseListener.isClicked(MouseEvent.BUTTON1))
                {
                    System.out.println("MOUSE BUTTON 1 CLICKED!");
                    //this.newBishop.Coordinates.y++;
                }


                /*
            for (int i = 0; i < this.chessBoard.Tiles.size(); i++)
            {
                if (this.chessBoard.Tiles.get(i).TileCollisionBox.CheckCollisionBoxMouse(this.mouseListener.GetMousePos()))
                {
                    System.out.println("COLLISION SPOTTED ! TILE LOCATION X: "+ this.chessBoard.Tiles.get(i).Tilecoordinates.x + " Y: "+ this.chessBoard.Tiles.get(i).Tilecoordinates.y );
                    System.out.println("MOUSE LOCATION X: "+ this.mouseListener.GetMousePos().x + " Y: "+ this.mouseListener.GetMousePos().y );

                    if (this.mouseListener.isReleased(MouseEvent.BUTTON1))
                    {
                        this.IsPieceHoveringClick = true;
                    }

                    if(this.mouseListener.isClicked(MouseEvent.BUTTON1) && !this.IsPieceHovering && IsPieceHoveringClick)
                    {
                        this.IsPieceHovering = true;
                        this.IsPieceHoveringClick = false;
                    }
                    else if (this.mouseListener.isClicked(MouseEvent.BUTTON1) && this.IsPieceHovering && IsPieceHoveringClick) {

                        this.IsPieceHovering = false;
                        this.IsPieceHoveringClick = false;
                        for (int y = 0; y < whiteplayer.pieces.get(0).Possible_Moves.size(); y++) {

                            if (whiteplayer.pieces.get(0).Possible_Moves.get(y).x == this.chessBoard.Tiles.get(i).Tilecoordinates.x && whiteplayer.pieces.get(0).Possible_Moves.get(y).y == this.chessBoard.Tiles.get(i).Tilecoordinates.y)
                            {
                                whiteplayer.pieces.get(0).Coordinates.SetValues(this.chessBoard.Tiles.get(i).Tilecoordinates);

                            }
                        }
                    }


                }

            }



            if (this.IsPieceHovering)
            {
                //whiteplayer.pieces.get(0).Coordinates.SetValues((mouseListener.GetMousePos().x.intValue() / Board.SQUARE_SIZE)  + 1,(mouseListener.GetMousePos().y.intValue() / Board.SQUARE_SIZE) + 1);
            }
            */

            whiteplayer.MovePlayerPieces();


            System.out.println("ENTITY 1 X: "+whiteplayer.pieces.get(0).Coordinates.x + " Y: " + whiteplayer.pieces.get(0).Coordinates.y);


            //newqueen.GetPossibleMoves(true,newqueen.Coordinates);

            BufferStrategy bufferstrategy = current_canvas.getBufferStrategy();
            Graphics graphics = bufferstrategy.getDrawGraphics();

            gh.paintComponent(graphics);

            graphics.dispose();
            bufferstrategy.show();

        }


    }

}
