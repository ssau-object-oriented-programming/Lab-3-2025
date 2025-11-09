package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private static class FunctionNode {
        FunctionPoint data;
        FunctionNode next;
        FunctionNode prev;

        FunctionNode(FunctionPoint data) {
            this.data = data;
        }
    }

    private final FunctionNode head;
    private int pointsCount;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Число точек меньше 2");
        }

        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.pointsCount = 0;
        this.lastAccessedNode = head;
        this.lastAccessedIndex = -1;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + step * i;
            FunctionPoint fp = new FunctionPoint(x, 0);
            addPointInternal(fp);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Число точек меньше 2");
        }

        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.pointsCount = 0;
        this.lastAccessedNode = head;
        this.lastAccessedIndex = -1;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + step * i;
            double y = values[i];
            addPointInternal(new FunctionPoint(x, y));
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }

        int fromHeadDist = index;
        int fromTailDist = pointsCount - index - 1;
        int fromLastDist = Integer.MAX_VALUE;
        if (lastAccessedIndex >= 0) {
            fromLastDist = Math.abs(index - lastAccessedIndex);
        }

        FunctionNode current;
        int currentIndex;

        if (fromHeadDist <= fromTailDist && fromHeadDist <= fromLastDist) {
            current = head.next;
            currentIndex = 0;
            while (currentIndex < index) {
                current = current.next;
                currentIndex++;
            }
        } else if (fromTailDist < fromHeadDist && fromTailDist <= fromLastDist) {
            current = head.prev;
            currentIndex = pointsCount - 1;
            while (currentIndex > index) {
                current = current.prev;
                currentIndex--;
            }
        } else {

            current = lastAccessedNode;
            currentIndex = lastAccessedIndex;
            if (currentIndex <= index) {
                while (currentIndex < index) {
                    current = current.next;
                    currentIndex++;
                }
            } else {
                while (currentIndex > index) {
                    current = current.prev;
                    currentIndex--;
                }
            }
        }


        lastAccessedNode = current;
        lastAccessedIndex = index;
        return current;
    }

    private FunctionNode addNodeToTailInternal(FunctionPoint fp) {
        FunctionNode node = new FunctionNode(fp);
        FunctionNode tail = head.prev;

        tail.next = node;
        node.prev = tail;
        node.next = head;
        head.prev = node;

        lastAccessedNode = node;
        lastAccessedIndex = pointsCount;
        pointsCount++;
        return node;
    }

    private FunctionNode addNodeByIndexInternal(int index, FunctionPoint fp) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        if (index == pointsCount) {
            return addNodeToTailInternal(fp);
        } else {

            FunctionNode nextNode = getNodeByIndex(index);
            FunctionNode prevNode = nextNode.prev;
            FunctionNode newNode = new FunctionNode(fp);

            prevNode.next = newNode;
            newNode.prev = prevNode;
            newNode.next = nextNode;
            nextNode.prev = newNode;

            lastAccessedNode = newNode;
            lastAccessedIndex = index;
            pointsCount++;
            return newNode;
        }
    }

    private FunctionNode deleteNodeByIndexInternal(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        FunctionNode node = getNodeByIndex(index);
        FunctionNode prevNode = node.prev;
        FunctionNode nextNode = node.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        lastAccessedNode = head;
        lastAccessedIndex = -1;

        pointsCount--;
        node.next = null;
        node.prev = null;
        return node;
    }

    private void addPointInternal(FunctionPoint p) {
        FunctionPoint copy = new FunctionPoint(p);
        addNodeToTailInternal(copy);
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.data);
    }

    public void setPoint(int index, FunctionPoint p) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        double x = p.getX();

        if (index > 0) {
            double leftX = getNodeByIndex(index - 1).data.getX();
            if (x <= leftX) {
                throw new InappropriateFunctionPointException("Новая точка нарушает порядок X (меньше или равна левой)");
            }
        }
        if (index < pointsCount - 1) {
            double rightX = getNodeByIndex(index + 1).data.getX();
            if (x >= rightX) {
                throw new InappropriateFunctionPointException("Новая точка нарушает порядок X (больше или равна правой)");
            }
        }

        FunctionNode node = getNodeByIndex(index);
        node.data = new FunctionPoint(p);

        lastAccessedNode = node;
        lastAccessedIndex = index;
    }

    public double getPointX(int index) {
        return getPoint(index).getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }

        if (index > 0) {
            double leftX = getNodeByIndex(index - 1).data.getX();
            if (x <= leftX) {
                throw new InappropriateFunctionPointException("Новое x нарушает порядок (<= left)");
            }
        }

        if (index < pointsCount - 1) {
            double rightX = getNodeByIndex(index + 1).data.getX();
            if (x >= rightX) {
                throw new InappropriateFunctionPointException("Новое x нарушает порядок (>= right)");
            }
        }

        FunctionNode node = getNodeByIndex(index);
        node.data.setX(x);

        lastAccessedNode = node;
        lastAccessedIndex = index;
    }

    public double getPointY(int index) {
        return getPoint(index).getY();
    }

    public void setPointY(int index, double y) {
        FunctionNode node = getNodeByIndex(index);
        node.data.setY(y);
        lastAccessedNode = node;
        lastAccessedIndex = index;
    }

    public void deletePoint(int index) {
        if (pointsCount < 3) {
            throw new IllegalStateException("Нельзя удалить точку — останется менее двух точек");
        }

        deleteNodeByIndexInternal(index);

    }

    public void addPoint(FunctionPoint p) throws InappropriateFunctionPointException {
        double x = p.getX();
        for (int i = 0; i < pointsCount; i++) {
            if (getNodeByIndex(i).data.getX() == x) {
                throw new InappropriateFunctionPointException("Точка с таким x уже существует: " + x);
            }
        }

        int index = 0;
        while (index < pointsCount && getNodeByIndex(index).data.getX() < x) {
            index++;
        }
        addNodeByIndexInternal(index, new FunctionPoint(p));
    }

    public double getFunctionValue(double x) {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        double left = head.next.data.getX();
        double right = head.prev.data.getX();
        if (x < left || x > right) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount; i++) {
            FunctionNode node = getNodeByIndex(i);
            if (node.data.getX() == x) {
                return node.data.getY();
            }
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            FunctionNode n1 = getNodeByIndex(i);
            FunctionNode n2 = getNodeByIndex(i + 1);
            double x1 = n1.data.getX();
            double y1 = n1.data.getY();
            double x2 = n2.data.getX();
            double y2 = n2.data.getY();
            if (x1 < x && x < x2) {
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }

    public double getLeftDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.next.data.getX();
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.prev.data.getX();
    }
}
