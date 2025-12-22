package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
	private FunctionPoint[] points;
	private int size;

	public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
		if (leftX >= rightX) {
			throw new IllegalArgumentException("leftX must be < rightX");
		}
		if (pointsCount < 2) {
			throw new IllegalArgumentException("pointsCount must be >= 2");
		}
		this.points = new FunctionPoint[pointsCount];
		this.size = pointsCount;
		double step = (rightX - leftX) / (pointsCount - 1);
		for (int i = 0; i < pointsCount; i++) {
			double x = leftX + i * step;
			this.points[i] = new FunctionPoint(x, 0.0);
		}
	}

	public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
		if (leftX >= rightX) {
			throw new IllegalArgumentException("leftX must be < rightX");
		}
		if (values == null || values.length < 2) {
			throw new IllegalArgumentException("values must contain at least two elements");
		}
		int n = values.length;
		this.points = new FunctionPoint[n];
		this.size = n;
		double step = (rightX - leftX) / (n - 1);
		for (int i = 0; i < n; i++) {
			double x = leftX + i * step;
			this.points[i] = new FunctionPoint(x, values[i]);
		}
	}

	@Override
	public double getLeftDomainBorder() {
		return points[0].getX();
	}

	@Override
	public double getRightDomainBorder() {
		return points[size - 1].getX();
	}

	@Override
	public double getFunctionValue(double x) {
		double left = getLeftDomainBorder();
		double right = getRightDomainBorder();
		if (x < left || x > right) {
			return Double.NaN;
		}
		for (int i = 0; i < size; i++) {
			if (Double.compare(points[i].getX(), x) == 0) {
				return points[i].getY();
			}
		}
		for (int i = 0; i < size - 1; i++) {
			double x0 = points[i].getX();
			double x1 = points[i + 1].getX();
			if (x >= x0 && x <= x1) {
				double y0 = points[i].getY();
				double y1 = points[i + 1].getY();
				return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
			}
		}
		return Double.NaN;
	}

	@Override
	public int getPointsCount() {
		return size;
	}

	@Override
	public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
		checkIndex(index);
		return new FunctionPoint(points[index]);
	}

	@Override
	public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
		checkIndex(index);
		double left = (index == 0) ? Double.NEGATIVE_INFINITY : points[index - 1].getX();
		double right = (index == size - 1) ? Double.POSITIVE_INFINITY : points[index + 1].getX();
		if (point.getX() <= left || point.getX() >= right) {
			throw new InappropriateFunctionPointException("Point x is out of allowed interval");
		}
		points[index] = new FunctionPoint(point);
	}

	@Override
	public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
		checkIndex(index);
		return points[index].getX();
	}

	@Override
	public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
		checkIndex(index);
		double left = (index == 0) ? Double.NEGATIVE_INFINITY : points[index - 1].getX();
		double right = (index == size - 1) ? Double.POSITIVE_INFINITY : points[index + 1].getX();
		if (x <= left || x >= right) {
			throw new InappropriateFunctionPointException("New x is out of allowed interval");
		}
		points[index].setX(x);
	}

	@Override
	public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
		checkIndex(index);
		return points[index].getY();
	}

	@Override
	public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
		checkIndex(index);
		points[index].setY(y);
	}

	@Override
	public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
		if (size <= 2) {
			throw new IllegalStateException("Cannot delete point: at least three points required to delete");
		}
		checkIndex(index);
		int numMoved = size - index - 1;
		if (numMoved > 0) {
			System.arraycopy(points, index + 1, points, index, numMoved);
		}
		points[size - 1] = null;
		size--;
	}

	@Override
	public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
		for (int i = 0; i < size; i++) {
			if (Double.compare(points[i].getX(), point.getX()) == 0) {
				throw new InappropriateFunctionPointException("Point with same x already exists");
			}
		}
		int pos = 0;
		while (pos < size && points[pos].getX() < point.getX()) {
			pos++;
		}
		if (size >= points.length) {
			int newCapacity = Math.max(points.length * 2, points.length + 1);
			FunctionPoint[] newArr = new FunctionPoint[newCapacity];
			System.arraycopy(points, 0, newArr, 0, size);
			points = newArr;
		}
		if (size - pos > 0) {
			System.arraycopy(points, pos, points, pos + 1, size - pos);
		}
		points[pos] = new FunctionPoint(point);
		size++;
	}

	private void checkIndex(int index) throws FunctionPointIndexOutOfBoundsException {
		if (index < 0 || index >= size) {
			throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
		}
	}
}


