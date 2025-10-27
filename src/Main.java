import functions.*;

public class Main {
    public static void main(String[] args) {

        // 0) Конструкторы: IllegalArgumentException
        try { new ArrayTabulatedFunction(2, 2, 5); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(Array): левая >= правая  " + e.getMessage());
        }
        try { new ArrayTabulatedFunction(0, 1, 1); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(Array): pointsCount < 2  " + e.getMessage());
        }
        try { new LinkedListTabulatedFunction(3, 1, 4); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(List): левая >= правая  " + e.getMessage());
        }
        try { new LinkedListTabulatedFunction(0, 1, 1); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(List): pointsCount < 2  " + e.getMessage());
        }
        // Конструктор с массивом значений: values.length < 2
        try { new ArrayTabulatedFunction(0, 1, new double[]{42}); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(Array): values.length < 2  " + e.getMessage());
        }
        try { new LinkedListTabulatedFunction(0, 1, new double[]{13}); }
        catch (IllegalArgumentException e) {
            System.out.println(" IAE(List): values.length < 2  " + e.getMessage());
        }

        // 1) Базовые проверки на массивной реализации (y = 5x + 10)
        TabulatedFunction f = new ArrayTabulatedFunction(0, 10, 6);
        for (int i = 0; i < f.getPointsCount(); ++i) {
            double x = f.getPointX(i);
            f.setPointY(i, 5 * x + 10);
        }

        System.out.println("\n ArrayTabulatedFunction: исходные точки (y = 5x+10) ");
        for (int i = 0; i < f.getPointsCount(); ++i) {
            System.out.printf("точка %d: x=%.2f, y=%.2f%n", i, f.getPointX(i), f.getPointY(i));
        }
        System.out.printf("границы: [%.2f; %.2f]%n", f.getLeftDomainBorder(), f.getRightDomainBorder());

        // Интерполяция + вне диапазона + точные границы
        System.out.println("\nЗначения функции:");
        double[] probe = {
                f.getLeftDomainBorder() - 1.0,
                f.getLeftDomainBorder(), // Точная левая граница
                1.0, 3.0, 5.5,
                f.getRightDomainBorder(), // Точная правая граница
                f.getRightDomainBorder() + 1.0
        };
        for (double x : probe) {
            double y = f.getFunctionValue(x);
            System.out.printf("f(%6.3f) = %s%n", x, Double.isNaN(y) ? "NaN" : String.format("%.6f", y));
        }

        // Геттеры/сеттеры
        System.out.println("\nТест геттеров/сеттеров:");
        System.out.printf("до setPointY: y[2]=%.2f%n", f.getPointY(2));
        f.setPointY(2, 999.0);
        System.out.printf("после setPointY: y[2]=%.2f%n", f.getPointY(2));

        try {
            double leftX = f.getPointX(1);
            double rightX = f.getPointX(3);
            double mid = (leftX + rightX) / 2.0;
            f.setPointX(2, mid);
            System.out.printf("после setPointX: x[2]=%.6f (между %.6f и %.6f)%n", f.getPointX(2), leftX, rightX);
        } catch (FunctionPointIndexOutOfBoundsException | InappropriateFunctionPointException e) {
            System.out.println("Ошибка: корректный setPointX не прошёл " + e.getMessage());
        }

        try {
            double tooBig = f.getPointX(3) + 100.0;
            f.setPointX(2, tooBig); // ломаем порядок
            System.out.println("Ошибка: ожидалось исключение в setPointX(ломаем порядок)!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(setPointX): " + e.getMessage());
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Не тот тип исключения (ожидался Inappropriate): " + e.getMessage());
        }

        try {
            f.setPoint(1, new FunctionPoint(1.0, 15.0)); // корректная замена
            System.out.printf("после setPoint: #1 -> x=%.2f, y=%.2f%n", f.getPointX(1), f.getPointY(1));
        } catch (FunctionPointIndexOutOfBoundsException | InappropriateFunctionPointException e) {
            System.out.println("Ошибка: корректный setPoint не прошёл " + e.getMessage());
        }

        try {
            double nextX = f.getPointX(2);
            f.setPoint(1, new FunctionPoint(nextX + 0.01, 123.0));
            System.out.println("Ошибка: ожидалось исключение в setPoint(ломаем порядок)!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(setPoint): " + e.getMessage());
        }

        // Проверки addPoint с учётом машинного эпсилон
        try {
            // корректная вставка разница по x > epsilon
            f.addPoint(new FunctionPoint(7.5, 5 * 7.5 + 10));
            System.out.println("после addPoint(7.5, f(7.5)) количество = " + f.getPointsCount());
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка: корректный addPoint не прошёл " + e.getMessage());
        }

        try {
            // явный дубликат x
            double dupX = f.getPointX(0);
            f.addPoint(new FunctionPoint(dupX, 111.0));
            System.out.println("Ошибка: ожидалось исключение addPoint(дубликат х)!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(addPoint дубликат): " + e.getMessage());
        }

        try {
            // "почти дубликат" — x отличается меньше, чем эпсилон (1e-10 < 1e-9) - тоже дубликат
            double baseX = f.getPointX(0);
            double almostSame = baseX + 1e-10;
            f.addPoint(new FunctionPoint(almostSame, 777.0));
            System.out.println("Ошибка: ожидалось исключение addPoint(почти-дубликат по eps)!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(addPoint почти-дубликат): " + e.getMessage());
        }

        try {
            // разница чуть больше эпсилон — добавление должно пройти
            double baseX = f.getPointX(1);
            double slightlyDiff = baseX + 1e-6;
            f.addPoint(new FunctionPoint(slightlyDiff, 888.0));
            System.out.println(" addPoint(x с дельтой > eps) прошёл, count = " + f.getPointsCount());
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка: addPoint(дельта > eps) неожиданно упал: " + e.getMessage());
        }

        try {
            // Почти касаемся правого соседа для индекса 0: x0 = x1 - 1e-10
            double x1 = f.getPointsCount() > 1 ? f.getPointX(1) : f.getPointX(0) + 1.0;
            double almostBadX0 = x1 - 1e-10;
            f.setPointX(0, almostBadX0);
            System.out.println("Ошибка: ожидалось Inappropriate в setPointX(0 ~ x1-ε)!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(Array setPointX(0 ~ x1-ε)): " + e.getMessage());
        } catch (FunctionPointIndexOutOfBoundsException ignore) { }

        try {
            // Почти касаемся левого соседа для последнего индекса: x_last = x_{last-1} + 1e-10
            int last = f.getPointsCount() - 1;
            if (last >= 1) {
                double leftN = f.getPointX(last - 1);
                double almostBadXLast = leftN + 1e-10;
                f.setPointX(last, almostBadXLast);
                System.out.println("Ошибка: ожидалось Inappropriate в setPointX(last ~ left+ε)!");
            }
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(Array setPointX(last ~ left+ε)): " + e.getMessage());
        } catch (FunctionPointIndexOutOfBoundsException ignore) { }

        try {
            // Почти-допустимый setPoint(0): x0 = x1 - 1e-10 - тоже нарушение
            if (f.getPointsCount() > 1) {
                double x1 = f.getPointX(1);
                double x0 = x1 - 1e-10;
                f.setPoint(0, new FunctionPoint(x0, f.getPointY(0)));
                System.out.println("Ошибка: ожидалось Inappropriate в setPoint(0 ~ x1-ε)!");
            }
        } catch (InappropriateFunctionPointException e) {
            System.out.println(" Inappropriate(Array setPoint(0 ~ x1-ε)): " + e.getMessage());
        } catch (FunctionPointIndexOutOfBoundsException ignore) { }


        try {
            f.deletePoint(0);
            System.out.println("после deletePoint(0) количество = " + f.getPointsCount());
        } catch (FunctionPointIndexOutOfBoundsException | IllegalStateException e) {
            System.out.println("Ошибка: deletePoint(0) не прошёл — " + e.getMessage());
        }
        try {
            while (f.getPointsCount() > 2) f.deletePoint(0);
            f.deletePoint(0); // должно упасть
            System.out.println("Ошибка: ожидалось IllegalStateException при удалении при 2 точках!");
        } catch (IllegalStateException e) {
            System.out.println(" IllegalState(delete при <3 точках): " + e.getMessage());
        }

        // Индексы: отрицательный и сильно большой
        try { f.getPoint(-1); System.out.println("Ошибка: ожидалось FPIndexOOBE в getPoint(-1)!"); }
        catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(" FPIndexOOBE(getPoint -1): " + e.getMessage());
        }
        try { f.getPoint(99); System.out.println("Ошибка: ожидалось FPIndexOOBE в getPoint(99)!"); }
        catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(" FPIndexOOBE(getPoint 99): " + e.getMessage());
        }

        // 2) LinkedListTabulatedFunction — проверки у головы/хвоста списка
        try {
            TabulatedFunction g = new LinkedListTabulatedFunction(-2, 4, 4); // X = {-2, 0, 2, 4}
            for (int i = 0; i < g.getPointsCount(); i++) {
                double x = g.getPointX(i);
                g.setPointY(i, 3 * x * x - 2); // y = 3x^2 - 2
            }

            System.out.println("\n LinkedListTabulatedFunction: точки");
            for (int i = 0; i < g.getPointsCount(); i++) {
                System.out.printf("точка %d: x=%.2f, y=%.2f%n", i, g.getPointX(i), g.getPointY(i));
            }
            System.out.println("range: [" + g.getLeftDomainBorder() + "; " + g.getRightDomainBorder() + "]");

            // Значения на границах и вне диапазона
            double[] xs = {
                    g.getLeftDomainBorder() - 1,
                    g.getLeftDomainBorder(),
                    -1, 0, 1.5,
                    g.getRightDomainBorder(),
                    g.getRightDomainBorder() + 1
            };
            System.out.println(" Значения в выборочных x (включая границы):");
            for (double x : xs) {
                double y = g.getFunctionValue(x);
                System.out.printf("g(%6.3f) = %s%n", x, Double.isNaN(y) ? "NaN" : String.format("%.6f", y));
            }

            // Вставка в начало (новая нулевая точка)
            double oldLeft = g.getLeftDomainBorder();
            double newX = oldLeft - 1.0;
            double newY = 3 * newX * newX - 2;
            try {
                g.addPoint(new FunctionPoint(newX, newY));
                System.out.println("\nПосле addPoint(newX<left):");
                for (int i = 0; i < g.getPointsCount(); i++) {
                    System.out.println("  " + i + ": x=" + g.getPointX(i) + ", y=" + g.getPointY(i));
                }
                System.out.println("новая левая граница: " + g.getLeftDomainBorder());
            } catch (InappropriateFunctionPointException e) {
                System.out.println("Ошибка неожиданный Inappropriate при вставке в начало: " + e.getMessage());
            }

            // Вставка в хвост (x > right)
            try {
                double right = g.getRightDomainBorder();
                double xTail = right + 1.0;
                double yTail = 3 * xTail * xTail - 2;
                g.addPoint(new FunctionPoint(xTail, yTail));
                System.out.println("\nПосле addPoint(x>right):");
                for (int i = 0; i < g.getPointsCount(); i++) {
                    System.out.println("  " + i + ": x=" + g.getPointX(i) + ", y=" + g.getPointY(i));
                }
                System.out.println("новая правая граница: " + g.getRightDomainBorder());
            } catch (InappropriateFunctionPointException e) {
                System.out.println("Ошибка неожиданный Inappropriate при вставке в хвост: " + e.getMessage());
            }

            // Удаление нулевой точки
            try {
                int before = g.getPointsCount();
                double nextX = g.getPointX(1), nextY = g.getPointY(1);
                g.deletePoint(0);
                System.out.println("\nПосле deletePoint(0):");
                for (int i = 0; i < g.getPointsCount(); i++) {
                    System.out.println("  " + i + ": x=" + g.getPointX(i) + ", y=" + g.getPointY(i));
                }
                System.out.println("левая граница: " + g.getLeftDomainBorder());
                System.out.println("ожидалось: количество=" + (before - 1) + ", новая нулевая=(" + nextX + ", " + nextY + ")");
            } catch (Exception e) {
                System.out.println("Ошибка неожиданное исключение при deletePoint(0): " + e.getMessage());
            }

            // Удаление хвостовой точки (последний индекс)
            try {
                int last = g.getPointsCount() - 1;
                double prevX = g.getPointX(last - 1), prevY = g.getPointY(last - 1);
                g.deletePoint(last);
                System.out.println("\nПосле deletePoint(last):");
                for (int i = 0; i < g.getPointsCount(); i++) {
                    System.out.println("  " + i + ": x=" + g.getPointX(i) + ", y=" + g.getPointY(i));
                }
                System.out.println("ожидалось: новый последний=(" + prevX + ", " + prevY + ")");
            } catch (Exception e) {
                System.out.println("Ошибка неожиданное исключение при deletePoint(last): " + e.getMessage());
            }

            // getPoint(i) должен возвращать копию
            {
                FunctionPoint p0 = g.getPoint(0);
                double x0 = p0.getX(), y0 = p0.getY();
                p0.setX(x0 + 1000);
                p0.setY(y0 + 1000);
                System.out.println("\n Проверка инкапсуляции: оригинал после изменений копии - x=" +
                        g.getPointX(0) + ", y=" + g.getPointY(0) + " (должны совпасть со старыми)");
            }

            // Корректный и некорректный setPointX на индексе 0 и добавлена проверка на последнем индексе
            {
                double x1 = g.getPointX(1);
                double okX0 = x1 - 0.5; // строго меньше соседа справа
                try {
                    g.setPointX(0, okX0);
                    System.out.println("\n setPointX(0, допустимое) - x0=" + g.getPointX(0));
                } catch (InappropriateFunctionPointException e) {
                    System.out.println("Ошибка неожиданный Inappropriate в setPointX(0, допустимое): " + e.getMessage());
                }

                double badX0 = x1 + 1.0; // ломаем порядок
                try {
                    g.setPointX(0, badX0);
                    System.out.println("Ошибка ожидалось исключение в setPointX(0, недопустимое)");
                } catch (InappropriateFunctionPointException e) {
                    System.out.println(" setPointX(0, недопустимое) исключение: " + e.getMessage());
                }

                // Для последней точки: x должен быть > x левого соседа
                int lastIdx = g.getPointsCount() - 1;
                double leftNeighbor = g.getPointX(lastIdx - 1);
                double okLast = leftNeighbor + 0.5; // допустимо
                double badLast = leftNeighbor - 0.1; // недопустимо
                try {
                    g.setPointX(lastIdx, okLast);
                    System.out.println(" setPointX(last, допустимое) - x_last=" + g.getPointX(lastIdx));
                } catch (InappropriateFunctionPointException e) {
                    System.out.println("Ошибка неожиданный Inappropriate в setPointX(last, допустимое): " + e.getMessage());
                }
                try {
                    g.setPointX(lastIdx, badLast);
                    System.out.println("Ошибка ожидалось исключение в setPointX(last, недопустимое)");
                } catch (InappropriateFunctionPointException e) {
                    System.out.println(" setPointX(last, недопустимое)  исключение: " + e.getMessage());
                }
            }

            // Корректный и некорректный setPoint на индексе 0
            {
                double x1 = g.getPointX(1);
                double okX = x1 - 0.25;
                double okY = 3 * okX * okX - 2;
                try {
                    g.setPoint(0, new FunctionPoint(okX, okY));
                    System.out.println("\n setPoint(0, допустимая точка) - x0=" + g.getPointX(0));
                } catch (InappropriateFunctionPointException e) {
                    System.out.println("Ошибка неожиданный Inappropriate в setPoint(0, допустимое): " + e.getMessage());
                }

                double badX = x1 + 0.1; // недопустимо
                try {
                    g.setPoint(0, new FunctionPoint(badX, 123.0));
                    System.out.println("Ошибка ожидалось исключение в setPoint(0, недопустимое)");
                } catch (InappropriateFunctionPointException e) {
                    System.out.println("setPoint(0, недопустимое) исключение: " + e.getMessage());
                }
            }

            // Интерполяция у головы между двумя первыми точками
            {
                double x0 = g.getPointX(0), y0 = g.getPointY(0);
                double x1 = g.getPointX(1), y1 = g.getPointY(1);
                double mid = (x0 + x1) / 2.0;
                double yMid = g.getFunctionValue(mid);
                double expected = 3 * mid * mid - 2;
                System.out.println("\nИнтерполяция у головы: mid=" + mid +
                        ", f(mid)=" + yMid + ", expected=" + expected);
            }

            // Дубликат x и нарушение порядка
            try {
                g.addPoint(new FunctionPoint(g.getPointX(1), 0)); // дубликат X
                System.out.println("Ошибка: ожидалось исключение addPoint(дубликат) у списка!");
            } catch (InappropriateFunctionPointException e) {
                System.out.println(" Inappropriate(list/addPoint дубликат): " + e.getMessage());
            }

            try {
                g.setPointX(1, g.getPointX(2) + 10); // ломаем порядок
                System.out.println("Ошибка: ожидалось исключение setPointX(некорректный) у списка!");
            } catch (InappropriateFunctionPointException e) {
                System.out.println(" Inappropriate(list/setPointX): " + e.getMessage());
            }

            try { g.getPoint(999); System.out.println("Ошибка: ожидалось FPIndexOOBE(list/getPoint 999)!"); }
            catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println(" FPIndexOOBE(list/getPoint 999): " + e.getMessage());
            }

            // Проверка IllegalState при удалениях у списка
            try {
                while (g.getPointsCount() > 2) g.deletePoint(0);
                g.deletePoint(0); // должно упасть
                System.out.println("Ошибка ожидалось IllegalStateException при удалении при 2 точках (list)");
            } catch (IllegalStateException e) {
                System.out.println(" IllegalState(list/delete при <3 точках): " + e.getMessage());
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка конструктора LinkedListTabulatedFunction: " + e.getMessage());
        }
    }
}
