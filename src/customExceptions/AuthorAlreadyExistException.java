package customExceptions;

public class AuthorAlreadyExistException extends Exception {
    public AuthorAlreadyExistException(String message) {
        super(message);
    }
}
