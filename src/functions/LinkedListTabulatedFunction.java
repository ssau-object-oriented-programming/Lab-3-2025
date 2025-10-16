package functions;


public class LinkedListTabulatedFunction implements TabulatedFunction {

    private static class FunctionNode{
        private FunctionPoint point;
        private  FunctionNode prev, next;
        private FunctionNode(FunctionPoint point){
            this.point = point;
        }
    }

    private final FunctionNode head;
    private int size;

    //Конструкто по умолчанию
    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null);
        head.prev = head;
        head.next = head;
        size = 0;
    }


    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount){
        this();
        if (leftX >= rightX || pointsCount < 2){
            throw new IllegalArgumentException();
        }
        double step = (rightX - leftX) / (pointsCount - 1);
        for(int i = 0; i < pointsCount; i++){
            FunctionNode node = addNodeToTail();
            node.point = new FunctionPoint(leftX, 0);
            leftX += step;
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        this();
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException();
        }
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            FunctionNode node = addNodeToTail();
            node.point = new FunctionPoint(leftX, values[i]);
            leftX += step;
        }
    }

    //Левая/правая граница области определения функции
    public double getLeftDomainBorder() throws InappropriateFunctionPointException {
        if (size == 0) throw new InappropriateFunctionPointException();
        return head.next.point.getX();
    }

    public double getRightDomainBorder() throws InappropriateFunctionPointException {
        if (size == 0) throw new InappropriateFunctionPointException();
        return head.prev.point.getX();
    }

    // Метод, возвращающий количество точек
    public int getPointsCount() {
        return size;
    }

    //Метод, возвращающий ссылку на объект по индексу
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        // Возвращаем копию точки, чтобы предотвратить несанкционированный доступ к точке
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    // Метод, заменяющий точку на заданную
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        FunctionNode node = getNodeByIndex(index);

        double x = point.getX();
        if ((index > 0 && x <= node.prev.point.getX()) || (index < size - 1 && x >= node.next.point.getX())) {
            throw new InappropriateFunctionPointException();
        }

        node.point = new FunctionPoint(point);
    }

    //Возвращает X точки с указанным индексом
    public double getPointX(int index) {
        return getPoint(index).getX();
    }

    // Метод, изменяющий значение абсциссы точки с указанным номером
    public void setPointX(int index, double x)throws InappropriateFunctionPointException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        FunctionNode node = getNodeByIndex(index);
        if ((index > 0 && x <= node.prev.point.getX()) || (index < size - 1 && x >= node.next.point.getX())) {
            throw new InappropriateFunctionPointException();
        }
        node.point.setX(x);
    }

    // Возвращает Y точки с указанным индексом
    public double getPointY(int index) {
        return getPoint(index).getY();
    }

    // Метод, изменяющий значение ординаты точки с указанным номером
    public void setPointY(int index, double y) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        getNodeByIndex(index).point.setY(y);
    }

    // добавление точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double x = point.getX();

        if (size == 0) {
            addNodeToTail().point = new FunctionPoint(point);
            return;
        }

        if (x < getLeftDomainBorder()) {
            FunctionNode node = addNodeByIndex(0); // добавляем в начало
            node.point = new FunctionPoint(point);
            return;

        }

        if (x > getRightDomainBorder()) {
            addNodeToTail().point = new FunctionPoint(point);
            return;
        }

        // Иначе вставляем в нужное место
        FunctionNode current = head.next;
        int index = 0;
        while (current != head && current.point.getX() < x) {
            current = current.next;
            index++;
        }

        if (current != head && current.point.getX() == x) {
            throw new InappropriateFunctionPointException();
        }
        FunctionNode node = addNodeByIndex(index);
        node.point = new FunctionPoint(point);
    }

    //Удаление точки
    public void deletePoint(int index) {
        if (size < 2) throw new IllegalStateException();
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        deleteNodeByIndex(index);
    }

    // Метод, возвращающий значение функции в точке х
    public double getFunctionValue(double x) throws InappropriateFunctionPointException {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new InappropriateFunctionPointException();
        }
        FunctionNode current = head.next;
        while (current.next != head) {
            double x1 = current.point.getX();
            double y1 = current.point.getY();
            double x2 = current.next.point.getX();
            double y2 = current.next.point.getY();

            if (x >= x1 && x <= x2) {
                return y1 + (x - x1) * (y2 - y1) / (x2 - x1);
            }
            current = current.next;
        }
        throw new InappropriateFunctionPointException();
    }

    //возвращает ссылку на объект элемента списка по его индексу
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException();
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

    //Добавление точки в конец и возвращение ссылки на этот объект
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(null);
        FunctionNode last = head.prev;

        last.next = newNode;
        newNode.prev = last;
        newNode.next = head;
        head.prev = newNode;

        size++;
        return newNode;
    }

    //Добавление элемента в список по указанному индексу и возвращение ссылки на объект этого элемента
    private FunctionNode addNodeByIndex(int index) {
        if (index == size) return addNodeToTail();
        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(null);

        newNode.next = nextNode;
        newNode.prev = nextNode.prev;
        nextNode.prev.next = newNode;
        nextNode.prev = newNode;

        size++;
        return newNode;
    }

    //удаление элемента в списке по указанному индексу
    private void deleteNodeByIndex(int index) {
        FunctionNode node = getNodeByIndex(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    //Метод вывода
    public void printTabulatedFunction() {

        for (int i = 0; i < size; i++) {
            double x = getPointX(i);
            double y = getPointY(i);
            System.out.println("x = " + x + ", y = " + y);
        }
    }
}
