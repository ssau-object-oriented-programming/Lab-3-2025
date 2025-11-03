import functions.*;

public class Main {
    public static void main(String[] args) {
        try {
            // создаем табулированную функцию на интервале от 0 до 10 с 5 точками
            TabulatedFunction quadraticFunc = new LinkedListTabulatedFunction(0, 10, 5);

            for (int i = 0; i < quadraticFunc.getPointsCount(); i++) {
                // получаем x координату текущей точки
                double x = quadraticFunc.getPointX(i);
                // устанавливаем y как 2.5 умножить на x
                quadraticFunc.setPointY(i, 2.5*x);
            }

            System.out.println("Квадратичная функция y = 2,5*x:");
            // выводим все точки функции
            for (int i = 0; i < quadraticFunc.getPointsCount(); i++) {
                // печатаем координаты каждой точки
                System.out.printf("(%.2f, %.2f) ", quadraticFunc.getPointX(i), quadraticFunc.getPointY(i));
            }
            System.out.println();

            // добавляем дополнительные точки в функцию
            // добавляем точку с x=1.5 и y=3.75
            quadraticFunc.addPoint(new FunctionPoint(1.5, 3.75));
            // добавляем точку с x=3.5 и y=8.75
            quadraticFunc.addPoint(new FunctionPoint(3.5, 8.75));

            System.out.println("После добавления точек:");
            // снова выводим все точки чтобы увидеть изменения
            for (int i = 0; i < quadraticFunc.getPointsCount(); i++) {
                // печатаем координаты каждой точки после добавления
                System.out.printf("(%.2f, %.2f) ", quadraticFunc.getPointX(i), quadraticFunc.getPointY(i));
            }
            System.out.println();

            // удаляем точку с индексом 3
            quadraticFunc.deletePoint(3);

            System.out.println("После удаления точки:");
            // выводим точки после удаления
            for (int i = 0; i < quadraticFunc.getPointsCount(); i++) {
                // печатаем координаты каждой точки после удаления
                System.out.printf("(%.2f, %.2f) ", quadraticFunc.getPointX(i), quadraticFunc.getPointY(i));
            }
            System.out.println();

            // изменяем точку с индексом 2 на новые координаты
            // устанавливаем точку с x=3.0 и y=4.0
            quadraticFunc.setPoint(2, new FunctionPoint(3.0, 4.0));

            System.out.println("После изменения точки:");
            // выводим точки после изменения
            for (int i = 0; i < quadraticFunc.getPointsCount(); i++) {
                // печатаем координаты каждой точки после изменения
                System.out.printf("(%.2f, %.2f) ", quadraticFunc.getPointX(i), quadraticFunc.getPointY(i));
            }
            System.out.println();

            // тестируем интерполяцию в различных точках
            System.out.println("Интерполяция:");
            // массив тестовых x координат
            double[] testX = {0.5, 1.2, 2.5, 3.2, 6.8};
            // для каждого x в массиве
            for (double x : testX) {
                // вычисляем значение функции в каждой тестовой точке
                double value = quadraticFunc.getFunctionValue(x);
                // печатаем результат
                System.out.printf("f(%.2f) = %.2f\n", x, value);
            }

            // выводим границы области определения
            // получаем левую границу
            double leftBorder = quadraticFunc.getLeftDomainBorder();
            // получаем правую границу
            double rightBorder = quadraticFunc.getRightDomainBorder();
            // печатаем границы
            System.out.printf("Область определения: [%.2f, %.2f]\n", leftBorder, rightBorder);

            // проверяем поведение функции вне области определения
            System.out.println("Вне области определения:");
            // вычисляем значение за левой границей
            double leftValue = quadraticFunc.getFunctionValue(-1);
            // вычисляем значение за правой границей
            double rightValue = quadraticFunc.getFunctionValue(11);
            // печатаем результаты
            System.out.printf("f(-1) = %.2f\n", leftValue);
            System.out.printf("f(11) = %.2f\n", rightValue);

            System.out.println("Финальное состояние:");
            // выводим конечное состояние всех точек
            for (int i = 0; i < quadraticFunc.getPointsCount(); i++) {
                // печатаем финальные координаты каждой точки
                System.out.printf("(%.2f, %.2f) ", quadraticFunc.getPointX(i), quadraticFunc.getPointY(i));
            }
            System.out.println();

        } catch (Exception e) {
            // если произошла любая ошибка
            System.out.println("Произошла ошибка: " + e.getMessage());
            // печатаем детали ошибки
            e.printStackTrace();
        }

        // ТЕСТИРОВАНИЕ ИСКЛЮЧЕНИЙ
        System.out.println("\nТестирование исключений: ");

        // вызываем метод тестирования исключений
        testExceptions();
    }

    public static void testExceptions() {
        try {
            System.out.println("1. Попытка создать функцию с неправильными границами:");
            // пытаемся создать функцию с левой границей больше правой
            TabulatedFunction badFunc = new LinkedListTabulatedFunction(10, 0, 5);
        } catch (IllegalArgumentException e) {
            // ловим исключение неправильного аргумента
            System.out.println("Поймано IllegalArgumentException: " + e.getMessage());
        }

        try {
            System.out.println("2. Попытка получить точку с неверным индексом:");
            // создаем нормальную функцию
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 5);
            // пытаемся получить точку с индексом 100 которого нет
            func.getPoint(100);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            // ловим исключение выхода за границы индексов
            System.out.println("Поймано FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        } catch (Exception e) {
            // ловим любое другое исключение
            System.out.println("Другая ошибка: " + e.getMessage());
        }

        try {
            System.out.println("3. Попытка нарушить порядок точек:");
            // создаем нормальную функцию
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 5);
            // пытаемся установить точку которая нарушает порядок по x
            func.setPoint(1, new FunctionPoint(8.0, 5.0));
        } catch (InappropriateFunctionPointException e) {
            // ловим исключение неподходящей точки
            System.out.println("Поймано InappropriateFunctionPointException: " + e.getMessage());
        } catch (Exception e) {
            // ловим любое другое исключение
            System.out.println("Другая ошибка: " + e.getMessage());
        }

        try {
            System.out.println("4. Попытка добавить точку с существующим X:");
            // создаем нормальную функцию
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 5);
            // пытаемся добавить точку с x=0 который уже есть
            func.addPoint(new FunctionPoint(0, 10.0));
        } catch (InappropriateFunctionPointException e) {
            // ловим исключение неподходящей точки
            System.out.println("Поймано InappropriateFunctionPointException: " + e.getMessage());
        } catch (Exception e) {
            // ловим любое другое исключение
            System.out.println("Другая ошибка: " + e.getMessage());
        }

        try {
            System.out.println("5. Попытка удалить слишком много точек:");
            // создаем функцию с минимальным количеством точек
            TabulatedFunction smallFunc = new ArrayTabulatedFunction(0, 2, 3);
            // удаляем первую точку
            smallFunc.deletePoint(0);
            // удаляем вторую точку
            smallFunc.deletePoint(0);
            // пытаемся удалить третью точку что вызовет ошибку
            smallFunc.deletePoint(0);
        } catch (IllegalStateException e) {
            // ловим исключение недопустимого состояния
            System.out.println("Поймано IllegalStateException: " + e.getMessage());
        } catch (Exception e) {
            // ловим любое другое исключение
            System.out.println("Другая ошибка: " + e.getMessage());
        }

        System.out.println("Тестирование исключений завершено");
    }
}