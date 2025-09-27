import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Тестирование ArrayTabulatedFunction:");
        testArrayTabulatedFunction();
        
        System.out.println("\nТестирование LinkedListTabulatedFunction:");
        testLinkedListTabulatedFunction();
    }
    
    private static void testArrayTabulatedFunction() {
        try {
            TabulatedFunctionImpl function = new ArrayTabulatedFunction(0, 10, 5);
            System.out.println("ArrayTabulatedFunction создан успешно");
            System.out.println("Количество точек: " + function.getPointsCount());
            System.out.println("Левая граница: " + function.getLeftDomainBorder());
            System.out.println("Правая граница: " + function.getRightDomainBorder());
            
            function.setPointY(0, 1.0);
            function.setPointY(1, 2.0);
            function.setPointY(2, 3.0);
            function.setPointY(3, 4.0);
            function.setPointY(4, 5.0);
            
            System.out.println("Значение функции при x=2.5: " + function.getFunctionValue(2.5));
            
            testExceptions(function);
            
        } catch (Exception e) {
            System.out.println("Ошибка создания ArrayTabulatedFunction: " + e.getMessage());
        }
    }
    
    private static void testLinkedListTabulatedFunction() {
        try {
            TabulatedFunctionImpl function = new LinkedListTabulatedFunction(0, 10, 5);
            System.out.println("LinkedListTabulatedFunction создан успешно");
            System.out.println("Количество точек: " + function.getPointsCount());
            System.out.println("Левая граница: " + function.getLeftDomainBorder());
            System.out.println("Правая граница: " + function.getRightDomainBorder());
            
            function.setPointY(0, 1.0);
            function.setPointY(1, 2.0);
            function.setPointY(2, 3.0);
            function.setPointY(3, 4.0);
            function.setPointY(4, 5.0);
            
            System.out.println("Значение функции при x=2.5: " + function.getFunctionValue(2.5));
            
            testExceptions(function);
            
        } catch (Exception e) {
            System.out.println("Ошибка создания LinkedListTabulatedFunction: " + e.getMessage());
        }
    }
    
    private static void testExceptions(TabulatedFunctionImpl function) {
        System.out.println("\nТестирование исключений:");
        
        try {
            new ArrayTabulatedFunction(10, 5, 3);
        } catch (IllegalArgumentException e) {
            System.out.println("Поймано IllegalArgumentException для некорректных границ: " + e.getMessage());
        }
        
        try {
            new ArrayTabulatedFunction(0, 10, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Поймано IllegalArgumentException для недостаточного количества точек: " + e.getMessage());
        }
        
        try {
            function.getPoint(-1);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Поймано FunctionPointIndexOutOfBoundsException для отрицательного индекса: " + e.getMessage());
        }
        
        try {
            function.getPoint(100);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Поймано FunctionPointIndexOutOfBoundsException для индекса вне границ: " + e.getMessage());
        }
        
        try {
            function.setPointX(1, function.getPointX(0) - 1);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Поймано InappropriateFunctionPointException для некорректного X: " + e.getMessage());
        }
        
        try {
            function.addPoint(new FunctionPoint(function.getPointX(0), 10));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Поймано InappropriateFunctionPointException для дублирующегося X: " + e.getMessage());
        }
        
        try {
            TabulatedFunctionImpl testFunction = new ArrayTabulatedFunction(0, 10, 5);
            testFunction.deletePoint(0);
            testFunction.deletePoint(0);
            testFunction.deletePoint(0);
            testFunction.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("Поймано IllegalStateException для недостаточного количества точек: " + e.getMessage());
        }
    }
}
