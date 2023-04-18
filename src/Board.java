import java.util.Vector;

public class Board
{
    Vector<Tile> Tiles;

    public void Board() {
        Integer TempColor = 0;
        Integer ColorCounter = 0;
        Math.Vec2<Integer> TempLocation = new Math.Vec2<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (ColorCounter > 1) {
                    ColorCounter = 0;
                }
                if (ColorCounter == 0) {
                    TempColor = 0;
                } else if (ColorCounter == 1) {
                    TempColor = 1;
                }
                TempLocation.SetValues(x, y);
                Tiles.add(new Tile(TempColor, TempLocation));
                ColorCounter++;
            }
        }
    }

}
