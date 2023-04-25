import java.awt.event.MouseEvent;
import java.util.Vector;

public class Rook extends piece{

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    MouseInputListener CurrentMouseListenerReference;

    Rook(int x_cord,int y_cord , int color)
    {
        super(x_cord, y_cord, color);
    }
    Rook(int x_cord,int y_cord , int color, Tile TilePieceStandingOn)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn);
        this.CurrentMouseListenerReference = null;
    }
    Rook(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , String file_path)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard,file_path);
        this.CurrentMouseListenerReference = null;
        this.TilePieceStandingOn.SetEmptinessState(false);
    }

    Rook(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , String file_path, MouseInputListener current_mouse_listener)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard,file_path);
        this.CurrentMouseListenerReference = current_mouse_listener;
        this.TilePieceStandingOn.SetEmptinessState(false);
    }

    boolean IsPieceHovering = false;
    boolean IsPieceHoveringClick = false;


    @Override
    public void Move(int newX, int newY) {

        for (int i = 0; i < this.CurrentGameBoard.Tiles.size(); i++)
        {
            if (this.CurrentGameBoard.Tiles.get(i).TileCollisionBox.CheckCollisionBoxMouse(this.CurrentMouseListenerReference.GetMousePos()))
            {
                System.out.println("COLLISION SPOTTED ! TILE LOCATION X: "+ this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.x + " Y: "+ this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.y + "EMPTY: "+  this.CurrentGameBoard.Tiles.get(i).isTileEmpty());
                System.out.println("MOUSE LOCATION X: "+ this.CurrentMouseListenerReference.GetMousePos().x + " Y: "+ this.CurrentMouseListenerReference.GetMousePos().y );

                if (this.CurrentMouseListenerReference.isReleased(MouseEvent.BUTTON1))
                {
                    this.IsPieceHoveringClick = true;
                }

                if(this.CurrentMouseListenerReference.isClicked(MouseEvent.BUTTON1) && !this.IsPieceHovering && IsPieceHoveringClick &&
                        this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.x.intValue() == this.Coordinates.x.intValue() &&
                        this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.y.intValue() == this.Coordinates.y.intValue())
                {
                    this.IsPieceHovering = true;
                    this.IsPieceHoveringClick = false;
                    this.Selected = true;
                }

                //System.out.println("IsPieceHovering: " + this.IsPieceHovering + " IsPieceHoveringClick: " + IsPieceHoveringClick);

                if (this.CurrentMouseListenerReference.isClicked(MouseEvent.BUTTON1) && this.IsPieceHovering && IsPieceHoveringClick) {

                    this.IsPieceHovering = false;
                    this.IsPieceHoveringClick = false;
                    this.Selected = false;

                    for (Math.Vec2<Integer> possibleMove : Possible_Moves) {

                        if (possibleMove.x.intValue() == this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.x.intValue() && possibleMove.y.intValue() == this.CurrentGameBoard.Tiles.get(i).Tilecoordinates.y.intValue()) {

                            this.TilePieceStandingOn.SetEmptinessState(true);
                            Coordinates.SetValues(this.CurrentGameBoard.Tiles.get(i).Tilecoordinates);
                            this.TilePieceStandingOn = this.CurrentGameBoard.Tiles.get(i);
                            this.TilePieceStandingOn.SetEmptinessState(false);
                        }

                    }
                }


            }

        }
    }

    @Override
    public void capture()
    {

    }

    boolean SwitchSide = false;

    Math.Vec2<Integer> tileTracer = new Math.Vec2<>();
    int Side = UP;

    @Override
    public Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty, Math.Vec2<Integer> input_Coordinates)
    {

        System.out.println("INSIDE ROOK IS EMPTY: " + isTileEmpty);

        if(!this.Possible_Moves.isEmpty() && this.ClearPossibleMoves)
        {
            this.ClearPossibleMoves = false;
            this.Possible_Moves.clear();
        }

        if(this.SwitchSide)
        {
            this.Side++;
            this.SwitchSide = false;
        }


        if (this.Side == UP && input_Coordinates.y < 8 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y++;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);

            System.out.println("REAL TILE: " + tileTracer.x +" "+ tileTracer.y+ " FETCHED TILE : "+this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).Tilecoordinates.x +" "+ this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).Tilecoordinates.y );

        }
        else if (this.Side == DOWN  && input_Coordinates.y > 1 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y--;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == LEFT  && input_Coordinates.x > 1 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x--;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == RIGHT  && input_Coordinates.x < 8  && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x++;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if(this.Side <= RIGHT)
        {
            this.SwitchSide = true;
            tileTracer.SetValues(this.Coordinates);
            GetPossibleMoves(true,tileTracer);

        }

        if(this.Side > RIGHT)
        {
            //System.out.println("ROOK POSSIBLE MOVES RETURN THE VALUE: ");
            this.Side = UP;
            this.ClearPossibleMoves = true;
            return this.Possible_Moves;
        }
        return null;
    }
}
