package test;

import core.Board;
import core.GraphicHandler;
import core.Math;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.util.Vector;

public class TestHandler
{
    core.Board GameBoard = new Board();
    core.Player TestPlayer;

    core.GraphicHandler graphicHandler;

    public TestHandler()
    {
        TestPlayer = new core.Player(GameBoard , null , false);
        graphicHandler = new GraphicHandler(GameBoard,TestPlayer,null);
    }

    @Test
    @DisplayName("Test Pawn possible moves")
    public void TestPawn()
    {
        Vector<Math.Vec2<Integer>> CorrectPossibleMoves = new Vector<>();
        CorrectPossibleMoves.add(new Math.Vec2<>(4,2));
        CorrectPossibleMoves.add(new Math.Vec2<>(4,1));

        TestPlayer.AddPiece("pawn" , 4 ,3);
        TestPlayer.GetLastPiece().GetPossibleMoves(true , TestPlayer.GetLastPiece().Coordinates);
        Vector<Math.Vec2<Integer>> PossibleMoves = TestPlayer.GetLastPiece().Possible_Moves;

        for (int i = 0; i < CorrectPossibleMoves.size(); i++)
        {
            System.out.println("Possible pos x: " + PossibleMoves.get(i).x + " y: " + PossibleMoves.get(i).y);

            Assertions.assertEquals(PossibleMoves.get(i).x , CorrectPossibleMoves.get(i).x);
            Assertions.assertEquals(PossibleMoves.get(i).y , CorrectPossibleMoves.get(i).y);
        }

        TestPlayer.ClearPieces();
    }

    @Test
    @DisplayName("Test King possible moves")
    public void TestKing()
    {
        Vector<Math.Vec2<Integer>> CorrectPossibleMoves = new Vector<>();
        CorrectPossibleMoves.add(new Math.Vec2<>(4, 2));
        CorrectPossibleMoves.add(new Math.Vec2<>(3, 3));
        CorrectPossibleMoves.add(new Math.Vec2<>(4, 4));
        CorrectPossibleMoves.add(new Math.Vec2<>(5, 3));
        CorrectPossibleMoves.add(new Math.Vec2<>(3, 2));
        CorrectPossibleMoves.add(new Math.Vec2<>(3, 4));
        CorrectPossibleMoves.add(new Math.Vec2<>(5, 2));
        CorrectPossibleMoves.add(new Math.Vec2<>(5, 4));

        TestPlayer.AddPiece("king" , 4 ,3);
        TestPlayer.GetLastPiece().GetPossibleMoves(true , TestPlayer.GetLastPiece().Coordinates);
        Vector<Math.Vec2<Integer>> PossibleMoves = TestPlayer.GetLastPiece().Possible_Moves;

        for (int i = 0; i < CorrectPossibleMoves.size(); i++)
        {
            System.out.println("Possible pos x: " + PossibleMoves.get(i).x + " y: " + PossibleMoves.get(i).y);

            Assertions.assertEquals(PossibleMoves.get(i).x , CorrectPossibleMoves.get(i).x);
            Assertions.assertEquals(PossibleMoves.get(i).y , CorrectPossibleMoves.get(i).y);
        }

        TestPlayer.ClearPieces();
    }

}
