import components.CheckersBoard;
import components.impl.CheckersBoardImpl;
import exceptions.BusyCellException;
import exceptions.ErrorException;
import exceptions.InvalidMoveException;
import exceptions.WhiteCellException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String whitePositions = sc.nextLine();
            String blackPositions = sc.nextLine();
            CheckersBoard board = new CheckersBoardImpl(whitePositions, blackPositions);
            while (sc.hasNext()) {
                try {
                    board.move(sc.nextLine());
                } catch (WhiteCellException e) {
                    System.out.println("white cell");
                    return;
                } catch (InvalidMoveException e) {
                    System.out.println("invalid move");
                    return;
                } catch (BusyCellException e) {
                    System.out.println("busy cell");
                    return;
                } catch (ErrorException e) {
                    System.out.println("error");
                    return;
                }
            }
            System.out.println(board.toString());
        }
    }
}
