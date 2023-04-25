import javax.lang.model.type.NullType;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Board extends JPanel
{
    Vector<Tile> Tiles = new Vector<>();

    Math.Vec4<Integer> MinMaxBoundaries = new Math.Vec4<>();

    public static final int SQUARE_SIZE = (int)(900.0f / 8.0f);

    Board() {

        int TempColor = 0;
        int ColorCounter = 0;

        Math.Vec2<Integer> TempLocation = new Math.Vec2<>();

        this.MinMaxBoundaries.SetValues(1,8,1,8);

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

    Board(int frame_size) {

        int TempColor = 0;
        int ColorCounter = 0;

        Math.Vec2<Integer> TempLocation = new Math.Vec2<>();

        this.MinMaxBoundaries.SetValues(1,8,1,8);

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

            //System.out.println("INDEX: " + String.valueOf(index) );

            if (index >= 0)
            {
                returntile = Tiles.get(index);
            }

        }

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


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int x = col * SQUARE_SIZE;
                int y = row * SQUARE_SIZE;
                if ((row + col) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {

                    Color DARK_CUSTOM_GREEN = GraphicHandler.HexToRgba("#7D945D");
                    g.setColor(DARK_CUSTOM_GREEN);
                }
                g.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
            }
        }

    }

}
