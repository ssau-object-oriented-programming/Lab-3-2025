import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("_ArrayTabulatedFunction_");
        testingFunction(new ArrayTabulatedFunction(0, 5, new double[]{0, 1, 8, 27, 64, 125}));

        System.out.println("\n_LinkedListTabulatedFunction_");
        testingFunction(new LinkedListTabulatedFunction(0, 5, new double[]{0, 1,8, 27, 64, 125}));

        System.out.println("\n_Exception test_");
        testingExceptions();
    }

    public static void testingFunction(TabulatedFunction func) {
        System.out.println("y = x^3, x = [0,5]");
        printFunctionInfo(func);
        
        System.out.println("\ncalculating values");
        double[] testPoints = {-1, 0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 5};
        for (double x : testPoints) {
            double result = func.getFunctionValue(x);
            if (Double.isNaN(result)) {
                System.out.printf("f(%.1f) = not defined%n", x);
            } else {
                System.out.printf("f(%.1f) = %.2f%n", x, result);
            }
        }
        
        System.out.println("\nadding points");
        try {
            func.addPoint(new FunctionPoint(0.5, 0.125));
            func.addPoint(new FunctionPoint(2.5, 15.625));
            System.out.println("result:");
            printFunctionInfo(func);
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println("\ncheck");
        for (double x : new double[]{0.5, 2.5}) {
            System.out.printf("f(%.1f) = %.3f%n", x, func.getFunctionValue(x));
        }
        
        System.out.println("\npoint changing");
        func.setPointY(2, 5.0);
        System.out.println("result:");
        printFunctionInfo(func);
        System.out.printf("f(%.1f) = %.3f%n", func.getPointX(2), func.getFunctionValue(func.getPointX(2)));
        
        System.out.println("\npoint deleting");
        func.deletePoint(3);
        System.out.println("result:");
        printFunctionInfo(func);
        
    }
    
    private static void printFunctionInfo(TabulatedFunction func) {
        System.out.println("Область определения: [" + func.getLeftDomainBorder() + ", " + func.getRightDomainBorder() + "]");
        System.out.println("Количество точек: " + func.getPointsCount());
        System.out.print("Точки: ");
        for (int i = 0; i < func.getPointsCount(); i++) {
            System.out.print("(" + func.getPointX(i) + "; " + func.getPointY(i) + ")");
            if (i < func.getPointsCount() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    private static void testingExceptions() {
        System.out.println("_invalid parameters test_");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(5, 0, 5);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 5, 1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("_invalid index test_");
        TabulatedFunction func = new ArrayTabulatedFunction(0, 5, new double[]{0, 1, 8, 27, 64, 125});
        try {
            func.getPointX(-1);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        try {
            func.getPointY(10);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("_invalid operations test_");
        try {
            func.setPoint(1, new FunctionPoint(0, 100));
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e.getMessage());
        }

        try {
            func.addPoint(new FunctionPoint(2, 100));
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("_invalid delete operation test_");
        TabulatedFunction deletedFunc = new ArrayTabulatedFunction(0, 1, new double[]{0, 1});
        try {
            deletedFunc.deletePoint(0);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("_point null test_");
        try {
            func.addPoint(null);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e.getMessage());
        }
    }
}
