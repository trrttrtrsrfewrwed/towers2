package exceptions;

/**
 * throwed when mistakes in moves occure
 */
public class ErrorException extends Exception {
    public ErrorException(String message){
        super(message);
    }
}
