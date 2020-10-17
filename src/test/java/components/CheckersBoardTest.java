package components;

import exceptions.BusyCellException;
import exceptions.ErrorException;
import exceptions.InvalidMoveException;
import exceptions.WhiteCellException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CheckersBoardTest {

    @Test
    public void testConstructor() {
        String white_positions = "h6_wWwWwWbb";
        String black_positions = "";
        CheckersBoard board = new CheckersBoard(white_positions, black_positions);
        String expectedBoard = "Desk{ null  null  null  null  null  null  null  null \n" +
                " null  null  null  null  null  null  null  null \n" +
                " null  null  null  null  null  null  null  {[b, b, W, w, W, w, W, w]top} \n" +
                " null  null  null  null  null  null  null  null \n" +
                " null  null  null  null  null  null  null  null \n" +
                " null  null  null  null  null  null  null  null \n" +
                " null  null  null  null  null  null  null  null \n" +
                " null  null  null  null  null  null  null  null \n" +
                "}";
        String actualBoard = board.toString();
        //System.out.println(board.toString());
        Assertions.assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void testCorrectNotHittingMove() {
        CheckersBoard board = new CheckersBoard("a1_W", "");
        try {
            board.NotHittingMove(7, 0, 4, 3);
            Assertions.assertNull(board.pillars[7][0]);
            Pillar expected_pillar = new Pillar();
            expected_pillar.pickUp(new Checker(CheckerColor.WHITE, true));
            Assertions.assertEquals(expected_pillar, board.pillars[4][3]);
        } catch (Exception ignored) {}
    }
    @Test
    public void testBusyCell() {
        Assertions.assertThrows(BusyCellException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_w b2_w", "");
            board.NotHittingMove(0, 7, 1, 6);
        });
    }

    @Test
    public void testWhiteCell() {
        Assertions.assertThrows(WhiteCellException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_w b2_w", "");
            board.NotHittingMove(7, 0, 7, 1);
        });
    }

    @Test
    public void testImpossibleMove() {
        Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_w b2_w", "");
            board.NotHittingMove(7, 0, 7, 2);
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_w b2_w", "");
            board.NotHittingMove(7, 0, 7, 2);
        } catch (ErrorException e) {
            Assertions.assertEquals("Impossible move", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testKingMove() {
        Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_w b2_w", "");
            board.NotHittingMove(0, 7, 2, 5);
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_w b2_w", "");
            board.NotHittingMove(0, 7, 2, 5);
        } catch (ErrorException e) {
            Assertions.assertEquals("King move from non-king", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testTooShortMove() {
        Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_w", "");
            board.SimpleHittingMove(7, 0, 6, 1, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_w", "");
            board.SimpleHittingMove(7, 0, 6, 1, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        } catch (ErrorException e) {
            Assertions.assertEquals("Too short hitting move", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testKingMove2() {
        Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_w", "");
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_w", "");
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        } catch (ErrorException e) {
            Assertions.assertEquals("King move from non-king", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testCutNumber() {
        Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_W", "b2_b c3_b");
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_W", "b2_b c3_b");
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        } catch (ErrorException e) {
            Assertions.assertEquals("More then one cut in one move", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testCutNumber2() {
        Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_W", "");
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_W", "");
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
        } catch (ErrorException e) {
            Assertions.assertEquals("No cuts in hitting move", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testValidCheckerCut() {
        Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_W", "b2_b");
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, true), new ArrayList<>(), new ArrayList<>());
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_W", "b2_b");
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, true), new ArrayList<>(), new ArrayList<>());
        } catch (ErrorException e) {
            Assertions.assertEquals("Wrong checker cut", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testCorrectHittingMove() {
        CheckersBoard board = new CheckersBoard("a1_W", "b2_b");
        try {
            board.SimpleHittingMove(7, 0, 4, 3, new Checker(CheckerColor.BLACK, false), new ArrayList<>(), new ArrayList<>());
            Assertions.assertNull(board.pillars[6][1]);
            Assertions.assertNull(board.pillars[7][0]);
            Pillar expected_pillar = new Pillar();
            expected_pillar.pickUp(new Checker(CheckerColor.WHITE, true));
            expected_pillar.pickUp(new Checker(CheckerColor.BLACK, false));
            Assertions.assertEquals(expected_pillar, board.pillars[4][3]);
        } catch (Exception ignored) {}
    }

    @Test
    public void testForbiddenCoords() {
        Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_W", "b2_b");
            ArrayList<Integer> forbidden_xs = new ArrayList<>();
            ArrayList<Integer> forbidden_ys = new ArrayList<>();
            forbidden_xs.add(2);
            forbidden_ys.add(5);
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), forbidden_xs, forbidden_ys);
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_W", "b2_b");
            ArrayList<Integer> forbidden_xs = new ArrayList<>();
            ArrayList<Integer> forbidden_ys = new ArrayList<>();
            forbidden_xs.add(2);
            forbidden_ys.add(5);
            board.SimpleHittingMove(0, 7, 3, 4, new Checker(CheckerColor.BLACK, false), forbidden_xs, forbidden_ys);
        } catch (ErrorException e) {
            Assertions.assertEquals("Cutting one pillar twice", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testBusyPath() {
        Assertions.assertThrows(ErrorException.class, ()->{
            CheckersBoard board = new CheckersBoard("a1_W b2_w", "");
            board.NotHittingMove(0, 7, 2, 5);
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_W b2_w", "");
            board.NotHittingMove(0,7,2,5);
        } catch (ErrorException e) {
            Assertions.assertEquals("Busy path", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testWrongCheckerColor() {
        Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoard board = new CheckersBoard("a1_W", "");
            board.enemyMove(CheckerColor.BLACK, "a1_W-b2_b");
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_W", "");
            board.enemyMove(CheckerColor.BLACK, "a1_W-b2_b");
        } catch (ErrorException e) {
            Assertions.assertEquals("Move with a checker of the wrong color", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testMistakeInNotation() {
        Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoard board = new CheckersBoard("a1_W", "");
            board.enemyMove(CheckerColor.WHITE, "a1_w-b2_b");
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_W", "");
            board.enemyMove(CheckerColor.WHITE, "a1_w-b2_b");
        } catch (ErrorException e) {
            Assertions.assertEquals("Mistake in notation of the pillar", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testCuttingFriendPillar() {
        Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoard board = new CheckersBoard("a1_W", "b2_w");
            board.enemyMove(CheckerColor.WHITE, "a1_W:c3_Ww");
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_W", "b2_w");
            board.enemyMove(CheckerColor.WHITE, "a1_W:c3_Ww");
        } catch (ErrorException e) {
            Assertions.assertEquals("cutting friend pillar", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testCorrectEnemyMove() {
        Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoard board = new CheckersBoard("a1_W", "b2_w");
            board.enemyMove(CheckerColor.WHITE, "a1_W:c3_Ww");
        });
        try {
            CheckersBoard board = new CheckersBoard("a1_W", "b2_w");
            board.enemyMove(CheckerColor.WHITE, "a1_W:c3_Ww");
        } catch (ErrorException e) {
            Assertions.assertEquals("cutting friend pillar", e.getMessage());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testInvalidMove() {
        Assertions.assertThrows(InvalidMoveException.class, ()-> {
            CheckersBoard board = new CheckersBoard("a1_W", "b2_b e5_b");
            board.enemyMove(CheckerColor.WHITE, "a1_W:c3_Wb");
        });
    }

    @Test
    public void testInvalidMove2() {
        Assertions.assertThrows(InvalidMoveException.class, ()-> {
            CheckersBoard board = new CheckersBoard("a1_w", "b2_b d4_b");
            board.enemyMove(CheckerColor.WHITE, "a1_w:c3_Wb");
        });
    }

    @Test
    public void testInvalidMove3() {
        Assertions.assertThrows(InvalidMoveException.class, ()-> {
            CheckersBoard board = new CheckersBoard("a1_w", "b2_b d2_b");
            board.enemyMove(CheckerColor.WHITE, "a1_w:c3_Wb");
        });
    }

    @Test
    public void testInvalidMove4() {
        CheckersBoard board = new CheckersBoard("a1_w e1_w", "b2_b d2_b");
        Assertions.assertDoesNotThrow(()-> board.enemyMove(CheckerColor.WHITE, "a1_w:c3_Wb"));
    }

    @Test
    public void testInvalidMove5() {
        CheckersBoard board = new CheckersBoard("a3_w", "b2_bb d2_bb d4_bb b4_bb");
        Assertions.assertDoesNotThrow(()-> board.enemyMove(CheckerColor.WHITE, "a3_w:c5_wb:e3_wbb:c1_wbbb:a3_wbbbb"));
    }

    @Test
    public void testInvalidMove6() {
        CheckersBoard board = new CheckersBoard("a3_W", "b2_b d2_b d4_b b4_b d6_b");
        Assertions.assertThrows(InvalidMoveException.class, ()-> board.enemyMove(CheckerColor.WHITE, "a3_W:c5_Wb:e3_Wbb:c1_Wbbb:a3_Wbbbb"));
    }

    @Test
    public void testChangesToKing() {
        CheckersBoard board = new CheckersBoard("b2_w", "a3_b");
        Assertions.assertDoesNotThrow(()-> board.enemyMove(CheckerColor.BLACK, "a3_b:c1_Bw"));
    }

    @Test
    public void testChangesToKing2() {
        Assertions.assertThrows(ErrorException.class, ()-> {
            CheckersBoard board = new CheckersBoard("b2_w", "a3_b");
            board.enemyMove(CheckerColor.BLACK, "a3_b:c1_bw");
        });
        try {
            CheckersBoard board;
            board = new CheckersBoard("b2_w", "a3_b");
            board.enemyMove(CheckerColor.BLACK, "a3_b:c1_bw");
        } catch (Exception e) {
            Assertions.assertEquals("Doesn't changes to king", e.getMessage());
        }
    }
    @Test
    public void testGetCoords() {
        String coords1 = "a2";
        String coords2 = "B2";
        int[] expectedCoords1 = {0, 6};
        int[] expectedCoords2 = {1, 6};
        int[] actualCoords1 = CheckersBoard.getCoords(coords1);
        int[] actualCoords2 = CheckersBoard.getCoords(coords2);
        Assertions.assertArrayEquals(expectedCoords1, actualCoords1);
        Assertions.assertArrayEquals(expectedCoords2, actualCoords2);
    }
}