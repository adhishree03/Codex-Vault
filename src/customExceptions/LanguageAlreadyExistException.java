package customExceptions;

public class LanguageAlreadyExistException extends Exception {
    public LanguageAlreadyExistException(String message) {
        super(message);
    }
}
