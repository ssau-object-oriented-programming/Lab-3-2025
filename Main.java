import functions.FunctionPoint;
import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.FunctionPointIndexOutOfBoundsException;
import functions.InappropriateFunctionPointException;

public class Main {

    // Вспомогательный метод для тестирования исключений
    public static void testFunction(TabulatedFunction func, String name) {
        System.out.println("\n--- Тестирование исключений для " + name + " ---");

        // 1. IllegalArgumentException (Проверка конструкторов)
        try {
            new ArrayTabulatedFunction(10, 0, 5); // leftX >= rightX
        } catch (IllegalArgumentException e) {
            System.out.println("Конструктор (OK): " + e.getMessage());
        }

        // 2. IllegalStateException (Проверка deletePoint при size <= 2)
        try {
            double[] values = {1, 2};
            TabulatedFunction shortFunc = new ArrayTabulatedFunction(0, 1, values);
            shortFunc.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("deletePoint (OK): " + e.getMessage());
        }

        // 3. FunctionPointIndexOutOfBoundsException
        try {
            func.getPoint(100);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("getPoint (OK): " + e.getMessage());
        }

        // 4. InappropriateFunctionPointException (Проверка addPoint - совпадение X)
        try {
            func.addPoint(new FunctionPoint(func.getPointX(1), 50));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("addPoint (OK): " + e.getMessage());
        } catch (Exception e) {
            // Исключения, не связанные с логикой addPoint
        }

        // 5. InappropriateFunctionPointException (Проверка setPointX - нарушение упорядоченности)
        try {
            // Пытаемся сделать X[1] меньше X[0]
            func.setPointX(1, func.getPointX(0) - 1);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("setPointX (OK): " + e.getMessage());
        } catch (Exception e) {
            // Исключения, не связанные с логикой setPointX
        }

        System.out.println("Тестирование завершено.");
    }

    public static void main(String[] args) {
        double[] values = {10, 20, 30, 40, 50};

        // Используем интерфейсный тип для объявления переменной
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 4, values);
        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(0, 4, values);

        System.out.println("--- Проверка работы ArrayTabulatedFunction ---");
        System.out.println("Значение в X=1.5: " + arrayFunc.getFunctionValue(1.5));

        System.out.println("\n--- Проверка работы LinkedListTabulatedFunction ---");
        System.out.println("Количество точек: " + linkedFunc.getPointsCount());
        System.out.println("Правая граница: " + linkedFunc.getRightDomainBorder());
        System.out.println("Значение в X=2.5: " + linkedFunc.getFunctionValue(2.5));

        // Тестируем исключения
        testFunction(arrayFunc, "ArrayTabulatedFunction");
        testFunction(linkedFunc, "LinkedListTabulatedFunction");
    }
}