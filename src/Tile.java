
public class Tile
{
    Math.Vec2<Integer>  Tilecoordinates;
    boolean TileEmpty;

    Tile()
    {
        TileEmpty = true;
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
