package functions;

/**
 * Табулированная функция на основе двусвязного циклического списка
 * с выделенной головой
 */
public class LinkedListTabulatedFunction implements TabulatedFunction {

    /**
     * Узел списка. Инкапсулирован как private static class:
     * внешний код не может получить ссылки на узлы, взаимодействие
     * идет через методы внешнего класса.
     */
    private static class FunctionNode {
        private FunctionPoint value;
        private FunctionNode next;
        private FunctionNode prev;

        FunctionNode(FunctionPoint value) {
            this.value = value == null ? null : new FunctionPoint(value);
        }
    }

    // Голова списка (не хранит данных, всегда присутствует)
    private final FunctionNode head;
    // Текущее количество значащих элементов (без головы)
    private int size;

    // Кэш последнего обращения для оптимизации getNodeByIndex
    private FunctionNode cachedNode;
    private int cachedIndex;

    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        cachedNode = null;
        cachedIndex = -1;
    }

    /* ==========================
     * Конструкторы табулированной функции
     * ========================== */

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        cachedNode = null;
        cachedIndex = -1;

        if (leftX >= rightX) {
            throw new IllegalArgumentException("The left boundary of the domain must be less than the right one");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("The number of points must be more than 2");
        }

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            addNodeToTailWithValue(new FunctionPoint(leftX + i * step, 0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {

        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        cachedNode = null;
        cachedIndex = -1;

        if (leftX >= rightX) {
            throw new IllegalArgumentException("The left boundary of the domain must be less than the right one");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("The number of points must be more than 2");
        }

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; ++i) {
            addNodeToTailWithValue(new FunctionPoint(leftX + i * step, values[i]));
        }
    }

    /* ==========================
     * Методы работы с табулированной функцией
     * ========================== */

    public double getLeftDomainBorder() {
        if (size == 0) {
            throw new IllegalStateException("Function has no points");
        }
        return head.next.value.getX();
    }

    public double getRightDomainBorder() {
        if (size == 0) {
            throw new IllegalStateException("Function has no points");
        }
        return head.prev.value.getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        FunctionNode current = head.next;
        while (current != head && current.value.getX() < x) {
            current = current.next;
        }

        if (current != head && current.value.getX() == x) {
            return current.value.getY();
        } else {
            FunctionNode prevNode = current.prev;
            double x1 = prevNode.value.getX();
            double y1 = prevNode.value.getY();
            double x2 = current.value.getX();
            double y2 = current.value.getY();
            return y1 + (x - x1) * (y2 - y1) / (x2 - x1);
        }
    }

    public int getPointsCount() {
        return size;
    }

    public void getPoint(int index) {
        FunctionNode node = getNodeByIndex(index);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Point must not be null");
        }
        FunctionNode node = getNodeByIndex(index);

        // Проверка порядка
        if (index > 0) {
            FunctionNode prev = node.prev;
            if (prev.value.getX() >= point.getX()) {
                throw new InappropriateFunctionPointException("The point disrupts the order");
            }
        }
        if (index < size - 1) {
            FunctionNode next = node.next;
            if (next.value.getX() <= point.getX()) {
                throw new InappropriateFunctionPointException("The point disrupts the order");
            }
        }
        node.value = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        FunctionNode node = getNodeByIndex(index);
        return node.value.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if (index > 0 && node.prev.value.getX() >= x) {
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        if (index < size - 1 && node.next.value.getX() <= x) {
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }

        node.value = new FunctionPoint(x, node.value.getY());
    }

    public double getPointY(int index) {
        FunctionNode node = getNodeByIndex(index);
        return node.value.getY();
    }

    public void setPointY(int index, double y) {
        FunctionNode node = getNodeByIndex(index);
        node.value = new FunctionPoint(node.value.getX(), y);
    }

    public void deletePoint(int index) {
        if (size < 3) {
            throw new IllegalStateException("Incorrect number of points");
        }
        FunctionNode node = getNodeByIndex(index);
        deleteNodeAndUpdateCache(node, index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Point must not be null");
        }

        FunctionNode current = head.next;
        int index = 0;
        while (current != head && current.value.getX() < point.getX()) {
            current = current.next;
            index++;
        }

        if (current != head && current.value.getX() == point.getX()) {
            throw new InappropriateFunctionPointException("Point with this X already exists");
        }

        FunctionNode node = new FunctionNode(point);
        insertBefore(current, node);

        if (cachedIndex >= 0 && index <= cachedIndex) {
            cachedIndex++;
        }
    }

    /**
     * Возвращает узел по индексу (0..size-1) с оптимизацией за счет кэша.
     */
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }

        // Выбор стартовой точки обхода: голова слева, голова справа или кэш
        FunctionNode startNode;
        int startIndex;

        boolean cacheValid = cachedNode != null && cachedIndex >= 0 && cachedIndex < size;
        int fromCache = cacheValid ? Math.abs(index - cachedIndex) : Integer.MAX_VALUE;
        int fromHead = index;               // расстояние от head.next
        int fromTail = size - 1 - index;    // расстояние от head.prev

        if (fromCache <= fromHead && fromCache <= fromTail) {
            startNode = cachedNode;
            startIndex = cachedIndex;
        } else if (fromHead <= fromTail) {
            startNode = head.next;
            startIndex = 0;
        } else {
            startNode = head.prev;
            startIndex = size - 1;
        }

        FunctionNode current = startNode;
        if (startIndex < index) {
            for (int i = startIndex; i < index; i++) {
                current = current.next;
            }
        } else if (startIndex > index) {
            for (int i = startIndex; i > index; i--) {
                current = current.prev;
            }
        }

        cachedNode = current;
        cachedIndex = index;
        return current;
    }

    /**
     * Добавляет узел в конец (перед головой) и возвращает его.
     */
    private FunctionNode addNodeToTail() {
        FunctionNode node = new FunctionNode(null);
        insertBefore(head, node);
        return node;
    }

    private void deleteNodeAndUpdateCache(FunctionNode node, int index) {
        unlink(node);
        if (cachedNode == node) {
            cachedNode = null;
            cachedIndex = -1;
        } else if (cachedIndex > index) {
            cachedIndex--;
        }
    }

    private FunctionNode addNodeToTailWithValue(FunctionPoint point) {
        FunctionNode node = new FunctionNode(point);
        insertBefore(head, node);
        return node;
    }

    /**
     * Добавляет узел по индексу (0..size) и возвращает его.
     * Если index == size — вставка в хвост.
     */
    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (index == size) {
            return addNodeToTail();
        }
        FunctionNode target = getNodeByIndex(index);
        FunctionNode node = new FunctionNode(null);
        insertBefore(target, node);
        // Сдвигаем кэш, если вставили раньше него
        if (cachedIndex >= index) {
            cachedIndex++;
        }
        return node;
    }

    /**
     * Удаляет узел по индексу и возвращает его (узел отсоединён).
     */
    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode node = getNodeByIndex(index);
        unlink(node);
        if (cachedNode == node) {
            cachedNode = null;
            cachedIndex = -1;
        } else if (cachedIndex > index) {
            cachedIndex--;
        }
        return node;
    }

    private void insertBefore(FunctionNode anchor, FunctionNode node) {
        node.prev = anchor.prev;
        node.next = anchor;
        anchor.prev.next = node;
        anchor.prev = node;
        size++;
    }

    private void unlink(FunctionNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
        node.next = null;
        node.prev = null;
    }
}
