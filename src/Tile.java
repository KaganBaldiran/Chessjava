


public class Tile
{
    Math.Vec2<Integer>  Tilecoordinates = new Math.Vec2<>();
    boolean TileEmpty;

    int Color;
    static final int BLACK = 0;
    static final int WHITE = 1;

    CollisionBox TileCollisionBox;

    Tile()
    {
        TileEmpty = true;

        this.TileCollisionBox = new CollisionBox((Tilecoordinates.x-1) * Board.SQUARE_SIZE_non_GUI + (Board.SQUARE_SIZE_non_GUI/2),
                                                  Tilecoordinates.y * Board.SQUARE_SIZE_non_GUI - (Board.SQUARE_SIZE_non_GUI/4),
                                                    Board.SQUARE_SIZE_non_GUI,
                                                   Board.SQUARE_SIZE_non_GUI);
    }
    Tile(int Color , Math.Vec2<Integer> coordinate)
    {
        TileEmpty = true;
        this.Color = Color;
        Tilecoordinates.SetValues(coordinate);

        this.TileCollisionBox = new CollisionBox((Tilecoordinates.x-1) * Board.SQUARE_SIZE_non_GUI + (Board.SQUARE_SIZE_non_GUI/2),
                                                  Tilecoordinates.y * Board.SQUARE_SIZE_non_GUI - (Board.SQUARE_SIZE_non_GUI/4),
                                                    Board.SQUARE_SIZE_non_GUI,
                                                   Board.SQUARE_SIZE_non_GUI);
    }

    void SetEmptinessState(boolean CurrentTileState)
    {
        TileEmpty = CurrentTileState;
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
