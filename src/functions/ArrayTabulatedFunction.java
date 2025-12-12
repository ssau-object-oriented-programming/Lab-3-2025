package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {

    private FunctionPoint[] funct;
    private int len;

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount)
    {
        if (leftX>=rightX){
            throw new IllegalArgumentException("The left boundary of the domain must be less than the right one");
        }
        if (pointsCount<2){
            throw new IllegalArgumentException("The number of points must be more than 2");
        }

        this.funct = new FunctionPoint[pointsCount];
        this.len = pointsCount;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            funct[i] = new FunctionPoint((leftX + i * step), 0);
        }
}

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values)
    {
        if (leftX>=rightX){
            throw new IllegalArgumentException("The left boundary of the domain must be less than the right one");
        }
        if (values.length<2){
            throw new IllegalArgumentException("The number of points must be more than 2");
        }

        this.funct = new FunctionPoint[values.length];
        this.len=values.length;
        double step = (rightX - leftX) / (len - 1);
        for (int i = 0; i < len; ++i) {
            funct[i] = new FunctionPoint((leftX + i * step), values[i]);
        }
    }

    public double getLeftDomainBorder()
    {
        return funct[0].getX();
    }

    public double getRightDomainBorder()
    {
        return funct[len-1].getX();
    }

    public double getFunctionValue(double x)
    {
        if (x<getLeftDomainBorder() || x>getRightDomainBorder()) {
            return Double.NaN;
        }
        else {
            int i = 0;
            while(i < len && funct[i].getX() < x) {
                i++;
            }
            if (funct[i].getX() == x) {
                return funct[i].getY();
            }
        else {
            double x1=funct[i-1].getX();
            double y1=funct[i-1].getY();
            double x2=funct[i].getX();
            double y2=funct[i].getY();
            double y = (y1 + (x - x1) * (y2 - y1) / (x2 - x1));
            return y;
        }

        }
    }
    public int getPointsCount()
    {
        return len;
    }
    public void getPoint(int index)
    {
        if(index > len-1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < len - 1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        if(index < len - 1 && funct[index+1].getX() > point.getX()){
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        if(index > 0 && funct[index-1].getX() < point.getX()){
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        funct[index]=new FunctionPoint(point);
    }
    public double getPointX(int index)
    {
        if(index>len-1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        return funct[index].getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < len - 1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        if(index < len - 1 && funct[index+1].getX() > x){
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        if(index > 0 && funct[index-1].getX() < x){
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        funct[index] = new FunctionPoint (x, funct[index].getY());
    }
    public double getPointY(int index)
    {
        if(index>len-1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        return funct[index].getY();
    }
    public void setPointY(int index, double y)
    {
        if(index>len-1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        funct[index] = new FunctionPoint (funct[index].getX(),y);
    }
    public void deletePoint(int index)
    {
        if(index>len-1){
            throw new FunctionPointIndexOutOfBoundsException("This point is out of bounds");
        }
        if(len<3){
            throw new IllegalStateException("Incorrect number of points");
        }
        if (index<len){
            System.arraycopy(funct,index+1, funct,index,len-index-1);
            funct[len-1]=null;
            len--;
        }
    }
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for(int i =0;i<len;++i) {
            if (funct[i].getX() == point.getX()) {
                throw new InappropriateFunctionPointException("This Point already exists");
            }
        }
        int i=0;
        while((i<len && funct[i].getX()<point.getX())){
            ++i;
        }
        if ((i<len && funct[i].getX() != point.getX()) || (i==len && funct[i-1].getX() != point.getX())){
            FunctionPoint[] array = new FunctionPoint[len+1];
            System.arraycopy(funct, 0, array, 0, i);
            array[i] = new FunctionPoint(point);
            System.arraycopy(funct, i, array, i + 1, len - i);
            len++;
            funct = array;
        }
        else {
            System.arraycopy(funct, i, funct, i + 1, len - i);
            funct[i] = new FunctionPoint(point);
            len++;
        }
    }
}

