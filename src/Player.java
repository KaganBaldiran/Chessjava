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

    String[] WhitePlayerTexturesFilePaths = {"pawn_white.png","knight_white.png",
                                             "rook_white.png" , "bishop_white.png",
                                              "Queen_white.png" ,"King_white.png"};
    String[] BlackPlayerTexturesFilePaths = {"pawn.png","Chess_cdt45.png",
                                             "rookpng.png" , "Chess_tile_bd.png",
                                             "queen_black.png" ,"King_black.png"};

    String RelativeFilePath = "resources/Rescaled_versions_256/";
    String[] CurrentTextures;
    Texture pawn_texture;


    boolean IsPlayersTurn = false;
    int Color;

    Player(int color , Board current_board , MouseInputListener current_mouse_listener)
    {
        this.Color = color;

        if (this.Color == Tile.BLACK)
        {
            CurrentTextures = BlackPlayerTexturesFilePaths;
        }
        else if (this.Color == Tile.WHITE)
        {
            CurrentTextures = WhitePlayerTexturesFilePaths;
        }

        pawn_texture = new Texture( RelativeFilePath + CurrentTextures[0]);

        System.out.println(BlackPlayerTexturesFilePaths[0]);

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

        pieces.add(new Knight(2,8,this.Color,current_board.FetchTile(2,8),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[1],current_mouse_listener,this));
        pieces.add(new Knight(7,8,this.Color,current_board.FetchTile(7,8),this.Current_Board_Reference,pieces.get(pieces.size()-1).piecetexture,current_mouse_listener,this));

        pieces.add(new Rook(8,8,this.Color,current_board.FetchTile(8,8),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[2],current_mouse_listener,this));
        pieces.add(new Rook(1,8,this.Color,current_board.FetchTile(1,8),this.Current_Board_Reference,pieces.get(pieces.size()-1).piecetexture,current_mouse_listener,this));

        pieces.add(new Bishop(3,8,this.Color,current_board.FetchTile(3,8),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[3],current_mouse_listener,this));
        pieces.add(new Bishop(6,8,this.Color,current_board.FetchTile(6,8),this.Current_Board_Reference,pieces.get(pieces.size()-1).piecetexture,current_mouse_listener,this));

        pieces.add(new Queen(4,8,this.Color,current_board.FetchTile(6,8),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[4],current_mouse_listener,this));

        pieces.add(new King(5,8,this.Color,current_board.FetchTile(5,8),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[5],current_mouse_listener,this));


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

        for (piece piece : this.pieces) {
            piece.paintComponent(g);
        }

       // g.dispose();
    }
    public boolean isPlayersTurn() {
        return IsPlayersTurn;
    }
    public void SetTurnState(boolean newState)
    {
        this.IsPlayersTurn = newState;
    }
}
