import functions.*;

public class Main {
    public static void main(String[] args) {
        // Для тестирования каждой из реализаций можно закомментировать одну из следующих 2 строк
        // TabulatedFunction cubic = new ArrayTabulatedFunction(-2.0, 2.0, 5);
        TabulatedFunction cubic = new LinkedListTabulatedFunction(-2.0, 2.0, 5);

        testBasicFunctionality(cubic, "LinkedListTabulatedFunction");
        testExceptions();
    }

    private static void testBasicFunctionality(TabulatedFunction function, String className) {
        try {
            System.out.println("Тестирование: " + className);
            // Заполняем значениями y = x^3
            for (int i = 0; i < function.getPointsCount(); i++) {
                double x = function.getPointX(i);
                function.setPointY(i, x * x * x);
            }
            // Выводим исходные точки
            System.out.println("Исходные точки:");
            function.printTabulatedFunction();
            // Тестируем интерполяцию
            double[] testPoints = {-3.0, -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 3.0};
            System.out.println("Тест интерполяции:");
            for (double x : testPoints) {
                double value = function.getFunctionValue(x);
                if (Double.isNaN(value)) {
                    System.out.println("не определено " + x);
                } else {
                    System.out.println(x + "=" + value + " ожидалось = " + x*x*x);
                }
            }
            // Меняем одну из точек
            FunctionPoint newPoint = new FunctionPoint(-1.5, -3.375);
            function.setPoint(1, newPoint);
            System.out.println("После замены точки:");
            function.printTabulatedFunction();
            // Добавляем новую точку
            FunctionPoint addedPoint = new FunctionPoint(1.7, 4.913);
            function.addPoint(addedPoint);
            System.out.println("После добавления точки:");
            function.printTabulatedFunction();
            // Удаляем точку
            function.deletePoint(3);
            System.out.println("После удаления точки:");
            function.printTabulatedFunction();

        } catch (Exception e) {
            System.out.println("Ошибка при тестировании " + className + ": " + e.getMessage());
        }
    }

    private static void testExceptions() {
        // Тестируем все требуемые исключения
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0.0, 10.0, 3);

            // FunctionPointIndexOutOfBoundsException
            try {
                func.getPoint(10);
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
            }

            // InappropriateFunctionPointException - нарушение порядка
            try {
                func.setPointX(1, 10.0);
            } catch (InappropriateFunctionPointException e) {
                System.out.println("InappropriateFunctionPointException: " + e.getMessage());
            }

            // InappropriateFunctionPointException - дублирование X
            try {
                func.addPoint(new FunctionPoint(5.0, 100.0));
            } catch (InappropriateFunctionPointException e) {
                System.out.println("InappropriateFunctionPointException: " + e.getMessage());
            }

            // IllegalStateException - удаление при малом количестве
            try {
                TabulatedFunction smallFunc = new ArrayTabulatedFunction(0.0, 2.0, 2);
                smallFunc.deletePoint(0);
            } catch (IllegalStateException e) {
                System.out.println("IllegalStateException: " + e.getMessage());
            }

            // IllegalArgumentException - некорректные параметры конструктора
            try {
                new ArrayTabulatedFunction(10.0, 0.0, 5);
            } catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}