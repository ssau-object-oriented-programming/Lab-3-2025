import functions.*;

public class Main {
    public static void main(String[] args) throws InappropriateFunctionPointException {
        System.out.println("=== ТЕСТИРОВАНИЕ TABULATEDFUNCTION С КВАДРАТИЧНОЙ ФУНКЦИЕЙ ===\n");

        // Создаем квадратичную функцию f(x) = x² - 3x + 2
        System.out.println("1. СОЗДАНИЕ КВАДРАТИЧНОЙ ФУНКЦИИ f(x) = x² - 3x + 2:");
        double leftX = 0;
        double rightX = 4;
        int pointsCount = 5;

        // Легко переключить реализацию: ArrayTabulatedFunction или LinkedListTabulatedFunction
        TabulatedFunction quadratic = new ArrayTabulatedFunction(leftX, rightX, pointsCount);

        // Заполняем значениями функции
        for (int i = 0; i < quadratic.getPointsCount(); i++) {
            double x = quadratic.getPointX(i);
            quadratic.setPointY(i, x * x - 3 * x + 2);
        }

        printFunction(quadratic, "Исходная функция");

        // Тест 2: Вычисление значений в различных точках
        System.out.println("\n2. ВЫЧИСЛЕНИЕ ЗНАЧЕНИЙ В РАЗЛИЧНЫХ ТОЧКАХ:");
        double[] testPoints = {-1.0, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 5.0};

        for (double x : testPoints) {
            double interpolated = quadratic.getFunctionValue(x);
            double exact = x * x - 3 * x + 2;
            System.out.printf("x=%.1f: ", x);

            if (Double.isNaN(interpolated)) {
                System.out.printf("вне области определения [%.1f, %.1f]%n",
                        quadratic.getLeftDomainBorder(), quadratic.getRightDomainBorder());
            } else {
                System.out.printf("интерполированное=%.4f, точное=%.4f", interpolated, exact);
                if (x >= leftX && x <= rightX) {
                    System.out.printf(" (погрешность=%.4f)", Math.abs(interpolated - exact));
                }
                System.out.println();
            }
        }

        // Тест 3: Добавление точки
        System.out.println("\n3. ДОБАВЛЕНИЕ ТОЧКИ (2.5, 0.75):");
        quadratic.addPoint(new FunctionPoint(2.5, 0.75));
        System.out.println("Количество точек после добавления: " + quadratic.getPointsCount());
        printFunction(quadratic, "После добавления точки");

        // Тест 4: Изменение точки
        System.out.println("\n4. ИЗМЕНЕНИЕ ТОЧКИ:");
        System.out.println("Попытка изменить X точки 3 на 3.2:");

        System.out.printf("До: (%.2f, %.2f)%n", quadratic.getPointX(3), quadratic.getPointY(3));
        try {

            quadratic.setPointX(3, 3.2);
        }
        catch (FunctionPointIndexOutOfBoundsException ex) {
            System.out.println("Нарушен порядок точек по x " + ex.getMessage());
        }
        System.out.printf("После: (%.2f, %.2f)%n", quadratic.getPointX(3), quadratic.getPointY(3));

        System.out.println("Изменение Y точки 3 на 10.0:");
        quadratic.setPointY(3, 10.0);
        System.out.printf("После: (%.2f, %.2f)%n", quadratic.getPointX(3), quadratic.getPointY(3));

        // Тест 5: Удаление точки
        System.out.println("\n5. УДАЛЕНИЕ ТОЧКИ 2:");
        quadratic.deletePoint(2);
        System.out.println("Количество точек после удаления: " + quadratic.getPointsCount());
        printFunction(quadratic, "После удаления точки");

        // Тест 6: Проверка после модификаций
        System.out.println("\n6. ПРОВЕРКА ИНТЕРПОЛЯЦИИ ПОСЛЕ МОДИФИКАЦИЙ:");
        double[] checkPoints = {1.2, 2.0, 2.8, 3.5};

        for (double x : checkPoints) {
            double value = quadratic.getFunctionValue(x);
            System.out.printf("f(%.1f) = %.4f", x, value);
            if (!Double.isNaN(value)) {
                double exact = x * x - 3 * x + 2;
                System.out.printf(" (точное=%.4f, отклонение=%.4f)", exact, Math.abs(value - exact));
            }
            System.out.println();
        }

        // Тест 7: Граничные значения
        System.out.println("\n7. ГРАНИЧНЫЕ ЗНАЧЕНИЯ:");
        System.out.printf("Область определения: [%.2f, %.2f]%n",
                quadratic.getLeftDomainBorder(), quadratic.getRightDomainBorder());
        System.out.printf("f(%.2f) = %.4f%n",
                quadratic.getLeftDomainBorder(), quadratic.getFunctionValue(quadratic.getLeftDomainBorder()));
        System.out.printf("f(%.2f) = %.4f%n",
                quadratic.getRightDomainBorder(), quadratic.getFunctionValue(quadratic.getRightDomainBorder()));
        // =========================
        // 8. ТЕСТЫ ИСКЛЮЧЕНИЙ
        // =========================
        System.out.println("\n8. ТЕСТЫ ИСКЛЮЧЕНИЙ:");

        // Конструкторы: левая граница >= правой
        try {
            new ArrayTabulatedFunction(5, 1, 3);
        } catch (IllegalArgumentException ex) {
            System.out.println("Ожидаемо: левая >= правой -> " + ex.getMessage());
        }

        // Конструкторы: меньше 2 точек
        try {
            new ArrayTabulatedFunction(0, 1, 1);
        } catch (IllegalArgumentException ex) {
            System.out.println("Ожидаемо: <2 точек -> " + ex.getMessage());
        }

        // Получение точки за пределами
        try {
            quadratic.getPoint(999);
        } catch (Exception ex) {
            System.out.println("Ожидаемо: getPoint(999) -> " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }

        // Удаление при недостатке точек (создаем маленькую функцию)
        try {
            TabulatedFunction tiny = new ArrayTabulatedFunction(0, 1, 2);
            tiny.deletePoint(0);
        } catch (Exception ex) {
            System.out.println("Ожидаемо: deletePoint при <3 -> " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }

        // Добавление точки с уже существующим X
        try {
            quadratic.addPoint(new FunctionPoint(quadratic.getPointX(0), 123));
        } catch (Exception ex) {
            System.out.println("Ожидаемо: addPoint с дубликатом X -> " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }

        // Нарушение порядка при setPointX
        try {
            quadratic.setPointX(1, quadratic.getPointX(0)); // сдвигаем влево, нарушая порядок
        } catch (Exception ex) {
            System.out.println("Ожидаемо: setPointX нарушает порядок -> " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }

        // Нарушение порядка при setPoint
        try {
            double badX = quadratic.getPointX(quadratic.getPointsCount() - 1);
            quadratic.setPoint(0, new FunctionPoint(badX, 0));
        } catch (Exception ex) {
            System.out.println("Ожидаемо: setPoint нарушает порядок -> " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }

        // =========================
        // 9. Быстрая проверка LinkedListTabulatedFunction (переключение реализации)
        // =========================
        System.out.println("\n9. БЫСТРАЯ ПРОВЕРКА LinkedListTabulatedFunction:");
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        for (int i = 0; i < listFunc.getPointsCount(); i++) {
            double x = listFunc.getPointX(i);
            listFunc.setPointY(i, x * x - 3 * x + 2);
        }
        System.out.println("Значение в 1.5: " + listFunc.getFunctionValue(1.5));
        System.out.println("Значение в 4.0: " + listFunc.getFunctionValue(4.0));
    }

    private static void printFunction(TabulatedFunction function, String name) {
        System.out.println(name + ":");
        System.out.println("Индекс\tX\t\tY");
        System.out.println("---------------------------");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.printf("%d\t%.4f\t%.4f%n",
                    i, function.getPointX(i), function.getPointY(i));
        }
    }
}