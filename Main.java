import functions.*;
public class Main {
    public static void main(String[] args) {
        try {
            TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 10, 5);
            testFunction(arrayFunc);
            TabulatedFunction listFunc = new LinkedListTabulatedFunction(0, 10, 5);
            testFunction(listFunc);
            testExceptions();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }

    private static void testFunction(TabulatedFunction func) {
        System.out.println("Testing function:");
        System.out.println("Left border: " + func.getLeftDomainBorder());
        System.out.println("Right border: " + func.getRightDomainBorder());
        System.out.println("Points count: " + func.getPointsCount());

        double testX = 5.0;
        System.out.println("Value at x=" + testX + ": " + func.getFunctionValue(testX));

        System.out.println("Value at x=-1: " + func.getFunctionValue(-1));
        System.out.println("Value at x=11: " + func.getFunctionValue(11));
    }

    private static void testExceptions() {
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(10, 0, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught IllegalArgumentException: " + e.getMessage());
        }

        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught IllegalArgumentException: " + e.getMessage());
        }

        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            func.getPoint(5);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Caught FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }

        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            func.deletePoint(1);
            func.deletePoint(0); 
        } catch (IllegalStateException e) {
            System.out.println("Caught IllegalStateException: " + e.getMessage());
        }
    }
}
