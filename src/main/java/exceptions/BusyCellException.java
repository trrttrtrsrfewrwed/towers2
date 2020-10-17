package exceptions;

/**
 * throwed when busy cell mistake occure
 */
public class BusyCellException extends Exception {
    public BusyCellException(String message){
        super(message);
    }
}
