package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {

    private class FunctionNode {
        public FunctionPoint value;
        public FunctionNode next;
        public FunctionNode prev;
    }

    private FunctionNode head;
    private int capacity;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;
    private double DEFAULT_EPSILON = 1e-9;

    private FunctionNode getNodeByIndex(int index) {
        FunctionNode answer;
        int curIndex;
        if (index < lastAccessedIndex) {
            if (index < lastAccessedIndex - index) {
                curIndex = 0;
                answer = head.next;
                while (curIndex != index) {
                    answer = answer.next;
                    curIndex++;
                }
            } else {
                curIndex = lastAccessedIndex;
                answer = lastAccessedNode;
                while (curIndex != index) {
                    answer = answer.prev;
                    curIndex--;
                }
            }
        } else {
            if (index - lastAccessedIndex < capacity - lastAccessedIndex - 1) {
                curIndex = lastAccessedIndex;
                answer = lastAccessedNode;
                while (curIndex != index) {
                    answer = answer.next;
                    curIndex++;
                }
            } else {
                curIndex = capacity - 1;
                answer = head.prev;
                while (curIndex != index) {
                    answer = answer.prev;
                    curIndex--;
                }
            }

        }
        lastAccessedNode = answer;
        lastAccessedIndex = index;
        return answer;
    }
    private FunctionNode addNodeToTail() {
        FunctionNode newTail = new FunctionNode();
        FunctionNode oldTail = head.prev;
        oldTail.next = newTail;
        newTail.prev = oldTail;
        newTail.next = head;
        head.prev = newTail;
        capacity++;
        lastAccessedNode = newTail;
        lastAccessedIndex = capacity - 1;
        return newTail;
    }
    private FunctionNode addNodeByIndex(int index) {
        FunctionNode newNode = new FunctionNode();
        FunctionNode rightNode = getNodeByIndex(index);
        FunctionNode leftNode = rightNode.prev;
        leftNode.next = newNode;
        newNode.prev = leftNode;
        newNode.next = rightNode;
        rightNode.prev = newNode;
        capacity++;
        lastAccessedNode = newNode;
        lastAccessedIndex = index;
        return newNode;
    }
    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode delNote = getNodeByIndex(index);
        delNote.prev.next = delNote.next;
        delNote.next.prev = delNote.prev;
        capacity--;
        lastAccessedNode = head.next;
        lastAccessedIndex = 0;
        return delNote;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException("Левая граница области должна быть меньше правой, количество точек должно быть больше 2");
        }
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        capacity = 0;
        FunctionNode curNode;
        double step = (rightX - leftX) / (pointsCount - 1);
        while (capacity != pointsCount) {
            curNode = this.addNodeToTail();
            curNode.value = new FunctionPoint(leftX + (capacity - 1) * step, 0);
        }
    }
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException("Левая граница области должна быть меньше правой, количество точек должно быть больше 2");
        }
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        capacity = 0;
        FunctionNode curNode;
        double step = (rightX - leftX) / (values.length - 1);
        while (capacity != values.length) {
            curNode = this.addNodeToTail();
            curNode.value = new FunctionPoint(leftX + (capacity - 1) * step, values[capacity - 1]);
        }
    }

    public double getLeftDomainBorder() { return this.getNodeByIndex(0).value.getX();}
    public double getRightDomainBorder() { return this.getNodeByIndex(capacity - 1).value.getX();}
    public double getFunctionValue(double x) {
        if (x < this.getNodeByIndex(0).value.getX() || x > this.getNodeByIndex(capacity - 1).value.getX()) {return Double.NaN;}
        for (int t = 0; t < capacity; t++) {
            if (Math.abs(x - this.getNodeByIndex(t).value.getX()) < DEFAULT_EPSILON) {return this.getNodeByIndex(t).value.getY();}
        }
        int t = 0;
        while (x > this.getNodeByIndex(t).value.getX()) {
            t++;
        }
        double x0 = this.getNodeByIndex(t - 1).value.getX();
        double x1 = this.getNodeByIndex(t).value.getX();
        double y0 = this.getNodeByIndex(t - 1).value.getY();
        double y1 = this.getNodeByIndex(t).value.getY();
        return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
    }

    public int getPointsCount() {return capacity;}
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        return new FunctionPoint(this.getNodeByIndex(index).value);
    }
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        if ((index == 0 && point.getX() >= this.getNodeByIndex(1).value.getX()) || (index == capacity - 1 && point.getX() <= this.getNodeByIndex(capacity - 2).value.getX())) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        if (point.getX() <= this.getNodeByIndex(index - 1).value.getX() || point.getX() >= this.getNodeByIndex(index + 1).value.getX()) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        this.getNodeByIndex(index).value = new FunctionPoint(point);
    }
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        return this.getNodeByIndex(index).value.getX();
    }
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        if ((index == 0 && x >= this.getNodeByIndex(1).value.getX()) || (index == capacity - 1 && x <= this.getNodeByIndex(capacity - 2).value.getX())) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        if (x <= this.getNodeByIndex(index - 1).value.getX() || x >= this.getNodeByIndex(index + 1).value.getX()) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        this.getNodeByIndex(index).value.setX(x);
    }
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        return this.getNodeByIndex(index).value.getY();
    }
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        this.getNodeByIndex(index).value.setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (capacity < 3) {
            throw new IllegalStateException("В таблице менее трех точек");
        }
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        this.deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for (int t = 0; t < capacity; t++) {
            if (Math.abs(point.getX() - this.getNodeByIndex(t).value.getX()) <= DEFAULT_EPSILON) {
                throw new InappropriateFunctionPointException("Точка с такой с такой же абсциссой уже имеется");
            }
        }
        FunctionPoint copy = new FunctionPoint(point);
        int pose = 0;
        while (pose < capacity && this.getNodeByIndex(pose).value.getX() < copy.getX()) {
            pose++;
        }
        if (pose == capacity) {
            this.addNodeToTail().value = copy;
        } else {
            this.addNodeByIndex(pose).value = copy;
        }
    }


}
