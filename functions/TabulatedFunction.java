package functions;

public interface TabulatedFunction {

    // возвращение левой границы области определения
    public double getLeftDomainBorder();

    // возвращение правой границы области определения
    public double getRightDomainBorder();

    // возвращение у, если точка лежит в области определения функции
    public double getFunctionValue(double x);

    // возвращения количества точек
    public int getPointsCount();

    // возвращение точки
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException ;

    // изменение указанной точки на заданную
    public void setPoint (int index, FunctionPoint point) throws InappropriateFunctionPointException, InappropriateFunctionPointException;

    // возвращение координаты х
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException ;

    // изменение значения х
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException ;

    // возвращения координаты у
    public double getPointY (int index) throws FunctionPointIndexOutOfBoundsException;

    // изменения значения у
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException ;

    // удаление точки
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException ;

    // добавление точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    // вывод массива точек
    public void printFunc();
}
