import functions.*;

public class Main {
    public static void main(String[] args) {

        // 0) Конструкторы: IllegalArgumentException
        try { new ArrayTabulatedFunction(2, 2, 5); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(Array): левая >= правая  " + e.getMessage());
        }
        try { new ArrayTabulatedFunction(0, 1, 1); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(Array): pointsCount < 2  " + e.getMessage());
        }
        try { new LinkedListTabulatedFunction(3, 1, 4); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(List): левая >= правая  " + e.getMessage());
        }
        try { new LinkedListTabulatedFunction(0, 1, 1); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(List): pointsCount < 2  " + e.getMessage());
        }
        // Конструктор с массивом значений: values.length < 2
        try { new ArrayTabulatedFunction(0, 1, new double[]{42}); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(Array): values.length < 2  " + e.getMessage());
        }
        try { new LinkedListTabulatedFunction(0, 1, new double[]{13}); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(List): values.length < 2  " + e.getMessage());
        }

        // 1) Базовые проверки на массивной реализации (y = 5x + 10)
        TabulatedFunction f = new ArrayTabulatedFunction(0, 10, 6);
        for (int i = 0; i < f.getPointsCount(); ++i) {
            double x = f.getPointX(i);
            f.setPointY(i, 5 * x + 10);
        }

        System.out.println("\n ArrayTabulatedFunction: исходные точки (y = 5x+10) ");
        for (int i = 0; i < f.getPointsCount(); ++i) {
            System.out.printf("точка %d: x=%.2f, y=%.2f%n", i, f.getPointX(i), f.getPointY(i));
        }
        System.out.printf("границы: [%.2f; %.2f]%n", f.getLeftDomainBorder(), f.getRightDomainBorder());

        // Интерполяция + вне диапазона
        System.out.println("\nЗначения функции:");
        double[] probe = {-1.0, 0.0, 1.0, 3.0, 5.5, 10.0, 11.0};
        for (double x : probe) {
            double y = f.getFunctionValue(x);
            System.out.printf("f(%4.1f) = %s%n", x, Double.isNaN(y) ? "NaN" : String.format("%.2f", y));
        }

        // Геттеры/сеттеры
        System.out.println("\nТест геттеров/сеттеров:");
        System.out.printf("до setPointY: y[2]=%.2f%n", f.getPointY(2));
        f.setPointY(2, 999.0);
        System.out.printf("после setPointY: y[2]=%.2f%n", f.getPointY(2));

        try {
            double leftX = f.getPointX(1);
            double rightX = f.getPointX(3);
            double mid = (leftX + rightX) / 2.0;
            f.setPointX(2, mid);
            System.out.printf("после setPointX: x[2]=%.2f (между %.2f и %.2f)%n", f.getPointX(2), leftX, rightX);
        } catch (FunctionPointIndexOutOfBoundsException | InappropriateFunctionPointException e) {
            System.out.println("Ошибка: корректный setPointX не прошёл " + e.getMessage());
        }

        try {
            double tooBig = f.getPointX(3) + 100.0;
            f.setPointX(2, tooBig);
            System.out.println("Ошибка: ожидалось исключение в setPointX(ломаем порядок)!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(setPointX): " + e.getMessage());
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Не тот тип исключения (ожидался Inappropriate): " + e.getMessage());
        }

        try {
            f.setPoint(1, new FunctionPoint(1.0, 5 * 1.0 + 10));
            System.out.printf("после setPoint: #1 -> x=%.2f, y=%.2f%n", f.getPointX(1), f.getPointY(1));
        } catch (FunctionPointIndexOutOfBoundsException | InappropriateFunctionPointException e) {
            System.out.println("Ошибка: корректный setPoint не прошёл " + e.getMessage());
        }

        try {
            double nextX = f.getPointX(2);
            f.setPoint(1, new FunctionPoint(nextX + 0.01, 123.0));
            System.out.println("Ошибка: ожидалось исключение в setPoint(ломаем порядок)!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(setPoint): " + e.getMessage());
        }

        try {
            f.addPoint(new FunctionPoint(7.5, 5 * 7.5 + 10));
            System.out.println("после addPoint(7.5, f(7.5)) количество = " + f.getPointsCount());
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка: корректный addPoint не прошёл " + e.getMessage());
        }

        try {
            double dupX = f.getPointX(0);
            f.addPoint(new FunctionPoint(dupX, 111.0));
            System.out.println("Ошибка: ожидалось исключение addPoint(дубликат х)!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(addPoint дубликат): " + e.getMessage());
        }

        // Удаление: до минимально допустимого количества (2) и ещё раз — IllegalStateException
        try {
            f.deletePoint(0);
            System.out.println("после deletePoint(0) количество = " + f.getPointsCount());
        } catch (FunctionPointIndexOutOfBoundsException | IllegalStateException e) {
            System.out.println("Ошибка: deletePoint(0) не прошёл — " + e.getMessage());
        }
        try {
            while (f.getPointsCount() > 2) f.deletePoint(0);
            f.deletePoint(0); // должно упасть
            System.out.println("Ошибка: ожидалось IllegalStateException при удалении при 2 точках!");
        } catch (IllegalStateException e) {
            System.out.println(" IllegalState(delete при <3 точках): " + e.getMessage());
        }

        // Индексы: отрицательный и сильно большой
        try { f.getPoint(-1); System.out.println("Ошибка: ожидалось FPIndexOOBE в getPoint(-1)!"); }
        catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(" FPIndexOOBE(getPoint -1): " + e.getMessage());
        }
        try { f.getPoint(99); System.out.println("Ошибка: ожидалось FPIndexOOBE в getPoint(99)!"); }
        catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(" FPIndexOOBE(getPoint 99): " + e.getMessage());
        }

        // 2) Короткий прогон для LinkedListTabulatedFunction (аналогичные проверки)
        try {
            TabulatedFunction g = new LinkedListTabulatedFunction(-2, 4, 4); // X = {-2,0,2,4}
            for (int i = 0; i < g.getPointsCount(); i++) {
                double x = g.getPointX(i);
                g.setPointY(i, 3 * x * x - 2); // y = 3x^2 - 2
            }

            System.out.println("\n LinkedListTabulatedFunction: точки");
            for (int i = 0; i < g.getPointsCount(); i++) {
                System.out.printf("точка %d: x=%.2f, y=%.2f%n", i, g.getPointX(i), g.getPointY(i));
            }

            System.out.println(" Значения в выборочных x ");
            double[] xs = {-3, -1, 0, 1.5, 5};
            for (double x : xs) {
                double y = g.getFunctionValue(x);
                System.out.printf("g(%4.1f) = %s%n", x, Double.isNaN(y) ? "NaN" : String.format("%.2f", y));
            }

            // Исключения
            try {
                g.addPoint(new FunctionPoint(g.getPointX(1), 0)); // дубликат X
                System.out.println("Ошибка: ожидалось исключение addPoint(дубликат) у списка!");
            } catch (InappropriateFunctionPointException e) {
                System.out.println(" Inappropriate(list/addPoint дубликат): " + e.getMessage());
            }

            try {
                g.setPointX(1, g.getPointX(2) + 10); // ломаем порядок
                System.out.println("Ошибка: ожидалось исключение setPointX(некорректный) у списка!");
            } catch (InappropriateFunctionPointException e) {
                System.out.println(" Inappropriate(list/setPointX): " + e.getMessage());
            }

            try { g.getPoint(999); System.out.println("Ошибка: ожидалось FPIndexOOBE(list/getPoint 999)!"); }
            catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println(" FPIndexOOBE(list/getPoint 999): " + e.getMessage());
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка конструктора LinkedListTabulatedFunction: " + e.getMessage());
        }
    }
}
