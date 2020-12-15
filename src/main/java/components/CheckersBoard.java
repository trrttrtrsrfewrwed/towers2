package components;

import exceptions.BusyCellException;
import exceptions.ErrorException;
import exceptions.InvalidMoveException;
import exceptions.WhiteCellException;

/**
 * Representation of CheckersBoard in Pillar Russian checkers
 */
public interface CheckersBoard {

    /**
     * @param move - move in standard checkers notation
     * @throws BusyCellException
     * @throws WhiteCellException
     * @throws ErrorException
     * @throws InvalidMoveException
     */
    void move(String move) throws BusyCellException, WhiteCellException, ErrorException, InvalidMoveException;
}
