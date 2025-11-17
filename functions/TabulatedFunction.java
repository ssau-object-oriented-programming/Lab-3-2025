package functions;

public interface TabulatedFunction {
    // получить левую границу обл. опр.
    double getLeftDomainBorder();

    // получить правую границу обл. опр.
    double getRightDomainBorder();

    // вычислить значение ф-ции в заданной точке
    double getFunctionValue(double x);

    // получить количество точек в ф-ции
    int getPointsCount();

    // получить точку по индексу
    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;

    // установить точку по индексу
    void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    // получить координату x по индексу
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;

    // установить координату x по индексу
    void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    // получить координату y по индексу
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;

    // установить координату y по индексу
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;

    // удалить точку по индексу
    void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException;

    // добавить новую точку
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
}