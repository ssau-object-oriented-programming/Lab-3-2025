package functions; // Указываем, что класс находится в пакете functions

// Описывает одну точку табулированной функции (пара x и y).
public class FunctionPoint {
    private double x;
    private double y;

    public FunctionPoint(double x, double y) { // создаёт объект точки с заданными координатами
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point) { // создаёт объект точки с теми же координатами, что у указанной точки
        this.x = point.x;
        this.y = point.y;
    }
    public FunctionPoint() { // создаёт точку с координатами (0; 0)
        this.x = 0;
        this.y = 0;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
}
