package functions;

public class ArrayTabulatedFunction implements TabulatedFunction{
    private FunctionPoint[] arrayPoints; //массив, хранящий точки функции
    private int arrayLen; //длина массива

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        double intervalLength = 0;
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        intervalLength = Math.abs(rightX - leftX) / (pointsCount - 1); //находим длину интервала между двумя точками
        arrayLen = pointsCount;
        arrayPoints = new FunctionPoint[arrayLen];
        for (int i = 0; i < pointsCount; i++) {
            if (i != (pointsCount - 1)) { //если не последняя точка
                arrayPoints[i] = new FunctionPoint(leftX, 0);
                leftX += intervalLength; //смещаем левую границу на длину интервала
            } else {
                arrayPoints[i] = new FunctionPoint(rightX, 0); //последней точке присваиваем значение правой границы
            }
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        int pointsCount = values.length;
        arrayLen = pointsCount;
        arrayPoints = new FunctionPoint[arrayLen];
        double intervalLength = 0;
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        intervalLength = Math.abs(leftX - rightX) / (pointsCount - 1); //находим длину интервала между двумя точками

        for (int i = 0; i < pointsCount; i++) {
            if (i != (pointsCount - 1)) { //если не последняя точка
                arrayPoints[i] = new FunctionPoint(leftX, values[i]); //присваиваем значение из массива значений функции
                leftX += intervalLength; //смещаем левую границу на длину интервала
            } else {
                arrayPoints[i] = new FunctionPoint(rightX, values[i]); //последней точке присваиваем значение правой границы
            }
        }
    }

    public double getLeftDomainBorder() { //левая граница
        return arrayPoints[0].getx();
    }

    public double getRightDomainBorder() { //правая граница
        return arrayPoints[arrayLen - 1].getx();
    }

    public double getFunctionValue(double x) {
        if (x > this.getRightDomainBorder() || x < this.getLeftDomainBorder()) {//если точка вне интервала
            return Double.NaN;
        }
        if (x == this.getLeftDomainBorder()) {
            return arrayPoints[0].gety();
        }
        if (x == this.getRightDomainBorder()) {
            return arrayPoints[arrayLen - 1].gety();
        }
        int ind = 1;
        for (int i = 1; i < (arrayLen - 1); i++)
        {
            if (x >= arrayPoints[i - 1].getx() && x <= arrayPoints[i + 1].getx()) {
                ind = i; //находим индекс
            }
        }
        double X1 = getPointX(ind - 1);
        double Y1 = arrayPoints[ind - 1].gety();
        double X2 = getPointX(ind);
        double Y2 = arrayPoints[ind].gety();
        return ((Y2 - Y1) * (x - X1) / (X2 - X1)) + Y1; // ищем значение функции через уравнение прямой, проходящей через 2 точки
    }

    public int getPointsCount() { //количество точек
        return arrayLen;
    }

    public FunctionPoint getPoint(int index) { //возвращение копии точки
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        return new FunctionPoint(arrayPoints[index].getx(), arrayPoints[index].gety());
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException { //замена точки
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        double leftPoint;
        double rightPoint;
        if (index > 0) { //находим значение x слева
            leftPoint = arrayPoints[index - 1].getx();
        } else {
            leftPoint = point.getx(); //если индекс первой точки, то можем изменить значение левого x на значение новой точки
        }
        if (index < (arrayLen - 1)) { //находим значение x справа
            rightPoint = arrayPoints[index + 1].getx();
        } else {
            rightPoint = point.getx(); //если точка последняя
        }
        if (leftPoint > point.getx() || rightPoint < point.getx()) {
            throw new InappropriateFunctionPointException("Точка вне интервала");
        }
        arrayPoints[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) { //Значение x с указанным индексом
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        return arrayPoints[index].getx();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException{  //изменение x у точки с заданным индексом
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        setPoint(index, new FunctionPoint(x, arrayPoints[index].gety())); //вызываем метод, который заменяет точку на новую с измененным x
    }

    public double getPointY(int index) { //Значение y с указанным индексом
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        return arrayPoints[index].gety();
    }

    public void setPointY(int index, double y) throws InappropriateFunctionPointException{
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        setPoint(index, new FunctionPoint(arrayPoints[index].getx(), y));//вызываем метод, который заменяет точку на новую с измененным y
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= arrayLen) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        if (arrayLen < 3) {
            throw new IllegalStateException("Точек меньше трех");
        }
        for (int i = index; i < (arrayLen - 1); i++) {
            arrayPoints[i] = arrayPoints[i + 1]; //сдвигаем все элементы влево из-за удаленного
        }
        arrayLen -= 1; //длина уменьшилась на 1 элемент
        arrayPoints[arrayLen] = null; //последний элемент теперь ни на что не ссылается
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        double X = point.getx();
        int ind = 0;
        for (int i = 1; i < (arrayLen - 1); i++) {
            if (X == arrayPoints[i - 1].getx() || X == arrayPoints[i + 1].getx()) {
                throw new InappropriateFunctionPointException("Совпадение абсцисс");
            }
            if (X > arrayPoints[i - 1].getx() && X < arrayPoints[i + 1].getx()) {
                ind = i; //находим индекс для новой точки
            }
        }
        if (X > arrayPoints[arrayLen - 1].getx()) { //если x больше последнего x, добавляем в конец
            ind = arrayLen;
        }
        FunctionPoint[] arrayPoints1 = new FunctionPoint[arrayLen + 1];
        System.arraycopy(arrayPoints, 0, arrayPoints1, 0, ind); //скопировали все элементы, которые будут стоять до нового
        arrayPoints1[ind] = point; //добавили новую точку
        System.arraycopy(arrayPoints, ind, arrayPoints1, ind + 1, arrayLen - ind); //скопировали все элементы, которые будут стоять после нового
        arrayLen++;
        arrayPoints = arrayPoints1;
    }
}