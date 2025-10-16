import functions.*;

public class Main {
    public static void main(String[] args) throws InappropriateFunctionPointException {

        //Проверка ArrayTabulatedFunction
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(-1.0, 1.0, 5);
        System.out.println("Исходные точки:");
        arrayFunction.printTabulatedFunction();

        // Замена точки
        FunctionPoint newPoint = new FunctionPoint(0.55, 0.25);
        arrayFunction.setPoint(3, newPoint);
        System.out.println("После замены точки:");
        arrayFunction.printTabulatedFunction();

        // Добавление новой точки
        FunctionPoint addedPoint1 = new FunctionPoint(0.7, 0.3);
        arrayFunction.addPoint(addedPoint1);
        System.out.println("После добавления точки:");
        arrayFunction.printTabulatedFunction();

        // Удаление точки
        arrayFunction.deletePoint(3);
        System.out.println("После удаления точки:");
        arrayFunction.printTabulatedFunction();

        // Проверка выбросов исключений
        try {
            arrayFunction.getPoint(100);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(" Поймано исключение: " + e);
        }

        try {
            arrayFunction.setPointX(2, 10);
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Поймано исключение: " + e);
        }

        try {
            arrayFunction.addPoint(addedPoint1); // Повторная точка по X
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Поймано исключение: " + e);
        }

        //Проверка LinkedListTabulatedFunction
        TabulatedFunction listFunction = new LinkedListTabulatedFunction(-1.0, 1.0, 5);
        System.out.println("Исходные точки:");
        listFunction.printTabulatedFunction();

        // Замена точки
        FunctionPoint newPoint1 = new FunctionPoint(0.55, 0.25);
        listFunction.setPoint(3, newPoint1);
        System.out.println("После замены точки:");
        listFunction.printTabulatedFunction();

        // Добавление новой точки
        FunctionPoint addedPoint2 = new FunctionPoint(0.7, 0.3);
        listFunction.addPoint(addedPoint2);
        System.out.println("После добавления точки:");
        listFunction.printTabulatedFunction();

        // Удаление точки
        listFunction.deletePoint(3);
        System.out.println("После удаления точки:");
        listFunction.printTabulatedFunction();

        // Проверка выбросов исключений
        try {
            listFunction.getPoint(100);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(" Поймано исключение: " + e);
        }

        try {
            listFunction.setPointX(2, 10);
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Поймано исключение: " + e);
        }

        try {
            listFunction.addPoint(addedPoint2); // Повторная точка по X
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Поймано исключение: " + e);
        }
    }
}
