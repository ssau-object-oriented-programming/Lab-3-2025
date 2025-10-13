package functions;

public class FunctionPoint {
    private double x;
    private double y;

    // Создаёт объект точки с координатами
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Создаёт объект точки с теми же координатами точки
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    // Создаёт объект точки с координатами (0;0)
    public FunctionPoint() {
        this.x = 0;
        this.y = 0;
    }

    // Задать X
    public void setX(double x) {
        this.x = x;
    }

    // Получить X
    public double getX() {
        return x;
    }

    // Задать Y
    public void setY(double y) {
        this.y = y;
    }

    // Получить Y
    public double getY() {
        return y;
    }
}