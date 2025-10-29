package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points;  //массив для хранения точек функции
    private int pointsCount;         //текущее количество точек в массиве

    // конструктор 1 создает функцию с равномерно распределенными точками по x
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX)// проверка корректности входных данных
            throw new IllegalArgumentException("Left border >= right border");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Points count < 2");

        this.pointsCount = pointsCount;
        points = new FunctionPoint[pointsCount];
        //вычисление шага между точками
        double step = (rightX - leftX) / (pointsCount - 1);
        //создание точек с координатой y = 0
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i * step, 0);
        }
    }

    // конструктор 2 создает функцию с заданными значениями y
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX)//проверка корректности входных данных
            throw new IllegalArgumentException("Left border >= right border");
        if (values.length < 2)
            throw new IllegalArgumentException("Points count < 2");

        pointsCount = values.length;
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);//вычисление шага между точками
        for (int i = 0; i < pointsCount; i++) {//создание точек с заданными значениями y
            points[i] = new FunctionPoint(leftX + i * step, values[i]);
        }
    }

    //возвращает количество точек в функции
    public int getPointsCount() {
        return pointsCount;
    }

    //возвращает левую границу области определения
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    //возвращает правую границу области определения
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    //вычисляет значение функции в точке x с помощью линейной интерполяции
    public double getFunctionValue(double x) {
        //проверка что x находится в области определения
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())//проверка что x находится в области определения
            return Double.NaN;

        for (int i = 0; i < pointsCount - 1; i++) { //поиск интервала, содержащего x
            if (x == points[i].getX())//если x точно совпадает с точкой из массива
                return points[i].getY();

            //если x находится между двумя точками - линейная интерполяция
            if (x > points[i].getX() && x < points[i + 1].getX()) {
                double x1 = points[i].getX();
                double x2 = points[i + 1].getX();
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        if (x == points[pointsCount - 1].getX())//проверка последней точки
            return points[pointsCount - 1].getY();

        return Double.NaN;
    }

    private void checkIndex(int index) {//проверяет корректность индекса
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Index " + index + " out of bounds");
    }

    public FunctionPoint getPoint(int index) {//возвращает копию точки по индексу
        checkIndex(index);
        return new FunctionPoint(points[index]);
    }

    //заменяет точку по указанному индексу
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        checkIndex(index);
        double x = point.getX();
        if ((index > 0 && x <= points[index - 1].getX()) ||
                (index < pointsCount - 1 && x >= points[index + 1].getX()))//возвращает копию точки по индексу
            throw new InappropriateFunctionPointException("X out of order");
        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) {//возвращает координату x точки по индексу
        checkIndex(index);
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException { //устанавливает координату x точки по индексу
        checkIndex(index);
        if ((index > 0 && x <= points[index - 1].getX()) ||
                (index < pointsCount - 1 && x >= points[index + 1].getX()))//проверка сохранения упорядоченности
            throw new InappropriateFunctionPointException("X out of order");
        points[index].setX(x);
    }

    //возвращает координату y точки по индексу
    public double getPointY(int index) {
        checkIndex(index);
        return points[index].getY();
    }

    //устанавливает координату y точки по индексу
    public void setPointY(int index, double y) {
        checkIndex(index);
        points[index].setY(y);
    }

    //добавляет новую точку в массив с сохранением упорядоченности по x
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for (int i = 0; i < pointsCount; i++) {//проверка на дублирование координаты x
            if (points[i].getX() == point.getX())
                throw new InappropriateFunctionPointException("Duplicate X");
        }

        //увеличение размера массива при необходимости
        if (pointsCount == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[pointsCount + 1];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        //поиск позиции для вставки (массив упорядочен по x)
        int index = 0;
        while (index < pointsCount && points[index].getX() < point.getX()) index++;

        //сдвиг элементов для освобождения места
        System.arraycopy(points, index, points, index + 1, pointsCount - index);
        points[index] = new FunctionPoint(point);  //вставка новой точки
        pointsCount++;
    }

    public void deletePoint(int index) {//удаляет точку по указанному индексу
        checkIndex(index);
        if (pointsCount <= 2)//проверка что после удаления останется минимум 2 точки
            throw new IllegalStateException("Cannot delete: less than 3 points");
        //сдвиг элементов для удаления точки
        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
    }
}