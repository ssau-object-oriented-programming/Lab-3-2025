package functions;

// Исключение при попытке нарушить порядок точек (например, вставить дубликат x)
public class InappropriateFunctionPointException extends Exception 
{
    public InappropriateFunctionPointException(String message) 
    {
        super(message);
    }

    public InappropriateFunctionPointException() 
    {
        super();
    }
}