package functions;

public class LinkedListTabulatedFunction implements TabulatedFunctionImpl {
    private FunctionNode head;
    private int pointsCount;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;
    
    private static class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;
        
        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }
        
        public FunctionPoint getPoint() {
            return point;
        }
        
        public void setPoint(FunctionPoint point) {
            this.point = point;
        }
        
        public FunctionNode getPrev() {
            return prev;
        }
        
        public void setPrev(FunctionNode prev) {
            this.prev = prev;
        }
        
        public FunctionNode getNext() {
            return next;
        }
        
        public void setNext(FunctionNode next) {
            this.next = next;
        }
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        this.pointsCount = pointsCount;
        this.head = new FunctionNode(null);
        this.head.setPrev(head);
        this.head.setNext(head);
        
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = new FunctionNode(new FunctionPoint(x, 0));
            addNodeToTail(newNode);
        }
        
        this.lastAccessedNode = head.getNext();
        this.lastAccessedIndex = 0;
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Массив значений должен содержать не менее 2 элементов");
        }
        
        this.pointsCount = values.length;
        this.head = new FunctionNode(null);
        this.head.setPrev(head);
        this.head.setNext(head);
        
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = new FunctionNode(new FunctionPoint(x, values[i]));
            addNodeToTail(newNode);
        }
        
        this.lastAccessedNode = head.getNext();
        this.lastAccessedIndex = 0;
    }
    
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (pointsCount - 1) + "]");
        }
        
        if (lastAccessedNode == null || lastAccessedIndex < 0 || lastAccessedIndex >= pointsCount) {
            lastAccessedNode = head.getNext();
            lastAccessedIndex = 0;
        }
        
        int distanceFromLast = Math.abs(index - lastAccessedIndex);
        int distanceFromHead = index;
        int distanceFromTail = pointsCount - 1 - index;
        
        FunctionNode targetNode;
        int newLastIndex;
        
        if (distanceFromLast <= distanceFromHead && distanceFromLast <= distanceFromTail) {
            targetNode = lastAccessedNode;
            newLastIndex = lastAccessedIndex;
            
            if (index > lastAccessedIndex) {
                for (int i = lastAccessedIndex; i < index; i++) {
                    targetNode = targetNode.getNext();
                }
            } else if (index < lastAccessedIndex) {
                for (int i = lastAccessedIndex; i > index; i--) {
                    targetNode = targetNode.getPrev();
                }
            }
        } else if (distanceFromHead <= distanceFromTail) {
            targetNode = head.getNext();
            newLastIndex = 0;
            for (int i = 0; i < index; i++) {
                targetNode = targetNode.getNext();
            }
        } else {
            targetNode = head.getPrev();
            newLastIndex = pointsCount - 1;
            for (int i = pointsCount - 1; i > index; i--) {
                targetNode = targetNode.getPrev();
            }
        }
        
        lastAccessedNode = targetNode;
        lastAccessedIndex = index;
        
        return targetNode;
    }
    
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(new FunctionPoint(0, 0));
        return addNodeToTail(newNode);
    }
    
    private FunctionNode addNodeToTail(FunctionNode newNode) {
        FunctionNode tail = head.getPrev();
        
        newNode.setPrev(tail);
        newNode.setNext(head);
        tail.setNext(newNode);
        head.setPrev(newNode);
        
        pointsCount++;
        
        if (lastAccessedNode == head) {
            lastAccessedNode = newNode;
            lastAccessedIndex = pointsCount - 1;
        }
        
        return newNode;
    }
    
    private FunctionNode addNodeByIndex(int index) {
        FunctionNode newNode = new FunctionNode(new FunctionPoint(0, 0));
        return addNodeByIndex(index, newNode);
    }
    
    private FunctionNode addNodeByIndex(int index, FunctionNode newNode) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + pointsCount + "]");
        }
        
        if (index == pointsCount) {
            return addNodeToTail(newNode);
        }
        
        FunctionNode targetNode = getNodeByIndex(index);
        
        newNode.setPrev(targetNode.getPrev());
        newNode.setNext(targetNode);
        targetNode.getPrev().setNext(newNode);
        targetNode.setPrev(newNode);
        
        pointsCount++;
        
        if (lastAccessedIndex >= index) {
            lastAccessedIndex++;
        }
        
        return newNode;
    }
    
    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (pointsCount - 1) + "]");
        }
        
        FunctionNode targetNode = getNodeByIndex(index);
        
        targetNode.getPrev().setNext(targetNode.getNext());
        targetNode.getNext().setPrev(targetNode.getPrev());
        
        pointsCount--;
        
        if (lastAccessedIndex > index) {
            lastAccessedIndex--;
        } else if (lastAccessedIndex == index) {
            if (lastAccessedIndex >= pointsCount) {
                lastAccessedIndex = pointsCount - 1;
                lastAccessedNode = head.getPrev();
            } else {
                lastAccessedNode = targetNode.getNext();
            }
        }
        
        return targetNode;
    }
    
    @Override
    public double getLeftDomainBorder() {
        return head.getNext().getPoint().getX();
    }
    
    @Override
    public double getRightDomainBorder() {
        return head.getPrev().getPoint().getX();
    }
    
    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        
        if (x == getLeftDomainBorder()) {
            return head.getNext().getPoint().getY();
        }
        
        if (x == getRightDomainBorder()) {
            return head.getPrev().getPoint().getY();
        }
        
        FunctionNode current = head.getNext();
        while (current != head) {
            FunctionNode next = current.getNext();
            if (next == head) break;
            
            double x1 = current.getPoint().getX();
            double x2 = next.getPoint().getX();
            
            if (x >= x1 && x <= x2) {
                double y1 = current.getPoint().getY();
                double y2 = next.getPoint().getY();
                
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            
            current = next;
        }
        
        return Double.NaN;
    }
    
    @Override
    public int getPointsCount() {
        return pointsCount;
    }
    
    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return new FunctionPoint(getNodeByIndex(index).getPoint());
    }
    
    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double newX = point.getX();
        
        if (index > 0) {
            FunctionNode prevNode = node.getPrev();
            if (prevNode != head && newX <= prevNode.getPoint().getX()) {
                throw new InappropriateFunctionPointException("Координата X " + newX + " не находится в допустимом диапазоне");
            }
        }
        
        if (index < pointsCount - 1) {
            FunctionNode nextNode = node.getNext();
            if (nextNode != head && newX >= nextNode.getPoint().getX()) {
                throw new InappropriateFunctionPointException("Координата X " + newX + " не находится в допустимом диапазоне");
            }
        }
        
        node.setPoint(new FunctionPoint(point));
    }
    
    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).getPoint().getX();
    }
    
    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        
        if (index > 0) {
            FunctionNode prevNode = node.getPrev();
            if (prevNode != head && x <= prevNode.getPoint().getX()) {
                throw new InappropriateFunctionPointException("Координата X " + x + " не находится в допустимом диапазоне");
            }
        }
        
        if (index < pointsCount - 1) {
            FunctionNode nextNode = node.getNext();
            if (nextNode != head && x >= nextNode.getPoint().getX()) {
                throw new InappropriateFunctionPointException("Координата X " + x + " не находится в допустимом диапазоне");
            }
        }
        
        node.getPoint().setX(x);
    }
    
    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).getPoint().getY();
    }
    
    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        getNodeByIndex(index).getPoint().setY(y);
    }
    
    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (pointsCount <= 2) {
            throw new IllegalStateException("Невозможно удалить точку: требуется минимум 2 точки");
        }
        
        deleteNodeByIndex(index);
    }
    
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode current = head.getNext();
        while (current != head) {
            if (Math.abs(current.getPoint().getX() - point.getX()) < 1e-10) {
                throw new InappropriateFunctionPointException("Точка с координатой X " + point.getX() + " уже существует");
            }
            current = current.getNext();
        }
        
        int insertIndex = 0;
        current = head.getNext();
        while (current != head && current.getPoint().getX() < point.getX()) {
            insertIndex++;
            current = current.getNext();
        }
        
        FunctionNode newNode = new FunctionNode(new FunctionPoint(point));
        addNodeByIndex(insertIndex, newNode);
    }
}
