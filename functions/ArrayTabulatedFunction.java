package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points;
    private int pointsCount;

    // Конструктор1 : создает объект функции по заданным границам области определения и кол-ва точек для табулирования
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX - 1e-10) {
            throw new IllegalArgumentException("Левая граница >= правой, левая: " + leftX + ", правая: " + rightX);
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше двух: " + pointsCount);
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount];

        // Вычисляем шаг
        double step = (rightX - leftX) / (pointsCount - 1);
        // Создаем точки с равномерным распределением по x
        for (int i = 0; i < pointsCount; i++) {
            double currentX = leftX + i * step;
            points[i] = new FunctionPoint(currentX, 0.0);
        }
    }

    // Конструктор 2: вместо кол-ва точек получает значения функции в виде массива
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX - 1e-10) {
            throw new IllegalArgumentException("Левая граница >= правой, левая: " + leftX + ", правая: " + rightX);
        }
        if (values == null || values.length < 2) {
            throw new IllegalArgumentException("Массив должен содержать не менее 2 элементов");
        }
        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount];

        // Вычисляем шаг между точками
        double step = (rightX - leftX) / (pointsCount - 1);
        // Создаем точки с заданными значениями y
        for (int i = 0; i < pointsCount; i++) {
            double currentX = leftX + i * step;
            points[i] = new FunctionPoint(currentX, values[i]);
        }
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();

            if (x >= x1 - 1e-10 && x <= x2 + 1e-10) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        // Проверка корректности по x
        if (index > 0 && point.getX() <= points[index - 1].getX() + 1e-10) {
            throw new InappropriateFunctionPointException("Неккоректно по х");
        }
        if (index < pointsCount - 1 && point.getX() >= points[index + 1].getX() - 1e-10) {
            throw new InappropriateFunctionPointException("Неккоректно по х");
        }
        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        // Проверка корректности позиции
        if (index > 0 && x <= points[index - 1].getX() + 1e-10) {
            throw new InappropriateFunctionPointException("Неккоректно по х");
        }
        if (index < pointsCount - 1 && x >= points[index + 1].getX()- 1e-10) {
            throw new InappropriateFunctionPointException("Неккоректно по х");
        }
        points[index].setX(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        points[index].setY(y);
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException("Точка не может быть удалена, их <= 2");
        }
        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int IndexIns = 0;
        while (IndexIns < pointsCount && points[IndexIns].getX() < point.getX() - 1e-10) {
            IndexIns++;
        }

        // Проверка на дублирование
        if (IndexIns < pointsCount && Math.abs(points[IndexIns].getX() - point.getX()) <= 1e-10 ) {
            throw new InappropriateFunctionPointException("Точка с такой х " + point.getX() + " уже есть");
        }

        // Увеличение массива
        if (pointsCount == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        // Сдвиг элементов для освобождения места
        System.arraycopy(points, IndexIns, points, IndexIns + 1, pointsCount - IndexIns);

        // Вставка новой точки
        points[IndexIns] = new FunctionPoint(point);
        pointsCount++;
    }
}