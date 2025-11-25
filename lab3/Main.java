import functions.*;

public class Main {
    public static void main(String[] args) {
        double left = -2;
        double right = 3;
        int count = 6;
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(left, right, count);
        System.out.println("ArrayTabulatedFunction");
        System.out.println("Функция f(x) = 2x² + 3x - 1");
        System.out.println("Левая граница функции: " + arrayFunc.getLeftDomainBorder());
        System.out.println("Правая граница функции: " + arrayFunc.getRightDomainBorder());
        for (int i = 0; i < arrayFunc.getPointsCount(); i++) {
            double x = arrayFunc.getPointX(i);
            arrayFunc.setPointY(i, 2*x*x + 3*x - 1);
        }
        System.out.println("\nТочки ArrayTabulatedFunction:");
        for (int i = 0; i < arrayFunc.getPointsCount(); i++) {
            System.out.println("(" + arrayFunc.getPointX(i) + "; " + arrayFunc.getPointY(i) + ")");
        }
        System.out.println("\nПроверка значений ArrayTabulatedFunction:");
        for (double x = -3; x <= 4; x += 0.5) {
            System.out.println("f(" + x + ") = " + arrayFunc.getFunctionValue(x));
        }
        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(left, right, count);
        System.out.println("\nLinkedListTabulatedFunction");
        System.out.println("Функция f(x) = 2x² + 3x - 1");
        System.out.println("Левая граница функции: " + linkedFunc.getLeftDomainBorder());
        System.out.println("Правая граница функции: " + linkedFunc.getRightDomainBorder());
        for (int i = 0; i < linkedFunc.getPointsCount(); i++) {
            double x = linkedFunc.getPointX(i);
            linkedFunc.setPointY(i, 2*x*x + 3*x - 1);
        }
        System.out.println("\nТочки LinkedListTabulatedFunction:");
        for (int i = 0; i < linkedFunc.getPointsCount(); i++) {
            System.out.println("(" + linkedFunc.getPointX(i) + "; " + linkedFunc.getPointY(i) + ")");
        }
        System.out.println("\nПроверка значений LinkedListTabulatedFunction:");
        for (double x = -3; x <= 4; x += 0.5) {
            System.out.println("f(" + x + ") = " + linkedFunc.getFunctionValue(x));
        }
        double[] values = {1, -2, -1, 4, 13, 26};
        TabulatedFunction func1 = new ArrayTabulatedFunction(-2, 3, values);
        System.out.println("\nArrayTabulatedFunction с массивом значений");
        System.out.println("Функция создана с массивом значений:");
        for (int i = 0; i < func1.getPointsCount(); i++) {
            System.out.println("x=" + func1.getPointX(i) + ", y=" + func1.getPointY(i));
        }
        TabulatedFunction func2 = new LinkedListTabulatedFunction(-2, 3, values);
        System.out.println("\nLinkedListTabulatedFunction с массивом значений");
        System.out.println("Функция создана с массивом значений:");
        for (int i = 0; i < func2.getPointsCount(); i++) {
            System.out.println("x=" + func2.getPointX(i) + ", y=" + func2.getPointY(i));
        }
        System.out.println("\nОперации с ArrayTabulatedFunction");
        System.out.println("Изменяем значение y третьей точки:");
        try {
            arrayFunc.setPoint(2, new FunctionPoint(0, -1));
            System.out.println("Новая точка 2: x = " + arrayFunc.getPointX(2) + ", y = " + arrayFunc.getPointY(2));
        } catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при изменении точки: " + e.getMessage());
        }
        System.out.println("\nДобавляем новую точку (x=1.5, y=8):");
        try {
            arrayFunc.addPoint(new FunctionPoint(1.5, 8));
            System.out.println("Точки ArrayTabulatedFunction после добавления:");
            for (int i = 0; i < arrayFunc.getPointsCount(); i++) {
                System.out.println("(" + arrayFunc.getPointX(i) + "; " + arrayFunc.getPointY(i) + ")");
            }
        } catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при добавлении точки: " + e.getMessage());
        }

        System.out.println("\nОперации с LinkedListTabulatedFunction");
        System.out.println("Изменяем значение y третьей точки:");
        try {
            linkedFunc.setPoint(2, new FunctionPoint(0, -1));
            System.out.println("Новая точка 2: x = " + linkedFunc.getPointX(2) + ", y = " + linkedFunc.getPointY(2));
        } catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при изменении точки: " + e.getMessage());
        }
        System.out.println("\nДобавляем новую точку (x=1.5, y=8):");
        try {
            linkedFunc.addPoint(new FunctionPoint(1.5, 8));
            System.out.println("Точки LinkedListTabulatedFunction после добавления:");
            for (int i = 0; i < linkedFunc.getPointsCount(); i++) {
                System.out.println("(" + linkedFunc.getPointX(i) + "; " + linkedFunc.getPointY(i) + ")");
            }
        } catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при добавлении точки: " + e.getMessage());
        }
        System.out.println("\nНаходим значение y для 2.5 с помощью линейной интерполяции:");
        double valueAt25Array = arrayFunc.getFunctionValue(2.5);
        double valueAt25Linked = linkedFunc.getFunctionValue(2.5);
        System.out.println("ArrayTabulatedFunction f(2.5) = " + valueAt25Array);
        System.out.println("LinkedListTabulatedFunction f(2.5) = " + valueAt25Linked);
        System.out.println("\nУдаляем четвертую точку:");
        try {
            arrayFunc.deletePoint(3);
            System.out.println("Точки ArrayTabulatedFunction после удаления:");
            for (int i = 0; i < arrayFunc.getPointsCount(); i++) {
                System.out.println("(" + arrayFunc.getPointX(i) + "; " + arrayFunc.getPointY(i) + ")");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении точки: " + e.getMessage());
        }
        try {
            linkedFunc.deletePoint(3);
            System.out.println("Точки LinkedListTabulatedFunction после удаления:");
            for (int i = 0; i < linkedFunc.getPointsCount(); i++) {
                System.out.println("(" + linkedFunc.getPointX(i) + "; " + linkedFunc.getPointY(i) + ")");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении точки: " + e.getMessage());
        }
        testExceptionsWithInterface();
    }
    private static void testExceptionsWithInterface() {
        System.out.println("Проверка исключений");
        System.out.println("ArrayTabulatedFunction:");
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 5, 3);
        testSimpleExceptions(arrayFunc);
        System.out.println("LinkedListTabulatedFunction:");
        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(0, 5, 3);
        testSimpleExceptions(linkedFunc);
        System.out.println("Проверка исключений в конструкторах:");
        testConstructorExceptions();
    }
    private static void testSimpleExceptions(TabulatedFunction func) {
        try {
            func.getPoint(10);
            System.out.println("Ошибка: не выброшено FunctionPointIndexOutOfBoundsException");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }
        try {
            func.addPoint(new FunctionPoint(2.0, 5.0));
            func.addPoint(new FunctionPoint(2.0, 10.0)); // Дубликат X
            System.out.println("Ошибка: не выброшено InappropriateFunctionPointException");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("InappropriateFunctionPointException: " + e.getMessage());
        }
        try {
            TabulatedFunction tempFunc;
            if (func instanceof ArrayTabulatedFunction) {
                tempFunc = new ArrayTabulatedFunction(0, 2, 3);
            } else {
                tempFunc = new LinkedListTabulatedFunction(0, 2, 3);
            }
            tempFunc.deletePoint(0);

            tempFunc.deletePoint(0); // Должно остаться 1 точка - ошибка
            System.out.println("Ошибка: не выброшено IllegalStateException");
        } catch (IllegalStateException e) {
            System.out.println("IllegalStateException: " + e.getMessage());
        }
        System.out.println();
    }
    private static void testConstructorExceptions() {
        try {
            new ArrayTabulatedFunction(5, 3, 4); // Левая > правой
            System.out.println("Ошибка: не выброшено IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException (левая > правой): " + e.getMessage());
        }
        try {
            new LinkedListTabulatedFunction(0, 5, 1); // Точек < 2
            System.out.println("Ошибка: не выброшено IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException (точек < 2): " + e.getMessage());
        }
    }
}



