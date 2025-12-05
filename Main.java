import functions.*;

public class Main {
    public static void main(String[] args) {

        //Машинный эпсилон
        double DEFAULT_EPSILON = 1e-9;

        //y = x^2
        double[] values = {2.25, 1, 0.25, 0, 0.25, 1, 2.25, 4, 6.25};
        TabulatedFunction func_array = new ArrayTabulatedFunction(-1.5, 2.5, values);
        TabulatedFunction func_list = new LinkedListTabulatedFunction(-1.5, 2.5, values);

        //---------------------------------------Проверка исключений--------------------------------------------------
        try {
            new LinkedListTabulatedFunction(1.5, -2.5, values);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        try {
            func_array.getPoint(-1);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        try {
            func_list.setPoint(3, new FunctionPoint(2, 3));
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e.getMessage());
        }
        try {
            func_list.addPoint(new FunctionPoint(2, 3));
        } catch (InappropriateFunctionPointException e) {
            System.out.println(e.getMessage());
        }

        //------------------------------------------Проверка функционала------------------------------------------------
        //найдем интерполяционные значения для произвольного набора аргументов и сравним результаты двух реализаций
        double[] arguments = {-2, -1.5, 0.2, 0.3, 1, 1.2, 2.04};
        for (double arg: arguments) {
            if (Math.abs(func_list.getFunctionValue(arg) - func_array.getFunctionValue(arg)) > DEFAULT_EPSILON) {
                System.out.println("Интерполяционные значения не совпали");
            }
        }

        //добавляем точки {0.3;0.09) и {0.1;0,01}
        FunctionPoint p1 = new FunctionPoint(0.3, 0.09);
        FunctionPoint p2 = new FunctionPoint(0.1, 0.01);
        try {
            func_list.addPoint(p1);
            func_list.addPoint(p2);
            func_array.addPoint(p1);
            func_array.addPoint(p2);
        } catch (InappropriateFunctionPointException e) {}
        for (int t = 0; t < func_list.getPointsCount(); t++) {
            if (Math.abs(func_list.getPointX(t) - func_array.getPointX(t)) > DEFAULT_EPSILON) {
                System.out.println("Значения X координат точек не совпали");
            }
            if (Math.abs(func_list.getPointY(t) - func_array.getPointY(t)) > DEFAULT_EPSILON) {
                System.out.println("Значения Y координат точек не совпали");
            }
        }

        // удаляем точки, под индексами 2 и 5
        func_array.deletePoint(2);
        func_array.deletePoint(5);
        func_list.deletePoint(2);
        func_list.deletePoint(5);
        for (int t = 0; t < func_list.getPointsCount(); t++) {
            if (Math.abs(func_list.getPointX(t) - func_array.getPointX(t)) > DEFAULT_EPSILON) {
                System.out.println("Значения X координат точек не совпали");
            }
            if (Math.abs(func_list.getPointY(t) - func_array.getPointY(t)) > DEFAULT_EPSILON) {
                System.out.println("Значения Y координат точек не совпали");
            }
        }

        // изменим значения точки 4 на {0.3;0.09}
        try {
            func_array.setPointX(4, 0.3);
            func_array.setPointY(4, 0.09);
            func_list.setPointX(4, 0.3);
            func_list.setPointY(4, 0.09);
        } catch (InappropriateFunctionPointException e) {
            throw new RuntimeException(e);
        }
        for (int t = 0; t < func_list.getPointsCount(); t++) {
            if (Math.abs(func_list.getPointX(t) - func_array.getPointX(t)) > DEFAULT_EPSILON) {
                System.out.println("Значения X координат точек не совпали");
            }
            if (Math.abs(func_list.getPointY(t) - func_array.getPointY(t)) > DEFAULT_EPSILON) {
                System.out.println("Значения Y координат точек не совпали");
            }
        }
        //------------------------------------------------------------------------------------------------------------
    }
}
