package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {

    // ВНУТРЕННИЙ КЛАСС ДЛЯ УЗЛА СПИСКА
    // Он private, так как никто снаружи не должен знать о его существовании.
    // Это инкапсуляция.
    private class FunctionNode {
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;
    }

    private final FunctionNode head; // Голова списка, не хранит данные
    private int count; // Храним количество точек, чтобы не считать каждый раз

    // КОНСТРУКТОРЫ
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException("Invalid arguments for function creation");
        }
        this.count = pointsCount;
        this.head = new FunctionNode();
        this.head.prev = head;
        this.head.next = head;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            addNodeToTail().point = new FunctionPoint(leftX + i * step, 0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException("Invalid arguments for function creation");
        }
        this.count = values.length;
        this.head = new FunctionNode();
        this.head.prev = head;
        this.head.next = head;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            addNodeToTail().point = new FunctionPoint(leftX + i * step, values[i]);
        }
    }

    // МЕТОДЫ ДЛЯ РАБОТЫ СО СПИСКОМ (внутренняя логика)
    private FunctionNode getNodeByIndex(int index) {
        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode();
        newNode.prev = head.prev;
        newNode.next = head;
        head.prev.next = newNode;
        head.prev = newNode;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index) {
        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode();
        newNode.prev = nextNode.prev;
        newNode.next = nextNode;
        nextNode.prev.next = newNode;
        nextNode.prev = newNode;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode toDelete = getNodeByIndex(index);
        toDelete.prev.next = toDelete.next;
        toDelete.next.prev = toDelete.prev;
        toDelete.prev = null;
        toDelete.next = null;
        return toDelete;
    }

    // РЕАЛИЗАЦИЯ МЕТОДОВ ТАБУЛИРОВАННОЙ ФУНКЦИИ
    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        FunctionNode current = head.next;
        for (int i = 0; i < count; i++) {
            if (current.point.getX() <= x && x <= current.next.point.getX()) {
                double x1 = current.point.getX();
                double y1 = current.point.getY();
                double x2 = current.next.point.getX();
                double y2 = current.next.point.getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.next;
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return count;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= count) {
            throw new FunctionPointIndexOutOfBoundsException("Index is out of bounds");
        }
    }

    public FunctionPoint getPoint(int index) {
        checkIndex(index);
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        checkIndex(index);
        double newX = point.getX();
        FunctionNode node = getNodeByIndex(index);
        double leftBound = (node.prev == head) ? Double.NEGATIVE_INFINITY : node.prev.point.getX();
        double rightBound = (node.next == head) ? Double.POSITIVE_INFINITY : node.next.point.getX();
        if (newX <= leftBound || newX >= rightBound) {
            throw new InappropriateFunctionPointException("X is out of interval");
        }
        node.point = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        checkIndex(index);
        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        checkIndex(index);
        FunctionNode node = getNodeByIndex(index);
        double leftBound = (node.prev == head) ? Double.NEGATIVE_INFINITY : node.prev.point.getX();
        double rightBound = (node.next == head) ? Double.POSITIVE_INFINITY : node.next.point.getX();
        if (x <= leftBound || x >= rightBound) {
            throw new InappropriateFunctionPointException("X is out of interval");
        }
        node.point.setX(x);
    }

    public double getPointY(int index) {
        checkIndex(index);
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) {
        checkIndex(index);
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) {
        checkIndex(index);
        if (count < 3) {
            throw new IllegalStateException("Cannot delete point, less than 3 points in function");
        }
        deleteNodeByIndex(index);
        count--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode current = head.next;
        int index = 0;
        while (current != head && current.point.getX() < point.getX()) {
            current = current.next;
            index++;
        }
        if (current != head && current.point.getX() == point.getX()) {
            throw new InappropriateFunctionPointException("Point with such X already exists");
        }
        addNodeByIndex(index).point = new FunctionPoint(point);
        count++;
    }
}
