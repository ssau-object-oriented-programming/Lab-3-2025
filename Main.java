import functions.FunctionPoint;
import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.FunctionPointIndexOutOfBoundsException;
import functions.InappropriateFunctionPointException;

public class Main {
    public static void main(String[] args) {
        // Создаем функцию f(x) = 3x + 2 на интервале [1, 5] с 4 точками
        TabulatedFunction f = new ArrayTabulatedFunction(1, 5, 4);
        for (int i = 0; i < f.getPointsCount(); i++) {
            // Для каждого x устанавливаем значение y = 3x + 2
            f.setPointY(i, 3 * f.getPointX(i) + 2);
        }

        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("x,y "+ f.getPointX(i) + " " + f.getPointY(i));
        }

        double[] val = {5, 8, 11, 14}; // Значения y = 3x + 2 для x = 1,2,3,4
        TabulatedFunction fun1 = new LinkedListTabulatedFunction(1, 4, val);
        for (int i = 0; i < fun1.getPointsCount(); i++) {
            System.out.println("x,y "+ fun1.getPointX(i) + " " + fun1.getPointY(i));
        }

        System.out.println("Левая граница " + f.getLeftDomainBorder());
        System.out.println("Правая граница " + f.getRightDomainBorder());

        System.out.println("Значение функции в точке x = 1.5 входящей в интервал: " + f.getFunctionValue(1.5));
        System.out.println("Значение функции в точке x = 3.5 входящей в интервал: " + f.getFunctionValue(3.5));
        System.out.println("Значение функции в точке x = 0 не входящей в интервал: " + f.getFunctionValue(0));
        System.out.println("Значение функции в точке x = 6 не входящей в интервал: " + f.getFunctionValue(6));

        System.out.println("Возвращение копии второй точки " + f.getPoint(1).getX() + " " + f.getPoint(1).getY());

        System.out.println("Замена второй точки в начальной функции (вне интервала)");
        FunctionPoint p1 = new FunctionPoint(0.5, 10); // x=0.5 вне интервала [1,5]
        try {
            f.setPoint(1, p1);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при установке точки: " + e.getMessage());
        }
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("x,y "+ f.getPointX(i) + " " + f.getPointY(i));
        }

        System.out.println("Замена 2 точки в начальной функции");
        FunctionPoint p2 = new FunctionPoint(2.5, 12);
        try {
            f.setPoint(1, p2);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при установке точки: " + e.getMessage());
        }
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("x,y "+ f.getPointX(i) + " " + f.getPointY(i));
        }

        System.out.println("Замена абсциссы 3 точки");
        System.out.println("Замена абсциссы 3 точки");
        try {
            f.setPointX(2, 3.8);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при установке X: " + e.getMessage());
        }
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("x,y "+ f.getPointX(i) + " " + f.getPointY(i));
        }

        System.out.println("Замена ординаты 2 точки");
        f.setPointY(1, 15);
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("x,y "+ f.getPointX(i) + " " + f.getPointY(i));
        }

        System.out.println("Удаление 3 точки");
        f.deletePoint(2);
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("x,y "+ f.getPointX(i) + " " + f.getPointY(i));
        }

        System.out.println("Добавление 2 новых точек");
        FunctionPoint p3 = new FunctionPoint(2.2, 9);
        FunctionPoint p4 = new FunctionPoint(3.5, 13);
        try {
            f.addPoint(p3);
            f.addPoint(p4);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при добавлении точки: " + e.getMessage());
        }
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("x,y "+ f.getPointX(i) + " " + f.getPointY(i));
        }

        System.out.println("тест исключений");

        System.out.println("Исключения конструкторов:");
        System.out.println("Задали границы 8 и 3:");
        try {
            TabulatedFunction f2 = new LinkedListTabulatedFunction(8, 3, 6);
        } catch (IllegalArgumentException e) {
            System.out.println("Левая граница больше правой: 8 > 3");
        }
        System.out.println("Передали только 1 элемент:");
        try {
            double[] val1 = {7};
            TabulatedFunction f3 = new LinkedListTabulatedFunction(2, 9, val1);
        } catch (IllegalArgumentException e) {
            System.out.println("Массив содержит только 1 элемент");
        }
        System.out.println("Остальные исключения:");
        try {
            f.getPoint(8);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Индекс 8 превышает количество точек");
        }

        try {
            f.addPoint(new FunctionPoint(2.5, 8));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Точка с X=2.5 уже существует");
        }
        System.out.println("Задали 3 точки:");
        try {
            TabulatedFunction f4 = new LinkedListTabulatedFunction(0, 6, 3);
            f4.deletePoint(1);
            f4.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("Нельзя удалить точку - останется меньше 2 точек");
        }
        System.out.println("Поменяли абсциссу первой точки:");
        try {
            f.setPointX(1, 0.8);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("X=0.8 меньше предыдущей точки");
        }
        System.out.println("Удаление 10 точки:");
        try {
            f.deletePoint(10);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Индекс 10 для удаления не существует");
        }
    }
}