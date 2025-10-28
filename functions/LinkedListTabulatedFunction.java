package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {//внутреннний класс для узла списка
    private static class FunctionNode {
        FunctionPoint point; //точка функции
        FunctionNode next; //ссылка на след узел
        FunctionNode prev; //ссылка на пред узел
        FunctionNode(FunctionPoint p) { point = p; } //конструктор узла
    }

    private final FunctionNode head = new FunctionNode(null); // выделенная голова
    private int pointsCount; //количество точек в функции

    public LinkedListTabulatedFunction(double leftX, double rightX, int count) {  //конструктор 1 создает функцию с равномерно распределенными точками по х
        if (leftX >= rightX) //проверка корректности входных данных
            throw new IllegalArgumentException("Left >= Right");
        if (count < 2)
            throw new IllegalArgumentException("Points count < 2");

        head.next = head.prev = head; // циклический список
        pointsCount = 0;
        double step = (rightX - leftX) / (count - 1); //вычисление шага между точками
        for (int i = 0; i < count; i++)
            addPoint(new FunctionPoint(leftX + i * step, 0));//создание count точек с координатой у = 0
    }
    //конструктор 2 создает функцию с заданными значениями у
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Left >= Right");//проверка корректности входных данных
        if (values.length < 2)
            throw new IllegalArgumentException("Points count < 2");

        head.next = head.prev = head;  //инициализация циклического списка
        pointsCount = 0;
        double step = (rightX - leftX) / (values.length - 1);//вычисление шага между точками
        for (int i = 0; i < values.length; i++)
            addPoint(new FunctionPoint(leftX + i * step, values[i]));
    }

    private FunctionNode getNodeByIndex(int index) { //метод для получения узла по индексу
        if (index < 0 || index >= pointsCount) //проверка выхода за границы
            throw new FunctionPointIndexOutOfBoundsException("Index " + index + " out of bounds");
        FunctionNode node = head.next; //поиск узла путем последовательного прохода по списку
        for (int i = 0; i < index; i++)
            node = node.next;
        return node;
    }

    public int getPointsCount() { return pointsCount; } //возвращает количество точек в функции
    public double getLeftDomainBorder() { return head.next.point.getX(); }//возвращает левую границу области определения (X первой точки)

    public double getRightDomainBorder() { return head.prev.point.getX(); } //возвращает правую границу области определения (X последней точки)

    public double getFunctionValue(double x) { //вычисляет значение функции в точке x с помощью линейной интерполяции
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) return Double.NaN;//проверка что x находится в области определения
        FunctionNode node = head.next;
        while (node.next != head) { //поиск интервала, содержащего x
            double x1 = node.point.getX();
            double x2 = node.next.point.getX();
            if (x == x1) return node.point.getY();
            if (x > x1 && x < x2)
                return node.point.getY() + (node.next.point.getY() - node.point.getY()) * (x - x1) / (x2 - x1);
            node = node.next;
        }
        if (x == head.prev.point.getX()) return head.prev.point.getY();
        return Double.NaN;
    }
    //возвращает копию точки по индексу
    public FunctionPoint getPoint(int index) { return new FunctionPoint(getNodeByIndex(index).point); }
    //заменяет точку по указанному индексу
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double x = point.getX();
        if ((node.prev != head && x <= node.prev.point.getX()) ||//проверка, что новое значение X сохраняет упорядоченность списка
                (node.next != head && x >= node.next.point.getX()))
            throw new InappropriateFunctionPointException("X out of order");
        node.point = new FunctionPoint(point); //создание копии точки для защиты от изменений
    }
    //возвращает координату X точки по индексу
    public double getPointX(int index) { return getNodeByIndex(index).point.getX(); }
//устанавливает координату X точки по индексу
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        if ((node.prev != head && x <= node.prev.point.getX()) ||
                (node.next != head && x >= node.next.point.getX()))
            throw new InappropriateFunctionPointException("X out of order");
        node.point.setX(x);
    }
    //возвращает уу точки по индексу
    public double getPointY(int index) { return getNodeByIndex(index).point.getY(); }
    //устанавливает у точки по индексу
    public void setPointY(int index, double y) { getNodeByIndex(index).point.setY(y); }
    //добавляет новую точку в список с сохранением упорядоченности по X
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = head.next; //проверка на дублирование координаты X
        while (node != head) {
            if (node.point.getX() == point.getX())
                throw new InappropriateFunctionPointException("Duplicate X");
            node = node.next;
        }

        FunctionNode newNode = new FunctionNode(new FunctionPoint(point)); //создание нового узла с копией точки
        if (head.next == head) { // пустой список
            head.next = head.prev = newNode; //вставка в пустой список
            newNode.next = newNode.prev = head;
        } else {
            node = head.next;
            while (node != head && node.point.getX() < point.getX())
                node = node.next;
            newNode.prev = node.prev; //вставка перед найденным узлом
            newNode.next = node;
            node.prev.next = newNode;
            node.prev = newNode;
        }
        pointsCount++;
    }

    public void deletePoint(int index) {
        if (pointsCount <= 2) //удаляет точку по указанному индексу
            throw new IllegalStateException("Cannot delete: less than 3 points"); //проверка что после удаления останется минимум 2 точки
        FunctionNode node = getNodeByIndex(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        if (node == head.next) head.next = node.next; //обновление головы, если удаляется первый элемент
        pointsCount--;
    }
}

