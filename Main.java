import functions.*;

public class Main {
    private static void printPoints(TabulatedFunction func) {
        System.out.print("Точки: [");
        for (int i = 0; i < func.getPointsCount(); i++) {
            System.out.print("(" + func.getPointX(i) + ", " + func.getPointY(i) + ")");
            if (i < func.getPointsCount() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        double[] values = {4, 1, 0, 1, 4, 9};

        System.out.println(" Тестирование ArrayTabulatedFunction");
        testFunction(new ArrayTabulatedFunction(-2, 3, values));

        System.out.println("\n Тестирование LinkedListTabulatedFunction");
        testFunction(new LinkedListTabulatedFunction(-2, 3, values));

        System.out.println("\n Проверка исключений ");
        testExceptions();
    }

    private static void testFunction(TabulatedFunction func) {
        System.out.println("Исходная функция");
        System.out.println("Левая граница: " + func.getLeftDomainBorder());
        System.out.println("Правая граница: " + func.getRightDomainBorder());
        System.out.println("Кол-во точек: " + func.getPointsCount());
        printPoints(func);

        System.out.println(" Проверка значений функции");
        double[] testPoints = {-3, -2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2, 2.5, 3, 4};
        for (double x : testPoints) {
            double result = func.getFunctionValue(x);
            System.out.println("f(" + x + ") = " + result);
        }

        System.out.println("\n Проверка операций с точками");

        System.out.println("1. Добавляем точку (-0.7, 0.49):");
        try {
            func.addPoint(new FunctionPoint(-0.7, 0.49));
            printPoints(func);
            System.out.println("f(-0.7) = " + func.getFunctionValue(-0.7));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при добавлении: " + e.getMessage());
        }

        System.out.println("\n2. Удаляем точку с индексом 1:");
        try {
            func.deletePoint(1);
            printPoints(func);
        } catch (Exception e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }

        System.out.println("\n3. Изменяем y точки с индексом 2 на 0.5:");
        try {
            func.setPointY(2, 0.5);
            printPoints(func);
        } catch (Exception e) {
            System.out.println("Ошибка при изменении: " + e.getMessage());
        }

        System.out.println("\n Итоговый результат");
        System.out.println("f(-0.7) = " + func.getFunctionValue(-0.7));
        printPoints(func);
    }

    private static void testExceptions() {
        System.out.println("1. Неправильные границы функции (левая >= правой):");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(3, -2, new double[]{1, 2, 3});
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (IllegalArgumentException e) {
            System.out.println("   Получено: " + e.getMessage());
        }

        System.out.println("2. Слишком мало точек (меньше 2):");
        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(-2, 3, new double[]{4});
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (IllegalArgumentException e) {
            System.out.println("   Получено: " + e.getMessage());
        }

        System.out.println("3. Несуществующий индекс точки:");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(-2, 3, new double[]{4, 1, 0, 1, 4, 9});
            func.getPoint(10);
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("   Получено: " + e.getMessage());
        }

        System.out.println("4. Нарушение порядка точек по X:");
        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(-2, 3, 6);
            func.setPointX(2, 5.0);
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("   Получено: " + e.getMessage());
        }

        System.out.println("5. Добавление точки с уже существующим X:");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(-2, 3, new double[]{4, 1, 0, 1, 4, 9});
            func.addPoint(new FunctionPoint(1.0, 5.0));
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("   Получено: " + e.getMessage());
        }

        System.out.println("6. Удаление точки когда их останется слишком мало:");
        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(-2, 3, new double[]{4, 1, 0});
            func.deletePoint(0);
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (IllegalStateException e) {
            System.out.println("   Получено: " + e.getMessage());
        }
    }
}