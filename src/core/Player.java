package core;

import javax.swing.*;
import java.awt.*;
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

    Semaphore IsPlayersTurn = new Semaphore(2);
    int Color;

    public Player(Board current_board, MouseInputListener current_mouse_listener, boolean InvertPlaces)
    {
        this.Color = Tile.WHITE;

        IsPlayersTurn.SetMutexTrue();

        CurrentTextures = WhitePlayerTexturesFilePaths;

        pawn_texture = new Texture( RelativeFilePath + CurrentTextures[0]);

        System.out.println(BlackPlayerTexturesFilePaths[0]);

        this.Current_Board_Reference = current_board;

        int y_axis = 2;

        this.mouselistenerreference = current_mouse_listener;

        if (this.Color == Tile.BLACK)
        {
            y_axis = Math.UV_Tools.Invert_Y_Axis( Current_Board_Reference.FetchTile(1,y_axis).Tilecoordinates,Tile.BLACK).y;
        }

        for(piece piece : this.pieces)
        {
            if (InvertPlaces) {
                Tile old_tile = piece.TilePieceStandingOn;
                piece.Coordinates.SetValues(Math.UV_Tools.Invert_Y_Axis(piece.Coordinates, Tile.WHITE));
                piece.TilePieceStandingOn = this.Current_Board_Reference.FetchTile(piece.Coordinates.x, piece.Coordinates.y);
                piece.TilePieceStandingOn.SetEmptinessState(false);
                old_tile.SetEmptinessState(false);
            }

            piece.TilePieceStandingOn.setPieceThatStandsOnThisTile(this.pieces.get(piece.PieceIndexInPlayer));
        }

    }

    public void AddPiece(String PieceType , int xPosition , int yPosition)
    {
        if(PieceType.equalsIgnoreCase("pawn"))
        {
            pieces.add(new Pawn(xPosition,yPosition,this.Color,Current_Board_Reference.FetchTile(xPosition,yPosition),this.Current_Board_Reference,pawn_texture,mouselistenerreference,this));
            pieces.get(pieces.size() - 1).TilePieceStandingOn.setPieceThatStandsOnThisTile(this.pieces.get(pieces.get(pieces.size() - 1).PieceIndexInPlayer));
        }
        else if(PieceType.equalsIgnoreCase("king"))
        {
            pieces.add(new King(xPosition,yPosition,this.Color,Current_Board_Reference.FetchTile(xPosition,yPosition),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[5],mouselistenerreference,this));
            pieces.get(pieces.size() - 1).TilePieceStandingOn.setPieceThatStandsOnThisTile(this.pieces.get(pieces.get(pieces.size() - 1).PieceIndexInPlayer));
        }
        else if(PieceType.equalsIgnoreCase("knight"))
        {
            pieces.add(new Knight(xPosition,yPosition,this.Color,Current_Board_Reference.FetchTile(xPosition,yPosition),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[1],mouselistenerreference,this));
            pieces.get(pieces.size() - 1).TilePieceStandingOn.setPieceThatStandsOnThisTile(this.pieces.get(pieces.get(pieces.size() - 1).PieceIndexInPlayer));
        }
        else if(PieceType.equalsIgnoreCase("rook"))
        {
            pieces.add(new Rook(xPosition,yPosition,this.Color,Current_Board_Reference.FetchTile(xPosition,yPosition),this.Current_Board_Reference,pieces.get(pieces.size()-1).piecetexture,mouselistenerreference,this));
            pieces.get(pieces.size() - 1).TilePieceStandingOn.setPieceThatStandsOnThisTile(this.pieces.get(pieces.get(pieces.size() - 1).PieceIndexInPlayer));
        }
        else if(PieceType.equalsIgnoreCase("queen"))
        {
            pieces.add(new Queen(xPosition,yPosition,this.Color,Current_Board_Reference.FetchTile(xPosition,yPosition),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[4],mouselistenerreference,this));
            pieces.get(pieces.size() - 1).TilePieceStandingOn.setPieceThatStandsOnThisTile(this.pieces.get(pieces.get(pieces.size() - 1).PieceIndexInPlayer));
        }
        else if(PieceType.equalsIgnoreCase("bishop"))
        {
            pieces.add(new Bishop(xPosition,yPosition,this.Color,Current_Board_Reference.FetchTile(xPosition,yPosition),this.Current_Board_Reference,pieces.get(pieces.size()-1).piecetexture,mouselistenerreference,this));
            pieces.get(pieces.size() - 1).TilePieceStandingOn.setPieceThatStandsOnThisTile(this.pieces.get(pieces.get(pieces.size() - 1).PieceIndexInPlayer));
        }

    }

    public piece GetPiece(int index)
    {
        return this.pieces.get(index);
    }

    public piece GetLastPiece()
    {
        return this.pieces.get(this.pieces.size() - 1);
    }
    public void ClearPieces()
    {
        this.pieces.clear();
    }

    Player(int color , Board current_board , MouseInputListener current_mouse_listener , boolean InvertPlaces)
    {

        this.Color = color;

        if(this.Color == Tile.BLACK)
        {
            IsPlayersTurn.SetMutexFalse();
        }
        else if(this.Color == Tile.WHITE)
        {
            IsPlayersTurn.SetMutexTrue();
        }

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

        pieces.add(new Queen(4,8,this.Color,current_board.FetchTile(4,8),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[4],current_mouse_listener,this));

        pieces.add(new King(5,8,this.Color,current_board.FetchTile(5,8),this.Current_Board_Reference,RelativeFilePath + CurrentTextures[5],current_mouse_listener,this));

        for(piece piece : this.pieces)
        {
            if (InvertPlaces) {
                Tile old_tile = piece.TilePieceStandingOn;
                piece.Coordinates.SetValues(Math.UV_Tools.Invert_Y_Axis(piece.Coordinates, Tile.WHITE));
                piece.TilePieceStandingOn = this.Current_Board_Reference.FetchTile(piece.Coordinates.x, piece.Coordinates.y);
                piece.TilePieceStandingOn.SetEmptinessState(false);
                old_tile.SetEmptinessState(false);
            }

            piece.TilePieceStandingOn.setPieceThatStandsOnThisTile(this.pieces.get(piece.PieceIndexInPlayer));
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

            if(piece != null) {
                piece.Move();
            }

        }
    }

    public void GetPosssibleMoves()
    {
        for (piece piece : this.pieces)
        {
            if(piece != null) {
                if (piece.Selected) {

                    piece.GetPossibleMoves(true, piece.Coordinates);
                }
            }
        }
    }

    public void Capture(Player OpponentPlayer)
    {
        for (piece piece : this.pieces)
        {
            if(piece != null) {

                  piece.capture(OpponentPlayer);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for (piece piece : this.pieces) {
            if(piece != null) {
                piece.paintComponent(g);
            }
        }

    }
    public boolean isPlayersTurn() {
        return IsPlayersTurn.IsMutexTrue();
    }
    public void SetTurnState(boolean newState)
    {
        if (newState)
        {
            IsPlayersTurn.SetMutexTrue();
        }
        else
        {
            IsPlayersTurn.SetMutexFalse();
        }

    }
}
