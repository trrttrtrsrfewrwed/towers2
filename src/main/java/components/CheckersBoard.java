package components;

import exceptions.BusyCellException;
import exceptions.ErrorException;
import exceptions.InvalidMoveException;
import exceptions.WhiteCellException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Representation of CheckersBoard in Pillar Russian checkers
 */
public class CheckersBoard {
    Pillar[][] pillars = new Pillar[8][8];

    /**
     * Constructor of CheckersBoard, fills it with pillars
     *
     * @param white_positions - coordinates of white checkers (and towers with a white lid)
     * @param black_positions - coordinates of black checkers (and towers with a black lid)
     */
    public CheckersBoard(String white_positions, String black_positions) {
        for (String wh_pos : white_positions.split(" ")) {
            if (!wh_pos.isEmpty()) {
                addPillar(wh_pos);
            }
        }
        for (String bl_pos : black_positions.split(" ")) {
            if (!bl_pos.isEmpty()) {
                addPillar(bl_pos);
            }
        }
    }


    /**
     * @return String representation of CheckersBoard
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Desk{");
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                str.append(" ").append(pillars[i][j]).append(" ");
            }
            str.append("\n");
        }
        str.append('}');
        return str.toString();
    }

    /**
     *
     * @param move - move in standard checkers notation
     * @throws BusyCellException
     * @throws WhiteCellException
     * @throws ErrorException
     * @throws InvalidMoveException
     */
    public void move(String move) throws BusyCellException, WhiteCellException, ErrorException, InvalidMoveException {
        String[] moves = move.split(" ");
        String white_move = moves[0];
        String black_move = moves[1];
        enemyMove(CheckerColor.WHITE, white_move);
        enemyMove(CheckerColor.BLACK, black_move);
    }

    private boolean isPossibleToCut(CheckerColor color, int x, int y, ArrayList<Integer> forbidden_xs, ArrayList<Integer> forbidden_ys) {
        boolean is_king = pillars[y][x].isKing();
        if (is_king) {
            int tmp_x;
            int tmp_y;
            for (int x_step = -1; x_step <= 1; x_step += 2) {
                for (int y_step = -1; y_step <= 1; y_step += 2) {
                    tmp_x = x;
                    tmp_y = y;
                    while (inBounds(tmp_x + x_step, tmp_y + y_step)) {
                        tmp_x += x_step;
                        tmp_y += y_step;
                        if (isForbidden(tmp_x, tmp_y, forbidden_xs, forbidden_ys)) {
                            break;
                        }
                        if (isBusy(tmp_x, tmp_y)) {
                            if (!pillars[tmp_y][tmp_x].getColor().equals(color)) {
                                if (inBounds(tmp_x + x_step, tmp_y + y_step) && !isBusy(tmp_x + x_step, tmp_y + y_step)) {
                                    return true;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            for (int x_step = -1; x_step <= 1; x_step += 2) {
                for (int y_step = -1; y_step <= 1; y_step += 2) {
                    if (!inBounds(x + x_step, y + y_step)) {
                        continue;
                    }
                    if (isForbidden(x + x_step, y + y_step, forbidden_xs, forbidden_ys)) {
                        continue;
                    }
                    if (!isBusy(x + x_step, y + y_step)) {
                        continue;
                    }
                    if (pillars[y + y_step][x + x_step].getColor().equals(color)) {
                        continue;
                    }
                    if (inBounds(x + 2*x_step, y + 2*y_step) && !isBusy(x + 2*x_step, y + 2*y_step)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean IsChangesToKing(CheckerColor color, int x, int y, Pillar next_pillar) {
        if (color.equals(CheckerColor.WHITE) && y == 0) {
            return next_pillar.isKing();
        }
        if (color.equals(CheckerColor.BLACK) && y == 7) {
            return next_pillar.isKing();
        }
        return true;
    }

    void enemyMove(CheckerColor color, String move) throws BusyCellException, WhiteCellException, ErrorException, InvalidMoveException{
        boolean is_hitting = false;
        String[] positions;
        if (move.contains("-")) {
            positions = move.split("-");
        } else {
            is_hitting = true;
            positions = move.split(":");
        }
        ArrayList<Integer> forbidden_xs = new ArrayList<Integer>();
        ArrayList<Integer> forbidden_ys = new ArrayList<Integer>();

        for (int i = 0; i < positions.length - 1; ++i) {
            String pos = positions[i];
            int[] coords = getCoords(pos.substring(0, 2));
            int x_coord = coords[0];
            int y_coord = coords[1];
            Pillar expected_pillar = getPillar(pos);
            Pillar actual_pillar = pillars[y_coord][x_coord];
            if (!expected_pillar.getColor().equals(color)) {
                throw new ErrorException("Move with a checker of the wrong color");
            }
            if (!expected_pillar.equals(actual_pillar)) {
                throw new ErrorException("Mistake in notation of the pillar");
            } else {
                String next_pos = positions[i + 1];
                int[] next_coords = getCoords(next_pos.substring(0, 2));
                int next_x = next_coords[0];
                int next_y = next_coords[1];
                if (!IsChangesToKing(color, next_x, next_y, getPillar(next_pos))) {
                    throw new ErrorException("Doesn't changes to king");
                }
                if (is_hitting) {
                    char symbol = next_pos.charAt(next_pos.length() - 1);
                    CheckerColor enemy_color = (Character.toLowerCase(symbol) == 'b' ?
                            CheckerColor.BLACK : CheckerColor.WHITE);
                    if (color.equals(enemy_color)) {
                        throw new ErrorException("cutting friend pillar");
                    }
                    SimpleHittingMove(x_coord, y_coord, next_x, next_y,
                            new Checker(enemy_color,
                                    !Character.isLowerCase(symbol)), forbidden_xs, forbidden_ys);
                } else {
                    NotHittingMove(x_coord, y_coord, next_x, next_y);
                }
            }
        }

        if (is_hitting) {
            String pos = positions[positions.length - 1];
            int[] coords = getCoords(pos.substring(0, 2));
            int x_coord = coords[0];
            int y_coord = coords[1];
            if (isPossibleToCut(color, x_coord, y_coord, forbidden_xs, forbidden_ys)) {
                throw new InvalidMoveException("");
            }
        }
    }

    private void CheckMove(int from_x, int from_y, int to_x, int to_y) throws BusyCellException, WhiteCellException, ErrorException {
        if (isWhite(to_x, to_y)) {
            throw new WhiteCellException("");
        }
        if (isBusy(to_x, to_y)) {
            throw new BusyCellException("");
        }

        // Check that x and y distances of the move are equal
        int x_distance = to_x - from_x;
        int y_distance = to_y - from_y;
        if (Math.abs(x_distance) != Math.abs(y_distance)) {
            throw new ErrorException("Impossible move");
        }
    }

    void NotHittingMove(int from_x, int from_y, int to_x, int to_y) throws BusyCellException, WhiteCellException, ErrorException {
        Pillar pillar = pillars[from_y][from_x];
        int x_distance = to_x - from_x;
        int y_distance = to_y - from_y;
        CheckMove(from_x, from_y, to_x, to_y);
        if (!pillar.isKing() && Math.abs(x_distance) > 1) {
            throw new ErrorException("King move from non-king");
        }

        // Go through the path of the move and check if it is empty or not
        int x_step = (x_distance > 0) ? 1 : -1;
        int y_step = (y_distance > 0) ? 1 : -1;
        int x = from_x + x_step;
        int y = from_y + y_step;
        while (x != to_x) {
            if (isBusy(x, y)) {
                throw new ErrorException("Busy path");
            }
            x += x_step;
            y += y_step;
        }

        // Performing move
        pillars[from_y][from_x] = null;
        pillars[to_y][to_x] = pillar;
    }

    private boolean isForbidden(int x, int y, ArrayList<Integer> forbidden_xs, ArrayList<Integer> forbidden_ys) {
        for (int i = 0; i < forbidden_xs.size(); ++i) {
            if (x == forbidden_xs.get(i) && y == forbidden_ys.get(i)) {
                return true;
            }
        }
        return false;
    }

    void SimpleHittingMove(int from_x, int from_y, int to_x, int to_y, Checker expected_cut, ArrayList<Integer> forbidden_xs, ArrayList<Integer> forbidden_ys) throws BusyCellException, WhiteCellException, ErrorException {
        Pillar cuttingPillar = pillars[from_y][from_x];
        Pillar cutPillar = null;
        int x_distance = to_x - from_x;
        int y_distance = to_y - from_y;
        CheckMove(from_x, from_y, to_x, to_y);
        if (Math.abs(x_distance) < 2) {
            throw new ErrorException("Too short hitting move");
        }
        if (!cuttingPillar.isKing() && Math.abs(x_distance) > 2) {
            throw new ErrorException("King move from non-king");
        }

        // Go through the path of our move and check if there is one and only one enemy's pillar
        int x_step = (x_distance > 0) ? 1 : -1;
        int y_step = (y_distance > 0) ? 1 : -1;
        int x = from_x + x_step;
        int y = from_y + y_step;
        int enemy_pillar_cnt = 0;
        int cut_x = 0;
        int cut_y = 0;
        while (x != to_x) {
            // Check if we cutting one pillar twice
            if (isForbidden(x, y, forbidden_xs, forbidden_ys)) {
                throw new ErrorException("Cutting one pillar twice");
            }
            if (isBusy(x, y)) {
                ++enemy_pillar_cnt;
                if (enemy_pillar_cnt > 1) {
                    throw new ErrorException("More then one cut in one move");
                }
                cut_x = x;
                cut_y = y;
                cutPillar = pillars[cut_y][cut_x];
                forbidden_xs.add(x);
                forbidden_ys.add(y);
            }
            x += x_step;
            y += y_step;
        }
        if (cutPillar == null) {
            throw new ErrorException("No cuts in hitting move");
        }

        // Update information about the cut pillar on the checkers board
        Checker actual_cut = cutPillar.cutDown();
        if (cutPillar.isEmpty()) {
            pillars[cut_y][cut_x] = null;
            // Forget that this cell was forbidden
            forbidden_xs.remove(forbidden_xs.size() - 1);
            forbidden_ys.remove(forbidden_ys.size() - 1);
        }
        // Check that information about cut checker is valid
        if (!actual_cut.equals(expected_cut)) {
            throw new ErrorException("Wrong checker cut");
        }
        // Performing move
        cuttingPillar.pickUp(actual_cut);
        pillars[from_y][from_x] = null;
        pillars[to_y][to_x] = cuttingPillar;
    }

    private boolean isWhite(int x_coord, int y_coord) {
        return (x_coord + y_coord) % 2 == 0;
    }

    private boolean isBusy(int x_coord, int y_coord) {
        return pillars[y_coord][x_coord] != null;
    }

    static int[] getCoords(String coords) {
        return new int[]{Character.toLowerCase(coords.charAt(0)) - 'a', 7 - (coords.charAt(1) - '1')};
    }

    private Pillar getPillar(String pillar_str) {
        Pillar pillar = new Pillar();
        for (int i = 3; i < pillar_str.length(); ++i) {
            char symbol = pillar_str.charAt(i);
            Checker checker = new Checker((Character.toLowerCase(symbol) == 'b' ? CheckerColor.BLACK : CheckerColor.WHITE), !Character.isLowerCase(symbol));
            pillar.pickUp(checker);
        }
        return pillar;
    }

    private void addPillar(String pillar_str) {
        int[] coords = getCoords(pillar_str.substring(0, 2));
        int x_coord = coords[0];
        int y_coord = coords[1];
        pillars[y_coord][x_coord] = getPillar(pillar_str);
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }
}