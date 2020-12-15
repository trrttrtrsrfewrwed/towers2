package components.impl;

import components.CheckersBoard;
import components.impl.CheckersBoardImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckersBoardImplTest {

    @Test
    public void testConstructor() {
        String white_positions = "h6_wWwWwWbb";
        String black_positions = "";
        CheckersBoardImpl board = new CheckersBoardImpl(white_positions, black_positions);
        String expectedBoard = "h6_wWwWwWbb \n";
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
        int[] actualCoords1 = CheckersBoardImpl.getCoords(coords1);
        int[] actualCoords2 = CheckersBoardImpl.getCoords(coords2);
        Assertions.assertArrayEquals(expectedCoords1, actualCoords1);
        Assertions.assertArrayEquals(expectedCoords2, actualCoords2);
    }
}