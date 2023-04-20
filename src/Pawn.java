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
    }
    Pawn(int x_cord, int y_cord , int color , Tile TilePieceStandingOn)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn);
        this.First_turn = true;
        this.tileTracer = new Math.Vec2<>();
    }
    Pawn(int x_cord, int y_cord , int color , Tile TilePieceStandingOn , Board CurrentBoard)
    {
        super(x_cord, y_cord, color, TilePieceStandingOn, CurrentBoard);
        this.First_turn = true;
        this.tileTracer = new Math.Vec2<>();
    }

        @Override
    public void Move(int newX, int newY) {
        this.Coordinates.SetValues(newX,newY);
    }

    @Override
    public void capture()
    {

    }

    int times_to_repeat = 1;

    @Override
    public Vector<Math.Vec2<Integer>> GetPossibleMoves(boolean isTileEmpty , Math.Vec2<Integer> input_Coordinates) {


        if(!this.Possible_Moves.isEmpty() && this.Count_of_reco == 0)
        {
            this.Possible_Moves.clear();
        }

        if (this.First_turn)
        {
            times_to_repeat = 2;
            this.First_turn = false;
        }

        if (isTileEmpty && this.Count_of_reco < times_to_repeat)
        {
            this.Count_of_reco++;
            tileTracer.SetValues(input_Coordinates);

            tileTracer.y += 1;

            if (this.Color == Tile.BLACK)
            {
                tileTracer = Math.UV_Tools.Invert_Y_Axis(tileTracer,Tile.WHITE);
            }

            this.Possible_Moves.add(new Math.Vec2<>(tileTracer.x,tileTracer.y));

            GetPossibleMoves(this.CurrentGameBoard.FetchTile(tileTracer.x, tileTracer.y).isTileEmpty(),tileTracer);

        }

        else if(!isTileEmpty || this.Count_of_reco == times_to_repeat)
        {

           this.Count_of_reco = 0;
           this.times_to_repeat = 1;

            return this.Possible_Moves;
        }
        return null;
    }
}
