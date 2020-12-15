package components.impl;

import components.Checker;
import components.CheckerColor;
import components.Pillar;
import exceptions.BusyCellException;
import exceptions.ErrorException;
import exceptions.WhiteCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class NotHittingMoveTest {
    @Test
    public void testCorrectNotHittingMove() {
        Assertions.assertDoesNotThrow(()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "");
            board.notHittingMove(0, 7, 3, 4);
            Assertions.assertNull(board.getPillars()[7][0]);
            Pillar expected_pillar = new Pillar();
            expected_pillar.pickUp(new Checker(CheckerColor.WHITE, true));
            Assertions.assertEquals(expected_pillar, board.getPillars()[4][3]);
        });
    }


    @Test
    public void testBusyCell() {
        Assertions.assertThrows(BusyCellException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_w b2_w", "");
            board.notHittingMove(0, 7, 1, 6);
        });
    }

    @Test
    public void testWhiteCell() {
        Assertions.assertThrows(WhiteCellException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_w b2_w", "");
            board.notHittingMove(7, 0, 7, 1);
        });
    }

    @Test
    public void testImpossibleMove() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_w b2_w", "");
            board.notHittingMove(7, 0, 7, 2);
        });
        Assertions.assertEquals("Impossible move", e.getMessage());
    }

    @Test
    public void testKingMove() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_w b2_w", "");
            board.notHittingMove(0, 7, 2, 5);
        });
        Assertions.assertEquals("King move from non-king", e.getMessage());
    }

    @Test
    public void testBusyPath() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W b2_w", "");
            board.notHittingMove(0, 7, 2, 5);
        });
        Assertions.assertEquals("Busy path", e.getMessage());
    }
}
