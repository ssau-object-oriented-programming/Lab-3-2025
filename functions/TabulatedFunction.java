package functions;

// Интерфейс табулированной функции: задаёт контракт для всех реализаций
public interface TabulatedFunction 
{
    // Возвращает левую границу области определения (минимальное x)
    double getLeftDomainBorder();
    
    // Вычисляет значение функции в точке x (линейная интерполяция между узлами)
    double getFunctionValue(double x);
    
    // Возвращает общее количество точек в таблице
    int getPointsCount();
    
    // Возвращает копию точки по индексу
    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;
    
    // Заменяет точку по индексу (проверяет упорядоченность по x)
    void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    
    // Возвращает x-координату точки по индексу
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;
    
    // Устанавливает x-координату точки (проверяет, чтобы не нарушить порядок)
    void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    
    // Возвращает y-координату точки по индексу
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;
    
    // Устанавливает y-координату точки
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;
    
    // Удаляет точку по индексу (требуется минимум 3 точки)
    void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException;
    
    // Добавляет новую точку в таблицу (автоматически вставляется в нужное место)
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
}