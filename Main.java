import functions.*;

public class Main {
    public static void printFun(TabulatedFunction fun, int cou){
        for (int i=0; i < cou; i++) { //Вывод значений x и y
            System.out.println("(X, Y) "+ fun.getPointX(i) + " " + fun.getPointY(i));
        }
        System.out.println();
    }
    public static void main(String[] s) throws InappropriateFunctionPointException{
        TabulatedFunction fun = new LinkedListTabulatedFunction(1, 10, 5); //задаем функцию на интервале 1 10, 5 точек

        for (int i=0; i < fun.getPointsCount(); i++) { //для каждого x устанавливаем значение y=2x
            fun.setPointY(i,2 * fun.getPointX(i)); //по индексу получаем x, умножаем на 2 и записываем в y
        }
        System.out.println("Конструктор 1: ");
        printFun(fun, fun.getPointsCount());
        System.out.println("Конструктор 2: ");
        double[] val ={1, 2, 3, 5, 9}; //Проверка второго конструктора
        TabulatedFunction fun1 = new LinkedListTabulatedFunction(1, 10, val); //для каждого x устанавливаем значение из массива
        printFun(fun1, fun1.getPointsCount());

        System.out.println("Левая граница первой функции(y=2x) " + fun.getLeftDomainBorder());
        System.out.println("Правая граница первой функции(y=2x) " + fun.getRightDomainBorder());

        System.out.println("Значение функции(y=2x) в точке x = 1.25, входящей в интервал: " + fun.getFunctionValue(1.25));
        System.out.println("Значение функции(y=2x) в точке x = 8 входящей в интервал: " + fun.getFunctionValue(8));
        System.out.println("Значение функции(y=2x) в точке x = 0 не входящей в интервал: " + fun.getFunctionValue(0));
        System.out.println("Значение функции(y=2x) в точке x = 14 не входящей в интервал: " + fun.getFunctionValue(14));

        System.out.println("Функция, которая возвращает копию второй точки(X, Y): " + fun.getPoint(1).getx() + " " + fun.getPoint(1).gety());

        System.out.println("Заменим вторую точку в начальной функции");
        FunctionPoint p1 = new FunctionPoint(2, 1);
        fun.setPoint(1, p1);
        printFun(fun, fun.getPointsCount());

        System.out.println("Заменим у третьей точки ординату");
        fun.setPointY(2, 16);
        printFun(fun, fun.getPointsCount());

        System.out.println("Заменим у третьей точки абсциссу");
        fun.setPointX(2, 6.6);
        printFun(fun, fun.getPointsCount());

        System.out.println("Удалим третью точку");
        fun.deletePoint(2);
        printFun(fun, fun.getPointsCount());

        System.out.println("Добавим 3 новых точки");
        FunctionPoint p2 = new FunctionPoint(11, 9);
        FunctionPoint p3 = new FunctionPoint(4, 8);
        fun.addPoint(p2);
        fun.addPoint(p3);
        fun.addPoint(new FunctionPoint());
        printFun(fun, fun.getPointsCount());

        System.out.println("Исключения");

        try {
            TabulatedFunction fun2 = new LinkedListTabulatedFunction(1, 1, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("Границы равны");
        }

        try {
            double[] val1 ={1};
            TabulatedFunction fun3 = new LinkedListTabulatedFunction(1, 10, val1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            fun.getPoint(fun.getPointsCount());
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        try {
            fun.setPointY(-1, 5);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        try {
            fun.setPoint(3, new FunctionPoint(12,1));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Выход за границы интервала соседних точек");
        }

        try {
            fun.addPoint(new FunctionPoint(4,1));
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e.getMessage());
        }

        try {
            TabulatedFunction fun4 = new LinkedListTabulatedFunction(1, 10, 2);
            fun4.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
