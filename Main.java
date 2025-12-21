import functions.*;

public class Main {
    public static void main(String[] args) {
        
        double[] values = { -216, -125, -64, -27, -8, -1, 0, 1, 8, 27, 64, 125, 216 };
        TabulatedFunction func = new LinkedListTabulatedFunction(-6.0, 6.0, values);

        System.out.println("Initial function:");
        printPoints(func);

        try 
        {
            func.addPoint(new FunctionPoint(1.5, 3.375));
            System.out.println("Added (1.5, 3.375)");

            func.deletePoint(0);
            System.out.println("Deleted point at index 0");

            func.setPointY(8, 54);
            System.out.println("Set y=54 at index 8");

            // Попытка нарушить порядок — должно вызвать исключение
            func.setPointX(0, 12);
        } 
        catch (FunctionPointIndexOutOfBoundsException | InappropriateFunctionPointException | IllegalStateException e) 
        {
            System.err.println("Exception caught: " + e.getMessage());
        }

        System.out.println("\nAfter operations:");
        printPoints(func);
    }

    // Вспомогательный метод для вывода всех точек
    private static void printPoints(TabulatedFunction f) 
    {
        for (int i = 0; i < f.getPointsCount(); i++) 
        {
            FunctionPoint p = f.getPoint(i);
            System.out.printf("x: %.3f\t y: %.3f%n", p.getX(), p.getY());
        }
    }
}