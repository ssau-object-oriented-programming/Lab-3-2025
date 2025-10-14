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
            // Создание табулированной функции
            double[] values = {10, 20, 30, 40, 50, 60};
            TabulatedFunction func = new ArrayTabulatedFunction(0, 5, values);

            System.out.println("Функция успешно создана");
            System.out.printf("Область определения: [%.1f, %.1f]\n",
                    func.getLeftDomainBorder(), func.getRightDomainBorder());
            System.out.println("Количество точек: " + func.getPointsCount());

        } catch (Exception e) {
            System.out.println("Ошибка при тестировании ArrayTabulatedFunction: " + e.getMessage());
        }
    }

    private static void testLinkedListTabulatedFunction() {
        System.out.println("ТЕСТИРОВАНИЕ LinkedListTabulatedFunction");

        try {
            // Создание табулированной функции
            double[] values = {0, 1, 4, 9, 16, 25};
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 5, values);

            System.out.println("Функция успешно создана");
            System.out.printf("Область определения: [%.1f, %.1f]\n",
                    func.getLeftDomainBorder(), func.getRightDomainBorder());
            System.out.println("Количество точек: " + func.getPointsCount());

        } catch (Exception e) {
            System.out.println("Ошибка при тестировании LinkedListTabulatedFunction: " + e.getMessage());
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
                func.addPoint(new FunctionPoint(2.0, 100)); // Точка с существующим X
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

        } catch (Exception e) {
            System.out.println("Неожиданная ошибка при тестировании исключений: " + e.getMessage());
        }
    }
}