package functions;

public class ArrayTabulatedFunction implements TabulatedFunction 
{
    private FunctionPoint[] points;
    private int size; // актуальное количество точек (может быть меньше длины массива)

    // Проверяет, что индекс находится в допустимых пределах
    private boolean isValidIndex(int index)
    {
        return index >= 0 && index < size;
    }

    private boolean isXExists(double x) 
    {
        for (int i = 0; i < size; i++) 
        {
            if (Double.compare(points[i].getX(), x) == 0) 
            return true;
        }
        return false;
    }

    // Находит позицию, куда нужно вставить новую точку (чтобы сохранить порядок по x)
    private int findInsertIndex(double x) 
    {
        for (int i = size - 1; i >= 0; i--) 
        {
            if (Double.compare(points[i].getX(), x) < 0) 
            return i + 1;
        }
        return 0;
    }

    private void ensureCapacity() 
    {
        if (size == points.length) 
        {
            FunctionPoint[] newPoints = new FunctionPoint[size * 2];
            System.arraycopy(points, 0, newPoints, 0, size);
            points = newPoints;
        }
    }

    // Выполняет линейную интерполяцию между двумя соседними точками
    private double interpolate(double x, FunctionPoint left, FunctionPoint right) 
    {
        return (x - left.getX()) * (right.getY() - left.getY()) / (right.getX() - left.getX()) + left.getY();
    }

    // Конструктор: равномерное разбиение отрезка [leftX, rightX] на pointCount точек
    public ArrayTabulatedFunction(double leftX, double rightX, int pointCount) 
    {
        if (Double.compare(leftX, rightX) >= 0 || pointCount < 2) 
        {
            throw new IllegalArgumentException("Invalid parameters: leftX >= rightX or pointCount < 2");
        }
        
        size = pointCount;
        points = new FunctionPoint[pointCount];
        double step = (rightX - leftX) / (pointCount - 1);
        
        for (int i = 0; i < pointCount; i++) 
        {
            points[i] = new FunctionPoint(leftX + step * i, 0.0);
        }
    }

    // Конструктор: задаёт y-значения явно через массив
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) 
    {
        if (Double.compare(leftX, rightX) >= 0 || values.length < 2) 
        {
            throw new IllegalArgumentException("Invalid parameters: leftX >= rightX or values.length < 2");
        }
        
        size = values.length;
        points = new FunctionPoint[size];
        double step = (rightX - leftX) / (size - 1);
        
        for (int i = 0; i < size; i++) 
        {
            points[i] = new FunctionPoint(leftX + step * i, values[i]);
        }
    }

    @Override
    public double getLeftDomainBorder() 
    {
        return points[0].getX();
    }

    // Вычисляет значение функции в точке x с использованием линейной интерполяции
    @Override
    public double getFunctionValue(double x) 
    {
        if (size == 0 || Double.compare(x, points[0].getX()) < 0 || Double.compare(x, points[size - 1].getX()) > 0) 
        {
            return Double.NaN;
        }

        if (Double.compare(x, points[size - 1].getX()) == 0) 
        {
            return points[size - 1].getY();
        }
        
        // Поиск справа налево ускоряет интерполяцию для больших x
        for (int i = size - 1; i > 0; i--) 
        {
            if (Double.compare(x, points[i - 1].getX()) >= 0) 
            {
                return interpolate(x, points[i - 1], points[i]);
            }
        }
        return Double.NaN;
    }

    @Override
    public int getPointsCount() 
    {
        return size;
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return new FunctionPoint(points[index]);
    }

    @Override
    public void setPoint(int index, FunctionPoint point)
        throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException 
        {
        if (!isValidIndex(index)) 
            {
                 throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        
            }
        
            double x = point.getX();
        
            // Определяем границы допустимого x для этой позиции
        
            double leftBound = (index == 0) ? Double.NEGATIVE_INFINITY : points[index - 1].getX();
        
            double rightBound = (index == size - 1) ? Double.POSITIVE_INFINITY : points[index + 1].getX();
        
            // x должен строго лежать между соседними точками
        
            if (Double.compare(x, leftBound) > 0 && Double.compare(x, rightBound) < 0) 
            {
                points[index] = new FunctionPoint(point);
            } 
            else 
            {
            throw new InappropriateFunctionPointException("x must be strictly between neighbors");
            }
        }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return points[index].getX();
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        
        // Повторно используем логику setPoint
        setPoint(index, new FunctionPoint(x, points[index].getY()));
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return points[index].getY();
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        points[index].setY(y);
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (size < 3) {
            throw new IllegalStateException("Cannot delete: fewer than 3 points");
        }
        // Сдвигаем элементы влево, чтобы «закрыть дыру»
        System.arraycopy(points, index + 1, points, index, size - index - 1);
        points[--size] = null;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException 
    {
        double x = point.getX();
        if (isXExists(x)) 
        {
            throw new InappropriateFunctionPointException("Point with x=" + x + " already exists");
        }
        ensureCapacity();
        int pos = findInsertIndex(x);
        // Сдвигаем элементы вправо, чтобы освободить место
        System.arraycopy(points, pos, points, pos + 1, size - pos);
        points[pos] = new FunctionPoint(point);
        size++;
    }
}