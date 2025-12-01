package functions;

public class FunctionPoint {
    private double x;
    private double y;

    //Конструкторы
    //Создает объект точки с заданными координатами
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //Создает объект точки с теми же координатами, что и у указанной точки
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    //Создает точкку (0,0)
    public FunctionPoint() {
        this(0,0);
    }

    //Геттеры
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
