import functions.*;

public class Main {
    public static void main(String[] args) {
        double[] values = { 0, 1, 4, 9, 16 };

        // Используем тип интерфейса
        TabulatedFunction function = new ArrayTabulatedFunction(0, 4, values);
        // TabulatedFunction function = new LinkedListTabulatedFunction(0, 4, values); // - для другой реализации

        System.out.println("     Исходная функция (" + function.getClass().getSimpleName() + ")");
        printFunction(function);

        System.out.println("\n     Тестирование исключений");

        // 1. Выход за границы индекса
        try {
            System.out.println("Попытка получить точку с индексом 10 !!!!!");
            function.getPoint(10);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 2. Некорректный X при изменении
        try {
            System.out.println("Попытка изменить X точки 2 на 0,5 (неверно) !!!!!");
            function.setPointX(2, 0.5);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 3. Добавление точки с существующим X
        try {
            System.out.println("Попытка добавить точку (3, 9) !!!!!");
            function.addPoint(new FunctionPoint(3.0, 9.0));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 4. Удаление из маленькой функции
        try {
            System.out.println("Попытка удалить точку из функции с 2 точками !!!!!");
            TabulatedFunction smallFunc = new ArrayTabulatedFunction(0, 1, new double[]{0, 1});
            smallFunc.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 5. Неверные аргументы конструктора
        try {
            System.out.println("Попытка создать функцию с 1 точкой !!!!!");
            new ArrayTabulatedFunction(0, 1, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }
    }

    public static void printFunction(TabulatedFunction func) {
        System.out.println("Функция из " + func.getPointsCount() + " точек:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint p = func.getPoint(i);
            System.out.println("Точка " + i + ": (" + p.getX() + "; " + p.getY() + ")");
        }
    }
}
