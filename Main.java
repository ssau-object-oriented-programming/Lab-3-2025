import functions.*;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println(" Testing ArrayTabulatedFunction ");
            TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 10, 5);
            testFunction(arrayFunc);
            System.out.println("\n Testing LinkedListTabulatedFunction ");
            TabulatedFunction listFunc = new LinkedListTabulatedFunction(0, 10, 5);
            testFunction(listFunc);
            testAllExceptions();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }
    private static void testFunction(TabulatedFunction func) {
        System.out.println("\n Initial function state ");
        System.out.println("Type: " + func.getClass().getSimpleName());
        System.out.println("Left border: " + func.getLeftDomainBorder());
        System.out.println("Right border: " + func.getRightDomainBorder());
        System.out.println("Points count: " + func.getPointsCount());
        System.out.println("Initial points:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint point = func.getPoint(i);
            System.out.println("  Point[" + i + "]: (" + point.getX() + ", " + point.getY() + ")");
        }
        System.out.println("\n Testing function values ");
        double testX = 5.0;
        System.out.println("Value at x=" + testX + ": " + func.getFunctionValue(testX));
        System.out.println("Value at x=-1: " + func.getFunctionValue(-1));
        System.out.println("Value at x=11: " + func.getFunctionValue(11));
        System.out.println("\n Testing POINT ADDITION ");
        System.out.println("\n1. Adding point to the END:");
        FunctionPoint newPoint1 = new FunctionPoint(12, 24);
        System.out.println("Adding: (" + newPoint1.getX() + ", " + newPoint1.getY() + ")");
        try {
            func.addPoint(newPoint1);
            System.out.println("Points count after addition: " + func.getPointsCount());
            System.out.println("Last point: (" + func.getPoint(func.getPointsCount() - 1).getX() +
                    ", " + func.getPoint(func.getPointsCount() - 1).getY() + ")");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Caught InappropriateFunctionPointException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getClass().getName() + " - " + e.getMessage());
        }
        System.out.println("\n2. Note: insertPoint method might not be available in TabulatedFunction interface");
        System.out.println("   Testing available methods instead:");
        System.out.println("\nAll points after addition:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint point = func.getPoint(i);
            System.out.println("  Point[" + i + "]: (" + point.getX() + ", " + point.getY() + ")");
        }
        System.out.println("\n Testing POINT DELETION ");
        try {
            System.out.println("\n1. Deleting point at index 0:");
            FunctionPoint firstPoint = func.getPoint(0);
            System.out.println("Deleting point: (" + firstPoint.getX() + ", " + firstPoint.getY() + ")");
            func.deletePoint(0);
            System.out.println("Points count after deletion: " + func.getPointsCount());
            System.out.println("New first point (index 0): (" + func.getPoint(0).getX() +
                    ", " + func.getPoint(0).getY() + ")");
        } catch (Exception e) {
            System.out.println("deletePoint method not available or error: " + e.getMessage());
        }
        System.out.println("\nAll points after operations:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint point = func.getPoint(i);
            System.out.println("  Point[" + i + "]: (" + point.getX() + ", " + point.getY() + ")");
        }
        System.out.println("\n Testing POINT REPLACEMENT ");
        try {
            System.out.println("\n1. Replacing point at index 0:");
            FunctionPoint oldPoint0 = func.getPoint(0);
            FunctionPoint newPoint0 = new FunctionPoint(0, 100);
            System.out.println("Old point: (" + oldPoint0.getX() + ", " + oldPoint0.getY() + ")");
            System.out.println("New point: (" + newPoint0.getX() + ", " + newPoint0.getY() + ")");
            func.setPoint(0, newPoint0);
            System.out.println("After replacement - Point[0]: (" + func.getPoint(0).getX() +
                    ", " + func.getPoint(0).getY() + ")");
        } catch (Exception e) {
            System.out.println("setPoint method not available or error: " + e.getMessage());
        }
        System.out.println("\n--- Final function state ---");
        System.out.println("Points count: " + func.getPointsCount());
        System.out.println("Left border: " + func.getLeftDomainBorder());
        System.out.println("Right border: " + func.getRightDomainBorder());
        System.out.println("\nVerifying X values are sorted:");
        boolean sorted = true;
        for (int i = 1; i < func.getPointsCount(); i++) {
            double x1 = func.getPoint(i - 1).getX();
            double x2 = func.getPoint(i).getX();
            if (x1 >= x2) {
                System.out.println("  ERROR: x[" + (i - 1) + "] = " + x1 + " >= x[" + i + "] = " + x2);
                sorted = false;
            }
        }
        if (sorted) {
            System.out.println("   All X values are properly sorted");
        }
    }
    private static void testAllExceptions() {
        System.out.println("\n Testing all exceptions and special cases ");
        System.out.println("\n1. Constructor exceptions:");
        try {
            System.out.println("  a) Creating function with left > right:");
            TabulatedFunction func = new ArrayTabulatedFunction(10, 0, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("     Caught IllegalArgumentException: " + e.getMessage());
        }
        try {
            System.out.println("  b) Creating function with < 2 points:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("     Caught IllegalArgumentException: " + e.getMessage());
        }
        System.out.println("\n2. Index out of bounds exceptions:");
        try {
            System.out.println("  a) Accessing out of bounds index (getPoint):");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            func.getPoint(5);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("     Caught FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }
        try {
            System.out.println("  b) Deleting at invalid index:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            func.deletePoint(5);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("     Caught FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("     deletePoint method might not be available");
        }
        try {
            System.out.println("  c) Replacing at invalid index:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            func.setPoint(5, new FunctionPoint(15, 30));
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("     Caught FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("     setPoint method might not be available");
        }
        System.out.println("\n3. Logic exceptions:");
        try {
            System.out.println("  a) Deleting when only 2 points remain:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            func.deletePoint(1);
            func.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println("     Caught IllegalStateException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("     deletePoint method might not be available");
        }
        System.out.println("\n4. Duplicate x values exceptions:");
        try {
            System.out.println("  a) Adding point with duplicate X:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            func.addPoint(new FunctionPoint(5, 100));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("     Caught InappropriateFunctionPointException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("     Caught exception: " + e.getClass().getName() + " - " + e.getMessage());
        }
        try {
            System.out.println("  b) Replacing point with X that breaks sorting:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 5);
            FunctionPoint point1 = func.getPoint(1);
            FunctionPoint point3 = func.getPoint(3);
            func.setPoint(2, new FunctionPoint(point1.getX() - 1, 50));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("     Caught InappropriateFunctionPointException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("     Caught exception: " + e.getClass().getName() + " - " + e.getMessage());
        }
        System.out.println("\n5. Special cases:");
        try {
            System.out.println("  a) Adding point at the same X as boundary:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            func.addPoint(new FunctionPoint(0, 50));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("     Caught InappropriateFunctionPointException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("     Caught exception: " + e.getClass().getName() + " - " + e.getMessage());
        }
        try {
            System.out.println("  b) Replacing with same X but different Y:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 5);
            FunctionPoint point2 = func.getPoint(2);
            func.setPoint(2, new FunctionPoint(point2.getX(), point2.getY() + 10));
            System.out.println("     Allowed: Same X with different Y");
        } catch (Exception e) {
            System.out.println("     Unexpected exception: " + e.getClass().getName());
        }
        try {
            System.out.println("  c) Testing with negative index:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);
            func.getPoint(-1);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("     Caught FunctionPointIndexOutOfBoundsException for negative index");
        }
        System.out.println("\n ALL EXCEPTION TESTS COMPLETED ");
    }
}