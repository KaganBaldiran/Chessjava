package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.Vector;

public abstract class piece extends JPanel
{
    Math.Vec2<Integer> Coordinates = new Math.Vec2<>();
    Tile TilePieceStandingOn;
    Board CurrentGameBoard;
    Vector<Math.Vec2<Integer>> Possible_Moves = new Vector<>();
    boolean ClearPossibleMoves = false;
    int Color;
    Texture piecetexture;
    boolean Selected = false;
    MouseInputListener CurrentMouseListenerReference;
    Player player_this_piece_belongs;

    boolean NewTileHasEnemyPiece = false;
    piece NewTileOldEnemyPiece;


    int PieceIndexInPlayer;
    String PieceType;

    Math.Vec2<Integer> LastPlayedMove = new Math.Vec2<>(0,0);


    public boolean IsInsideBoundries(int x_cord , int y_cord)
    {
       return !(this.CurrentGameBoard.MinMaxBoundaries.x >= x_cord ||
               this.CurrentGameBoard.MinMaxBoundaries.y <= x_cord ||
               this.CurrentGameBoard.MinMaxBoundaries.z >= y_cord ||
               this.CurrentGameBoard.MinMaxBoundaries.w <= y_cord);
    }

    piece(int x_cord, int y_cord , int color)
    {
        this.Coordinates = new Math.Vec2<>();
       this.Coordinates.SetValues(x_cord,y_cord);
       this.CurrentGameBoard = new Board();
       this.Color = color;
       this.PieceType = this.getClass().getName();
    }
    piece(int x_cord, int y_cord , int color , Tile TilePieceStandingOn)
    {
        this.Coordinates.SetValues(x_cord,y_cord);
        this.Color = color;
        this.TilePieceStandingOn = TilePieceStandingOn;
        this.TilePieceStandingOn.setPieceThatStandsOnThisTile(this);
        this.CurrentGameBoard = new Board();
        this.PieceType = this.getClass().getName();
    }
    piece(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , String texture_file_path , MouseInputListener current_mouse_listener , Player player_this_piece_belongs)
    {
        this.Coordinates.SetValues(x_cord,y_cord);
        this.Color = color;
        this.TilePieceStandingOn = TilePieceStandingOn;
        this.TilePieceStandingOn.setPieceThatStandsOnThisTile(this);
        this.CurrentGameBoard = CurrentBoard;
        this.piecetexture = new Texture(texture_file_path);
        piecetexture.Position.SetValues((this.Coordinates.x -1 ) * Board.SQUARE_SIZE , (this.Coordinates.y -1 ) * Board.SQUARE_SIZE);
        piecetexture.setScale(Board.SQUARE_SIZE * 0.0045f);
        this.CurrentMouseListenerReference = current_mouse_listener;
        this.player_this_piece_belongs = player_this_piece_belongs;
        this.PieceIndexInPlayer = player_this_piece_belongs.pieces.size();
        this.PieceType = this.getClass().getName();
    }

    piece(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , Texture existing_texture , MouseInputListener current_mouse_listener , Player player_this_piece_belongs)
    {
        this.Coordinates.SetValues(x_cord,y_cord);
        this.Color = color;
        this.TilePieceStandingOn = TilePieceStandingOn;
        this.TilePieceStandingOn.setPieceThatStandsOnThisTile(this);
        this.CurrentGameBoard = CurrentBoard;
        this.piecetexture = existing_texture;
        piecetexture.Position.SetValues((this.Coordinates.x -1 ) * Board.SQUARE_SIZE , (this.Coordinates.y -1 ) * Board.SQUARE_SIZE);
        //piecetexture.Position.SetValues(100,100);
        piecetexture.setScale(Board.SQUARE_SIZE * 0.0045f);
        this.CurrentMouseListenerReference = current_mouse_listener;
        this.player_this_piece_belongs = player_this_piece_belongs;
        this.PieceIndexInPlayer = player_this_piece_belongs.pieces.size();
        this.PieceType = this.getClass().getName();
    }

    void ReferenceTile(Tile input_tile)
    {
        this.TilePieceStandingOn = input_tile;
    }

    public boolean IsTileEmpty(int tile_index)
    {
        return CurrentGameBoard.Tiles.get(tile_index).isTileEmpty();
    };
    public boolean IsTileEmpty(Tile tile_i)
    {
        return tile_i.isTileEmpty();
    };

    boolean IsPieceHovering = false;
    boolean IsPieceHoveringClick = false;
    boolean FirstMove = false;
    Tile TileMouseIsOn = new Tile(Color,new Math.Vec2<>(0,0));

    Math.Vec2<Double> previousMousePos = new Math.Vec2<>(0.0,0.0);

    java.util.Timer Timer = new Timer();

    public void Move()
    {
            for (int i = 0; i < this.CurrentGameBoard.Tiles.size(); i++) {

                if (this.CurrentGameBoard.Tiles.get(i).TileCollisionBox.CheckCollisionBoxMouse(this.CurrentMouseListenerReference.GetMousePos())) {

                    TileMouseIsOn = this.CurrentGameBoard.Tiles.get(i);

                    //System.out.println("COLLISION SPOTTED ! TILE LOCATION X: " + this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.x + " Y: " + this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.y + "EMPTY: " + this.CurrentGameBoard.Tiles.get(i).isTileEmpty());
                    //System.out.println("MOUSE LOCATION X: " + this.CurrentMouseListenerReference.GetMousePos().x + " Y: " + this.CurrentMouseListenerReference.GetMousePos().y);

                    if (this.CurrentMouseListenerReference.isReleased(MouseEvent.BUTTON1)) {
                        this.IsPieceHoveringClick = true;
                    }

                    if (this.CurrentMouseListenerReference.isClicked(MouseEvent.BUTTON1) && !this.IsPieceHovering && IsPieceHoveringClick &&
                            this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.x.intValue() == this.Coordinates.x.intValue() &&
                            this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.y.intValue() == this.Coordinates.y.intValue()) {

                        this.Possible_Moves.clear();
                        boolean permitted = true;
                        for(piece piece : player_this_piece_belongs.pieces)
                        {
                            if (piece.Selected) {
                                permitted = false;
                                break;
                            }
                        }
                        if(permitted)
                        {
                            this.IsPieceHovering = true;
                            this.IsPieceHoveringClick = false;
                            this.Selected = true;
                        }

                    }

                    if (this.CurrentMouseListenerReference.isClicked(MouseEvent.BUTTON1) && this.IsPieceHovering && IsPieceHoveringClick) {

                        this.IsPieceHovering = false;
                        this.IsPieceHoveringClick = false;
                        this.Selected = false;

                        for (Math.Vec2<Integer> possibleMove : Possible_Moves) {

                            if (possibleMove.x.intValue() == this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.x.intValue() && possibleMove.y.intValue() == this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.y.intValue()) {


                                if(this.CurrentGameBoard.Tiles.get(i).PieceThatStandsOnThisTile != null) {
                                    if (this.CurrentGameBoard.Tiles.get(i).PieceThatStandsOnThisTile.Color != this.Color) {
                                        this.NewTileHasEnemyPiece = true;
                                        NewTileOldEnemyPiece = this.CurrentGameBoard.Tiles.get(i).PieceThatStandsOnThisTile;
                                    }
                                }

                                this.FirstMove = true;
                                this.TilePieceStandingOn.SetEmptinessState(true);
                                Coordinates.SetValues(this.CurrentGameBoard.Tiles.get(i).Tilecoordinates);
                                LastPlayedMove.SetValues(this.CurrentGameBoard.Tiles.get(i).Tilecoordinates);
                                this.TilePieceStandingOn = this.CurrentGameBoard.Tiles.get(i);
                                this.TilePieceStandingOn.setPieceThatStandsOnThisTile(this.player_this_piece_belongs.pieces.get(this.PieceIndexInPlayer));
                                this.TilePieceStandingOn.SetEmptinessState(false);

                            }

                        }
                    }


                }

            }


        //this.previousMousePos.SetValues(this.CurrentMouseListenerReference.GetMousePos());

    }
    public abstract void capture(Player OpponentPlayer);
    public abstract Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty, Math.Vec2<Integer> input_Coordinates);

    public static final Color TRANSPARENT_LIGHT_GRAY = new Color(java.awt.Color.LIGHT_GRAY.getRed()/ 255,java.awt.Color.LIGHT_GRAY.getGreen() / 255,java.awt.Color.LIGHT_GRAY.getBlue() / 255,0.2f );

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (this.Selected) {

            if (!Possible_Moves.isEmpty())
            {
                for (Math.Vec2<Integer> possibleMove : Possible_Moves) {

                    if (possibleMove.x < 9) {
                        int centerX = (possibleMove.x - 1) * (Board.SQUARE_SIZE) + (Board.SQUARE_SIZE / 2);
                        int centerY = (possibleMove.y - 1) * (Board.SQUARE_SIZE) + (Board.SQUARE_SIZE / 2);
                        int radius = (int) (Board.SQUARE_SIZE * 0.25f);

                        g.setColor(TRANSPARENT_LIGHT_GRAY);
                        g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

                    }

                }

            }

            int x = (this.Coordinates.x -1) * Board.SQUARE_SIZE;
            int y = (this.Coordinates.y -1) * Board.SQUARE_SIZE;

            g.setColor(Board.LIGHT_YELLOW);

            g.fillRect(x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
        }

        piecetexture.Position.SetValues((this.Coordinates.x -1 ) * Board.SQUARE_SIZE - (Board.SQUARE_SIZE/10) , (this.Coordinates.y -1 ) * Board.SQUARE_SIZE - (Board.SQUARE_SIZE/10)  );

        piecetexture.paintComponent(g);

    }

    public String GetPieceType()
    {
        return PieceType;
    }



}


