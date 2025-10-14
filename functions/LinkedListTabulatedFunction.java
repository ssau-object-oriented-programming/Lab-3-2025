// LinkedListTabulatedFunction.java
package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {

    // Внутренний класс для узла списка
    private static class FunctionNode {
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;

        FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }

    private FunctionNode head; // Голова списка
    private FunctionNode lastAccessedNode; // Последний доступный узел для оптимизации
    private int lastAccessedIndex; // Индекс последнего доступного узла
    private int size; // Кол-во точек

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения >= правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Кол-во точек < 2");
        }

        initializeList();

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + step * i;
            addNodeToTail(new FunctionPoint(x, 0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values)
            throws IllegalArgumentException {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения >= правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Кол-во точек < 2");
        }

        initializeList();

        double step = (rightX - leftX) / (values.length - 1);

        for (int i = 0; i < values.length; i++) {
            double x = leftX + step * i;
            addNodeToTail(new FunctionPoint(x, values[i]));
        }
    }

    // Инициализация пустого списка
    private void initializeList() {
        head = new FunctionNode(null);
        head.prev = head;
        head.next = head;
        size = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;
    }

    // Получение узла по индексу с оптимизацией
    private FunctionNode getNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        // Начинаем с последнего доступного узла
        FunctionNode node;
        int startIndex;

        if (lastAccessedIndex != -1 && Math.abs(index - lastAccessedIndex) < Math.min(index, size - index)) {
            node = lastAccessedNode;
            startIndex = lastAccessedIndex;
        } else {
            node = head.next;
            startIndex = 0;
        }

        // Движение вперед или назад
        if (index > startIndex) {
            for (int i = startIndex; i < index; i++) {
                node = node.next;
            }
        } else if (index < startIndex) {
            for (int i = startIndex; i > index; i--) {
                node = node.prev;
            }
        }

        // Сохраняем для следующего доступа
        lastAccessedNode = node;
        lastAccessedIndex = index;

        return node;
    }

    // Добавление узла в конец списка
    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode newNode = new FunctionNode(point);
        FunctionNode tail = head.prev;

        newNode.prev = tail;
        newNode.next = head;
        tail.next = newNode;
        head.prev = newNode;

        size++;
        lastAccessedNode = newNode;
        lastAccessedIndex = size - 1;

        return newNode;
    }

    // Добавление узла по индексу
    private FunctionNode addNodeByIndex(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        if (index == size) {
            return addNodeToTail(point);
        }

        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.prev;
        FunctionNode newNode = new FunctionNode(point);

        newNode.prev = prevNode;
        newNode.next = nextNode;
        prevNode.next = newNode;
        nextNode.prev = newNode;

        size++;
        lastAccessedNode = newNode;
        lastAccessedIndex = index;

        return newNode;
    }

    // Удаление узла по индексу
    private FunctionNode deleteNodeByIndex(int index)
            throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.prev;
        FunctionNode nextNode = nodeToDelete.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        size--;

        // Обновление последнего доступного узла
        if (size == 0) {
            lastAccessedNode = head;
            lastAccessedIndex = -1;
        } else if (index == lastAccessedIndex) {
            lastAccessedNode = (index == size) ? prevNode : nextNode;
            lastAccessedIndex = (index == size) ? index - 1 : index;
        } else if (index < lastAccessedIndex) {
            lastAccessedIndex--;
        }

        return nodeToDelete;
    }

    // Реализация методов интерфейса TabulatedFunction

    public int getPointsCount() {
        return size;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        // Проверка порядка X
        if (index > 0 && point.getX() <= node.prev.point.getX()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }
        if (index < size - 1 && point.getX() >= node.next.point.getX()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }

        node.point = new FunctionPoint(point);
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        // Проверка порядка X
        if (index > 0 && x <= node.prev.point.getX()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }
        if (index < size - 1 && x >= node.next.point.getX()) {
            throw new InappropriateFunctionPointException("Новый х должен быть между соседними точками");
        }

        node.point.setX(x);
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (size <= 2) {
            throw new IllegalStateException("Невозможно удалить точку, так как кол-во точек меньше 3");
        }
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // Проверка на дублирование X
        FunctionNode current = head.next;
        while (current != head) {
            if (Math.abs(current.point.getX() - point.getX()) < 1e-10) {
                throw new InappropriateFunctionPointException("Точка с таким х уже существует");
            }
            current = current.next;
        }

        // Поиск позиции для вставки
        int index = 0;
        current = head.next;
        while (current != head && current.point.getX() < point.getX()) {
            current = current.next;
            index++;
        }

        addNodeByIndex(index, new FunctionPoint(point));
    }

    public double getLeftDomainBorder() {
        if (size == 0) return Double.NaN;
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        if (size == 0) return Double.NaN;
        return head.prev.point.getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        // Поиск интервала, содержащего x
        FunctionNode current = head.next;
        while (current.next != head) {
            double x1 = current.point.getX();
            double x2 = current.next.point.getX();

            if (x >= x1 && x <= x2) {
                double y1 = current.point.getY();
                double y2 = current.next.point.getY();

                // Линейная интерполяция
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.next;
        }

        return Double.NaN;
    }
}