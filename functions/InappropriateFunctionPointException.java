package functions;

public class InappropriateFunctionPointException extends RuntimeException {
    
    public InappropriateFunctionPointException() {
        super("Некорректная точка функции");
    }
    
    public InappropriateFunctionPointException(String message) {
        super(message);
    }
    
    public InappropriateFunctionPointException(String message, Throwable cause) {
        super(message, cause);
    }
}
