import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.FunctionPoint;
import functions.TabulatedFunction;


public class Main {
    public static void main(String[] args) {

        //TabulatedFunction f = new ArrayTabulatedFunction(0, 4, new double[]{0, 1, 4, 9, 16});
         TabulatedFunction f = new LinkedListTabulatedFunction(0, 4, new double[]{0, 1, 4, 9, 16});

        System.out.println("Исходная табулированная функция:");
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("(" + f.getPoint(i).getX() + ", " + f.getPoint(i).getY() + ")");
        }


        System.out.println("\nПроверка значений функции:");
        double[] testX = {-1, 0, 1.5, 2, 2.5, 3.7, 5};
        for (double x : testX) {
            System.out.println("f(" + x + ") = " + f.getFunctionValue(x));
        }
        System.out.println("\nПроверка методов добавления, изменения и удаления для LinkedListTabulatedFunction:");

        try {
            // Добавление новой точки
            System.out.println("Добавляем точку (5, 25):");
            f.addPoint(new FunctionPoint(5, 25));

            // Изменение значения существующей точки
            System.out.println("Изменяем значение второй точки:");
            FunctionPoint p = f.getPoint(1);
            f.setPointY(1, p.getY() + 10);  // прибавим 10 к y второй точки

            // Удаление точки
            System.out.println("Удаляем первую точку:");
            f.deletePoint(0);

            // Выводим текущее состояние точек
            System.out.println("Текущее состояние точек после операций:");
            for (int i = 0; i < f.getPointsCount(); i++) {
                System.out.println("(" + f.getPoint(i).getX() + ", " + f.getPoint(i).getY() + ")");
            }

        } catch (Exception e) {
            System.out.println("Возникло исключение при проверке LinkedListTabulatedFunction: " + e);
        }



        System.out.println("\nПроверка исключений:");

        try {
            f.getPoint(-1);
        } catch (Exception e) {
            System.out.println("Ожидаемое исключение (неверный индекс): " + e);
        }

        try {
            f.addPoint(new FunctionPoint(2, 100));
        } catch (Exception e) {
            System.out.println("Ожидаемое исключение (существует x): " + e);
        }

        try {
            while (f.getPointsCount() > 2) {
                f.deletePoint(0);
            }
            f.deletePoint(0);
        } catch (Exception e) {
            System.out.println("Ожидаемое исключение (нельзя удалить, останется < 3 точек): " + e);
        }



        System.out.println("\nФинальное состояние точек:");
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("(" + f.getPoint(i).getX() + ", " + f.getPoint(i).getY() + ")");
        }

        System.out.println("\nРабота программы завершена.");
    }
}

