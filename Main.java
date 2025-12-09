import functions.*;

public class Main {
    
    public static void main(String[] args) {
        System.out.println(" ТЕСТИРОВАНИЕ КЛАССОВ ТАБУЛИРОВАННЫХ ФУНКЦИЙ \n");
        
        // Тестируем ArrayTabulatedFunction
        System.out.println("1. ТЕСТИРОВАНИЕ ArrayTabulatedFunction:");
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 10, 5);
        testTabulatedFunction(arrayFunc);
        
        // Тестируем LinkedListTabulatedFunction
        System.out.println("\n2. ТЕСТИРОВАНИЕ LinkedListTabulatedFunction:");
        TabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(0, 10, 5);
        testTabulatedFunction(linkedListFunc);
        
        // Тестируем исключения конструкторов
        System.out.println("\n3. ТЕСТИРОВАНИЕ ИСКЛЮЧЕНИЙ В КОНСТРУКТОРАХ:");
        testConstructorExceptions();
        
        System.out.println("\n ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ ");
    }
    
    public static void testTabulatedFunction(TabulatedFunction function) {
        System.out.println("Исходная функция: " + function);
        System.out.println("Количество точек: " + function.getPointsCount());
        
        // Тест 1: Некорректные индексы
        testInvalidIndices(function);
        
        // Тест 2: Некорректные точки (нарушение порядка)
        testInvalidPoints(function);
        
        // Тест 3: Добавление дублирующихся точек
        testAddPointExceptions(function);
        
        // Тест 4: Удаление точек
        testDeletePointExceptions(function);
        
        // Тест 5: Корректные операции
        testValidOperations(function);
    }
    
    private static void testInvalidIndices(TabulatedFunction function) {
        System.out.println("\n Тест исключений для некорректных индексов ");
        
        // getPoint с отрицательным индексом
        try {
            function.getPoint(-1);
            System.out.println("ОШИБКА: getPoint(-1) не выбросил исключение");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("getPoint(-1): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // getPoint с индексом больше размера
        try {
            function.getPoint(100);
            System.out.println("ОШИБКА: getPoint(100) не выбросил исключение");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("getPoint(100): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // getPointX с некорректным индексом
        try {
            function.getPointX(-5);
            System.out.println("ОШИБКА: getPointX(-5) не выбросил исключение");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("getPointX(-5): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // setPointY с некорректным индексом
        try {
            function.setPointY(10, 5.0);
            System.out.println("ОШИБКА: setPointY(10, 5.0) не выбросил исключение");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("setPointY(10, 5.0): " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Неожиданное исключение: " + e.getClass().getSimpleName());
        }
    }
    
    private static void testInvalidPoints(TabulatedFunction function) {
        System.out.println("\n Тест исключений для некорректных точек ");
        
        // Попытка установить первую точку правее второй
        try {
            function.setPoint(0, new FunctionPoint(5.0, 1.0));
            System.out.println("ОШИБКА: setPoint(0) с нарушением порядка не выбросил исключение");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("setPoint(0) с нарушением порядка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Попытка установить последнюю точку левее предпоследней
        int lastIndex = function.getPointsCount() - 1;
        try {
            function.setPoint(lastIndex, new FunctionPoint(1.0, 1.0));
            System.out.println("ОШИБКА: setPoint(last) с нарушением порядка не выбросил исключение");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("setPoint(last) с нарушением порядка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Попытка установить среднюю точку вне диапазона
        try {
            function.setPoint(2, new FunctionPoint(10.0, 1.0));
            System.out.println("ОШИБКА: setPoint(2) с нарушением диапазона не выбросил исключение");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("setPoint(2) с нарушением диапазона: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Попытка изменить X координату с нарушением порядка через setPointX
        try {
            function.setPointX(0, 5.0);
            System.out.println("ОШИБКА: setPointX(0, 5.0) с нарушением порядка не выбросил исключение");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("setPointX(0, 5.0) с нарушением порядка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
    }
    
    private static void testAddPointExceptions(TabulatedFunction function) {
        System.out.println("\n Тест исключений при добавлении точек ");
        
        // Добавление точки с существующей X координатой
        try {
            function.addPoint(new FunctionPoint(2.5, 10.0));
            System.out.println("ОШИБКА: addPoint с существующим X не выбросил исключение");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("addPoint с существующим X: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Корректное добавление точки (должно сработать)
        try {
            function.addPoint(new FunctionPoint(3.7, 7.5));
            System.out.println("addPoint с новым X: успешно добавлена");
            System.out.println("  Теперь точек: " + function.getPointsCount());
        } catch (Exception e) {
            System.out.println("ОШИБКА: addPoint с новым X выбросил исключение: " + e.getMessage());
        }
    }
    
    private static void testDeletePointExceptions(TabulatedFunction function) {
        System.out.println("\n Тест исключений при удалении точек ");
        
        // Удаление с некорректным индексом
        try {
            function.deletePoint(100);
            System.out.println("ОШИБКА: deletePoint(100) не выбросил исключение");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("deletePoint(100): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Удаление с отрицательным индексом
        try {
            function.deletePoint(-1);
            System.out.println("ОШИБКА: deletePoint(-1) не выбросил исключение");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("deletePoint(-1): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Создаем минимальную функцию для теста ограничения на удаление
        TabulatedFunction minFunction;
        String className = function.getClass().getSimpleName();
        
        if (className.equals("ArrayTabulatedFunction")) {
            minFunction = new ArrayTabulatedFunction(0, 2, 3);
        } else {
            minFunction = new LinkedListTabulatedFunction(0, 2, 3);
        }
        
        System.out.println("  Создана минимальная функция для теста удаления");
        
        // Попытка удалить точку, когда останется только 2 точки
        try {
            minFunction.deletePoint(0);
            minFunction.deletePoint(0); // Теперь должно остаться 2 точки
            System.out.println("  Успешно удалено 2 точки, осталось: " + minFunction.getPointsCount());
            
            // Попытка удалить последнюю точку (должно остаться 2)
            minFunction.deletePoint(0);
            System.out.println("ОШИБКА: deletePoint при pointsCount < 3 не выбросил исключение");
        } catch (IllegalStateException e) {
            System.out.println("deletePoint при pointsCount < 3: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
    }
    
    private static void testValidOperations(TabulatedFunction function) {
    System.out.println("\n Тест корректных операций ");
    
    try {
        // Проверяем границы области определения
        System.out.println("Левая граница: " + function.getLeftDomainBorder());
        System.out.println("Правая граница: " + function.getRightDomainBorder());
        
        // Получаем и устанавливаем Y координату
        System.out.println("координата точки 1: " + function.getPointY(1));
        function.setPointY(1, 15.0);
        System.out.println("координата точки 1 после setPointY: " + function.getPointY(1));
        
        // Получаем точку
        FunctionPoint point = function.getPoint(2);
        System.out.println("Точка 2: " + point);
        
        // Получаем значения функции в разных точках
        System.out.println("f(2.5) = " + function.getFunctionValue(2.5));
        System.out.println("f(5.0) = " + function.getFunctionValue(5.0));
        System.out.println("f(15.0) = " + function.getFunctionValue(15.0)); // Вне области определения
        System.out.println("f(-5.0) = " + function.getFunctionValue(-5.0)); // Вне области определения
        
        // Изменяем X координату корректно - используем предпоследнюю точку
        int lastIndex = function.getPointsCount() - 1;
        int pointToChange = Math.max(0, lastIndex - 1); // Предпоследняя или первая
        double newX = function.getPointX(pointToChange) + 0.1;
        function.setPointX(pointToChange, newX);
        System.out.println("координата точки " + pointToChange + " после корректного изменения: " + function.getPointX(pointToChange));
        
        // Добавляем еще одну точку (только если это не LinkedListTabulatedFunction с null)
        if (!function.getClass().getSimpleName().equals("LinkedListTabulatedFunction")) {
            function.addPoint(new FunctionPoint(4.5, 20.0));
            System.out.println("Добавлена новая точка, теперь точек: " + function.getPointsCount());
        }
        
        // Удаляем точку (если осталось больше 3 точек)
        if (function.getPointsCount() > 3) {
            function.deletePoint(1);
            System.out.println("Удалена точка 1, теперь точек: " + function.getPointsCount());
        }
        
        System.out.println("Все корректные операции выполнены успешно");
        
    } catch (Exception e) {
        System.out.println("ОШИБКА в корректных операциях: " + e.getMessage());
        // e.printStackTrace(); // Можно закомментировать для чистоты вывода
    }
}
    
    private static void testConstructorExceptions() {
        System.out.println(" Тест исключений конструкторов ArrayTabulatedFunction ");
        
        // Конструктор 1: leftX >= rightX
        try {
            new ArrayTabulatedFunction(10, 5, 5);
            System.out.println("ОШИБКА: ArrayTabulatedFunction(10, 5, 5) не выбросил исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("ArrayTabulatedFunction(10, 5, 5): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Конструктор 1: pointsCount < 2
        try {
            new ArrayTabulatedFunction(0, 10, 1);
            System.out.println("ОШИБКА: ArrayTabulatedFunction(0, 10, 1) не выбросил исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("ArrayTabulatedFunction(0, 10, 1): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Конструктор 2: leftX >= rightX
        try {
            new ArrayTabulatedFunction(10, 5, new double[]{1, 2, 3});
            System.out.println("ОШИБКА: ArrayTabulatedFunction(10, 5, [...]) не выбросил исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("ArrayTabulatedFunction(10, 5, [...]): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Конструктор 2: массив длины < 2
        try {
            new ArrayTabulatedFunction(0, 10, new double[]{1});
            System.out.println("ОШИБКА: ArrayTabulatedFunction(0, 10, [один элемент]) не выбросил исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("ArrayTabulatedFunction(0, 10, [один элемент]): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        System.out.println("\n Тест исключений конструкторов LinkedListTabulatedFunction ");
        
        // Конструктор 1: leftX >= rightX
        try {
            new LinkedListTabulatedFunction(10, 5, 5);
            System.out.println("ОШИБКА: LinkedListTabulatedFunction(10, 5, 5) не выбросил исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("LinkedListTabulatedFunction(10, 5, 5): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Конструктор 1: pointsCount < 2
        try {
            new LinkedListTabulatedFunction(0, 10, 1);
            System.out.println("ОШИБКА: LinkedListTabulatedFunction(0, 10, 1) не выбросил исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("LinkedListTabulatedFunction(0, 10, 1): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Конструктор 2: leftX >= rightX
        try {
            new LinkedListTabulatedFunction(10, 5, new double[]{1, 2, 3});
            System.out.println("ОШИБКА: LinkedListTabulatedFunction(10, 5, [...]) не выбросил исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("LinkedListTabulatedFunction(10, 5, [...): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
        
        // Конструктор 2: массив длины < 2
        try {
            new LinkedListTabulatedFunction(0, 10, new double[]{1});
            System.out.println("ОШИБКА: LinkedListTabulatedFunction(0, 10, [один элемент]) не выбросил исключение");
        } catch (IllegalArgumentException e) {
            System.out.println("LinkedListTabulatedFunction(0, 10, [один элемент]): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданное исключение: " + e.getClass().getSimpleName());
        }
    }
}