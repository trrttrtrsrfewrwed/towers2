package exceptions;

/**
 * throwed when invalid move mistake occure
 */
public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message){
        super(message);
    }
}
