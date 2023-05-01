import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Player extends JPanel
{
    Vector<piece> pieces = new Vector<>();
    Vector<piece> TakenPieces = new Vector<>();
    Board Current_Board_Reference;

    MouseInputListener mouselistenerreference;


    Texture pawn_texture = new Texture("resources\\Rescaled_versions_256\\pawn.png");

    int Color;

    Player(int color , Board current_board , MouseInputListener current_mouse_listener) throws IOException {
        this.Color = color;

        this.Current_Board_Reference = current_board;

        int y_axis = 2;

        this.mouselistenerreference = current_mouse_listener;

        if (this.Color == Tile.BLACK)
        {
           y_axis = Math.UV_Tools.Invert_Y_Axis( Current_Board_Reference.FetchTile(1,y_axis).Tilecoordinates,Tile.BLACK).y;
        }

        for (int x = 1; x < 9; x++)
        {
            pieces.add(new Pawn(x,7,this.Color,current_board.FetchTile(x,7),this.Current_Board_Reference,pawn_texture,current_mouse_listener,this));
        }

        //BufferedImage originalImage = ImageIO.read(new File("path/to/image.png"));



        pieces.add(new Knight(2,8,this.Color,current_board.FetchTile(2,8),this.Current_Board_Reference,"resources\\Rescaled_versions_256\\Chess_cdt45.png",current_mouse_listener,this));
        pieces.add(new Knight(7,8,this.Color,current_board.FetchTile(7,8),this.Current_Board_Reference,pieces.get(pieces.size()-1).piecetexture,current_mouse_listener,this));

        pieces.add(new Rook(8,8,this.Color,current_board.FetchTile(8,8),this.Current_Board_Reference,"resources\\Rescaled_versions_256\\rookpng.png",current_mouse_listener,this));
        pieces.add(new Rook(1,8,this.Color,current_board.FetchTile(1,8),this.Current_Board_Reference,pieces.get(pieces.size()-1).piecetexture,current_mouse_listener,this));

        pieces.add(new Bishop(3,8,this.Color,current_board.FetchTile(3,8),this.Current_Board_Reference,"resources\\Rescaled_versions_256\\Chess_tile_bd.png",current_mouse_listener,this));
        pieces.add(new Bishop(6,8,this.Color,current_board.FetchTile(6,8),this.Current_Board_Reference,pieces.get(pieces.size()-1).piecetexture,current_mouse_listener,this));

        pieces.add(new Queen(4,8,this.Color,current_board.FetchTile(6,8),this.Current_Board_Reference,"resources\\Rescaled_versions_256\\queen_black.png",current_mouse_listener,this));

        pieces.add(new King(5,8,this.Color,current_board.FetchTile(5,8),this.Current_Board_Reference,"resources\\Rescaled_versions_256\\King_black.png",current_mouse_listener,this));


        if (this.Color == Tile.BLACK)
        {
            for(piece piece : this.pieces)
            {
                piece.Coordinates.SetValues(Math.UV_Tools.Invert_Y_Axis(piece.Coordinates,Tile.BLACK));
            }
        }

    }

    public void TakeOpponentPiece(piece takenPiece)
    {
        TakenPieces.add(takenPiece);
    }

    public void MovePlayerPieces()
    {
        for (piece piece : this.pieces)
        {

            piece.Move();


        }
    }

    public void GetPosssibleMoves()
    {
        for (piece piece : this.pieces)
        {
            if (piece.Selected)
            {
                piece.GetPossibleMoves(true, piece.Coordinates);

            }
        }

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for (int i = 0; i < this.pieces.size(); i++)
        {
            pieces.get(i).paintComponent(g);
        }

        g.dispose();
    }
}
