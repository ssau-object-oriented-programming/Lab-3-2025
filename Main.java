import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Lab 3: Testing ArrayTabulatedFunction ===");
        testArrayFunction();
        
        System.out.println("\n=== Lab 3: Testing LinkedListTabulatedFunction ===");
        testLinkedListFunction();
        
        System.out.println("\n=== Testing exception cases ===");
        testExceptions();
    }
    
    private static void testArrayFunction() {
        try {
            // Создаём функцию с не-нулевыми значениями
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 11);
            System.out.println("Created ArrayTabulatedFunction with sin(x) values");
            testFunction(func);
        } catch (Exception e) {
            System.out.println("Error creating array function: " + e.getMessage());
        }
    }
    
    private static void testLinkedListFunction() {
        try {
            // Создаём функцию с не-нулевыми значениями
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 10, 11);
            System.out.println("Created LinkedListTabulatedFunction with sin(x) values");
            testFunction(func);
        } catch (Exception e) {
            System.out.println("Error creating linked list function: " + e.getMessage());
        }
    }
    
    private static void testFunction(TabulatedFunction func) {
        // Метод из предыдущего раздела
        // ... (код метода выше)
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
        
        // Test adding point with existing X
        try {
            TabulatedFunction func6 = new ArrayTabulatedFunction(0, 10, 3);
            func6.addPoint(new FunctionPoint(5.0, 100.0));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("6. InappropriateFunctionPointException caught: " + e.getMessage());
        }
    }
}