import functions.*;

public class Main {
    public static void main(String[] args) throws Exception {

        double left = 0.0;
        double right = 4.0;
        double[] values = {0.0, 1.0, 4.0, 9.0, 16.0};

        System.out.println("ArrayTabulatedFunction");
        TabulatedFunction arrayFunc =
                new ArrayTabulatedFunction(left, right, values);

        testFunction(arrayFunc);

        System.out.println("\nLinkedListTabulatedFunction");
        TabulatedFunction listFunc =
                new LinkedListTabulatedFunction(left, right, values);

        testFunction(listFunc);

        System.out.println("\n Тест добавления и удаления (LinkedList)");

        LinkedListTabulatedFunction linked =
                new LinkedListTabulatedFunction(left, right, values);

        System.out.println("Начальное количество точек: " + linked.getPointsCount());

        linked.addPoint(new FunctionPoint(2.5, 6.25));
        System.out.println("После addPoint(x=2.5): " + linked.getPointsCount());

        for (int i = 0; i < linked.getPointsCount(); i++) {
            System.out.println("x=" + linked.getPointX(i) +
                               ", y=" + linked.getPointY(i));
        }

        linked.deletePoint(2);
        System.out.println("После deletePoint(2): " + linked.getPointsCount());

        for (int i = 0; i < linked.getPointsCount(); i++) {
            System.out.println("x=" + linked.getPointX(i) +
                               ", y=" + linked.getPointY(i));
        }
    }

    private static void testFunction(TabulatedFunction f) {
        System.out.println("Границы: [" +
                f.getLeftDomainBorder() + ", " +
                f.getRightDomainBorder() + "]");

        System.out.println("Значения в узлах:");
        for (int i = 0; i < f.getPointsCount(); i++) {
            double x = f.getPointX(i);
            System.out.println("x=" + x + ", y=" + f.getPointY(i));
        }

        System.out.println("Значения между узлами:");
        double[] testX = {0.5, 1.5, 2.5, 3.5};
        for (double x : testX) {
            System.out.println("f(" + x + ") = " + f.getFunctionValue(x));
        }

        System.out.println("Проверка исключений:");

        try {
            f.getPoint(-1);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("OK: " + e.getMessage());
        }

        try {
            f.setPointX(1, f.getPointX(0));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("OK: " + e.getMessage());
        }
    }
}
