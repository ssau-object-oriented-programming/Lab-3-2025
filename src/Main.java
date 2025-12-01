import functions.FunctionPointIndexOutOfBoundsException;
import functions.InappropriateFunctionPointException;
import functions.FunctionPoint;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;

public class Main {
    public static void main(String[] args) {

        System.out.println("--- Тестирование через интерфейс TabulatedFunction ---");

        // Тестирование ArrayTabulatedFunction
        System.out.println("\n--- ArrayTabulatedFunction ---");
        testTabulatedFunction(new ArrayTabulatedFunction(0, 6, 10));

        // Тестирование LinkedListTabulatedFunction
        System.out.println("\n--- LinkedListTabulatedFunction ---");
        testTabulatedFunction(new LinkedListTabulatedFunction(0, 6, 10));

        // Дополнительное тестирование исключений
        System.out.println("\n--- Тестирование исключений ---");
        testExceptions();
    }

    public static void testTabulatedFunction(TabulatedFunction f) {

        System.out.println("Функция f(x) = 2x + 1");
        System.out.println("Область определения: [" + f.getLeftDomainBorder() + "; "
                + f.getRightDomainBorder() + "]");
        System.out.println("Количество точек: " + f.getPointsCount());
        System.out.println("\nТочки функции: ");

        for (int i = 0; i < f.getPointsCount(); i++) {
            f.setPointY(i, 2 * f.getPointX(i) + 1);
            System.out.printf("%d: (%.2f; %.2f)%n", i+1, f.getPointX(i), f.getPointY(i));
        }

        // Проверка метода getFunctionValue
        System.out.println("\nЗначения функции f(x) в разных x: ");
        double[] x_val = {-1.3, 18, 1, 6, 43, 3.1};
        for (int i = 0; i < x_val.length; i++) {
            if (Double.isNaN(f.getFunctionValue(x_val[i])))
                System.out.println("Значение x = " + x_val[i] + " лежит вне области определения функции" );
            else
                System.out.printf("f(%.2f) = %.2f%n", x_val[i], f.getFunctionValue(x_val[i]));
        }

        // Проверка остальных методов
        testFunctionOperations(f);
    }

    public static void testFunctionOperations(TabulatedFunction f) {
        System.out.println("\n--- Тестирование операций с точками ---");

        try {
            // Проверка setPoint #1
            System.out.println("\nЗамена точки №3 на точку (2.5; 6)");
            FunctionPoint test1 = new FunctionPoint(2.5, 6);
            f.setPoint(2, test1);
            System.out.printf("Точка %d: (%.2f; %.2f)%n", 3, f.getPointX(2), f.getPointY(2));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка setPoint #2
            System.out.println("\nЗамена точки №8 на точку (5; 11)");
            FunctionPoint test2 = new FunctionPoint(5, 11);
            f.setPoint(7, test2);
            System.out.printf("Точка %d: (%.2f; %.2f)%n", 8, f.getPointX(7), f.getPointY(7));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка setPoint #3
            System.out.println("\nЗамена точки №25 на точку (12; 7)");
            FunctionPoint test3 = new FunctionPoint(12, 7);
            f.setPoint(24, test3);
            System.out.printf("Точка %d: (%.2f; %.2f)%n", 25, f.getPointX(24), f.getPointY(24));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка getPointX #1
            System.out.println("\nАбсцисса точки №16");
            System.out.println(f.getPointX(15));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка getPointX #2
            System.out.println("\nАбсцисса точки №1");
            System.out.println(f.getPointX(0));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка getPointY #1
            System.out.println("\nОрдината точки №16");
            System.out.println(f.getPointY(15));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка getPointY #3
            System.out.println("\nОрдината точки №1");
            System.out.println(f.getPointY(0));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка setPointX #1
            System.out.println("\nЗамена X точки №4 на 1.1");
            f.setPointX(3, 1.1);
            System.out.printf("Точка %d: (%.2f; %.2f)%n", 4, f.getPointX(3), f.getPointY(3));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка setPointX #2
            System.out.println("\nЗамена X точки №9 на 5.5");
            f.setPointX(8, 5.5);
            System.out.printf("Точка %d: (%.2f; %.2f)%n", 9, f.getPointX(8), f.getPointY(8));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка setPointY #1
            System.out.println("\nЗамена Y точки №9 на 12");
            f.setPointY(8,12);
            System.out.printf("Точка %d: (%.2f; %.2f)%n", 9, f.getPointX(8), f.getPointY(8));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка setPointY #2
            System.out.println("\nЗамена Y точки №21 на 8.5");
            f.setPointY(20, 8.5);
            System.out.printf("Точка %d: (%.2f; %.2f)%n", 21, f.getPointX(20), f.getPointY(20));
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка deletePoint
            System.out.printf("\nУдаление точки №%d: (%.2f; %.2f)%n", 5, f.getPointX(4), f.getPointY(4));
            f.deletePoint(4);
            System.out.println("Удаление прошло успешно");

        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка addPoint #1
            System.out.println("\nДобавление точки (4, 9)");
            FunctionPoint newPoint = new FunctionPoint(4, 9);
            f.addPoint(newPoint);
            System.out.println("Добавление прошло успешно");

        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка addPoint #2
            System.out.println("\nДобавление точки (3, 7)");
            FunctionPoint newPoint = new FunctionPoint(3, 7);
            f.addPoint(newPoint);
            System.out.println("Добавление прошло успешно");
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Проверка addPoint #3
            System.out.println("\nДобавление точки (10, 21)");
            FunctionPoint newPoint = new FunctionPoint(10, 21);
            f.addPoint(newPoint);
            System.out.println("Добавление прошло успешно");
        } catch (Exception e) {
            System.out.println(e);
        }

        // Вывод итоговых точек
        System.out.println("\nИтоговые точки функции: ");
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.printf("%d: (%.2f; %.2f)%n", i+1, f.getPointX(i), f.getPointY(i));
        }
    }

    public static void testExceptions() {
        System.out.println("\n--- Тестирование исключений ---");

        //Некорректный конструктор
        try {
            TabulatedFunction f = new ArrayTabulatedFunction(10, 0, 5);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        //Выход индекса за границы
        try {
            TabulatedFunction f = new LinkedListTabulatedFunction(0, 10, 3);
            f.getPoint(10);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(e);
        }

        //Удаление когда мало точек
        try {
            TabulatedFunction f = new LinkedListTabulatedFunction(0, 10, 2);
            f.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println(e);
        }

        //Добавление уже существующей
        try {
            double[] y_values = {17, 5, -7, -19};
            TabulatedFunction f = new LinkedListTabulatedFunction(-2, 4, y_values);
            FunctionPoint newPoint = new FunctionPoint(0, 9);
            f.addPoint(newPoint);
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e);
        }
    }
}

