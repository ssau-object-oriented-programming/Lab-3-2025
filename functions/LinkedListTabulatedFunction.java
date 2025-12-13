package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private static class FunctionNode {
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;
        
        FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }
    
    private FunctionNode head;
    private int size;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;
    
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointCount) throws IllegalArgumentException {
    if (leftX >= rightX || pointCount < 2) {
        throw new IllegalArgumentException("Invalid parameters: leftX must be less than rightX and pointCount >= 2");
    }
    
    initHead();
    this.size = 0;  // Начинаем с 0, будем увеличивать при добавлении
    
    double step = (rightX - leftX) / (pointCount - 1);
    for (int i = 0; i < pointCount; i++) {
        double x = leftX + i * step;
        FunctionNode newNode = addNodeToTail();
        newNode.point = new FunctionPoint(x, 0);
    }
    
    lastAccessedNode = (size > 0) ? head.next : head;
    lastAccessedIndex = 0;
}
    
    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) throws IllegalArgumentException {
    if (xValues.length < 2 || xValues.length != yValues.length) {
        throw new IllegalArgumentException("Arrays must have same length and at least 2 elements");
    }
    
    for (int i = 1; i < xValues.length; i++) {
        if (xValues[i] <= xValues[i - 1]) {
            throw new IllegalArgumentException("X values must be strictly increasing");
        }
    }
    
    initHead();
    this.size = 0;  // Начинаем с 0
    
    for (int i = 0; i < xValues.length; i++) {
        FunctionNode newNode = addNodeToTail();
        newNode.point = new FunctionPoint(xValues[i], yValues[i]);
    }
    
    lastAccessedNode = (size > 0) ? head.next : head;
    lastAccessedIndex = 0;
}
    
    private void initHead() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
    }
    
    private FunctionNode getNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        
        // Optimization: start from last accessed node if possible
        if (lastAccessedNode != null && Math.abs(index - lastAccessedIndex) < Math.min(index, size - index)) {
            if (index > lastAccessedIndex) {
                // Move forward
                FunctionNode current = lastAccessedNode;
                for (int i = lastAccessedIndex; i < index; i++) {
                    current = current.next;
                }
                lastAccessedNode = current;
                lastAccessedIndex = index;
                return current;
            } else if (index < lastAccessedIndex) {
                // Move backward
                FunctionNode current = lastAccessedNode;
                for (int i = lastAccessedIndex; i > index; i--) {
                    current = current.prev;
                }
                lastAccessedNode = current;
                lastAccessedIndex = index;
                return current;
            } else {
                return lastAccessedNode;
            }
        }
        
        // Start from beginning
        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        
        lastAccessedNode = current;
        lastAccessedIndex = index;
        return current;
    }
    
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(null);
        FunctionNode tail = head.prev;
        
        newNode.prev = tail;
        newNode.next = head;
        tail.next = newNode;
        head.prev = newNode;
        
        size++;
        return newNode;
    }
    
    private FunctionNode addNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        
        if (index == size) {
            return addNodeToTail();
        }
        
        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.prev;
        
        FunctionNode newNode = new FunctionNode(null);
        newNode.prev = prevNode;
        newNode.next = nextNode;
        prevNode.next = newNode;
        nextNode.prev = newNode;
        
        size++;
        
        if (index <= lastAccessedIndex) {
            lastAccessedIndex++;
        }
        
        return newNode;
    }
    
    private FunctionNode deleteNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        
        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.prev;
        FunctionNode nextNode = nodeToDelete.next;
        
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        
        size--;
        
        // Update lastAccessedNode if it was deleted or affected
        if (nodeToDelete == lastAccessedNode) {
            lastAccessedNode = (nextNode != head) ? nextNode : head.next;
        } else if (index < lastAccessedIndex) {
            lastAccessedIndex--;
        }
        
        return nodeToDelete;
    }
    
    @Override
    public int getPointCount() {
        return size;
    }
    
    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).point.getX();
    }
    
    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).point.getY();
    }
    
    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        
        if ((index > 0 && x <= node.prev.point.getX()) || 
            (index < size - 1 && x >= node.next.point.getX())) {
            throw new InappropriateFunctionPointException(x);
        }
        
        node.point.setX(x);
    }
    
    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        getNodeByIndex(index).point.setY(y);
    }
    
    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return (FunctionPoint) getNodeByIndex(index).point.clone();
    }
    
    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double x = point.getX();
        
        if ((index > 0 && x <= node.prev.point.getX()) || 
            (index < size - 1 && x >= node.next.point.getX())) {
            throw new InappropriateFunctionPointException(x);
        }
        
        node.point = (FunctionPoint) point.clone();
    }
    
    @Override
    public double getFunctionValue(double x) {
        if (size == 0 || x < head.next.point.getX() || x > head.prev.point.getX()) {
            return Double.NaN;
        }
        
        FunctionNode current = head.next;
        while (current != head) {
            if (FunctionPoint.equals(current.point.getX(), x)) {
                return current.point.getY();
            }
            
            if (current.next != head && x >= current.point.getX() && x <= current.next.point.getX()) {
                // linear interpolation
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
    
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // Check if point with same x exists
        FunctionNode current = head.next;
        while (current != head) {
            if (FunctionPoint.equals(current.point.getX(), point.getX())) {
                throw new InappropriateFunctionPointException(point.getX());
            }
            current = current.next;
        }
        
        // Find position to insert
        current = head.next;
        int index = 0;
        while (current != head && current.point.getX() < point.getX()) {
            current = current.next;
            index++;
        }
        
        FunctionNode newNode = addNodeByIndex(index);
        newNode.point = (FunctionPoint) point.clone();
    }
    
    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (size < 3) {
            throw new IllegalStateException("Cannot delete point: minimum 3 points required");
        }
        
        deleteNodeByIndex(index);
    }
    
    @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    FunctionNode current = head.next;
    boolean first = true;
    while (current != head) {
        if (!first) sb.append(", ");
        sb.append(current.point);
        current = current.next;
        first = false;
    }
    sb.append("}");
    return sb.toString();
}
}