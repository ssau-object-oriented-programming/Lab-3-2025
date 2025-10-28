package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private class FunctionNode {
        private FunctionPoint point; // Данные узла - точка функции
        private FunctionNode prev; // Ссылка на предыдущий узел
        private FunctionNode next; // Ссылка на следующий узел

        public FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) { // Конструктор
            this.point = point;
            this.prev = prev;
            this.next = next;
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

    private FunctionNode head; // Голова списка
    private int pointsCount;

    // Конструктор 1
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой, левая: " + leftX + ", правая: " + rightX);
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше двух: " + pointsCount);
        }
        // Инициализация пустого списка с головой
        head = new FunctionNode(null, null, null);
        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double currentX = leftX + i * step;
            addNodeToTail(new FunctionPoint(currentX, 0.0));
        }
    }

    // Конструктор 2
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой, левая: " + leftX + ", правая: " + rightX);
        }
        if (values == null || values.length < 2) {
            throw new IllegalArgumentException("Массив должен содержать не менее 2 элементов");
        }
        // Инициализация пустого списка с головой
        head = new FunctionNode(null, null, null);
        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double currentX = leftX + i * step;
            addNodeToTail(new FunctionPoint(currentX, values[i]));
        }
    }

    public double getLeftDomainBorder() {
        if (pointsCount == 0)
            return Double.NaN;
        return head.getNext().getPoint().getX();
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0)
            return Double.NaN;
        return head.getPrev().getPoint().getX();
    }

    public double getFunctionValue(double x) {
        if (pointsCount == 0)
            return Double.NaN;
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        // Поиск интервала для интерполяции
        FunctionNode current = head.getNext();
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = current.getPoint().getX();
            double x2 = current.getNext().getPoint().getX();

            if (x >= x1 && x <= x2) {
                double y1 = current.getPoint().getY();
                double y2 = current.getNext().getPoint().getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.getNext();
        }

        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).getPoint());
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if (index > 0 && point.getX() <= getNodeByIndex(index - 1).getPoint().getX()) {
            throw new InappropriateFunctionPointException("x должен быть больше предыдущего");
        }
        if (index < pointsCount - 1 && point.getX() >= getNodeByIndex(index + 1).getPoint().getX()) {
            throw new InappropriateFunctionPointException("x должен быть меньше следующего");
        }

        node.setPoint(new FunctionPoint(point));
    }

    public double getPointX(int index) {
        return getNodeByIndex(index).getPoint().getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionPoint currentPoint = getNodeByIndex(index).getPoint();
        FunctionPoint newPoint = new FunctionPoint(x, currentPoint.getY());
        setPoint(index, newPoint);
    }

    public double getPointY(int index) {
        return getNodeByIndex(index).getPoint().getY();
    }

    public void setPointY(int index, double y) {
        FunctionNode node = getNodeByIndex(index);
        FunctionPoint currentPoint = node.getPoint();
        node.setPoint(new FunctionPoint(currentPoint.getX(), y));
    }

    public void deletePoint(int index) {
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode current = head.getNext();
        int insertIndex = 0;

        while (current != head && current.getPoint().getX() < point.getX()) {
            current = current.getNext();
            insertIndex++;
        }

        if (current != head && current.getPoint().getX() == point.getX()) {
            throw new InappropriateFunctionPointException("Точка с х " + point.getX() + " уже существует");
        }

        // Вставка новой точки
        addNodeByIndex(insertIndex, new FunctionPoint(point));
    }

    // Возвращает узел по указанному индексу
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        // Линейный поиск узла
        FunctionNode current = head.getNext();
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }
    // Добавляет узел в конец списка
    private FunctionNode addNodeToTail(FunctionPoint point) {
        // Создаем новый узел между последним узлом и головой
        FunctionNode newNode = new FunctionNode(point, head.getPrev(), head);
        // Обновляем ссылки соседних узлов
        head.getPrev().setNext(newNode);
        head.setPrev(newNode);
        pointsCount++;
        return newNode;
    }
    // Добавляет узел по указанному индексу
    private FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (index == pointsCount) {
            return addNodeToTail(point);
        }
        // Вставка в середину списка
        FunctionNode nodeIndex = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(point, nodeIndex.getPrev(), nodeIndex);

        nodeIndex.getPrev().setNext(newNode);
        nodeIndex.setPrev(newNode);
        pointsCount++;
        return newNode;
    }
    // Удаляет узел по указанному индексу
    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException("Не может быть удаленаБ должно быть >=2 точек");
        }
        // проходим сквозь удаляемый узел
        FunctionNode nodeToDelete = getNodeByIndex(index);
        nodeToDelete.getPrev().setNext(nodeToDelete.getNext());
        nodeToDelete.getNext().setPrev(nodeToDelete.getPrev());
        pointsCount--;
        return nodeToDelete;
    }
}