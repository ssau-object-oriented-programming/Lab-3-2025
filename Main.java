import functions.*;

public class Main {
    public static void main(String[] args) {
        double[] data = {2, 5, 10, 17, 26};

        // тестирование ArrayTabulatedFunction
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(1, 5, data);
        testFunction(arrayFunc, "ArrayTabulatedFunction");

        // тестирование LinkedListTabulatedFunction
        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(1, 5, data);
        testFunction(linkedFunc, "LinkedListTabulatedFunction");

        // тестирование исключений
        testExceptions();
    }

    private static void testFunction(TabulatedFunction func, String type) {
        System.out.println("Тестирование " + type + ":");
        System.out.println("Область определения: " + func.getLeftDomainBorder() + " - " + func.getRightDomainBorder());
        showPoints(func);

        System.out.println("f(3.2) = " + func.getFunctionValue(3.2));
        System.out.println("f(0) = " + func.getFunctionValue(0));

        func.setPointY(1, 7);
        System.out.println("После смены Y:");
        showPoints(func);

        func.deletePoint(2);
        System.out.println("После удаления:");
        showPoints(func);

        try {
            func.addPoint(new FunctionPoint(2.8, 15));
            System.out.println("После добавления:");
            showPoints(func);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при добавлении точки: " + e.getMessage());
        }

        System.out.println();
    }

    private static void showPoints(TabulatedFunction f) {
        System.out.println("Список точек:");
        for (int i = 0; i < f.getPointsCount(); i++) {
            double x = f.getPointX(i);
            double y = f.getPointY(i);
            System.out.println(i + ": x=" + x + " y=" + y);
        }
    }

    private static void testExceptions() {
        System.out.println("Проверка исключений:");

        // неправильные параметры конструкторов
        try {
            new ArrayTabulatedFunction(5, 1, 3);
        } catch (IllegalArgumentException e) {
            System.out.println("ArrayTabulatedFunction: " + e.getMessage());
        }

        try {
            new LinkedListTabulatedFunction(5, 1, 3);
        } catch (IllegalArgumentException e) {
            System.out.println("LinkedListTabulatedFunction: " + e.getMessage());
        }

        try {
            new ArrayTabulatedFunction(1, 5, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("ArrayTabulatedFunction: " + e.getMessage());
        }

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 4, new double[]{1, 2, 3, 4, 5});
        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(0, 4, new double[]{1, 2, 3, 4, 5});

        // выход за границы
        try {
            arrayFunc.getPoint(-1);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Array getPoint(-1): " + e.getMessage());
        }

        try {
            linkedFunc.getPoint(10);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("LinkedList getPoint(10): " + e.getMessage());
        }

        // нарушение порядка точек
        try {
            arrayFunc.setPoint(2, new FunctionPoint(0.5, 10));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Array setPoint: " + e.getMessage());
        }

        try {
            linkedFunc.setPointX(1, 3.5);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("LinkedList setPointX: " + e.getMessage());
        }

        // дублирование точек
        try {
            arrayFunc.addPoint(new FunctionPoint(2.0, 100));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Array addPoint: " + e.getMessage());
        }

        // удаление при недостаточном количестве точек
        TabulatedFunction minArrayFunc = new ArrayTabulatedFunction(0, 1, new double[]{1, 2});
        try {
            minArrayFunc.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("Array deletePoint: " + e.getMessage());
        }
    }
}