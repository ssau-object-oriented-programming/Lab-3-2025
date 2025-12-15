import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Testing with TabulatedFunction interface ===");
        
        // Тестирование ArrayTabulatedFunction через интерфейс
        System.out.println("\n1. Testing ArrayTabulatedFunction through TabulatedFunction interface:");
        testFunction("ArrayTabulatedFunction", true);
        
        // Тестирование LinkedListTabulatedFunction через интерфейс
        System.out.println("\n\n2. Testing LinkedListTabulatedFunction through TabulatedFunction interface:");
        testFunction("LinkedListTabulatedFunction", false);
        
        // Тестирование исключений
        System.out.println("\n\n3. Testing exception cases:");
        testExceptions();
    }
    
    private static void testFunction(String className, boolean isArray) {
        TabulatedFunction func;
        
        try {
            // Создание функции через интерфейс
            if (isArray) {
                func = new ArrayTabulatedFunction(0, 10, 11);
            } else {
                func = new LinkedListTabulatedFunction(0, 10, 11);
            }
            
            System.out.println("Created " + className + " with sin(x) values");
            System.out.println("Function: " + func);
            System.out.println("Point count: " + func.getPointCount());
            
            // Тестирование базовых операций
            System.out.println("\nBasic operations:");
            System.out.println("Point at index 0: " + func.getPoint(0));
            System.out.println("Point at index 5: " + func.getPoint(5));
            System.out.println("Last point: " + func.getPoint(func.getPointCount() - 1));
            
            // Тестирование значений функции
            System.out.println("\nFunction values:");
            System.out.println("f(2.5) = " + func.getFunctionValue(2.5));
            System.out.println("f(5.0) = " + func.getFunctionValue(5.0));
            System.out.println("f(15.0) (outside range) = " + func.getFunctionValue(15.0));
            
            // Тестирование модификации
            System.out.println("\nModification tests:");
            func.setPointY(3, 10.0);
            System.out.println("After setPointY(3, 10.0): " + func.getPoint(3));
            
            // Тестирование добавления точки
            try {
                func.addPoint(new FunctionPoint(1.5, 0.5));
                System.out.println("Successfully added point (1.5, 0.5)");
                System.out.println("New point count: " + func.getPointCount());
            } catch (InappropriateFunctionPointException e) {
                System.out.println("Cannot add point (1.5, 0.5): " + e.getMessage());
            }
            
            // Тестирование удаления точки (если достаточно точек)
            if (func.getPointCount() > 3) {
                try {
                    FunctionPoint deleted = func.getPoint(2);
                    func.deletePoint(2);
                    System.out.println("Successfully deleted point: " + deleted);
                    System.out.println("Point count after deletion: " + func.getPointCount());
                } catch (Exception e) {
                    System.out.println("Cannot delete point: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error creating " + className + ": " + e.getMessage());
        }
    }
    
    private static void testExceptions() {
        System.out.println("Testing ArrayTabulatedFunction exceptions through interface:");
        testExceptionCases(true);
        
        System.out.println("\nTesting LinkedListTabulatedFunction exceptions through interface:");
        testExceptionCases(false);
    }
    
    private static void testExceptionCases(boolean isArray) {
        String className = isArray ? "ArrayTabulatedFunction" : "LinkedListTabulatedFunction";
        int testNum = 1;
        
        // Тест 1: IllegalArgumentException (левая граница >= правой)
        try {
            System.out.print(testNum++ + ". ");
            TabulatedFunction func;
            if (isArray) {
                func = new ArrayTabulatedFunction(10, 0, 5);
            } else {
                func = new LinkedListTabulatedFunction(10, 0, 5);
            }
            System.out.println("ERROR: Should have thrown IllegalArgumentException!");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException caught: " + e.getMessage());
        }
        
        // Тест 2: IllegalArgumentException (мало точек)
        try {
            System.out.print(testNum++ + ". ");
            TabulatedFunction func;
            if (isArray) {
                func = new ArrayTabulatedFunction(0, 10, 1);
            } else {
                func = new LinkedListTabulatedFunction(0, 10, 1);
            }
            System.out.println("ERROR: Should have thrown IllegalArgumentException!");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException caught: " + e.getMessage());
        }
        
        // Тест 3: FunctionPointIndexOutOfBoundsException
        try {
            System.out.print(testNum++ + ". ");
            TabulatedFunction func;
            if (isArray) {
                func = new ArrayTabulatedFunction(0, 10, 5);
            } else {
                func = new LinkedListTabulatedFunction(0, 10, 5);
            }
            func.getPoint(10);
            System.out.println("ERROR: Should have thrown FunctionPointIndexOutOfBoundsException!");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException caught: " + e.getMessage());
        }
        
        // Тест 4: InappropriateFunctionPointException (нарушение порядка)
        try {
            System.out.print(testNum++ + ". ");
            TabulatedFunction func;
            if (isArray) {
                func = new ArrayTabulatedFunction(0, 10, 5);
            } else {
                func = new LinkedListTabulatedFunction(0, 10, 5);
            }
            func.setPointX(2, -1.0);
            System.out.println("ERROR: Should have thrown InappropriateFunctionPointException!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("InappropriateFunctionPointException caught: " + e.getMessage());
        }
        
        // Тест 5: IllegalStateException (удаление при малом количестве точек)
        try {
            System.out.print(testNum++ + ". ");
            TabulatedFunction func;
            if (isArray) {
                func = new ArrayTabulatedFunction(0, 2, 3);
            } else {
                func = new LinkedListTabulatedFunction(0, 2, 3);
            }
            
            // Удаляем 2 точки (должно работать)
            func.deletePoint(0); // Удаляем первую → остается 2 точки
            func.deletePoint(0); // Удаляем вторую → остается 1 точка
            
            // Пытаемся удалить последнюю точку → должно быть IllegalStateException
            func.deletePoint(0); // Пытаемся удалить единственную точку!
            
            System.out.println("ERROR: Should have thrown IllegalStateException!");
        } catch (IllegalStateException e) {
            System.out.println("IllegalStateException caught: " + e.getMessage());
        }
        
        // Тест 6: InappropriateFunctionPointException (дубликат точки)
        try {
            System.out.print(testNum++ + ". ");
            TabulatedFunction func;
            if (isArray) {
                func = new ArrayTabulatedFunction(0, 10, 5);
            } else {
                func = new LinkedListTabulatedFunction(0, 10, 5);
            }
            func.addPoint(new FunctionPoint(5.0, 2.0));
            System.out.println("ERROR: Should have thrown InappropriateFunctionPointException!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("InappropriateFunctionPointException caught: " + e.getMessage());
        }
    }
}