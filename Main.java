import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Testing ArrayTabulatedFunction:");
        testFunction(new ArrayTabulatedFunction(0, 10, 11));
        
        System.out.println("\nTesting LinkedListTabulatedFunction:");
        testFunction(new LinkedListTabulatedFunction(0, 10, 11));
        
        System.out.println("\nTesting exception cases:");
        testExceptions();
    }
    
    private static void testFunction(TabulatedFunction func) {
        System.out.println("Function created: " + func);
        System.out.println("Point count: " + func.getPointCount());
        
        try {
            System.out.println("Point at index 0: " + func.getPoint(0));
            System.out.println("Point at index 5: " + func.getPoint(5));
            System.out.println("Point at last index: " + func.getPoint(func.getPointCount() - 1));
            
            System.out.println("Value at x=2.5: " + func.getFunctionValue(2.5));
            System.out.println("Value at x=15 (outside): " + func.getFunctionValue(15));
            
            // Test setting values
            func.setPointY(3, 10.0);
            System.out.println("After setting Y at index 3: " + func.getPoint(3));
            
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
    
    private static void testExceptions() {
        System.out.println("Testing ArrayTabulatedFunction exceptions:");
        
        try {
            // Invalid constructor parameters
            TabulatedFunction func1 = new ArrayTabulatedFunction(10, 0, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("1. IllegalArgumentException caught: " + e.getMessage());
        }
        
        try {
            TabulatedFunction func2 = new ArrayTabulatedFunction(0, 10, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("2. IllegalArgumentException caught: " + e.getMessage());
        }
        
        try {
            TabulatedFunction func3 = new ArrayTabulatedFunction(0, 10, 3);
            // Out of bounds access
            func3.getPoint(10);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("3. FunctionPointIndexOutOfBoundsException caught: " + e.getMessage());
        }
        
        try {
            TabulatedFunction func4 = new ArrayTabulatedFunction(0, 10, 3);
            // Inappropriate X value
            func4.setPointX(1, 5.0); // Valid
            func4.setPointX(1, -1.0); // Invalid - less than previous
        } catch (InappropriateFunctionPointException e) {
            System.out.println("4. InappropriateFunctionPointException caught: " + e.getMessage());
        }
        
        try {
            TabulatedFunction func5 = new ArrayTabulatedFunction(0, 10, 3);
            // Try to delete when only 3 points exist
            func5.deletePoint(0);
            func5.deletePoint(0);
            func5.deletePoint(0); // Should throw IllegalStateException
        } catch (IllegalStateException e) {
            System.out.println("5. IllegalStateException caught: " + e.getMessage());
        }
        
        System.out.println("\nTesting LinkedListTabulatedFunction exceptions:");
        
        try {
            TabulatedFunction func6 = new LinkedListTabulatedFunction(0, 10, 3);
            // Add point with existing X
            func6.addPoint(new FunctionPoint(5.0, 100.0));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("6. InappropriateFunctionPointException caught: " + e.getMessage());
        }
    }
}