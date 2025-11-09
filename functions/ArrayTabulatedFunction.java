package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] point;
    private int pointsCount;
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        if(leftX >= rightX){
            throw new IllegalArgumentException("левая граница больше или равна правой");
        }
        if(pointsCount < 2){
            throw new IllegalArgumentException("число точек меньше 2");
        }
        this.pointsCount = pointsCount;
        point = new FunctionPoint[pointsCount];
        double prom = (rightX-leftX)/(pointsCount-1);
        for(int i = 0; i < pointsCount; i++){
            double x = leftX + prom * i;
            point[i] = new FunctionPoint(x,0);
        }
    }
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        if(leftX >= rightX){
            throw new IllegalArgumentException("левая граница больше или равна правой");
        }
        if(values.length < 2){
            throw new IllegalArgumentException("число точек меньше 2");
        }
        this.pointsCount = values.length;
        point = new FunctionPoint[pointsCount];
        double prom = (rightX-leftX)/(pointsCount-1);
        for(int i = 0; i < pointsCount; i++) {
            double x = leftX + prom * i;
            double y = values[i];
            point[i] = new FunctionPoint(x, y);
        }
    }
    public double getLeftDomainBorder(){
        return point[0].getX();
    }
    public double getRightDomainBorder(){
        return point[point.length - 1].getX();
    }

    public double getFunctionValue(double x){
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        for(int i = 0; i < pointsCount; i++){
            if(point[i].getX() == x){
                return point[i].getY();
            }
        }
        for (int i = 0; i < pointsCount - 1; i++){
            double x1 = point[i].getX();
            double y1 = point[i].getY();
            double x2 = point[i + 1].getX();
            double y2 = point[i + 1].getY();
            if(x1 < x && x < x2){
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }
    public int getPointsCount(){
        return pointsCount;
    }
    public FunctionPoint getPoint(int index){
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("неверный индекс");
        }
        return new FunctionPoint(point[index]);
    }
    public void setPoint(int index, FunctionPoint p) throws InappropriateFunctionPointException{
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("неверный индекс");
        }
        double x = p.getX();
        if ((index > 0 && x <= point[index - 1].getX()) || (index < pointsCount - 1 && x >= point[index + 1].getX())){
            throw new InappropriateFunctionPointException("новая точка нарущает порядок х");
        }
        point[index] = new FunctionPoint(p);
    }
    public double getPointX(int index){
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("неверный индекс");
        }
        return point[index].getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException{
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("неверный индекс");
        }
        if ((index > 0 && x <= point[index -1].getX()) || (index < pointsCount -1 && x >= point[index +1].getX())){
            throw new InappropriateFunctionPointException("новый х нарушает порядок точек");
        }
        point[index].setX(x);
    }
    public double getPointY(int index){
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("неверный индекс");
        }
        return point[index].getY();
    }
    public void setPointY(int index, double y){
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("неверный индекс");
        }
        point[index].setY(y);
    }
    public void deletePoint(int index){
        if (pointsCount < 3){
            throw new IllegalStateException("нельзя удалить точку - останеться меньше 2");
        }
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("неверный индекс");
        }
        FunctionPoint[] newPoint = new FunctionPoint[pointsCount - 1];
        System.arraycopy(point, 0, newPoint, 0, index);
        System.arraycopy(point, index + 1, newPoint, index, pointsCount - index - 1);
        point = newPoint;
        pointsCount--;
    }
    public void addPoint(FunctionPoint p) throws InappropriateFunctionPointException{
        double x =p.getX();
        for (int i = 0; i < pointsCount; i++){
            if(point[i].getX() == x){
                throw new InappropriateFunctionPointException("точка с таким х уже существует");
            }
        }
        FunctionPoint[] newPoint = new FunctionPoint[pointsCount + 1];
        int i = 0;
        while (i < pointsCount && point[i].getX() < x){
            newPoint[i] = point[i];
            i++;
        }
        newPoint[i] = new FunctionPoint(p);
        for(int j = i; j < pointsCount; j++){
            newPoint[j + 1] = point[j];
        }
        point=newPoint;
        pointsCount++;
    }

}
