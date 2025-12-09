package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    
    public FunctionPointIndexOutOfBoundsException() {
        super("Индекс точки функции выходит за границы");
    }
    
    public FunctionPointIndexOutOfBoundsException(String message) {
        super(message);
    }
    
    public FunctionPointIndexOutOfBoundsException(int index) {
        super("Индекс точки функции выходит за границы: " + index);
    }
}
