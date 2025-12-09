package functions;

public interface TabulatedFunction {
    
    // Методы доступа к области определения
    double getLeftDomainBorder();
    double getRightDomainBorder();
    
    // Методы работы с функцией
    double getFunctionValue(double x);
    int getPointsCount();
    
    // Методы работы с точками
    FunctionPoint getPoint(int index);
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
    
    // Методы работы с координатами точек
    double getPointX(int index);
    void setPointX(int index, double x) throws InappropriateFunctionPointException;
    double getPointY(int index);
    void setPointY(int index, double y);
    
    // Методы модификации набора точек
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    void deletePoint(int index);
}