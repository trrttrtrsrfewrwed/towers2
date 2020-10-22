import components.CheckersBoard;
import exceptions.BusyCellException;
import exceptions.ErrorException;
import exceptions.InvalidMoveException;
import exceptions.WhiteCellException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String white_positions = sc.nextLine();
        String black_positions = sc.nextLine();
        CheckersBoard board = new CheckersBoard(white_positions, black_positions);
        int n = sc.nextInt();
        sc.nextLine();
        // System.out.println(n);
        for (int i = 0; i < n; ++i) {
            try {
                board.move(sc.nextLine());
            } catch (WhiteCellException e) {
                System.out.println("white cell");
            } catch (InvalidMoveException e) {
                System.out.println("invalid move");
            } catch (BusyCellException e) {
                System.out.println("busy cell");
            } catch (ErrorException e) {
                System.out.println("error");
                System.out.println(e.getMessage());
            }
        }
        System.out.println(board.toString());
    }
}
