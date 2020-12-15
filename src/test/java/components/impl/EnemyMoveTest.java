package components.impl;

import components.CheckerColor;
import components.CheckersBoard;
import exceptions.ErrorException;
import exceptions.InvalidMoveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnemyMoveTest {
    @Test
    public void testWrongCheckerColor() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "");
            board.enemyMove(CheckerColor.BLACK, "a1_W-b2_b");
        });
        Assertions.assertEquals("Move with a checker of the wrong color", e.getMessage());
    }

    @Test
    public void testMistakeInNotation() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoardImpl board = new CheckersBoardImpl("a1_w", "");
            board.enemyMove(CheckerColor.WHITE, "a1_wb-b2_b");
        });
        Assertions.assertEquals("Mistake in notation of the pillar", e.getMessage());
    }

    @Test
    public void testCuttingFriendPillar() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "b2_w");
            board.enemyMove(CheckerColor.WHITE, "a1_W:c3_Ww");
        });
        Assertions.assertEquals("cutting friend pillar", e.getMessage());
    }

    @Test
    public void testCorrectEnemyMove() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "b2_w");
            board.enemyMove(CheckerColor.WHITE, "a1_W:c3_Ww");
        });
        Assertions.assertEquals("cutting friend pillar", e.getMessage());
    }

    @Test
    public void testInvalidMove() {
        Assertions.assertThrows(InvalidMoveException.class, ()-> {
            CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "b2_b e5_b");
            board.enemyMove(CheckerColor.WHITE, "a1_W:c3_Wb");
        });
    }

    @Test
    public void testInvalidMove2() {
        Assertions.assertThrows(InvalidMoveException.class, ()-> {
            CheckersBoardImpl board = new CheckersBoardImpl("a1_w", "b2_b d4_b");
            board.enemyMove(CheckerColor.WHITE, "a1_w:c3_Wb");
        });
    }

    @Test
    public void testInvalidMove3() {
        Assertions.assertThrows(InvalidMoveException.class, ()-> {
            CheckersBoardImpl board = new CheckersBoardImpl("a1_w", "b2_b d2_b");
            board.enemyMove(CheckerColor.WHITE, "a1_w:c3_Wb");
        });
    }

    @Test
    public void testInvalidMove4() {
        CheckersBoardImpl board = new CheckersBoardImpl("a1_w e1_w", "b2_b d2_b");
        Assertions.assertDoesNotThrow(()-> board.enemyMove(CheckerColor.WHITE, "a1_w:c3_Wb"));
    }

    @Test
    public void testInvalidMove5() {
        CheckersBoardImpl board = new CheckersBoardImpl("a3_w", "b2_bb d2_bb d4_bb b4_bb");
        Assertions.assertDoesNotThrow(()-> board.enemyMove(CheckerColor.WHITE, "a3_w:c5_wb:e3_wbb:c1_wbbb:a3_wbbbb"));
    }

    @Test
    public void testInvalidMove6() {
        CheckersBoardImpl board = new CheckersBoardImpl("a3_W", "b2_b d2_b d4_b b4_b d6_b");
        Assertions.assertThrows(InvalidMoveException.class, ()-> board.enemyMove(CheckerColor.WHITE, "a3_W:c5_Wb:e3_Wbb:c1_Wbbb:a3_Wbbbb"));
    }

    @Test
    public void testInvalidMove7() {
        CheckersBoardImpl board = new CheckersBoardImpl("a1_W", "b2_b");
        Assertions.assertThrows(InvalidMoveException.class, ()-> board.enemyMove(CheckerColor.WHITE, "a1_W-b2_W"));
    }

    @Test
    public void testInvalidMove8() {
        CheckersBoardImpl board = new CheckersBoardImpl("a1_W a3_w", "b2_b");
        Assertions.assertThrows(InvalidMoveException.class, ()-> board.enemyMove(CheckerColor.WHITE, "a3_w-b2_w"));
    }

    /*@Test
    public void testChangesToKing() {
        CheckersBoardImpl board = new CheckersBoardImpl("b2_w d2_w", "a3_b");
        Assertions.assertDoesNotThrow(()-> board.enemyMove(CheckerColor.BLACK, "a3_b:c1_Bw:e3_Bww"));
    }

    @Test
    public void testChangesToKing2() {
        Exception e = Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoardImpl board = new CheckersBoardImpl("b2_w", "a3_b");
            board.enemyMove(CheckerColor.BLACK, "a3_b:c1_bw");
        });
        Assertions.assertEquals("Doesn't change to king", e.getMessage());
    }*/
}
