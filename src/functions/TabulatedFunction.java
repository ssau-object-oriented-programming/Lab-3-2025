package functions;

public interface TabulatedFunction {

    //Левая/правая граница области определения функции
    double getLeftDomainBorder() throws InappropriateFunctionPointException;
    double getRightDomainBorder() throws InappropriateFunctionPointException;

    // Метод, возвращающий значение функции в точке x
    double getFunctionValue(double x) throws InappropriateFunctionPointException;

    // Метод, возвращающий количество точек
    int getPointsCount();
    //Метод, возвращающий ссылку на объект по индексу
    FunctionPoint getPoint(int index);
    //Возвращает X точки с указанным индексом
    double getPointX(int index);
    // Возвращает Y точки с указанным индексом
    double getPointY(int index);

    // Метод, заменяющий точку по индексу на заданную
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
    // Метод, изменяющий абсциссу точки по индексу
    void setPointX(int index, double x) throws InappropriateFunctionPointException;
    // Метод, изменяющий ординату точки по индексу
    void setPointY(int index, double y);

    // Удаление точки
    void deletePoint(int index);
    // Добавление точки
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    void printTabulatedFunction();
}
