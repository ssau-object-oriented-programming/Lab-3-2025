package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {

    private static class FunctionNode {
        private FunctionNode prev;
        private FunctionNode next;
        private FunctionPoint point;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
            this.next = null;
            this.prev = null;
        }

        //геттеры и сеттеры
        public FunctionPoint getPoint() {return point;}

        public void setPoint(FunctionPoint point) {this.point = point;}

        //возвращаем предыдущий узел
        public FunctionNode getPrev() {return prev;}

        //устанавливаем предыдущий узел
        public void setPrev(FunctionNode prev) {this.prev = prev;}

        //возвращаем следующий узел
        public FunctionNode getNext() {return next;}

        //устанавливаем следующий узел
        public void setNext(FunctionNode next) {this.next = next;}
    }

    private FunctionNode head = new FunctionNode(null); //голова списка
    private int pointsCount;


    private FunctionNode getNodeByIndex(int index) {

        FunctionNode curr;
        if (index < pointsCount / 2) {
            //двигаемся от головы вперед
            curr = head.getNext(); //начинаем с 1ого значащего элемента
            for (int i = 0; i < index; i++) {
                curr = curr.getNext();
            }
        } else {
            //двигаемся от хвоста назад
            curr = head.getPrev(); //с последнего элемента
            for (int i = pointsCount - 1; i > index; i--) {
                curr = curr.getPrev();
            }
        }

        return curr;
    }

    //добвление в конец
    private FunctionNode addNodeToTail(FunctionPoint point) {

        FunctionNode tail = head.getPrev();
        FunctionNode newNode = new FunctionNode(point);

        tail.setNext(newNode);
        newNode.setPrev(tail);
        newNode.setNext(head);
        head.setPrev(newNode);

        pointsCount++;

        return newNode;
    }

    private FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        FunctionNode newNode = new FunctionNode(point);

        if (index == pointsCount)
            return addNodeToTail(point); // добавляем в конец

        FunctionNode currNode = getNodeByIndex(index);

        newNode.setPrev(currNode.getPrev());
        newNode.setNext(currNode);
        currNode.getPrev().setNext(newNode);
        currNode.setPrev(newNode);

        pointsCount++;

        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {

        FunctionNode delNode = getNodeByIndex(index);

        delNode.getNext().setPrev(delNode.getPrev());
        delNode.getPrev().setNext(delNode.getNext());

        pointsCount--;

        return delNode;
    }

    //создание табулированной функции
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница больше правой");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Количество точек меньше двух");

        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = Math.abs(rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = new FunctionPoint(leftX + i * step, 0);
            addNodeToTail(point);
        }
    }

    //табулированная функция с заданными значениями по оси ординат
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] value) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (value.length < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }

        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = Math.abs(rightX - leftX) / (value.length - 1);
        for (int i = 0; i < value.length; i++) {
            FunctionPoint point = new FunctionPoint(leftX + i * step, value[i]);
            addNodeToTail(point);
        }
    }

    //возвращение левой границы
    public double getLeftDomainBorder() {return head.getNext().getPoint().getX();}

    //возвращение правой границы
    public double getRightDomainBorder() {return head.getPrev().getPoint().getX();}

    //возвращение у, если точка лежит в области определения функции
    public double getFunctionValue(double x) {
        double leftX = getLeftDomainBorder();
        double rightX = getRightDomainBorder();

        if (x >= leftX && x <= rightX) {

            FunctionNode curr = head.getNext();
            while (curr != head && curr.getNext() != head) {
                double x1 = curr.getPoint().getX();
                double x2 = curr.getNext().getPoint().getX();

                // совпадает ли х
                if (Math.abs(x - x1) < 1e-10)
                    return curr.getPoint().getY();
                if (Math.abs(x - x2) < 1e-10)
                    return curr.getNext().getPoint().getY();
                // попадает ли x в интервал
                if (x > x1 && x < x2) {
                    double y1 = curr.getPoint().getY();
                    double y2 = curr.getNext().getPoint().getY();
                    return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                }
                // переходим к следующему
                curr = curr.getNext();
            }
        }
        return Double.NaN;
    }

    //возвращения количества точек
    public int getPointsCount() {
        return pointsCount;
    }

    //возвращение точки
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        double x = getNodeByIndex(index).getPoint().getX();
        double y = getNodeByIndex(index).getPoint().getY();
        return new FunctionPoint(x, y);
    }

    //изменение указанной точки на заданную
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if ((index > 0 && point.getX() < getNodeByIndex(index - 1).getPoint().getX()) || (index < pointsCount - 1 && point.getX() > getNodeByIndex(index + 1).getPoint().getX()))
            throw new InappropriateFunctionPointException("Неверный Х");

        getNodeByIndex(index).setPoint(new FunctionPoint(point));

    }

    //возвращение координаты х
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        return getNodeByIndex(index).getPoint().getX();
    }

    //изменение значения х
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if ((index != 0 && x <= getNodeByIndex(index - 1).getPoint().getX()) ||
                (index != pointsCount - 1 && x >= getNodeByIndex(index + 1).getPoint().getX()))
            throw new InappropriateFunctionPointException("Неверный Х");

        getNodeByIndex(index).getPoint().setX(x);
    }

    //возвращения координаты у
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        return getNodeByIndex(index).getPoint().getY();
    }

    //изменения значения у
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        getNodeByIndex(index).getPoint().setY(y);
    }

    //удаление точки
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if (pointsCount < 3)
            throw new FunctionPointIndexOutOfBoundsException("Недостаточное количество точек");

        deleteNodeByIndex(index);

    }

    //добавление точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index = 0;

        double newX = point.getX();

        // находим позицию для вставки точки
        FunctionNode curr = head.getNext();
        while (curr != head && newX > curr.getPoint().getX()) {
            curr = curr.getNext();
            index++;
        }

        if (curr != head && Math.abs(curr.getPoint().getX() - newX) < 1e-10)
            throw new InappropriateFunctionPointException("Точка с таким Х уже существует");

        addNodeByIndex(index, point);

    }

    // вывод массива точек
    public void printFunc() {
        FunctionNode curr = head.getNext();
        while (curr != head) {
            System.out.printf("(%.2f, %.2f) ", curr.getPoint().getX(), curr.getPoint().getY());
            curr = curr.getNext();
        }
        System.out.println();
    }
}
