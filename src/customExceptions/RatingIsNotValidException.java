package customExceptions;

public class RatingIsNotValidException extends Exception {
    public RatingIsNotValidException(String message) {
        super(message);
    }
}
