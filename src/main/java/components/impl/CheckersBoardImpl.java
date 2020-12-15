package components.impl;

import components.Checker;
import components.CheckerColor;
import components.CheckersBoard;
import components.Pillar;
import exceptions.BusyCellException;
import exceptions.ErrorException;
import exceptions.InvalidMoveException;
import exceptions.WhiteCellException;

import java.util.ArrayList;

public class CheckersBoardImpl implements CheckersBoard {
    private Pillar[][] pillars = new Pillar[8][8];

    /**
     * Constructor of CheckersBoard, fills it with pillars
     *
     * @param whitePositions - coordinates of white checkers (and towers with a white lid)
     * @param blackPositions - coordinates of black checkers (and towers with a black lid)
     */
    public CheckersBoardImpl(String whitePositions, String blackPositions) {
        for (String whPos : whitePositions.split(" ")) {
            if (!whPos.isEmpty()) {
                addPillar(whPos);
            }
        }
        for (String blPos : blackPositions.split(" ")) {
            if (!blPos.isEmpty()) {
                addPillar(blPos);
            }
        }
    }

    @Override
    public String toString() {
        return getOneColor(CheckerColor.WHITE) + "\n" + getOneColor(CheckerColor.BLACK);
    }

    @Override
    public void move(String move) throws BusyCellException, WhiteCellException, ErrorException, InvalidMoveException {
        String[] moves = move.split(" ");
        String whiteMove = moves[0];
        String blackMove = moves[1];
        enemyMove(CheckerColor.WHITE, whiteMove);
        enemyMove(CheckerColor.BLACK, blackMove);
    }

    Pillar[][] getPillars() {
        return pillars;
    }

    private String getOneColor(CheckerColor color) {
        StringBuilder str = new StringBuilder();
        for (int j = 0; j < 8; ++j) {
            for (int i = 7; i >= 0; --i) {
                if (pillars[i][j] != null && pillars[i][j].getColor().equals(color)) {
                    str.append((char) (j + 'a')).append(8 - i).append("_").append(pillars[i][j]).append(" ");
                }
            }
        }
        return str.toString();
    }

    private boolean isPossibleToCut(CheckerColor color, int x, int y, ArrayList<Integer> forbiddenXs, ArrayList<Integer> forbiddenYs) {
        boolean isKing = pillars[y][x].isKing();
        if (isKing) {
            int tmpX;
            int tmpY;
            for (int xStep = -1; xStep <= 1; xStep += 2) {
                for (int yStep = -1; yStep <= 1; yStep += 2) {
                    tmpX = x;
                    tmpY = y;
                    while (inBounds(tmpX + xStep, tmpY + yStep)) {
                        tmpX += xStep;
                        tmpY += yStep;
                        if (isForbidden(tmpX, tmpY, forbiddenXs, forbiddenYs)) {
                            break;
                        }
                        if (isBusy(tmpX, tmpY)) {
                            if (!pillars[tmpY][tmpX].getColor().equals(color)) {
                                if (inBounds(tmpX + xStep, tmpY + yStep) && !isBusy(tmpX + xStep, tmpY + yStep)) {
                                    return true;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            for (int xStep = -1; xStep <= 1; xStep += 2) {
                for (int yStep = -1; yStep <= 1; yStep += 2) {
                    if (!inBounds(x + xStep, y + yStep)) {
                        continue;
                    }
                    if (isForbidden(x + xStep, y + yStep, forbiddenXs, forbiddenYs)) {
                        continue;
                    }
                    if (!isBusy(x + xStep, y + yStep)) {
                        continue;
                    }
                    if (pillars[y + yStep][x + xStep].getColor().equals(color)) {
                        continue;
                    }
                    if (inBounds(x + 2 * xStep, y + 2 * yStep) && !isBusy(x + 2 * xStep, y + 2 * yStep)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isPossibleToCut(CheckerColor color) {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (pillars[j][i] != null && pillars[j][i].getColor().equals(color)) {
                    if (isPossibleToCut(color, i, j, new ArrayList<>(), new ArrayList<>())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void changeToKing(int y, Pillar pillar) {
        if ((pillar.getColor().equals(CheckerColor.WHITE) && y == 0) || (pillar.getColor().equals(CheckerColor.BLACK) && y == 7)) {
            pillar.changeToKing();
        }
    }

    /*private static boolean IsChangesToKing(CheckerColor color, int y, Pillar nextPillar) {
        if (color.equals(CheckerColor.WHITE) && y == 0) {
            return nextPillar.isKing();
        }
        if (color.equals(CheckerColor.BLACK) && y == 7) {
            return nextPillar.isKing();
        }
        return true;
    }*/

    void enemyMove(CheckerColor color, String move) throws BusyCellException, WhiteCellException, ErrorException, InvalidMoveException {
        boolean isHitting = false;
        String[] positions;
        if (move.contains("-")) {
            positions = move.split("-");
        } else {
            isHitting = true;
            positions = move.split(":");
        }
        ArrayList<Integer> forbiddenXs = new ArrayList<Integer>();
        ArrayList<Integer> forbiddenYs = new ArrayList<Integer>();

        for (int i = 0; i < positions.length - 1; ++i) {
            String pos = positions[i];
            int[] coords = getCoords(pos.substring(0, 2));
            int xCoord = coords[0];
            int yCoord = coords[1];
            Pillar expectedPillar = getPillar(pos);
            Pillar actualPillar = pillars[yCoord][xCoord];
            if (!expectedPillar.getColor().equals(color)) {
                throw new ErrorException("Move with a checker of the wrong color");
            }
            if (!expectedPillar.toString().toLowerCase().equals(actualPillar.toString().toLowerCase())) {
                throw new ErrorException("Mistake in notation of the pillar");
            } else {
                String nextPos = positions[i + 1];
                int[] nextCoords = getCoords(nextPos.substring(0, 2));
                int nextX = nextCoords[0];
                int nextY = nextCoords[1];
                /*if (!IsChangesToKing(color, nextY, getPillar(nextPos))) {
                    throw new ErrorException("Doesn't change to king");
                }*/
                if (isHitting) {
                    char symbol = nextPos.charAt(nextPos.length() - 1);
                    CheckerColor enemyColor = (Character.toLowerCase(symbol) == 'b' ?
                            CheckerColor.BLACK : CheckerColor.WHITE);
                    if (color.equals(enemyColor)) {
                        throw new ErrorException("cutting friend pillar");
                    }
                    simpleHittingMove(xCoord, yCoord, nextX, nextY,
                            new Checker(enemyColor,
                                    !Character.isLowerCase(symbol)), forbiddenXs, forbiddenYs);
                } else {
                    if (isPossibleToCut(color)) {
                        throw new InvalidMoveException("");
                    }
                    notHittingMove(xCoord, yCoord, nextX, nextY);
                }
                changeToKing(nextY, pillars[nextY][nextX]);
            }
        }

        if (isHitting) {
            String pos = positions[positions.length - 1];
            int[] coords = getCoords(pos.substring(0, 2));
            int xCoord = coords[0];
            int yCoord = coords[1];
            if (isPossibleToCut(color, xCoord, yCoord, forbiddenXs, forbiddenYs)) {
                throw new InvalidMoveException("");
            }
        }
    }

    private void checkMove(int fromX, int fromY, int toX, int toY) throws BusyCellException, WhiteCellException, ErrorException {
        if (isWhite(toX, toY)) {
            throw new WhiteCellException("");
        }
        if (isBusy(toX, toY)) {
            throw new BusyCellException("");
        }

        // Check that x and y distances of the move are equal
        int x_distance = toX - fromX;
        int y_distance = toY - fromY;
        if (Math.abs(x_distance) != Math.abs(y_distance)) {
            throw new ErrorException("Impossible move");
        }
    }

    void notHittingMove(int fromX, int fromY, int toX, int toY) throws BusyCellException, WhiteCellException, ErrorException {
        Pillar pillar = pillars[fromY][fromX];
        int xDistance = toX - fromX;
        int yDistance = toY - fromY;
        checkMove(fromX, fromY, toX, toY);
        if (!pillar.isKing() && Math.abs(xDistance) > 1) {
            throw new ErrorException("King move from non-king");
        }

        // Go through the path of the move and check if it is empty or not
        int xStep = (xDistance > 0) ? 1 : -1;
        int yStep = (yDistance > 0) ? 1 : -1;
        int x = fromX + xStep;
        int y = fromY + yStep;
        while (x != toX) {
            if (isBusy(x, y)) {
                throw new ErrorException("Busy path");
            }
            x += xStep;
            y += yStep;
        }

        // Performing move
        pillars[fromY][fromX] = null;
        pillars[toY][toX] = pillar;
    }

    private boolean isForbidden(int x, int y, ArrayList<Integer> forbiddenXs, ArrayList<Integer> forbiddenYs) {
        for (int i = 0; i < forbiddenXs.size(); ++i) {
            if (x == forbiddenXs.get(i) && y == forbiddenYs.get(i)) {
                return true;
            }
        }
        return false;
    }

    void simpleHittingMove(int fromX, int fromY, int toX, int toY, Checker expectedCut, ArrayList<Integer> forbiddenXs, ArrayList<Integer> forbiddenYs) throws BusyCellException, WhiteCellException, ErrorException {
        Pillar cuttingPillar = pillars[fromY][fromX];
        Pillar cutPillar = null;
        int xDistance = toX - fromX;
        int yDistance = toY - fromY;
        checkMove(fromX, fromY, toX, toY);
        if (Math.abs(xDistance) < 2) {
            throw new ErrorException("Too short hitting move");
        }
        if (!cuttingPillar.isKing() && Math.abs(xDistance) > 2) {
            throw new ErrorException("King move from non-king");
        }

        // Go through the path of our move and check if there is one and only one enemy's pillar
        int xStep = (xDistance > 0) ? 1 : -1;
        int yStep = (yDistance > 0) ? 1 : -1;
        int x = fromX + xStep;
        int y = fromY + yStep;
        int enemyPillarCnt = 0;
        int cutX = 0;
        int cutY = 0;
        while (x != toX) {
            // Check if we cutting one pillar twice
            if (isForbidden(x, y, forbiddenXs, forbiddenYs)) {
                throw new ErrorException("Cutting one pillar twice");
            }
            if (isBusy(x, y)) {
                ++enemyPillarCnt;
                if (enemyPillarCnt > 1) {
                    throw new ErrorException("More then one cut in one move");
                }
                cutX = x;
                cutY = y;
                cutPillar = pillars[cutY][cutX];
                forbiddenXs.add(x);
                forbiddenYs.add(y);
            }
            x += xStep;
            y += yStep;
        }
        if (cutPillar == null) {
            throw new ErrorException("No cuts in hitting move");
        }

        // Update information about the cut pillar on the checkers board
        Checker actualCut = cutPillar.cutDown();
        if (cutPillar.isEmpty()) {
            pillars[cutY][cutX] = null;
            // Forget that this cell was forbidden
            forbiddenXs.remove(forbiddenXs.size() - 1);
            forbiddenYs.remove(forbiddenYs.size() - 1);
        }
        // Check that information about cut checker is valid
        if (!actualCut.equals(expectedCut)) {
            throw new ErrorException("Wrong checker cut");
        }
        // Performing move
        cuttingPillar.pickUp(actualCut);
        pillars[fromY][fromX] = null;
        pillars[toY][toX] = cuttingPillar;
    }

    private boolean isWhite(int xCoord, int yCoord) {
        return (xCoord + yCoord) % 2 == 0;
    }

    private boolean isBusy(int xCoord, int yCoord) {
        return pillars[yCoord][xCoord] != null;
    }

    static int[] getCoords(String coords) {
        return new int[]{Character.toLowerCase(coords.charAt(0)) - 'a', 7 - (coords.charAt(1) - '1')};
    }

    private Pillar getPillar(String pillarStr) {
        Pillar pillar = new Pillar();
        for (int i = 3; i < pillarStr.length(); ++i) {
            char symbol = pillarStr.charAt(i);
            Checker checker = new Checker((Character.toLowerCase(symbol) == 'b' ? CheckerColor.BLACK : CheckerColor.WHITE), !Character.isLowerCase(symbol));
            pillar.pickUp(checker);
        }
        return pillar;
    }

    private void addPillar(String pillarStr) {
        int[] coords = getCoords(pillarStr.substring(0, 2));
        int xCoord = coords[0];
        int yCoord = coords[1];
        pillars[yCoord][xCoord] = getPillar(pillarStr);
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }
}