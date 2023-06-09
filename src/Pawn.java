import javax.swing.*;
import java.util.Vector;

public class Pawn extends piece {

    boolean First_turn;
    Math.Vec2<Integer> tileTracer;
    int Count_of_reco = 0;

    Pawn(int x_cord , int y_cord , int color)
    {
        super(x_cord, y_cord, color);
        this.First_turn = true;
        this.tileTracer = new Math.Vec2<>();
        this.TilePieceStandingOn.SetEmptinessState(false);
    }
    Pawn(int x_cord, int y_cord , int color , Tile TilePieceStandingOn)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn);
        this.First_turn = true;
        this.tileTracer = new Math.Vec2<>();
        this.TilePieceStandingOn.SetEmptinessState(false);
    }
    Pawn(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , String file_path,  MouseInputListener current_mouse_listener, Player player_this_piece_belongs)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard,file_path,current_mouse_listener,player_this_piece_belongs);
        this.First_turn = true;
        this.tileTracer = new Math.Vec2<>();
        this.TilePieceStandingOn.SetEmptinessState(false);
    }
    Pawn(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard , Texture existing_texture , MouseInputListener current_mouse_listener, Player player_this_piece_belongs)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard, existing_texture, current_mouse_listener,player_this_piece_belongs);
        this.First_turn = true;
        this.tileTracer = new Math.Vec2<>();
        this.TilePieceStandingOn.SetEmptinessState(false);
    }


    @Override
    public void capture(Player OpponentPlayer)
    {

    }

    int times_to_repeat = 1;


    @Override
    public Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty , Math.Vec2<Integer> input_Coordinates) {

        if (!this.FirstMove)
        {
           times_to_repeat = 2;
        }
        if (this.Coordinates.y < 8 && this.Coordinates.y > 1)
        {
            tileTracer.SetValues(this.Coordinates);
            tileTracer.y--;
            //tileTracer.SetValues(Math.UV_Tools.Invert_Y_Axis(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).Tilecoordinates , Tile.WHITE));
            isTileEmpty = this.CurrentGameBoard.FetchTile(tileTracer.x , tileTracer.y).isTileEmpty();
            System.out.println("TILE " + input_Coordinates.x + " " + input_Coordinates.y + " " + isTileEmpty);

        }
        else
        {
            isTileEmpty = false;
        }

        if((!this.Possible_Moves.isEmpty() && this.Count_of_reco == 0) || this.ClearPossibleMoves)
        {
            this.ClearPossibleMoves = false;
            this.Possible_Moves.clear();
        }

        if (isTileEmpty && (this.Count_of_reco < times_to_repeat) && this.Coordinates.y < 8 && this.Coordinates.y >= 1)
        {
            this.Count_of_reco++;

            tileTracer.SetValues(input_Coordinates);
            tileTracer.y--;

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);

        }

        if(!isTileEmpty || this.Count_of_reco == times_to_repeat || !(input_Coordinates.y < 8 && input_Coordinates.y >= 1))
        {
            this.ClearPossibleMoves = true;
            this.Count_of_reco = 0;
            this.times_to_repeat = 1;
            First_turn = false;
            return this.Possible_Moves;
        }
        return null;
    }
}
