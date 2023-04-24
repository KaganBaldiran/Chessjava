import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public abstract class piece extends JPanel {

    Math.Vec2<Integer> Coordinates = new Math.Vec2<>();

    Tile TilePieceStandingOn;

    Board CurrentGameBoard;
    Vector<Math.Vec2<Integer>> Possible_Moves = new Vector<>();

    boolean ClearPossibleMoves = false;
    int Color;

    Texture piecetexture;



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
    }
    piece(int x_cord, int y_cord , int color , Tile TilePieceStandingOn)
    {
        this.Coordinates.SetValues(x_cord,y_cord);
        this.Color = color;
        this.TilePieceStandingOn = TilePieceStandingOn;
        this.CurrentGameBoard = new Board();
    }
    piece(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , String texture_file_path)
    {
        this.Coordinates.SetValues(x_cord,y_cord);
        this.Color = color;
        this.TilePieceStandingOn = TilePieceStandingOn;
        this.CurrentGameBoard = CurrentBoard;
        this.piecetexture = new Texture(texture_file_path);
        piecetexture.Position.SetValues((this.Coordinates.x -1 ) * Board.SQUARE_SIZE , (this.Coordinates.y -1 ) * Board.SQUARE_SIZE);
        //piecetexture.Position.SetValues(100,100);
        piecetexture.setScale(0.15f);
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

    public abstract void Move(int newX,int newY);
    public abstract void capture();
    public abstract Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty, Math.Vec2<Integer> input_Coordinates);

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        int centerX_piece = (this.Coordinates.x - 1) * (Board.SQUARE_SIZE) + (Board.SQUARE_SIZE / 2);
        int centerY_piece = (this.Coordinates.y - 1) * (Board.SQUARE_SIZE) + (Board.SQUARE_SIZE / 2) ;
        int radius_piece = 40;
        g.setColor(java.awt.Color.RED);
        g.fillOval(centerX_piece - radius_piece, centerY_piece - radius_piece, radius_piece * 2, radius_piece * 2);

        for (int i = 0; i < Possible_Moves.size(); i++)
        {
            int centerX = (Possible_Moves.get(i).x - 1) * (Board.SQUARE_SIZE) + (Board.SQUARE_SIZE / 2);
            int centerY = (Possible_Moves.get(i).y - 1) * (Board.SQUARE_SIZE) + (Board.SQUARE_SIZE / 2) ;
            int radius = 40;
            g.setColor(java.awt.Color.BLACK);
            g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        }

        piecetexture.paintComponent(g);

    }

}


