package components.impl;

import components.Checker;
import components.CheckerColor;
import components.Pillar;
import exceptions.ErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SimpleHittingMoveTest {
    @Test
    public void testTooShortMove() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_w", "");
            board.simpleHittingMove(7, 0, 6, 1, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        });
        Assertions.assertEquals("Too short hitting move", e.getMessage());
    }

    @Test
    public void testKingMove2() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_w", "");
            board.simpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        });
        Assertions.assertEquals("King move from non-king", e.getMessage());
    }

    @Test
    public void testCutNumber() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "b2_b c3_b");
            board.simpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        });
        Assertions.assertEquals("More then one cut in one move", e.getMessage());
    }

    @Test
    public void testCutNumber2() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "");
            board.simpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        });
        Assertions.assertEquals("No cuts in hitting move", e.getMessage());
    }

    @Test
    public void testValidCheckerCut() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "b2_b");
            board.simpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, true), new ArrayList<>(), new ArrayList<>());
        });
        Assertions.assertEquals("Wrong checker cut", e.getMessage());
    }

    @Test
    public void testCorrectHittingMove() {
        Assertions.assertDoesNotThrow(()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "b2_b");
            board.simpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
            Assertions.assertNull(board.getPillars()[6][1]);
            Assertions.assertNull(board.getPillars()[7][0]);
            Pillar expected_pillar = new Pillar();
            expected_pillar.pickUp(new Checker(CheckerColor.WHITE, true));
            expected_pillar.pickUp(new Checker(CheckerColor.BLACK, false));
            Assertions.assertEquals(expected_pillar, board.getPillars()[4][3]);
        });
    }

    @Test
    public void testForbiddenCoords() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "b2_b");
            ArrayList<Integer> forbidden_xs = new ArrayList<>();
            ArrayList<Integer> forbidden_ys = new ArrayList<>();
            forbidden_xs.add(2);
            forbidden_ys.add(5);
            board.simpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), forbidden_xs, forbidden_ys);
        });
        Assertions.assertEquals("Cutting one pillar twice", e.getMessage());
    }
}
