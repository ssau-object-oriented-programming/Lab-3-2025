package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction{
    private FunctionNode head = new FunctionNode(); //голова
    private int len; //длина списка
    private static class FunctionNode { //класс для хранения значения точки и ссылок на следующий и предыдущий элементы
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }
        public FunctionNode() {
            point = null;
            prev = null;
            next = null;
        }
        public FunctionPoint getPoint() {
            return point;
        }

        public FunctionNode getNext() {
            return next;
        }

        public FunctionNode getPrev() {
            return prev;
        }

        public void setNext(FunctionNode next) {
            this.next = next;
        }

        public void setPoint(FunctionPoint point) {
            this.point = point;
        }

        public void setPrev(FunctionNode prev) {
            this.prev = prev;
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= len) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне интервала");
        }
        FunctionNode cur = head;
        if (index < len / 2) { //Идем с начала списка
            for (int i = 0; i <= index; i++) {
                cur = cur.getNext();
            }
        }
        else { //Идем с конца списка
            for (int i = len - 1; i >= index; i--) {
                cur = cur.getPrev();
            }
        }
        return cur;
    }

    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode Node = new FunctionNode(point); //создали новый узел
        if (head.getNext() == null) { //если список пустой
            head.setNext(Node);
            head.setPrev(Node);
            Node.setPrev(head);
            Node.setNext(head);
        }
        else {
            head.getPrev().setNext(Node); //следующий элемент у предыдущего перед головой теперь Node
            Node.setPrev(head.getPrev()); //предыдущий элемент у Node теперь тот, который был у головы
            head.setPrev(Node); //Предыдущий элемент у головы Node
            Node.setNext(head); //следующий элемент у Node голова
        }
        len++;
        return Node;
    }

    private FunctionNode addNodeByIndex(int index, FunctionPoint point) { //добавление узла по индексу
        if (index < 0 || index > len) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне интервала");
        }
        FunctionNode cur = new FunctionNode(point);
        if (index==len) { //добавляем в конец, если индекс равен длине списка
            cur = addNodeToTail(point);
            return cur;
        };
        FunctionNode Next = getNodeByIndex(index); //получили ссылку на элемент, который был на исходном индексе
        cur.setNext(Next); //следующий после нового теперь Next
        cur.setPrev(Next.getPrev()); //предыдущий перед новым теперь предыдущий перед Next
        Next.getPrev().setNext(cur);
        Next.setPrev(cur); //предыдущий у Next теперь новый элемент
        len++;
        return cur;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= len) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне интервала");
        }
        FunctionNode Node = getNodeByIndex(index); //получаем элемент, который надо удалить
        Node.getPrev().setNext(Node.getNext()); //смена связей соседних элементов
        Node.getNext().setPrev(Node.getPrev());
        Node.setPrev(null);
        Node.setNext(null);
        len--;
        return Node;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        double intervalLength = 0;
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        intervalLength = Math.abs(rightX - leftX) / (pointsCount - 1); //находим длину интервала между двумя точками
        for (int i = 0; i < pointsCount; i++) {
            if (i != (pointsCount - 1)) { //если не последняя точка
                addNodeToTail(new FunctionPoint(leftX, 0));
                leftX += intervalLength; //смещаем левую границу на длину интервала
            } else {
                addNodeToTail(new FunctionPoint(rightX, 0)); //последней точке присваиваем значение правой границы
            }
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        int pointsCount = values.length;
        double intervalLength = 0;
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        intervalLength = Math.abs(leftX - rightX) / (pointsCount - 1); //находим длину интервала между двумя точками

        for (int i = 0; i < pointsCount; i++) {
            if (i != (pointsCount - 1)) { //если не последняя точка
                addNodeToTail(new FunctionPoint(leftX, values[i])); //присваиваем значение из массива значений функции
                leftX += intervalLength; //смещаем левую границу на длину интервала
            } else {
                addNodeToTail(new FunctionPoint(rightX, values[i])); //последней точке присваиваем значение правой границы
            }
        }
    }

    public double getLeftDomainBorder() { //левая граница
        return getNodeByIndex(0).getPoint().getx(); //получаем узел по индексу и с помощью метода получаем точку
    }

    public double getRightDomainBorder() { //правая граница
        return getNodeByIndex(len - 1).getPoint().getx();
    }

    public double getFunctionValue(double x) {
        if (x > this.getRightDomainBorder() || x < this.getLeftDomainBorder()) {//если точка вне интервала
            return Double.NaN;
        }
        if (x == this.getLeftDomainBorder()) {
            return getNodeByIndex(0).getPoint().gety();
        }
        if (x == this.getRightDomainBorder()) {
            return getNodeByIndex(len - 1).getPoint().gety();
        }
        int ind = 1;
        for (int i = 1; i < (len - 1); i++)
        {
            if (x >= getNodeByIndex(i - 1).getPoint().getx() && x <= getNodeByIndex(i + 1).getPoint().getx()) {
                ind = i; //находим индекс
            }
        }
        double X1 = getNodeByIndex(ind - 1).getPoint().getx();
        double Y1 = getNodeByIndex(ind - 1).getPoint().gety();
        double X2 = getNodeByIndex(ind).getPoint().getx();
        double Y2 = getNodeByIndex(ind).getPoint().gety();
        return ((Y2 - Y1) * (x - X1) / (X2 - X1)) + Y1; // ищем значение функции через уравнение прямой, проходящей через 2 точки
    }

    public int getPointsCount() { //количество точек
        return len;
    }

    public FunctionPoint getPoint(int index) { //возвращение копии точки
        if (index < 0 || index >= len) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        return new FunctionPoint(getNodeByIndex(index).getPoint().getx(), getNodeByIndex(index).getPoint().gety());
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException { //замена точки
        if (index < 0 || index >= len) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        double leftPoint;
        double rightPoint;

        if (index > 0) { //находим значение x слева
            leftPoint = getNodeByIndex(index - 1).getPoint().getx();
        } else {
            leftPoint = point.getx(); //если индекс первой точки, то можем изменить значение левого x на значение новой точки
        }
        if (index < (len - 1)) { //находим значение x справа
            rightPoint = getNodeByIndex(index + 1).getPoint().getx();
        } else {
            rightPoint = point.getx(); //если точка последняя
        }
        if (leftPoint > point.getx() || rightPoint < point.getx()) {
            throw new InappropriateFunctionPointException("Точка вне интервала");
        }
        getNodeByIndex(index).setPoint(new FunctionPoint(point));
    }

    public double getPointX(int index) { //Значение x с указанным индексом
        if (index < 0 || index >= len) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        return getNodeByIndex(index).getPoint().getx();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException{  //изменение x у точки с заданным индексом
        if (index < 0 || index >= len) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        setPoint(index, new FunctionPoint(x, getNodeByIndex(index).getPoint().gety())); //вызываем метод, который заменяет точку на новую с измененным x
    }

    public double getPointY(int index) { //Значение y с указанным индексом
        if (index < 0 || index >= len) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        return getNodeByIndex(index).getPoint().gety();
    }

    public void setPointY(int index, double y) throws InappropriateFunctionPointException{
        if (index < 0 || index >= len) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        setPoint(index, new FunctionPoint(getNodeByIndex(index).getPoint().getx(), y));//вызываем метод, который заменяет точку на новую с измененным y
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= len) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы");
        }
        if (len < 3) {
            throw new IllegalStateException("Точек меньше трех");
        }
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        double X = point.getx();
        int ind = 0;
        for (int i = 1; i < (len - 1); i++) {
            if (X == getNodeByIndex(i - 1).getPoint().getx() || X == getNodeByIndex(i + 1).getPoint().getx()) {
                throw new InappropriateFunctionPointException("Совпадение абсцисс");
            }
            if (X > getNodeByIndex(i - 1).getPoint().getx() && X < getNodeByIndex(i + 1).getPoint().getx()) {
                ind = i; //находим индекс для новой точки
            }
        }
        if (X > getNodeByIndex(len - 1).getPoint().getx()) { //если x больше последнего x, добавляем в конец
            ind = len;
        }
        addNodeByIndex(ind, point); //добавим точку по индексу
    }
}
