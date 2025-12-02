import functions.*;

public class Main {
    public static void main(String[] args) {
        double[] data = {0,1,4,9,16};
        // Тестируем ArrayTabulatedFunction
        System.out.println("=== Тестирование ArrayTabulatedFunction ===");
        testFunction(new ArrayTabulatedFunction(0, 4, data));
        
        // Тестируем LinkedListTabulatedFunction
        System.out.println("\n\n=== Тестирование LinkedListTabulatedFunction ===");
        testFunction(new LinkedListTabulatedFunction(0, 4, data));
    }
    
    private static void testFunction(TabulatedFunction function) {
        System.out.println("\nИсходная функция:");
        printFunctionInfo(function);
        
        // Проверяем значения функции в разных точках
        System.out.println("\nЗначения функции в разных точках:");
        double[] testPoints = {-1, 0, 0.5, 1, 1.5, 2, 3, 4, 5};
        for (double x : testPoints) {
            double y = function.getFunctionValue(x);
            System.out.printf("f(%.1f) = %s\n", x, Double.isNaN(y) ? "NaN" : String.format("%.2f", y));
        }
        
        // Изменяем точки
        System.out.println("\nПосле изменения точек:");
        function.setPointY(2, 5);
        try {
            function.setPoint(3, new FunctionPoint(3.5, 12.3));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при setPoint: " + e.getMessage());
        }
        printFunctionInfo(function);
        
        // Добавляем точку
        System.out.println("\nПосле добавления точки (2.5, 6.3):");
        try {
            function.addPoint(new FunctionPoint(2.5, 6.3));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при addPoint: " + e.getMessage());
        }
        printFunctionInfo(function);
        
        // Удаляем точку
        System.out.println("\nПосле удаления точки с индексом 1:");
        try {
            function.deletePoint(1);
        } catch (Exception e) {
            System.out.println("Ошибка при deletePoint: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        printFunctionInfo(function);
    }
    
    private static void printFunctionInfo(TabulatedFunction function) {
        System.out.printf("Область определения: [%.1f, %.1f]\n", 
            function.getLeftDomainBorder(), function.getRightDomainBorder());
        System.out.println("Точки функции:");
        for (int i = 0; i < function.getPointsCount(); i++) {
            FunctionPoint point = function.getPoint(i);
            System.out.printf("  (%4.1f; %6.2f)\n", point.getX(), point.getY());
        }
    }
}