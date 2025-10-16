package functions;


public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points;
    private int size;

    // Конструкторы
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        if (leftX >= rightX || pointsCount < 2){
            throw new IllegalArgumentException();
        }
        points = new FunctionPoint[pointsCount*2];
        size = pointsCount;
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0;i < size;i++){
            points[i] = new FunctionPoint(leftX + i*step,0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        if (leftX >= rightX || values.length  < 2){
            throw new IllegalArgumentException();
        }
        points = new FunctionPoint[values.length*2];
        size = values.length;

        double step = (rightX - leftX)/ (size -1);
        for(int i = 0; i < size;i++){
            points[i] = new FunctionPoint(leftX + i*step,values[i]);
        }
    }

    //Левая/правая граница области определения функции
    public double getLeftDomainBorder() throws InappropriateFunctionPointException{
        if (size ==0){
            throw new InappropriateFunctionPointException();
        }
        return points[0].getX();
    }

    public double getRightDomainBorder() throws InappropriateFunctionPointException{
        if (size ==0){
            throw new InappropriateFunctionPointException();
        }
        return points[size -1 ].getX();
    }

    // Метод, возвращающий значение функции в точке x
    public double getFunctionValue(double x) throws InappropriateFunctionPointException{
        if ((x < getLeftDomainBorder() || x > getRightDomainBorder()) || (size ==0)){
            throw  new InappropriateFunctionPointException();
        }
        for (int i = 0; i < size - 1; i++) {
            if (x >= points[i].getX() && x <= points[i + 1].getX()) {

                double x1 = points[i].getX();
                double y1 = points[i].getY();
                double x2 = points[i + 1].getX();
                double y2 = points[i + 1].getY();


                return (y1 + (x - x1) * (y2 - y1) / (x2 - x1));
            }
        }
        throw  new InappropriateFunctionPointException();
    }

    // Метод, возвращающий количество точек
    public int getPointsCount(){
        return size;
    }

    // Метод, возвращающий длину массива с учетом запаса
    public int getRealLength() {

        return points.length;
    }

    //Метод, возвращающий ссылку на объект по индексу
     public FunctionPoint getPoint(int index) {
        if (index >= 0 && index < size) {
            return new FunctionPoint(points[index]);
        }
        else{
             throw new FunctionPointIndexOutOfBoundsException();
        }
    }

    // Метод, заменяющий точку на заданную
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException();
        double newX = point.getX();
        if ((index > 0 && newX <= points[index - 1].getX()) || (index < size -1  && newX >=points[index +1].getX())) {
            throw new InappropriateFunctionPointException();
        }
        points[index] = new FunctionPoint(point);

    }

    //Возвращает X точки с указанным индексом
    public double getPointX(int index) {
        if (index < 0 || index >= size){
            throw new FunctionPointIndexOutOfBoundsException();
        }

        return points[index].getX();
    }

    // Метод, изменяющий значение абсциссы точки с указанным номером
    public void setPointX(int index,double x) throws InappropriateFunctionPointException  {
        if (index < 0 || index >= size){
            throw new FunctionPointIndexOutOfBoundsException();
        }
        if ((index > 0 && x <= points[index - 1].getX()) || (index < size-1 && x >=points[index +1].getX())) {
            throw new InappropriateFunctionPointException() ;
        }
        points[index] = new FunctionPoint(x,points[index].getY());
    }

    // Возвращает Y точки с указанным индексом
    public double getPointY(int index ){
        if (index < 0 || index >= size){
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return points[index].getY();
    }

    // Метод, изменяющий значение ординаты точки с указанным номером
    public void setPointY(int index, double y) {
        if (index < 0 || index >= size){
            throw new FunctionPointIndexOutOfBoundsException();
        }
        points[index] = new FunctionPoint(points[index].getX(),y);
    }

    // Удаление точки с указанным номером
    public void deletePoint(int index){
        if (size  < 2){
            throw new IllegalStateException();
        }
        if (index >= 0 && index < size){
            --size;
            System.arraycopy(points,index+1, points, index,size - index);
        }
        else{
            throw new FunctionPointIndexOutOfBoundsException();
        }
    }

    // добавление точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        double x = point.getX();

        for (int i =0; i < size; ++i){
            if (points[i].getX() == x){
                throw new InappropriateFunctionPointException();
            }
        }

        // если за пределами левой границы
        if (x < getLeftDomainBorder()){
            // если запас длины  в массиве еще есть
            if (size < points.length){
                System.arraycopy(points, 0, points, 1,size);
                points[0] = point;
                ++size;
            }
            else {
                FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];;
                System.arraycopy(points, 0, newPoints, 1, points.length);
                newPoints[0] = point;
                points = newPoints;
                ++size;
            }
        }
        // если за пределами правой границы
        else if (x > getRightDomainBorder()){
            if (size < points.length){
                points[size] = point;
                ++size;
            }
            else{
                FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
                System.arraycopy(points, 0, newPoints,0,points.length);
                newPoints[size] = point;
                points = newPoints;
                ++size;
            }
        }
        else{
            if (size < points.length){
                int index = 0;
                while(index < size && x > points[index].getX()){
                    ++index;
                }
                System.arraycopy(points, index,points,index + 1,size -index);
                points[index] = point;
                ++size;
            }
            else{
                FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
                int index = 0;
                while(index < size && x > points[index].getX()){
                    ++index;
                }
                System.arraycopy(points, 0, newPoints, 0, index);
                System.arraycopy(points, index, newPoints, index + 1, size - index);
                newPoints[index] = point;
                points = newPoints;
                ++size;
            }
        }

    }

    //Метод вывода
    public void printTabulatedFunction() {
        int pointsCount = getPointsCount();
        for (int i = 0; i < pointsCount; i++) {
            double x = getPointX(i);
            double y = getPointY(i);
            System.out.println("x = " + x + ", y = " + y);
        }
    }
}
