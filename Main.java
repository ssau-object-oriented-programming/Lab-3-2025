import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("ТЕСТИРОВАНИЕ КЛАССОВ TABULATED FUNCTION\n");

        //тестирование ArrayTabulatedFunction
        System.out.println("Тест: ArrayTabulatedFunction");
        testFunction(new ArrayTabulatedFunction(0, 4, new double[]{0, 1, 4, 9, 16}));

        //тестирование LinkedListTabulatedFunction
        System.out.println("\n Тест: LinkedListTabulatedFunction");
        testFunction(new LinkedListTabulatedFunction(0, 4, new double[]{0, 1, 4, 9, 16}));

        System.out.println("\nВСЕ ТЕСТЫ ЗАВЕРШЕНЫ");
    }

    private static void testFunction(TabulatedFunction func) {
        System.out.println("Тип функции: " + func.getClass().getSimpleName());
        System.out.println();

        //1.проверка вычисления значений
        System.out.println("1 Проверяем значения функции:");
        for (double x = 1; x <= 5; x += 0.5) {
            double y = func.getFunctionValue(x);
            System.out.printf("f(%.1f) = %s\n", x, Double.isNaN(y) ? "NaN" : y);
        }

        //2.проверка корректности точек
        System.out.println("\n2 Проверяем доступ к точкам:");
        try {
            System.out.println("Количество точек: " + func.getPointsCount());
            for (int i = 0; i < func.getPointsCount(); i++) {
                FunctionPoint p = func.getPoint(i);
                System.out.printf("Точка %d: (%.2f, %.2f)\n", i, p.getX(), p.getY());
            }
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Ошибка доступа к точке: " + e.getMessage());
        }

        //3.проверка ошибок индекса
        System.out.println("\n3 Проверяем ошибки индекса:");
        try {
            func.getPoint(1);
            System.out.println("Ошибка: ожидалось FunctionPointIndexOutOfBoundsException, но не было");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Ловим FunctionPointIndexOutOfBoundsException (1): " + e.getMessage());
        }

        try {
            func.getPoint(func.getPointsCount());
            System.out.println("Ошибка: ожидалось FunctionPointIndexOutOfBoundsException, но не было");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Ловим FunctionPointIndexOutOfBoundsException (index == count): " + e.getMessage());
        }

        //4.проверка нарушений порядка X
        System.out.println("\n4 Проверяем нарушение порядка X:");
        try {
            FunctionPoint bad = new FunctionPoint(100, 0);
            func.setPoint(1, bad);
            System.out.println("Ошибка: ожидалось InappropriateFunctionPointException при setPoint, но не было");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ловим InappropriateFunctionPointException при setPoint: " + e.getMessage());
        }

        try {
            func.setPointX(1, func.getPointX(0) -1);
            System.out.println("Ошибка: ожидалось InappropriateFunctionPointException при setPointX, но не было");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ловим InappropriateFunctionPointException при setPointX: " + e.getMessage());
        }

        //5. проверка добавления точки с дублирующим X
        System.out.println("\n5 Проверяем добавление точки с повторяющимся X:");
        try {
            double existingX = func.getPointX(2);
            func.addPoint(new FunctionPoint(existingX, 123));
            System.out.println("Ошибка: ожидалось InappropriateFunctionPointException, но не было");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ловим InappropriateFunctionPointException (дубль X): " + e.getMessage());
        }

        //6.проверка удаления точки при < 2 точках
        System.out.println("\n6 Проверяем удаление, если останется <2 точек:");
        try {
            TabulatedFunction small = new ArrayTabulatedFunction(0, 1, new double[]{0, 1});
            System.out.println("Создана функция с " + small.getPointsCount() + " точками");
            small.deletePoint(0);
            System.out.println("Ошибка: ожидалось IllegalStateException, но не было");
        } catch (IllegalStateException e) {
            System.out.println("Ловим IllegalStateException при deletePoint: " + e.getMessage());
        }

        System.out.println("\nПроверка " + func.getClass().getSimpleName() + " завершена успешно!\n");
    }
}
