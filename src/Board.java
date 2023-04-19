import javax.lang.model.type.NullType;
import java.util.Vector;

public class Board
{
    Vector<Tile> Tiles = new Vector<>();

    Board() {

        int TempColor = 0;
        int ColorCounter = 0;

        Math.Vec2<Integer> TempLocation = new Math.Vec2<>();

        for (int y = 1; y < 9; y++) {

            for (int x = 1; x < 9; x++) {

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

    public Tile FetchTile(int x_cord , int y_cord)
    {
        Tile returntile = null;

        if (!this.Tiles.isEmpty())
        {

            int index = ((y_cord - 1) * 8) + (x_cord - 1);

            System.out.println("INDEX: " + String.valueOf(index) );

            if (index >= 0)
            {
                returntile = Tiles.get(index);
            }

        }

        System.out.println("INSIDE THE FUNCTION: " + String.valueOf(returntile.Tilecoordinates.x) + " " +  String.valueOf(returntile.Tilecoordinates.y) );

        return returntile;
    };

    public Tile FetchTile(int index_in_array)
    {
        Tile returntile = null;

        if (index_in_array >= 0)
        {
            returntile = Tiles.get(index_in_array);
        }

        return returntile;
    };

}
