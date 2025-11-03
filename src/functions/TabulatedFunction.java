package functions;

public class TabulatedFunction{
    private FunctionPoint[] points;
    private int pointsCount;

    public TabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) {
            pointsCount = 2;
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount];

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    public TabulatedFunction(double leftX, double rightX, double[] values) {
        this.pointsCount = values.length;
        if (pointsCount < 2) {
            pointsCount = 2;
        }

        this.points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public double getLeftDomainBorder(){ return points[0].getX(); }

    public double getRightDomainBorder(){ return points[pointsCount-1].getX(); }

    public double getFunctionValue(double x) { 
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount; i++) {
            if (x == points[i].getX()) {
                return points[i].getY();
            }
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();

            if (x >= x1 && x <= x2) {

                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();

                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return Double.NaN;
    }

    public int getPointsCount(){ return pointsCount;}

    public FunctionPoint getPoints(int index) {
        if (index < 0 || index >= pointsCount)
            return null;
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) {
        if (index < 0 || index >= pointsCount) {
            return;
        }

        if (index > 0 && point.getX() <= points[index - 1].getX()) {
            return; 
        }
        if (index < pointsCount - 1 && point.getX() >= points[index + 1].getX()) {
            return; 
        }

        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            return Double.NaN;
        }
        return points[index].getX();
    }
    public void setPointX(int index, double x) {
        if (index < 0 || index >= pointsCount) {
            return;
        }

        if (index > 0 && x <= points[index - 1].getX()) {
            return; 
        }
        if (index < pointsCount - 1 && x >= points[index + 1].getX()) {
            return; 
        }

        points[index].setX(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            return Double.NaN;
        }
        return points[index].getY();
    }


    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            return;
        }
        points[index].setY(y);
    }


    public void deletePoint(int index) {
        if (pointsCount <= 2) {
            return;
        }

        if (index < 0 || index >= pointsCount) {
            return;
        }

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);

        pointsCount--;

        points[pointsCount] = null;
    }
    public void addPoint(FunctionPoint point) {
        int insertIndex = 0;
        while (insertIndex < pointsCount && points[insertIndex].getX() < point.getX()) {
            insertIndex++;
        }

        if (insertIndex < pointsCount && points[insertIndex].getX() == point.getX()) {
            return;
        }

        if (pointsCount == points.length) {
            int newCapacity = points.length * 3 / 2 + 1;
            FunctionPoint[] newPoints = new FunctionPoint[newCapacity];

            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        if (insertIndex < pointsCount) {
            System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        }

        points[insertIndex] = new FunctionPoint(point);

        pointsCount++;
    }
}
