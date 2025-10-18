import functions.*;

public class Main {
    public static void main(String[] args) {
        double[] values = { 0, 1, 4, 9, 16 };

        // ТЕСТИРОВАНИЕ ArrayTabulatedFunction
        System.out.println("               ТЕСТИРОВАНИЕ ArrayTabulatedFunction");
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(0, 4, values);
        testFunction(arrayFunction); // Общая проверка методов
        testExceptions(arrayFunction); // Проверка исключений

        // ТЕСТИРОВАНИЕ LinkedListTabulatedFunction
        System.out.println("\n\n               ТЕСТИРОВАНИЕ LinkedListTabulatedFunction");
        TabulatedFunction listFunction = new LinkedListTabulatedFunction(0, 4, values);
        testFunction(listFunction); // Общая проверка методов
        testExceptions(listFunction); // Проверка исключений
    }

    // Метод тестирует основные операции с функцией: получение значения, изменение, добавление и удаление точек.
    public static void testFunction(TabulatedFunction func) {
        System.out.println("     Исходная функция (" + func.getClass().getSimpleName() + ")");
        printFunction(func);

        System.out.println("\n     Проверка getFunctionValue");
        System.out.println("Значение в точке f(2,5) = " + func.getFunctionValue(2.5));

        System.out.println("\n     Проверка setPointY");
        System.out.println("Изменяем Y в точке с индексом 2 на 5,0");
        func.setPointY(2, 5.0);
        printFunction(func);
        // Чтобы не запутаться, возвращаем обратно
        func.setPointY(2, 4.0);

        System.out.println("\n     Проверка deletePoint");
        System.out.println("Удаляем точку с индексом 1");
        func.deletePoint(1);
        printFunction(func);

        System.out.println("\n     Проверка addPoint");
        System.out.println("Добавляем точку (1.0; 1.0) обратно");
        try {
            func.addPoint(new FunctionPoint(1.0, 1.0));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Не удалось добавить точку: " + e.getMessage());
        }
        printFunction(func);
    }

    // Этот метод тестирует выбрасывание всех необходимых исключений.
    public static void testExceptions(TabulatedFunction func) {
        System.out.println("\n     Тестирование исключений для " + func.getClass().getSimpleName());

        // 1. Выход за границы индекса
        try {
            System.out.println("Попытка получить точку с индексом 10 !!!!!");
            func.getPoint(10);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 2. Некорректный X при изменении
        try {
            System.out.println("Попытка изменить X точки 2 на 0,5 (неверно) !!!!!");
            func.setPointX(2, 0.5);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 3. Добавление точки с существующим X
        try {
            System.out.println("Попытка добавить точку (3.0; 9.0) !!!!!");
            func.addPoint(new FunctionPoint(3.0, 9.0));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }

        // 4. Удаление из маленькой функции
        try {
            System.out.println("Попытка удалить точку из функции с 2 точками !!!!!");
            // Создаем временный объект того же класса, что и тестируемый
            TabulatedFunction smallFunc;
            if (func instanceof ArrayTabulatedFunction) {
                smallFunc = new ArrayTabulatedFunction(0, 1, new double[]{0, 1});
            } else {
                smallFunc = new LinkedListTabulatedFunction(0, 1, new double[]{0, 1});
            }
            smallFunc.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("Перехвачено: " + e.getClass().getSimpleName());
        }
    }

    // Дополнительный метод для красивой печати
    public static void printFunction(TabulatedFunction func) {
        System.out.println("Функция из " + func.getPointsCount() + " точек:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint p = func.getPoint(i);
            System.out.println("Точка " + i + ": (" + p.getX() + "; " + p.getY() + ")");
        }
    }
}
