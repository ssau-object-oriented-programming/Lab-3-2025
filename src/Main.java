package functions;
import functions.FunctionPoint;
import functions.TabulatedFunction;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Лабораторная работа №2 ===");

        TabulatedFunction func = new TabulatedFunction(0, 4, new double[]{0, 1, 4, 9, 16});
        
        System.out.println("1. Исходная функция:");
        System.out.println("   Границы: [" + func.getLeftDomainBorder() + ", " + func.getRightDomainBorder() + "]");
        System.out.println("   Количество точек: " + func.getPointsCount());
        printPoints(func);
        
        System.out.println("\n2. Тестирование вычислений функции:");
        double[] testPoints = {-1, 0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 5};
        for (double x : testPoints) {
            double y = func.getFunctionValue(x);
            System.out.println("   f(" + x + ") = " + y);
        }

        System.out.println("\n3. Тестирование сеттеров:");

        System.out.println("   setPointY(2, 5): изменяем Y точки с индексом 2 с 4 на 5");
        func.setPointY(2, 5);
        System.out.println("   Результат: (" + func.getPointX(2) + ", " + func.getPointY(2) + ")");

        System.out.println("   setPointX(3, 3.5): изменяем X точки с индексом 3 с 3 на 3.5");
        func.setPointX(3, 3.5);
        System.out.println("   Результат: (" + func.getPointX(3) + ", " + func.getPointY(3) + ")");

        System.out.println("   setPoint(1, new FunctionPoint(1.2, 1.44)): заменяем точку с индексом 1");
        func.setPoint(1, new FunctionPoint(1.2, 1.44));
        System.out.println("   Результат: (" + func.getPointX(1) + ", " + func.getPointY(1) + ")");
        
        System.out.println("\n4. Функция после изменений через сеттеры:");
        printPoints(func);

        System.out.println("\n5. Тестирование граничных случаев сеттеров:");
        System.out.println("   Попытка setPointX(1, 0.5) (нарушение порядка):");
        double oldX = func.getPointX(1);
        func.setPointX(1, 0.5);
        System.out.println("   X остался прежним: " + (func.getPointX(1) == oldX));

        System.out.println("\n6. Добавление и удаление точек:");
        System.out.println("   Добавляем точку (2.5, 6.25):");
        func.addPoint(new FunctionPoint(2.5, 6.25));
        System.out.println("   Точек после добавления: " + func.getPointsCount());
        
        System.out.println("   Удаляем точку с индексом 1:");
        func.deletePoint(1);
        System.out.println("   Точек после удаления: " + func.getPointsCount());
        
        System.out.println("\n7. Финальное состояние функции:");
        printPoints(func);
    }
    
    private static void printPoints(TabulatedFunction func) {
        System.out.println("   Точки функции:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            System.out.println("     [" + i + "] (" + func.getPointX(i) + ", " + func.getPointY(i) + ")");
        }
    }
}