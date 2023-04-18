import java.util.Vector;

public class Board
{

    Vector<Tile> Tiles;



   Board()
   {
       Integer TempColor = new Integer;

       Integer ColorCounter = 0;

       for (int y = 0; y < 8; y++)
       {
           for (int x = 0; x < 8; x++)
           {
               if (ColorCounter > 1)
               {
                   ColorCounter = 0;
               }

               if (ColorCounter == 0)
               {
                   TempColor = Tile.BLACK;
               }
               else if (ColorCounter == 1)
               {
                   TempColor = Tile.WHITE;
               }

               Math.Vec2<Integer> TempLocation = new Math.Vec2<Integer>;
               TempLocation.SetValues(x,y);
               Tiles.add(new Tile(TempColor,TempLocation));
               
               ColorCounter++;

           }

       }

   }

   void Set




}
