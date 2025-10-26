package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private int size;                 // текущее количество точек
    private FunctionPoint[] points;   // упорядоченный по х массив точек

    // Проверка индекса на валидность
    // Исключение, если индекс вне [0, size - 1]
    private void requireIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("index=" + index);
        }
    }

    // Конструктор [leftX; rightX], pointsCount точек, все y=0
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (rightX <= leftX) {
            throw new IllegalArgumentException("leftX must be < rightX");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount must be >= 2");
        }
        this.size = pointsCount;
        this.points = new FunctionPoint[Math.max(pointsCount, 2)];

        // // Равномерный шаг по x
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }

    // Конструктор: [leftX; rightX], значения y — из массива
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (rightX <= leftX) {
            throw new IllegalArgumentException("leftX must be < rightX");
        }
        if (values == null || values.length < 2) {
            throw new IllegalArgumentException("values length must be >= 2");
        }
        this.size = values.length;
        this.points = new FunctionPoint[Math.max(size, 2)];

        double step = (rightX - leftX) / (size - 1);
        for (int i = 0; i < size; ++i) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }
    // Методы интерфейса TabulatedFunction

    // Количество точек в таблице
    @Override
    public int getPointsCount() {
        return size;
    }

    //Левая граница области определения. Если таблица пуста, вернется NaN
    @Override
    public double getLeftDomainBorder() {
        return (size == 0) ? Double.NaN : points[0].getX();
    }
    //Правая граница области определения. Если таблица пуста, вернется NaN
    @Override
    public double getRightDomainBorder() {
        return (size == 0) ? Double.NaN : points[size - 1].getX();
    }

    //Значение функции в точке x
    //Если x вне интервала возвращается NaN
    //Если x совпадает с одной из абсцисс, возвращается соответствующее y
    //Иначе применяется линейная интерполяция по найденному отрезку
    @Override
    public double getFunctionValue(double x) {
        if (size == 0) return Double.NaN;
        double left = points[0].getX();
        double right = points[size - 1].getX();
        if (x < left || x > right) return Double.NaN;

        // точное совпадение
        for (int i = 0; i < size; ++i) {
            if (x == points[i].getX()) return points[i].getY();
        }
        // интервал [xi, xi+1]
        for (int i = 0; i < size - 1; ++i) {
            double x0 = points[i].getX(), y0 = points[i].getY();
            double x1 = points[i + 1].getX(), y1 = points[i + 1].getY();
            if (x >= x0 && x <= x1) {
                return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
            }
        }
        return Double.NaN;
    }
        //Возвращает копию точки по индексу
    @Override
    public FunctionPoint getPoint(int index) {
        requireIndex(index);
        return new FunctionPoint(points[index]);
    }
    // Заменяет точку по индексу на переданную
    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) return;
        requireIndex(index);

        double newX = point.getX();
        if (index > 0) {
            double leftX = points[index - 1].getX();
            if (!(newX > leftX)) throw new InappropriateFunctionPointException("x must be > left neighbor");
        }
        if (index < size - 1) {
            double rightX = points[index + 1].getX();
            if (!(newX < rightX)) throw new InappropriateFunctionPointException("x must be < right neighbor");
        }
        points[index] = new FunctionPoint(point); // копия (инкапсуляция)
    }
    //  Абсцисса x точки по индексу
    @Override
    public double getPointX(int index) {
        requireIndex(index);
        return points[index].getX();
    }
    //  Меняет x точки по индексу. Порядок по x должен сохраниться
    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        requireIndex(index);
        if (size == 1) {
            points[0].setX(x);
            return;
        }

        if (index == 0) {
            double nextX = points[1].getX();
            if (x >= nextX) throw new InappropriateFunctionPointException("x must be < right neighbor");
            points[0].setX(x);
            return;
        }
        if (index == size - 1) {
            double prevX = points[size - 2].getX();
            if (x <= prevX) throw new InappropriateFunctionPointException("x must be > left neighbor");
            points[size - 1].setX(x);
            return;
        }
        double leftX = points[index - 1].getX();
        double rightX = points[index + 1].getX();
        if (x <= leftX || x >= rightX) {
            throw new InappropriateFunctionPointException("x must be strictly between neighbors");
        }
        points[index].setX(x);
    }
    // Ордината y точки по индексу
    @Override
    public double getPointY(int index) {
        requireIndex(index);
        return points[index].getY();
    }
    //Меняет y точки по индексу. Порядок по x не затрагивается
    @Override
    public void setPointY(int index, double y) {
        requireIndex(index);
        points[index].setY(y);
    }
       //   Удаляет точку по индексу со сдвигом хвоста влево.
      //    Минимально допустимое число точек равно 3.
    @Override
    public void deletePoint(int index) {
        requireIndex(index);
        if (size < 3) throw new IllegalStateException("cannot delete when points count < 3");
        System.arraycopy(points, index + 1, points, index, size - index - 1);
        points[--size] = null;
    }
        //Добавляет новую точку и сохраняет порядок по x
        //Если абсцисса x уже присутствует в таблице, бросает InappropriateFunctionPointException.
        //При нехватке места расширяет буфер по правилу 1.5x + 1.
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) return;
        double x = point.getX();
        int insertIndex = 0;
        while (insertIndex < size && points[insertIndex].getX() < x) {insertIndex++;}
        if (insertIndex < size && points[insertIndex].getX() == x) {
            throw new InappropriateFunctionPointException("duplicate x");
        }

        if (size >= (points == null ? 0 : points.length)) {
            int newCapacity = (points == null || points.length == 0) ? 2 : (points.length * 3 / 2 + 1);
            FunctionPoint[] newArray = new FunctionPoint[newCapacity];
            if (size > 0) System.arraycopy(points, 0, newArray, 0, size);
            points = newArray;}
            // Сдвигаем хвост вправо, чтобы освободить позицию insertIndex
        if (insertIndex < size) {System.arraycopy(points, insertIndex, points, insertIndex + 1, size - insertIndex);}
        // Вставляем копию точки
        points[insertIndex] = new FunctionPoint(point); // копия
        size++;
    }
}
