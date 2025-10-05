import functions.*;

import java.util.regex.Pattern;

public class Main {
    public static final double deltaX = 0.1;
    public static final double initialX = -1;
    public static final double endX = 3;
    public static void main(String[] args) {
        double[] arr = {0,1,4,9,16,25,36,49,64,81,100,121};

        TabulatedFunction f = new ArrayTabulatedFunction(0, 10, arr);
        TabulatedFunction f2 = new LinkedListTabulatedFunction(0, 10, arr);

        display(f);
        System.out.println("============");
        display(f2);

        System.out.println("============");
        System.out.println("============");
        System.out.println("============");

        test(f);
        System.out.println("============");
        test(f2);

        System.out.println("============");
        System.out.println("============ GET/SET X/Y");
        System.out.println("============");
        f = new ArrayTabulatedFunction(0, 10, arr); // жестокий способ откатить данные, но какой есть
        f2 = new LinkedListTabulatedFunction(0, 10, arr);

        test2(f);
        System.out.println("============");
        test2(f2);

        System.out.println("============");
        System.out.println("============ DELETE");
        System.out.println("============");
        f = new ArrayTabulatedFunction(0, 10, arr); // жестокий способ откатить данные, но какой есть
        f2 = new LinkedListTabulatedFunction(0, 10, arr);

        test3(f);
        System.out.println("============");
        test3(f2);

        System.out.println("============");
        System.out.println("============ ADD");
        System.out.println("============");
        f = new ArrayTabulatedFunction(0, 10, arr); // жестокий способ откатить данные, но какой есть
        f2 = new LinkedListTabulatedFunction(0, 10, arr);

        test4(f);
        System.out.println("============");
        test4(f2);

        System.out.println("============");
        System.out.println("============ size");
        System.out.println("============");

        System.out.println(f.getPointsCount());
        System.out.println("============");
        System.out.println(f2.getPointsCount());
    }
    public static void test4(TabulatedFunction f) {
        System.out.println("Test 1");
        try {
            f.addPoint(null);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("IndexOf error");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Inappropriate error");
        } catch (Exception e) {
            System.out.println("Another error");
        }
        try {
            f.addPoint(new FunctionPoint(0,0));
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("IndexOf error");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Inappropriate error");
        } catch (Exception e) {
            System.out.println("Another error");
        }
        try {
            f.addPoint(new FunctionPoint(500,0));
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("IndexOf error");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Inappropriate error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void test3(TabulatedFunction f) {
        System.out.println("Test 1");
        try {
            f.deletePoint(-1);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("IndexOf error");
        } catch (IllegalStateException e) {
            System.out.println("Illegal state error");
        }
        try {
            f.deletePoint(0);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("IndexOf error");
        } catch (IllegalStateException e) {
            System.out.println("Illegal state error");
        }
        try {
            for (int i = 0; i < 20; i++) f.deletePoint(0);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("IndexOf error");
        } catch (IllegalStateException e) {
            System.out.println("Illegal state error");
        }
    }
    public static void test2(TabulatedFunction f) {
        System.out.println("Test 1");
        try {
            f.getPointX(1);
            System.out.println("Passed");
        } catch (Exception e) {
            System.out.println("Non right error");
        }
        try {
            f.getPointX(-1);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Right error");
        }
        System.out.println("Test 2");
        try {
            f.setPointX(0,0);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Index error");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Inappropriate error");
        }
        try {
            f.setPointX(-1,0);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Index error");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Inappropriate error");
        }
        try {
            f.setPointX(0,50);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Index error");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Inappropriate error");
        }
        System.out.println("Test 3");
        try {
            f.getPointY(1);
            System.out.println("Passed");
        } catch (Exception e) {
            System.out.println("Non right error");
        }
        try {
            f.getPointY(-1);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Right error");
        }
        System.out.println("Test 4");
        try {
            f.setPointY(0,0);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Index error");
        }
        try {
            f.setPointY(-1,0);
            System.out.println("Passed");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Index error");
        }
    }
    public static void test(TabulatedFunction f) {
        System.out.println("Test 1");
        for (int i = 0; i >= -1; i--) {
            try {
                f.getPoint(i);
                System.out.println("Passed");
            } catch (FunctionPointIndexOutOfBoundsException err) {
                System.out.println("Right error");
            } catch (Exception err) {
                System.out.println("Another error");
            }
        }
        System.out.println("Test 2");
        for (int i = 0; i >= -1; i--) {
            try {
                f.setPoint(i, null);
                System.out.println("Passed");
            } catch (InappropriateFunctionPointException err) {
                System.out.println("Inappropriate error");
            } catch (FunctionPointIndexOutOfBoundsException err) {
                System.out.println("Right error");
            } catch (Exception err) {
                System.out.println("Another error");
            }
        }
        System.out.println("Test 3");
        for (int i = 2; i >= -1; i--) {
            try {
                f.setPoint(i, new FunctionPoint(0, 0));
                System.out.println("Passed");
            } catch (FunctionPointIndexOutOfBoundsException err) {
                System.out.println("IndexOf error");
            } catch (InappropriateFunctionPointException err) {
                System.out.println("Inappropriate error");
            } catch (Exception err) {
                System.out.println("Another error");
            }
        }
    }
    public static void display(TabulatedFunction tf) {
        double currentX = initialX;
        for (; currentX <= endX; currentX += deltaX) {
            System.out.print("~(" + (Math.round(currentX * 100) / 100d) + ") -> ");
            System.out.println(!Double.isNaN(tf.getFunctionValue(currentX)) ? Math.round(tf.getFunctionValue(currentX) * 100) / 100d : Double.NaN);
        }
    }
}
