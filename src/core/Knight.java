package core;

import java.util.Vector;

public class Knight extends piece
{

    public static final int UP_LEFT = 1;
    public static final int UP_RIGHT = 2;
    public static final int DOWN_LEFT = 3;
    public static final int DOWN_RIGHT = 4;
    public static final int LEFT_UP = 5;
    public static final int LEFT_DOWN = 6;
    public static final int RIGHT_UP = 7;
    public static final int RIGHT_DOWN = 8;


    Knight(int x_cord , int y_cord , int color)
    {
        super(x_cord, y_cord, color);
    }


    Knight(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , String file_path , MouseInputListener current_mouse_listener, Player player_this_piece_belongs)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard , file_path,current_mouse_listener,player_this_piece_belongs);
        this.TilePieceStandingOn.SetEmptinessState(false);
    }

    Knight(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , Texture existing_texture , MouseInputListener current_mouse_listener, Player player_this_piece_belongs)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard, existing_texture, current_mouse_listener,player_this_piece_belongs);
        this.TilePieceStandingOn.SetEmptinessState(false);
    }


    @Override
    public void capture(Player OpponentPlayer)
    {
        if(this.NewTileHasEnemyPiece)
        {
            if(NewTileOldEnemyPiece.PieceType == "core.King")
            {
                CheckMate = true;
                this.player_this_piece_belongs.CheckMate = true;
            }

            this.player_this_piece_belongs.TakeOpponentPiece(NewTileOldEnemyPiece);
            OpponentPlayer.pieces.remove(NewTileOldEnemyPiece.PieceIndexInPlayer);

            for (int i = 0; i < OpponentPlayer.pieces.size(); i++)
            {
                OpponentPlayer.pieces.get(i).PieceIndexInPlayer = i;
            }
            //NewTileHasEnemyPiece = false;
        }

    }

    boolean SwitchSide = false;

    Math.Vec2<Integer> tileTracer = new Math.Vec2<>();
    int Side = UP_LEFT;

    Math.Vec2<Boolean> IsEmptyflag;

    @Override
    public Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty, Math.Vec2<Integer> input_Coordinates) {

        input_Coordinates = this.Coordinates;

        if(!this.Possible_Moves.isEmpty() && this.ClearPossibleMoves)
        {
            this.ClearPossibleMoves = false;
            this.Possible_Moves.clear();
        }

        if(IsEmptyflag != null) {
            if (IsEmptyflag.y) {
                SwitchSide = true;
                tileTracer.SetValues(this.Coordinates);
            }
        }

        if(this.SwitchSide)
        {
            this.Side++;
            this.SwitchSide = false;
        }

        IsEmptyflag = CheckTileEmptiness(Side, input_Coordinates);

        isTileEmpty = IsEmptyflag.x;

        if (this.Side == UP_LEFT && input_Coordinates.y < 7 && input_Coordinates.x > 1 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y += 2;
            tileTracer.x--;

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == UP_RIGHT  && input_Coordinates.y < 7 && input_Coordinates.x < 8 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y += 2;
            tileTracer.x++;

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == DOWN_LEFT  && input_Coordinates.y > 2 && input_Coordinates.x > 1 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y -= 2;
            tileTracer.x--;

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == DOWN_RIGHT  && input_Coordinates.y > 2 && input_Coordinates.x < 8 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y -= 2;
            tileTracer.x++;

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == LEFT_UP  && input_Coordinates.y < 8  && input_Coordinates.x > 2 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x -= 2;
            tileTracer.y++;

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == LEFT_DOWN  && input_Coordinates.y > 1  && input_Coordinates.x > 2 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x -= 2;
            tileTracer.y--;

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == RIGHT_UP && input_Coordinates.y < 8  && input_Coordinates.x < 7 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x += 2;
            tileTracer.y++;

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if (this.Side == RIGHT_DOWN && input_Coordinates.y > 1  && input_Coordinates.x < 7 && isTileEmpty)
        {
            tileTracer.SetValues(input_Coordinates);

            tileTracer.x += 2;
            tileTracer.y--;

            this.SwitchSide = true;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);
        }
        else if(this.Side <= RIGHT_DOWN)
        {
            this.SwitchSide = true;
            tileTracer.SetValues(this.Coordinates);
            GetPossibleMoves(true,tileTracer);

        }

        if(this.Side > RIGHT_DOWN)
        {
            //System.out.println("QUEEN POSSIBLE MOVES RETURN THE VALUE: ");
            this.Side = UP_LEFT;
            this.ClearPossibleMoves = true;
            return this.Possible_Moves;
        }
        return null;
    }

    /*if (this.Side == UP_LEFT && input_Coordinates.y > 1 )
    {
        input_Coordinates.y--;
        result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
    }

*/
    Math.Vec2<Boolean> flag = new Math.Vec2<>();

    public Math.Vec2<Boolean> CheckTileEmptiness(int Side , Math.Vec2<Integer> Coordinates)
    {
        boolean result = false;
        Math.Vec2<Integer> input_Coordinates = new Math.Vec2<>(Coordinates);

        if(Side <= RIGHT_DOWN)
        {
            if (Side == UP_LEFT && input_Coordinates.y < 7 && input_Coordinates.x > 1)
            {
                input_Coordinates.y += 2;
                input_Coordinates.x--;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (Side == UP_RIGHT  && input_Coordinates.y < 7 && input_Coordinates.x < 8)
            {
                input_Coordinates.y += 2;
                input_Coordinates.x++;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (Side == DOWN_LEFT  && input_Coordinates.y > 2 && input_Coordinates.x > 1 )
            {
                input_Coordinates.y -= 2;
                input_Coordinates.x--;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (Side == DOWN_RIGHT  && input_Coordinates.y > 2 && input_Coordinates.x < 8 )
            {
                input_Coordinates.y -= 2;
                input_Coordinates.x++;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (Side == LEFT_UP  && input_Coordinates.y < 8  && input_Coordinates.x > 2)
            {
                input_Coordinates.x -= 2;
                input_Coordinates.y++;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (Side == LEFT_DOWN  && input_Coordinates.y > 1  && input_Coordinates.x > 2)
            {
                input_Coordinates.x -= 2;
                input_Coordinates.y--;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (Side == RIGHT_UP && input_Coordinates.y < 8  && input_Coordinates.x < 7 )
            {
                input_Coordinates.x += 2;
                input_Coordinates.y++;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }
            else if (Side == RIGHT_DOWN && input_Coordinates.y > 1  && input_Coordinates.x < 7)
            {
                input_Coordinates.x += 2;
                input_Coordinates.y--;
                result = this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).isTileEmpty();
            }

            System.out.println("IS IT EMPTY SIDE "+ Side + ": " + result);

        }

        flag.SetValues(result, false);

        if (!result) {
            if (this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).PieceThatStandsOnThisTile != null) {
                System.out.println("PIECE COLOR: " + " Main.piece name: " + this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).PieceThatStandsOnThisTile.PieceType + " " + this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).PieceThatStandsOnThisTile.Color + " this.color: " + this.Color);
                if (this.CurrentGameBoard.FetchTile(input_Coordinates.x, input_Coordinates.y).PieceThatStandsOnThisTile.Color != this.Color) {

                    flag.SetValues(true, true);
                }


            }


        }
        return flag;
    }

}
