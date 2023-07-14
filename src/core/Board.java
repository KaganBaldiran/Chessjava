package core;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Board extends JPanel
{
    Vector<Tile> Tiles = new Vector<>();

    Math.Vec4<Integer> MinMaxBoundaries = new Math.Vec4<>();

    static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize().getSize();

    //public static final int SQUARE_SIZE = (int)((screenSize.getHeight() * 0.90f) / 8.0f);
    public static int SQUARE_SIZE = (int)(((screenSize.getHeight() * 0.90f) / 8.0f)) + 1;
    public static int SQUARE_SIZE_UI = (int)(((screenSize.getHeight() * 0.90f) / 8.0f)) + 1;
    public static int SQUARE_SIZE_non_GUI = (int)(((screenSize.getHeight() * 0.90f) / 8.0f));


    public static final Color DARK_CUSTOM_GREEN = GraphicHandler.HexToRgba("#7D945D");
    public static final Color LIGHT_YELLOW = GraphicHandler.HexToRgba("#E4EA87");

    public boolean FlipTheBoard = false;

    public Board() {

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

    public static void UpdateSquareSize(float buffered_image_height)
    {
        SQUARE_SIZE_non_GUI = (int)((buffered_image_height / 8.0f));
    }

    public static void UpdateSQUARESIZEUI(int CurrentChessBoardSize)
    {
        SQUARE_SIZE_UI = (int) (CurrentChessBoardSize / 8.0f);
    }

    public static void UpdateDrawingSquareSize(double slideAmount)
    {
        SQUARE_SIZE = (int) (SQUARE_SIZE * slideAmount);
    }

    public void UpdateCollisionBoxes(Math.Vec2<Float> position)
    {
        for (Tile tile : Tiles)
        {
            tile.TileCollisionBox.SetValues((tile.Tilecoordinates.x-1) * Board.SQUARE_SIZE_UI + (Board.SQUARE_SIZE_UI / 2) + (position.x.intValue()),
                    tile.Tilecoordinates.y * Board.SQUARE_SIZE_UI  + position.y.intValue(),
                    Board.SQUARE_SIZE_UI , Board.SQUARE_SIZE_UI );
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

                if(!FlipTheBoard) {
                    if ((row + col) % 2 == 0) {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(DARK_CUSTOM_GREEN);
                    }
                }
                else
                {
                    if ((row + col) % 2 == 0) {
                        g.setColor(DARK_CUSTOM_GREEN);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                }
                g.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
            }
        }

    }

}
