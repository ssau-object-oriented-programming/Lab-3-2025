import functions.*;

public class Main {
    public static void main(String[] args) {
        //создаём экземпляры класса табулированной функции и заполняем их значениями для y=x^2
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(-6.0, 6.0, 9);
        TabulatedFunction listFunction = new LinkedListTabulatedFunction(-6.0, 6.0, 9);
        testFunction(arrayFunction);
        testFunction(listFunction);
        testExceptions();
    }

    public static void testFunction(TabulatedFunction function){
        try {
            //TabulatedFunction  = new ArrayTabulatedFunction(-6.0, 6.0, 9);

            for (int i = 0; i < function.getPointsCount(); i++){
                double x = function.getPointX(i);
                function.setPointY(i, function.getPointX(i)*function.getPointX(i));
            }

            System.out.println("Объект типа "+function.getClass().getSimpleName()+" успешно создан.");
            System.out.println();

            //демонстрация изначально заданной функции
            System.out.println("Функция y=x^2 из "+function.getPointsCount()+" точек на отрезке [-6.0, 6.0]: ");
            printFunction(function);
            System.out.println();

            //демонстрация значений функции в конкретных точках
            double[] testValues = {2.0, -10.0, 5.7};
            for (double testValue : testValues) {
                System.out.println("Значение функции в точке f(" + testValue + "): " + function.getFunctionValue(testValue));
            }
            System.out.println();

            //демонстрация границ области определения функции
            System.out.println("Функция определена на отрезке ["+function.getLeftDomainBorder()+", "+function.getRightDomainBorder()+"]");
            System.out.println();

            //демонстрация функции после удаления точки
            function.deletePoint(7);
            System.out.println("Функция после удаления 8ой точки: ");
            printFunction(function);
            System.out.println();

            //демонстрация функции после вставки точки
            FunctionPoint testPoint1 = new FunctionPoint(-5.8,21);
            function.addPoint(testPoint1);
            System.out.println("Функция после вставки точки c координатами (-5.8, 18.33):");
            printFunction(function);
            System.out.println();

            //демонстрация функции после замены точки
            FunctionPoint testPoint2 = new FunctionPoint(2.8,100);
            function.setPoint(6, testPoint2);
            System.out.println("Функция после замены 7ой точки:");
            printFunction(function);
            System.out.println();

            //демонстрация функции после замены конкретных координат (отдельно x и отдельно y)
            function.setPointX(2, -4.0);
            function.setPointY(5, 12.345);
            System.out.println("Функция после замены координат X у 3 точки и Y у 6 точки: ");
            printFunction(function);
            System.out.println();
        }
        catch(Exception e) {
            System.out.println("Ошибка создания"+function.getClass().getSimpleName()+": " + e.getMessage());
        }
    }

    public static void testExceptions() {
        System.out.println("Тестирование исключительных ситуаций ---> ");

        //тестирование неверных параметров конструктора
        testConstructorExceptions();

        //тестирование неверных индексов
        testIndexExceptions();

        //тестирование нарушения порядка точек
        testOrderExceptions();

        //тестирование операций с минимальным количеством точек
        testMinimumPointsExceptions();
    }

    public static void testConstructorExceptions() {
        System.out.println();
        System.out.println("1. Тестирование конструкторов:");

        try {
            TabulatedFunction func1 = new ArrayTabulatedFunction(5.0, 1.0, 3);
            System.out.println("Ошибка: Конструктор принял левую границу больше правой!");
        } catch (IllegalArgumentException e) {
            System.out.println("Конструктор отклонил левую границу больше правой: " + e.getMessage());
        }

        try {
            TabulatedFunction func2 = new LinkedListTabulatedFunction(1.0, 5.0, 1);
            System.out.println("Ошибка: Конструктор принял количество точек меньше 2!");
        } catch (IllegalArgumentException e) {
            System.out.println("Конструктор отклонил количество точек меньше 2: " + e.getMessage());
        }

        try {
            TabulatedFunction func3 = new ArrayTabulatedFunction(1.0, 5.0, new double[]{1.0});
            System.out.println("Ошибка: Конструктор принял массив длины меньше 2!");
        } catch (IllegalArgumentException e) {
            System.out.println("Конструктор отклонил массив длины меньше 2: " + e.getMessage());
        }
    }

    public static void testIndexExceptions() {
        System.out.println();
        System.out.println("2. Тестирование неверных индексов:");

        TabulatedFunction function = createTestFunction();

        try {
            function.getPoint(-1);
            System.out.println("Ошибка: Принят отрицательный индекс!");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Отклонён отрицательный индекс: " + e.getMessage());
        }

        try {
            function.getPoint(10);
            System.out.println("Ошибка: Принят слишком большой индекс!");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Отклонён индекс больше допустимого: " + e.getMessage());
        }

        try {
            function.setPoint(10, new FunctionPoint(3.0, 9.0));
            System.out.println("Ошибка: Принят неверный индекс в setPoint!");
        } catch (FunctionPointIndexOutOfBoundsException | InappropriateFunctionPointException e) {
            System.out.println("Отклонён неверный индекс в setPoint: " + e.getMessage());
        }
    }

    public static void testOrderExceptions() {
        System.out.println();
        System.out.println("3. Тестирование нарушения порядка:");

        TabulatedFunction function = createTestFunction();

        try {
            // Пытаемся установить точку с X меньше предыдущей
            function.setPoint(2, new FunctionPoint(0.5, 1.0));
            System.out.println("Ошибка: Принято нарушение порядка (X меньше предыдущего)!");
        } catch (IllegalArgumentException | InappropriateFunctionPointException e) {
            System.out.println("Отклонено нарушение порядка (X меньше предыдущего): " + e.getMessage());
        }

        try {
            // Пытаемся установить точку с X больше следующей
            function.setPoint(1, new FunctionPoint(2.5, 1.0));
            System.out.println("Ошибка: Принято нарушение порядка (X больше следующего)!");
        } catch (IllegalArgumentException | InappropriateFunctionPointException e) {
            System.out.println("Отклонено нарушение порядка (X больше следующего): " + e.getMessage());
        }

        try {
            // Пытаемся добавить точку с существующим X
            function.addPoint(new FunctionPoint(1.0, 5.0));
            System.out.println("Ошибка: Принято добавление точки с существующим X!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Отклонено добавление точки с существующим X: " + e.getMessage());
        }
    }

    public static void testMinimumPointsExceptions() {
        System.out.println();
        System.out.println("4. Тестирование минимального количества точек:");

        // Создаем функцию с минимальным количеством точек (2)
        TabulatedFunction function = new ArrayTabulatedFunction(0.0, 2.0, new double[]{0.0, 4.0});

        try {
            function.deletePoint(0);
            System.out.println("Ошибка: Принято удаление при количестве точек равном 2!");
        } catch (IllegalStateException e) {
            System.out.println("Отклонено удаление при количестве точек равном 2: " + e.getMessage());
        }

        try {
            function.deletePoint(1);
            System.out.println("Ошибка: Принято удаление при количестве точек равном 2!");
        } catch (IllegalStateException e) {
            System.out.println("Отклонено удаление при количестве точек равном 2: " + e.getMessage());
        }

        // Проверяем, что функция осталась неизменной
        System.out.println("Функция после попыток удаления (должна остаться неизменной):");
        printFunction(function);
    }

    //вспомогательный метод для создания тестовой функции
    public static TabulatedFunction createTestFunction() {
        TabulatedFunction function = new LinkedListTabulatedFunction(0.0, 2.0, 3);
        function.setPointY(0, 0.0);
        function.setPointY(1, 1.0);
        function.setPointY(2, 4.0);
        return function;
    }
    public static void printFunction(TabulatedFunction function){
        for (int i = 0; i < function.getPointsCount(); i++){
            System.out.println((i+1)+" точка: ("+function.getPointX(i)+", "+function.getPointY(i)+")");
        }
    }
}