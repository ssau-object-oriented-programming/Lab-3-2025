package functions;

public class FunctionPoint {
  private double x;
  private double y;
  
  public double getX() {
    return x;
  }
  public double getY() {
    return y;
  }

  public void setX(double source_x) { x = source_x;}
  public void setY(double source_y) { y = source_y;}
  
  public FunctionPoint() {
    x = 0;
    y = 0;
  }
  public FunctionPoint(double source_x, double source_y) {
    x = source_x;
    y = source_y;
  }
  public FunctionPoint(FunctionPoint point) {
    x = point.getX();
    y = point.getY();
  }
}

