package functions;

public class FunctionPoint {
    private double x; //координата точик x
    private double y; //точки y

    //создаёт объект точки с заданными координатами
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //создаёт объект точки с теми же координатами, что у указанной точки
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    //создаёт точку с координатами (0; 0)
    public FunctionPoint() {
        this(0, 0);
    }

    public double get_x() {
        return x;
    }

    public double get_y() {
        return y;
    }

    public void set_x(double x) {
        this.x = x;
    }

    public void set_y(double y) {
        this.y = y;
    }
}
