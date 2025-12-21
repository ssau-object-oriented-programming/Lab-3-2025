package functions;

// Исключение при обращении к несуществующему индексу точки
public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException 
{
    public FunctionPointIndexOutOfBoundsException(String message) 
    {
        super(message);
    }

    public FunctionPointIndexOutOfBoundsException() 
    {
        super();
    }
}