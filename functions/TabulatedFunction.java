// TabulatedFunction.java
package functions;

public interface TabulatedFunction {
    // Получение количества точек
    int getPointsCount();

    // Получение точки по индексу
    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;

    // Замена точки
    void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    // Получение координаты X точки
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;

    // Изменение координаты X точки
    void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;

    // Получение координаты Y точки
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;

    // Изменение координаты Y точки
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;

    // Удаление точки
    void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException;

    // Добавление точки
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    // Получение левой границы области определения
    double getLeftDomainBorder();

    // Получение правой границы области определения
    double getRightDomainBorder();

    // Получение значения функции в точке
    double getFunctionValue(double x);
}