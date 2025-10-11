package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    // Внутренний класс элемента списка
    private static class FunctionNode {
        FunctionPoint point;      // Данные точки
        FunctionNode prev;        // Ссылка на предыдущий элемент
        FunctionNode next;        // Ссылка на следующий элемент
        // Конструктор
        FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }
    private FunctionNode head; //Голова списка
    private int pointsCount; //Количество точек в списке
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }
        FunctionNode current;
        // Выбор оптимального направления обхода
        if (index < pointsCount / 2) {
            // Ищем с начала (если индекс в первой половине)
            current = head.next;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            // Ищем с конца (если индекс во второй половине)
            current = head.prev;
            for (int i = pointsCount - 1; i > index; i--) {
                current = current.prev;
            }
        }

        return current;
    }
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(null);
        FunctionNode lastNode = head.prev;
        // Устанавливаем связи нового узла:
        newNode.prev = lastNode;  // предыдущий для нового - старый последний
        newNode.next = head;      // следующий для нового - голова
        // Обновляем связи соседних узлов:
        lastNode.next = newNode;  // следующий для старого последнего - новый
        head.prev = newNode;      // предыдущий для головы - новый
        pointsCount++;
        return newNode;
    }
    private FunctionNode addNodeByIndex(int index) {
        // Проверка корректности индекса
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }
        // Если вставляем в конец, используем существующий метод
        if (index == pointsCount) {
            return addNodeToTail();
        }
        FunctionNode newNode = new FunctionNode(null);
        // Находим узел, который будет находиться после нового (по указанному индексу)
        FunctionNode nextNode = getNodeByIndex(index);
        // Узел, который будет находиться перед новым
        FunctionNode prevNode = nextNode.prev;
        // Устанавливаем связи нового узла:
        newNode.prev = prevNode;  // предыдущий для нового - узел перед nextNode
        newNode.next = nextNode;  // следующий для нового - nextNode
        // Обновляем связи соседних узлов:
        prevNode.next = newNode;  // следующий для prevNode - новый
        nextNode.prev = newNode;  // предыдущий для nextNode - новый
        pointsCount++;

        return newNode;
    }
    private FunctionNode deleteNodeByIndex(int index) {
        // Проверка корректности индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }
        // Находим узел для удаления
        FunctionNode nodeToDelete = getNodeByIndex(index);
        // Получаем соседние узлы
        FunctionNode prevNode = nodeToDelete.prev;
        FunctionNode nextNode = nodeToDelete.next;
        // Удаляем узел из списка - перебрасываем связи
        prevNode.next = nextNode;  // следующий для предыдущего - становится следующий от удаляемого
        nextNode.prev = prevNode;  // предыдущий для следующего - становится предыдущий от удаляемого
        // Очищаем связи удаленного узла
        nodeToDelete.prev = null;
        nodeToDelete.next = null;
        pointsCount--;
        return nodeToDelete;
    }
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения больше или равна правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }
        // Инициализация списка
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.pointsCount = 0;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, 0.0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения больше или равна правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }

        // Инициализация списка
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.pointsCount = 0;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, values[i]);
        }
    }
    public double getLeftDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.next.point.getX(); // Первый значащий узел
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.prev.point.getX(); // Последний значащий узел
    }

    public double getFunctionValue(double x) {
        if (pointsCount == 0 || x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        // Используем прямое обращение к узлам для поиска интервала
        FunctionNode current = head.next;
        while (current != head) {
            FunctionNode next = current.next;
            if (next != head && x >= current.point.getX() && x <= next.point.getX()) {
                // Линейная интерполяция
                double x1 = current.point.getX();
                double x2 = next.point.getX();
                double y1 = current.point.getY();
                double y2 = next.point.getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.next;
        }

        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.point); // Возвращаем копию
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double newX = point.getX();

        // Проверка порядка точек
        if (pointsCount == 1) {
            node.point = new FunctionPoint(point);
            return;
        }

        if (index == 0) {
            // Первый элемент: новая X должна быть < следующей
            if (newX >= node.next.point.getX()) {
                throw new InappropriateFunctionPointException("Новая X для первой точки должна быть меньше следующей точки");
            }
        } else if (index == pointsCount - 1) {
            // Последний элемент: новая X должна быть > предыдущей
            if (newX <= node.prev.point.getX()) {
                throw new InappropriateFunctionPointException("Новая X для последней точки должна быть больше предыдущей точки");
            }
        } else {
            // Средний элемент: новая X должна быть между соседними
            if (newX <= node.prev.point.getX() || newX >= node.next.point.getX()) {
                throw new InappropriateFunctionPointException("Новая X должна быть между соседними точками");
            }
        }

        node.point = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double newX = x;

        // Проверка порядка точек
        if (pointsCount == 1) {
            node.point.setX(x);
            return;
        }

        if (index == 0) {
            if (newX >= node.next.point.getX()) {
                throw new InappropriateFunctionPointException("Новая X для первой точки должна быть меньше следующей точки");
            }
        } else if (index == pointsCount - 1) {
            if (newX <= node.prev.point.getX()) {
                throw new InappropriateFunctionPointException("Новая X для последней точки должна быть больше предыдущей точки");
            }
        } else {
            if (newX <= node.prev.point.getX() || newX >= node.next.point.getX()) {
                throw new InappropriateFunctionPointException("Новая X должна быть между соседними точками");
            }
        }

        node.point.setX(x);
    }

    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) {
        if (pointsCount < 3) {
            throw new IllegalStateException("Невозможно удалить точку: количество точек меньше трех");
        }
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // Проверка на существование точки с таким X
        FunctionNode current = head.next;
        while (current != head) {
            if (Math.abs(current.point.getX() - point.getX()) < 1e-10) {
                throw new InappropriateFunctionPointException("Точка с такой X уже существует");
            }
            current = current.next;
        }

        // Поиск позиции для вставки
        int insertIndex = 0;
        current = head.next;
        while (current != head && current.point.getX() < point.getX()) {
            current = current.next;
            insertIndex++;
        }
        // Используем готовый метод для вставки
        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.point = new FunctionPoint(point);
    }

    public void printTabulatedFunction() {
        FunctionNode current = head.next;
        int index = 0;
        while (current != head) {
            System.out.println(index + ": x = " + current.point.getX() + ", y = " + current.point.getY());
            current = current.next;
            index++;
        }
    }
}