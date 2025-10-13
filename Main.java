import functions.*;

public class Main {
    public static void main(String[] args) {
        // пример f(x) = 2x + 1 на интервале [0, 5]
        double[] val = {1, 3, 5, 7, 9, 11};
        // Для проверки ArrayTabulatedFunction, раскомментируйте фрагмент кода ниже
        TabulatedFunction fuf1 = new ArrayTabulatedFunction(0, 5, val);

        test(fuf1, "ArrayTabulatedFunction");
        System.out.println("\n-=-\n");
        testExceptions();

        /*TabulatedFunction fuf2 = new LinkedListTabulatedFunction(0, 5, val);

        test(fuf2, "LinkedListTabulatedFunction");
        System.out.println("\n-=-\n");
        testExceptions();*/
    }

    private static void test(TabulatedFunction fuf, String className) {
        try {
            System.out.println("\nТестирование: " + className);
            // Заполняем значениями y = 2*x+1
            for (int i = 0; i < fuf.getPointsCount(); i++) {
                double x = fuf.getPointX(i);
                fuf.setPointY(i, 2*x+1);
            }

            // Выводим исходные точки
            System.out.println("\nИсходные точки: ");
            fuf.printTabulatedFunction();

            // Тестируем интерполяцию
            double[] test = {-2, 0, 0.5, 1, 2.5, 3, 4, 5, 6};
            System.out.println("\nТест интерполяции: ");
            for (double x : test) {
                double value = fuf.getFunctionValue(x);
                System.out.printf("f(%.1f) = %.1f\n", x, value);
            }

            // Меняем одну из точек
            FunctionPoint newPoint = new FunctionPoint(1.5, 6);
            fuf.setPoint(1, newPoint);

            System.out.println("\nПосле замены точки: ");
            fuf.printTabulatedFunction();

            // Добавляем новую точку
            FunctionPoint addedPoint = new FunctionPoint(1.7, 4);
            fuf.addPoint(addedPoint);

            System.out.println("\nПосле добавления точки: ");
            fuf.printTabulatedFunction();

            // Удаляем точку
            fuf.deletePoint(3);

            System.out.println("\nПосле удаления точки: ");
            fuf.printTabulatedFunction();

        }
        catch (Exception e) {
            System.err.println("Ошибка при тестировании " + className + ": " + e.getMessage());
        }
    }

    private static void testExceptions() {
        // Тестируем все требуемые исключения
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0.0, 10.0, 3);

            // FunctionPointIndexOutOfBoundsException
            try {
                func.getPoint(11);
            }
            catch (FunctionPointIndexOutOfBoundsException e) {
                System.err.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
            }

            // InappropriateFunctionPointException - нарушение порядка
            try {
                func.setPointX(1, 20.0);
            }
            catch (InappropriateFunctionPointException e) {
                System.err.println("InappropriateFunctionPointException: " + e.getMessage());
            }

            // InappropriateFunctionPointException - дублирование X
            try {
                func.addPoint(new FunctionPoint(5.0, 100.0));
            }
            catch (InappropriateFunctionPointException e) {
                System.err.println("InappropriateFunctionPointException: " + e.getMessage());
            }

            // IllegalStateException - удаление при малом количестве
            try {
                TabulatedFunction smallFunc = new ArrayTabulatedFunction(0.0, 2.0, 2);
                smallFunc.deletePoint(0);
            }
            catch (IllegalStateException e) {
                System.err.println("IllegalStateException: " + e.getMessage());
            }

            // IllegalArgumentException - некорректные параметры конструктора
            try {
                new ArrayTabulatedFunction(10.0, 0.0, 5);
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgumentException: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}