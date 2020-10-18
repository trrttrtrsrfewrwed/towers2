package components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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