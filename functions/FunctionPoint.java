package  functions;

public class FunctionPoint{
    private double x;
    private double y;

    // конструктор с параметрами
    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    // конструктор копирования
    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    // конструктор по умолчанию
    public FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }
    // получить x
    public double getX(){
        return x;
    }
    // получить y
    public double getY(){
        return y;
    }
    // установить x
    public void setX(double x){
        this.x = x;
    }
    // установить y
    public void setY(double y){
        this.y = y;
    }
}