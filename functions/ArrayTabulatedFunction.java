package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private static final double EPSILON = 1e-10;
    
    private FunctionPoint[] points;
    private int pointsCount;
    
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount];
        
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for(int i = 0; i < pointsCount; ++i) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }
    
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount];
        
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }
    
    @Override
    public double getLeftDomainBorder() {
        return points[0].getX();
    }
    
    @Override
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }
    
    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() - EPSILON || x > getRightDomainBorder() + EPSILON) {
            return Double.NaN;
        }
        
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();
            
            if (x >= x1 - EPSILON && x <= x2 + EPSILON) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                
                // Проверка на точное совпадение с граничными точками
                if (x > x1 - EPSILON && x < x1 + EPSILON) {
                    return y1;
                }
                if (x > x2 - EPSILON && x < x2 + EPSILON) {
                    return y2;
                }
                
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }
    
    @Override
    public int getPointsCount() {
        return pointsCount;
    }
    
    @Override
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return new FunctionPoint(points[index]);
    }
    
    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        
        if (index == 0) {
            setFirstPoint(point);
        } else if (index == pointsCount - 1) {
            setLastPoint(point);
        } else {
            setMiddlePoint(index, point);
        }
    }
    
    private void setFirstPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point.getX() >= points[1].getX() - EPSILON) {
            throw new InappropriateFunctionPointException(
                "X координата первой точки должна быть меньше X координаты второй точки");
        }
        points[0] = new FunctionPoint(point);
    }
    
    private void setLastPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point.getX() <= points[pointsCount - 2].getX() + EPSILON) {
            throw new InappropriateFunctionPointException(
                "X координата последней точки должна быть больше X координаты предыдущей точки");
        }
        points[pointsCount - 1] = new FunctionPoint(point);
    }
    
    private void setMiddlePoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        double prevX = points[index - 1].getX();
        double nextX = points[index + 1].getX();
        double newX = point.getX();
        
        if (newX <= prevX + EPSILON || newX >= nextX - EPSILON) {
            throw new InappropriateFunctionPointException(
                "X координата средней точки должна быть между " + prevX + " и " + nextX);
        }
        points[index] = new FunctionPoint(point);
    }
    
    @Override
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getX();
    }
    
    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        
        FunctionPoint tempPoint = new FunctionPoint(points[index]);
        tempPoint.setX(x);
        setPoint(index, tempPoint);
    }
    
    @Override
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getY();
    }
    
    @Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        points[index].setY(y);
    }
    
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // Проверка на дубликат
        for (int i = 0; i < pointsCount; i++) {
            double currentX = getPointX(i);
            if (point.getX() > currentX - EPSILON && point.getX() < currentX + EPSILON) {
                throw new InappropriateFunctionPointException(
                    "Точка с X=" + point.getX() + " уже существует");
            }
        }
        
        // Увеличиваем массив при необходимости
        if (pointsCount == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length + points.length / 2 + 1];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        // Находим позицию для вставки
        int insertIndex = 0;
        while (insertIndex < pointsCount && point.getX() > points[insertIndex].getX() + EPSILON) {
            insertIndex++;
        }

        // Сдвигаем элементы и вставляем новую точку
        System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }

    @Override
    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (pointsCount < 3) {
            throw new IllegalStateException("Нельзя удалить точку - функция должна содержать минимум 3 точки");
        }
        
        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
        
        // Обнуляем последний элемент для помощи GC
        if (pointsCount < points.length) {
            points[pointsCount] = null;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArrayTabulatedFunction[");
        for (int i = 0; i < pointsCount; i++) {
            if (i > 0) sb.append(", ");
            sb.append(points[i].toString());
        }
        sb.append("]");
        return sb.toString();
    }
}