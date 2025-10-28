package functions;

public class FunctionPoint {
    private double x;
    private double y;

    public FunctionPoint(double x, double y){ // создает обЬект точки с заданными координатами
        this.x=x;
        this.y=y;
    }
    public FunctionPoint(FunctionPoint point){ // создает обЬект точки с теми же координатами что у указанной точки
        this.x=point.x;
        this.y=point.y;
    }
    public FunctionPoint(){  // создает точку с координатами 0.0
        this(0.0,0.0);
    }
    public  double getX(){//получение доступа к значению поля x из другого класса
        return x;
    }
    public void setX(double x){
        this.x=x;
    }
    public  double getY(){//получение доступа к значению поля y из другого класса
        return y;
    }
    public void setY(double y){
        this.y=y;
    }
}

