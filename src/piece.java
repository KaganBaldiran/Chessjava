import java.util.Vector;

public abstract class piece {

    Math.Vec2<Integer> Coordinates;

    Tile TilePieceStandingOn;

    Board CurrentGameBoard;
    Vector<Math.Vec2<Integer>> Possible_Moves;
    int Color;

    piece(int x_cord, int y_cord , int color)
    {
        this.Coordinates = new Math.Vec2<>();
       this.Coordinates.SetValues(x_cord,y_cord);
       this.Color = color;
    }
    piece(int x_cord, int y_cord , int color , Tile TilePieceStandingOn)
    {
        this.Coordinates.SetValues(x_cord,y_cord);
        this.Color = color;
        this.TilePieceStandingOn = TilePieceStandingOn;
    }
    piece(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard)
    {
        this.Coordinates.SetValues(x_cord,y_cord);
        this.Color = color;
        this.TilePieceStandingOn = TilePieceStandingOn;
        this.CurrentGameBoard = CurrentBoard;
    }

    void ReferenceTile(Tile input_tile)
    {
        this.TilePieceStandingOn = input_tile;
    }

    public boolean IsTileEmpty(int tile_index)
    {
        return CurrentGameBoard.Tiles.get(tile_index).isTileEmpty();
    };

    public abstract void Move(int newX,int newY);
    public abstract void capture();
    public abstract Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty);


}


