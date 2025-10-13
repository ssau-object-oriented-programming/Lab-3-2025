package functions;
public class FunctionPoint {
    private double x;
    private double y;
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point) {
        x = point.x;
        y = point.y;
    }
    public FunctionPoint() {
        x = 0;
        y = 0;
    }
    public double getx() { //Чтобы получить доступ к значению поля x из другого класса
        return x;
    }
    public double gety() { //Чтобы получить доступ к значению поля y из другого класса
        return y;
    }
}