/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/**
 *
 * @author I5-10400F
 */

import functions.*;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== Тестирование ArrayTabulatedFunction ===");
            testArrayTabulatedFunction();
            
            System.out.println("\n=== Тестирование LinkedListTabulatedFunction ===");
            testLinkedListTabulatedFunction();
            
            System.out.println("\n=== Тестирование через интерфейс ===");
            testInterface();
            
        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testArrayTabulatedFunction() {
        try {
            // 1. Тест некорректных конструкторов
            System.out.println("1. Тест некорректных конструкторов:");
            try {
                TabulatedFunction func1 = new ArrayTabulatedFunction(5, 3, 5);
                System.out.println("  ОШИБКА: должно было быть исключение IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                System.out.println("   Перехвачено ожидаемое исключение: " + e.getMessage());
            }
            
            try {
                TabulatedFunction func2 = new ArrayTabulatedFunction(0, 5, 1);
                System.out.println("  ОШИБКА: должно было быть исключение IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                System.out.println("   Перехвачено ожидаемое исключение: " + e.getMessage());
            }
            
            // 2. Создание корректной функции
            System.out.println("\n2. Создание корректной функции f(x) = x^2:");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 4, 5);
            for (int i = 0; i < func.getPointsCount(); i++) {
                double x = func.getPointX(i);
                func.setPointY(i, x * x);
            }
            printFunctionPoints(func);
            
            // 3. Тест выхода за границы индекса
            System.out.println("\n3. Тест выхода за границы индекса:");
            try {
                func.getPoint(10);
                System.out.println("  ОШИБКА: должно было быть исключение FunctionPointIndexOutOfBoundsException");
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("   Перехвачено ожидаемое исключение: " + e.getMessage());
            }
            
            // 4. Тест нарушения упорядоченности
            System.out.println("\n4. Тест нарушения упорядоченности:");
            try {
                func.setPointX(2, 0.5); // Должно быть исключение
                System.out.println("  ОШИБКА: должно было быть исключение InappropriateFunctionPointException");
            } catch (InappropriateFunctionPointException e) {
                System.out.println("   Перехвачено ожидаемое исключение: " + e.getMessage());
            }
            
            // 5. Тест удаления точек
            System.out.println("\n5. Тест удаления точек:");
            func.deletePoint(1);
            System.out.println("  После удаления точки 1:");
            printFunctionPoints(func);
            
            try {
                func.deletePoint(0);
                func.deletePoint(0); // Должно быть исключение
                System.out.println("  ОШИБКА: должно было быть исключение IllegalStateException");
            } catch (IllegalStateException e) {
                System.out.println("   Перехвачено ожидаемое исключение: " + e.getMessage());
            }
            
            // 6. Тест добавления дублирующей точки
            System.out.println("\n6. Тест добавления дублирующей точки:");
            try {
                func.addPoint(new FunctionPoint(2.0, 4.0));
                System.out.println("  ОШИБКА: должно было быть исключение InappropriateFunctionPointException");
            } catch (InappropriateFunctionPointException e) {
                System.out.println("   Перехвачено ожидаемое исключение: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Ошибка при тестировании: " + e.getMessage());
        }
    }
    
    private static void testLinkedListTabulatedFunction() {
        try {
            // 1. Создание функции
            System.out.println("1. Создание функции f(x) = sin(x):");
            TabulatedFunction func = new LinkedListTabulatedFunction(0, Math.PI, 5);
            for (int i = 0; i < func.getPointsCount(); i++) {
                double x = func.getPointX(i);
                func.setPointY(i, Math.sin(x));
            }
            printFunctionPoints(func);
            
            // 2. Тест добавления точки
            System.out.println("\n2. Тест добавления точки:");
            func.addPoint(new FunctionPoint(1.5, Math.sin(1.5)));
            System.out.println("  После добавления точки (1.5, sin(1.5)):");
            printFunctionPoints(func);
            
            // 3. Тест удаления
            System.out.println("\n3. Тест удаления точки:");
            func.deletePoint(2);
            System.out.println("  После удаления точки 2:");
            printFunctionPoints(func);
            
            // 4. Тест вычисления значения
            System.out.println("\n4. Тест вычисления значения:");
            double[] testX = {0.5, 1.0, 2.0, 2.5};
            for (double x : testX) {
                double y = func.getFunctionValue(x);
                System.out.printf("  f(%.2f) = %.4f%n", x, y);
            }
            
        } catch (Exception e) {
            System.out.println("Ошибка при тестировании: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testInterface() {
        try {
            // Работа через интерфейс
            TabulatedFunction func1 = new ArrayTabulatedFunction(0, 2, 3);
            TabulatedFunction func2 = new LinkedListTabulatedFunction(0, 2, 3);
            
            System.out.println("ArrayTabulatedFunction: " + func1.getPointsCount() + " точек");
            System.out.println("LinkedListTabulatedFunction: " + func2.getPointsCount() + " точек");
            
            // Одинаковый интерфейс для разных реализаций
            func1.setPointY(0, 1.0);
            func2.setPointY(0, 1.0);
            
            func1.setPointY(1, 2.0);
            func2.setPointY(1, 2.0);
            
            func1.setPointY(2, 3.0);
            func2.setPointY(2, 3.0);
            
            System.out.println("\nСравнение значений в точке x=1.0:");
            System.out.println("  ArrayTabulatedFunction: f(1.0) = " + func1.getFunctionValue(1.0));
            System.out.println("  LinkedListTabulatedFunction: f(1.0) = " + func2.getFunctionValue(1.0));
            
        } catch (Exception e) {
            System.out.println("Ошибка при тестировании интерфейса: " + e.getMessage());
        }
    }
    
    private static void printFunctionPoints(TabulatedFunction function) {
        System.out.println("  Точки функции:");
        for (int i = 0; i < function.getPointsCount(); i++) {
            try {
                System.out.printf("    [%d] (%.4f; %.4f)%n", 
                    i, function.getPointX(i), function.getPointY(i));
            } catch (Exception e) {
                System.out.println("    Ошибка при получении точки " + i + ": " + e.getMessage());
            }
        }
    }
}
