package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
	private FunctionNode head;
	private int size;

	private static class FunctionNode {
		FunctionPoint point;
		FunctionNode prev;
		FunctionNode next;

		FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) {
			this.point = point;
			this.prev = prev;
			this.next = next;
		}
	}

	public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
		if (leftX >= rightX) {
			throw new IllegalArgumentException("leftX must be < rightX");
		}
		if (pointsCount < 2) {
			throw new IllegalArgumentException("pointsCount must be >= 2");
		}
		initEmptyList();
		double step = (rightX - leftX) / (pointsCount - 1);
		for (int i = 0; i < pointsCount; i++) {
			double x = leftX + i * step;
			addPointInternal(new FunctionPoint(x, 0.0));
		}
	}

	public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
		if (leftX >= rightX) {
			throw new IllegalArgumentException("leftX must be < rightX");
		}
		if (values == null || values.length < 2) {
			throw new IllegalArgumentException("values must contain at least two elements");
		}
		initEmptyList();
		int n = values.length;
		double step = (rightX - leftX) / (n - 1);
		for (int i = 0; i < n; i++) {
			double x = leftX + i * step;
			addPointInternal(new FunctionPoint(x, values[i]));
		}
	}

	private void initEmptyList() {
		head = new FunctionNode(null, null, null);
		head.next = head;
		head.prev = head;
		size = 0;
	}

	private void addPointInternal(FunctionPoint point) {
		FunctionNode tail = head.prev;
		FunctionNode node = new FunctionNode(point, tail, head);
		tail.next = node;
		head.prev = node;
		size++;
	}

	private FunctionNode getNodeByIndexInternal(int index) {
		if (index < 0 || index >= size) {
			return null;
		}
		FunctionNode current;
		if (index < size / 2) {
			current = head.next;
			for (int i = 0; i < index; i++) {
				current = current.next;
			}
		} else {
			current = head.prev;
			for (int i = size - 1; i > index; i--) {
				current = current.prev;
			}
		}
		return current;
	}

	@Override
	public double getLeftDomainBorder() {
		return head.next.point.getX();
	}

	@Override
	public double getRightDomainBorder() {
		return head.prev.point.getX();
	}

	@Override
	public double getFunctionValue(double x) {
		double left = getLeftDomainBorder();
		double right = getRightDomainBorder();
		if (x < left || x > right) {
			return Double.NaN;
		}
		FunctionNode current = head.next;
		for (int i = 0; i < size; i++) {
			if (Double.compare(current.point.getX(), x) == 0) {
				return current.point.getY();
			}
			FunctionNode next = current.next;
			if (next == head) break;
			double x0 = current.point.getX();
			double x1 = next.point.getX();
			if (x >= x0 && x <= x1) {
				double y0 = current.point.getY();
				double y1 = next.point.getY();
				return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
			}
			current = next;
		}
		return Double.NaN;
	}

	@Override
	public int getPointsCount() {
		return size;
	}

	@Override
	public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
		FunctionNode node = getNodeByIndexInternal(index);
		if (node == null) {
			throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
		}
		return new FunctionPoint(node.point);
	}

	@Override
	public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
		FunctionNode node = getNodeByIndexInternal(index);
		if (node == null) {
			throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
		}
		double left = (node.prev == head) ? Double.NEGATIVE_INFINITY : node.prev.point.getX();
		double right = (node.next == head) ? Double.POSITIVE_INFINITY : node.next.point.getX();
		if (point.getX() <= left || point.getX() >= right) {
			throw new InappropriateFunctionPointException("Point x is out of allowed interval");
		}
		node.point = new FunctionPoint(point);
	}

	@Override
	public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
		FunctionNode node = getNodeByIndexInternal(index);
		if (node == null) {
			throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
		}
		return node.point.getX();
	}

	@Override
	public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
		FunctionNode node = getNodeByIndexInternal(index);
		if (node == null) {
			throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
		}
		double left = (node.prev == head) ? Double.NEGATIVE_INFINITY : node.prev.point.getX();
		double right = (node.next == head) ? Double.POSITIVE_INFINITY : node.next.point.getX();
		if (x <= left || x >= right) {
			throw new InappropriateFunctionPointException("New x is out of allowed interval");
		}
		node.point.setX(x);
	}

	@Override
	public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
		FunctionNode node = getNodeByIndexInternal(index);
		if (node == null) {
			throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
		}
		return node.point.getY();
	}

	@Override
	public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
		FunctionNode node = getNodeByIndexInternal(index);
		if (node == null) {
			throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
		}
		node.point.setY(y);
	}

	@Override
	public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
		if (size < 3) {
			throw new IllegalStateException("Cannot delete point: at least three points required");
		}
		FunctionNode node = getNodeByIndexInternal(index);
		if (node == null) {
			throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
		}
		node.prev.next = node.next;
		node.next.prev = node.prev;
		size--;
	}

	@Override
	public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
		FunctionNode current = head.next;
		for (int i = 0; i < size; i++) {
			if (Double.compare(current.point.getX(), point.getX()) == 0) {
				throw new InappropriateFunctionPointException("Point with same x already exists");
			}
			current = current.next;
		}
		FunctionNode pos = head.next;
		int index = 0;
		while (index < size && pos.point.getX() < point.getX()) {
			pos = pos.next;
			index++;
		}
		FunctionNode node = new FunctionNode(new FunctionPoint(point), pos.prev, pos);
		pos.prev.next = node;
		pos.prev = node;
		size++;
	}
}


