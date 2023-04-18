


public class Tile
{
    Math.Vec2<Integer>  Tilecoordinates;
    boolean TileEmpty;
    piece PieceOnThisTile;
    int Color;
    static final int BLACK = 0;
    static final int WHITE = 1;

    Tile()
    {
        TileEmpty = true;
    }
    Tile(int Color , Math.Vec2<Integer> coordinate)
    {
        TileEmpty = true;
        this.Color = Color;
        Tilecoordinates = coordinate;
    }

    void SetPiece(piece CurrentPiece)
    {
        this.PieceOnThisTile = CurrentPiece;
        TileEmpty = false;
    }
    
    public boolean isTileEmpty() {
        return TileEmpty;
    }
    public Math.Vec2<Integer> getTilecoordinates()
    {
        return Tilecoordinates;
    }
    public void setTilecoordinates(Math.Vec2<Integer> tilecoordinates)
    {
        Tilecoordinates = tilecoordinates;
    }
}
