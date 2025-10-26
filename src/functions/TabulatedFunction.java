package functions;

public interface TabulatedFunction {
    int getPointsCount(); // Возвращает количество точек в табулированной функции
    double getLeftDomainBorder(); //Возвращает левую границу области определения функции
    double getRightDomainBorder();// Возвращает правую границу области определения функции

    double getFunctionValue(double x); // Вычисляет значение функции в заданной точке x

    FunctionPoint getPoint(int index); // Возвращает точку функции по указанному индексу

    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException; //Замена точки по индексу

    double getPointX(int index); //Возвращает x-координату точки по указанному индексу
    // Устанавливает x-координату точки по указанному индексу
    void setPointX(int index, double x) throws InappropriateFunctionPointException;

    double getPointY(int index);
    void setPointY(int index, double y);

    void deletePoint(int index);

    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
}
