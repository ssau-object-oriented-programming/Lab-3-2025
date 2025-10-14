import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ КЛАССОВ ТАБУЛИРОВАННОЙ ФУНКЦИИ ===\n");

        try {
            // Тестирование ArrayTabulatedFunction
            testArrayTabulatedFunction();

            System.out.println("\n" + "=".repeat(50) + "\n");

            // Тестирование LinkedListTabulatedFunction
            testLinkedListTabulatedFunction();

            System.out.println("\n" + "=".repeat(50) + "\n");

            // Тестирование исключений с правильными сообщениями
            testExceptionsWithCorrectMessages();

        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }

    private static void testArrayTabulatedFunction() {
        System.out.println("ТЕСТИРОВАНИЕ ArrayTabulatedFunction");

        try {
            // Создание табулированной функции f(x) = 10x + 10
            System.out.println("1. СОЗДАНИЕ ФУНКЦИИ f(x) = 10x + 10 НА ИНТЕРВАЛЕ [0, 5]:");
            double[] values = {10, 20, 30, 40, 50, 60};
            TabulatedFunction func = new ArrayTabulatedFunction(0, 5, values);

            // Вывод информации о функции
            System.out.printf("Область определения: [%.1f, %.1f]\n",
                    func.getLeftDomainBorder(), func.getRightDomainBorder());
            System.out.println("Количество точек: " + func.getPointsCount());

            // Вывод точек функции
            System.out.println("\n2. ТОЧКИ ТАБУЛИРОВАННОЙ ФУНКЦИИ:");
            for (int i = 0; i < func.getPointsCount(); i++) {
                FunctionPoint p = func.getPoint(i);
                System.out.printf("Точка %d: (%.1f; %.1f)\n", i, p.getX(), p.getY());
            }

            // Тестирование вычисления значений
            System.out.println("\n3. ВЫЧИСЛЕНИЕ ЗНАЧЕНИЙ ФУНКЦИИ:");
            double[] testX = {0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
            for (double x : testX) {
                double y = func.getFunctionValue(x);
                double expected = 10 * x + 10;
                System.out.printf("f(%.1f) = %.1f (ожидалось: %.1f) %s\n",
                        x, y, expected,
                        Math.abs(y - expected) < 0.1 ? "" : " ОШИБКА!");
            }

            // Тестирование модификации точек
            System.out.println("\n4. ТЕСТИРОВАНИЕ МОДИФИКАЦИИ ТОЧЕК:");
            System.out.printf("До изменения: (%.1f; %.1f) -> ", func.getPointX(2), func.getPointY(2));
            func.setPointY(2, 35);
            System.out.printf("После: (%.1f; %.1f)\n", func.getPointX(2), func.getPointY(2));

            // Тестирование добавления точки
            System.out.println("\n5. ТЕСТИРОВАНИЕ ДОБАВЛЕНИЯ ТОЧКИ:");
            System.out.println("Количество точек до добавления: " + func.getPointsCount());
            func.addPoint(new FunctionPoint(2.2, 32));
            System.out.println("Количество точек после добавления: " + func.getPointsCount());

            // Тестирование удаления точки
            System.out.println("\n6. ТЕСТИРОВАНИЕ УДАЛЕНИЯ ТОЧКИ:");
            System.out.println("Количество точек до удаления: " + func.getPointsCount());
            func.deletePoint(1);
            System.out.println("Количество точек после удаления: " + func.getPointsCount());

        } catch (Exception e) {
            System.out.println("Ошибка при тестировании ArrayTabulatedFunction: " + e.getMessage());
        }
    }

    private static void testLinkedListTabulatedFunction() {
        System.out.println("ТЕСТИРОВАНИЕ LinkedListTabulatedFunction");

        try {
            // Создание табулированной функции f(x) = x²
            System.out.println("1. СОЗДАНИЕ ФУНКЦИИ f(x) = x² НА ИНТЕРВАЛЕ [0, 5]:");
            double[] values = {0, 1, 4, 9, 16, 25};
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 5, values);

            // Вывод информации о функции
            System.out.printf("Область определения: [%.1f, %.1f]\n",
                    func.getLeftDomainBorder(), func.getRightDomainBorder());
            System.out.println("Количество точек: " + func.getPointsCount());

            // Вывод точек функции
            System.out.println("\n2. ТОЧКИ ТАБУЛИРОВАННОЙ ФУНКЦИИ:");
            for (int i = 0; i < func.getPointsCount(); i++) {
                FunctionPoint p = func.getPoint(i);
                System.out.printf("Точка %d: (%.1f; %.1f)\n", i, p.getX(), p.getY());
            }

            // Тестирование вычисления значений
            System.out.println("\n3. ВЫЧИСЛЕНИЕ ЗНАЧЕНИЙ ФУНКЦИИ:");
            double[] testX = {0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
            for (double x : testX) {
                double y = func.getFunctionValue(x);
                double expected = x * x;
                System.out.printf("f(%.1f) = %.1f (ожидалось: %.1f) %s\n",
                        x, y, expected,
                        Math.abs(y - expected) < 0.1 ? "" : " ОШИБКА!");
            }

            // Тестирование модификации точек
            System.out.println("\n4. ТЕСТИРОВАНИЕ МОДИФИКАЦИИ ТОЧЕК:");

            // Изменение ординаты
            System.out.println("Изменение ординаты точки с индексом 2:");
            System.out.printf("До: (%.1f; %.1f) -> ", func.getPointX(2), func.getPointY(2));
            func.setPointY(2, 5);
            System.out.printf("После: (%.1f; %.1f)\n", func.getPointX(2), func.getPointY(2));

            // Корректное изменение абсциссы
            System.out.println("\nКорректное изменение абсциссы с обновлением Y:");
            System.out.printf("До: (%.1f; %.1f) -> ", func.getPointX(1), func.getPointY(1));
            func.setPointX(1, 1.2);
            func.setPointY(1, 1.2 * 1.2); // обновляем Y соответственно новой X
            System.out.printf("После: (%.1f; %.1f)\n", func.getPointX(1), func.getPointY(1));

            // Тестирование добавления точки
            System.out.println("\n5. ТЕСТИРОВАНИЕ ДОБАВЛЕНИЯ ТОЧКИ:");
            System.out.println("Количество точек до добавления: " + func.getPointsCount());
            double newX = 2.2;
            double newY = newX * newX;
            func.addPoint(new FunctionPoint(newX, newY));
            System.out.println("Количество точек после добавления: " + func.getPointsCount());
            System.out.printf("Значение в добавленной точке: f(%.1f) = %.1f\n", newX, func.getFunctionValue(newX));

            // Вывод всех точек после добавления
            System.out.println("Точки после добавления:");
            for (int i = 0; i < func.getPointsCount(); i++) {
                FunctionPoint p = func.getPoint(i);
                System.out.printf("(%.1f; %.1f) ", p.getX(), p.getY());
            }
            System.out.println();

            // Тестирование удаления точки
            System.out.println("\n6. ТЕСТИРОВАНИЕ УДАЛЕНИЯ ТОЧКИ:");
            System.out.println("Количество точек до удаления: " + func.getPointsCount());
            func.deletePoint(2);
            System.out.println("Количество точек после удаления: " + func.getPointsCount());

            // Обновляем все значения Y после всех изменений, чтобы соответствовать f(x) = x²
            System.out.println("\n7. ОБНОВЛЕНИЕ ВСЕХ ЗНАЧЕНИЙ Y В СООТВЕТСТВИИ С f(x) = x²:");
            for (int i = 0; i < func.getPointsCount(); i++) {
                double x = func.getPointX(i);
                func.setPointY(i, x * x);
                System.out.printf("Точка %d: (%.1f; %.1f)\n", i, x, func.getPointY(i));
            }

            // Проверка интерполяции после всех исправлений
            System.out.println("\n8. ПРОВЕРКА ИНТЕРПОЛЯЦИИ ПОСЛЕ ИСПРАВЛЕНИЙ:");
            double[] interpolationX = {0.3, 1.1, 1.8, 2.7, 3.9, 4.6};
            for (double x : interpolationX) {
                double y = func.getFunctionValue(x);
                double expected = x * x;
                System.out.printf("f(%.1f) = %.1f (ожидалось: %.1f) %s\n",
                        x, y, expected,
                        Math.abs(y - expected) < 0.1 ? "" : " ОШИБКА!");
            }

            // Граничные значения
            System.out.println("\n9. ГРАНИЧНЫЕ ЗНАЧЕНИЯ:");
            System.out.printf("Левая граница: f(%.1f) = %.1f\n",
                    func.getLeftDomainBorder(), func.getFunctionValue(func.getLeftDomainBorder()));
            System.out.printf("Правая граница: f(%.1f) = %.1f\n",
                    func.getRightDomainBorder(), func.getFunctionValue(func.getRightDomainBorder()));

            System.out.println("\nТЕСТИРОВАНИЕ LinkedListTabulatedFunction ЗАВЕРШЕНО УСПЕШНО");

        } catch (Exception e) {
            System.out.println("Ошибка при тестировании LinkedListTabulatedFunction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testExceptionsWithCorrectMessages() {
        System.out.println("ТЕСТИРОВАНИЕ ИСКЛЮЧЕНИЙ");

        try {
            // Тестирование конструктора с неверными параметрами
            System.out.println("1. Тестирование конструктора:");
            try {
                TabulatedFunction func = new ArrayTabulatedFunction(5, 0, 5);
                System.out.println("ОШИБКА: Исключение не было выброшено!");
            } catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException: " + e.getMessage());
            }

            // Тестирование выхода за границы индексов
            System.out.println("\n2. Тестирование выхода за границы индексов:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 5, new double[]{10, 20, 30, 40, 50});

            try {
                func.getPoint(11);
                System.out.println("ОШИБКА: Исключение не было выброшено!");
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
            }

            // Тестирование несоответствующих точек
            System.out.println("\n3. Тестирование несоответствующих точек:");
            try {
                func.setPointX(2, 0.5); // X меньше предыдущей точки
                System.out.println("ОШИБКА: Исключение не было выброшено!");
            } catch (InappropriateFunctionPointException e) {
                System.out.println("InappropriateFunctionPointException: " + e.getMessage());
            }

            try {
                // Создаем новую функцию для теста добавления точки с существующим X
                TabulatedFunction func2 = new ArrayTabulatedFunction(0, 5, new double[]{10, 20, 30, 40, 50});
                func2.addPoint(new FunctionPoint(2.0, 100)); // Точка с существующим X
                System.out.println("ОШИБКА: Исключение не было выброшено!");
            } catch (InappropriateFunctionPointException e) {
                System.out.println("InappropriateFunctionPointException: " + e.getMessage());
            }

            // Тестирование удаления точек
            System.out.println("\n4. Тестирование удаления точек:");
            TabulatedFunction smallFunc = new ArrayTabulatedFunction(0, 2, new double[]{10, 20, 30});
            try {
                smallFunc.deletePoint(0);
                smallFunc.deletePoint(0);
                smallFunc.deletePoint(0); // Попытка удалить последнюю точку
                System.out.println("ОШИБКА: Исключение не было выброшено!");
            } catch (IllegalStateException e) {
                System.out.println("IllegalStateException: " + e.getMessage());
            }

            System.out.println("\n ВСЕ ИСКЛЮЧЕНИЯ ПРОТЕСТИРОВАНЫ УСПЕШНО");

        } catch (Exception e) {
            System.out.println("Неожиданная ошибка при тестировании исключений: " + e.getMessage());
        }
    }
}