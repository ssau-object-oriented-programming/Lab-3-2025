package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private static final double EPSILON = 1e-10;
    
    private FunctionNode head;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;
    private int pointsCount;
    
    // Конструкторы
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        initializeHead();
        
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            // Создаем узел с точкой сразу
            FunctionNode newNode = new FunctionNode(new FunctionPoint(x, 0.0));
            // Вставляем в конец
            insertNodeAtTail(newNode);
        }
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        initializeHead();
        
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            // Создаем узел с точкой сразу
            FunctionNode newNode = new FunctionNode(new FunctionPoint(x, values[i]));
            // Вставляем в конец
            insertNodeAtTail(newNode);
        }
    }
    
    // Инициализация головы списка (без данных)
    private void initializeHead() {
        head = new FunctionNode(null); // Голова без данных
        head.setPrev(head);
        head.setNext(head);
        pointsCount = 0;
        lastAccessedNode = head;
        lastAccessedIndex = -1;
    }
    
    // Приватные методы для работы со списком
    
    // 3. Метод получения узла по индексу с оптимизацией
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        
        // Оптимизация: выбор оптимального пути доступа
        FunctionNode currentNode;
        int currentIndex;
        
        if (lastAccessedNode != head && lastAccessedIndex != -1) {
            int distanceFromLast = Math.abs(index - lastAccessedIndex);
            int distanceFromStart = index;
            int distanceFromEnd = pointsCount - 1 - index;
            
            if (distanceFromLast <= distanceFromStart && distanceFromLast <= distanceFromEnd) {
                currentNode = lastAccessedNode;
                currentIndex = lastAccessedIndex;
                
                if (index > lastAccessedIndex) {
                    while (currentIndex < index) {
                        currentNode = currentNode.getNext();
                        currentIndex++;
                    }
                } else {
                    while (currentIndex > index) {
                        currentNode = currentNode.getPrev();
                        currentIndex--;
                    }
                }
            } else if (distanceFromStart <= distanceFromEnd) {
                currentNode = head.getNext();
                currentIndex = 0;
                while (currentIndex < index) {
                    currentNode = currentNode.getNext();
                    currentIndex++;
                }
            } else {
                currentNode = head.getPrev();
                currentIndex = pointsCount - 1;
                while (currentIndex > index) {
                    currentNode = currentNode.getPrev();
                    currentIndex--;
                }
            }
        } else {
            currentNode = head.getNext();
            currentIndex = 0;
            while (currentIndex < index) {
                currentNode = currentNode.getNext();
                currentIndex++;
            }
        }
        
        // Кэшируем последний доступ
        lastAccessedNode = currentNode;
        lastAccessedIndex = index;
        
        return currentNode;
    }
    
    // 4. Метод добавления узла в конец
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(new FunctionPoint(0, 0));
        insertNodeAtTail(newNode);
        return newNode;
    }
    
    // Вспомогательный метод для вставки узла в конец
    private void insertNodeAtTail(FunctionNode newNode) {
        if (pointsCount == 0) {
            newNode.setPrev(head);
            newNode.setNext(head);
            head.setNext(newNode);
            head.setPrev(newNode);
        } else {
            FunctionNode lastNode = head.getPrev();
            newNode.setPrev(lastNode);
            newNode.setNext(head);
            lastNode.setNext(newNode);
            head.setPrev(newNode);
        }
        
        pointsCount++;
        lastAccessedNode = newNode;
        lastAccessedIndex = pointsCount - 1;
    }
    
    // 5. Метод добавления узла по индексу
    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        
        if (index == pointsCount) {
            return addNodeToTail();
        }
        
        FunctionNode currentNode = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(new FunctionPoint(0, 0));
        FunctionNode prevNode = currentNode.getPrev();
        
        // Вставляем новый узел перед currentNode
        newNode.setPrev(prevNode);
        newNode.setNext(currentNode);
        prevNode.setNext(newNode);
        currentNode.setPrev(newNode);
        
        pointsCount++;
        lastAccessedNode = newNode;
        lastAccessedIndex = index;
        
        return newNode;
    }
    
    // 6. Метод удаления узла по индексу
    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (pointsCount < 3) {
            throw new IllegalStateException("Нельзя удалить точку - функция должна содержать минимум 3 точки");
        }
        
        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.getPrev();
        FunctionNode nextNode = nodeToDelete.getNext();
        
        // Перелинковываем соседние узлы
        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);
        
        pointsCount--;
        
        // Обновляем кэш
        if (lastAccessedNode == nodeToDelete) {
            lastAccessedNode = head;
            lastAccessedIndex = -1;
        } else if (lastAccessedIndex > index) {
            lastAccessedIndex--;
        }
        
        return nodeToDelete;
    }
    
    // Реализация методов интерфейса TabulatedFunction
    
    @Override
    public double getLeftDomainBorder() {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        return head.getNext().getPoint().getX();
    }
    
    @Override
    public double getRightDomainBorder() {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        return head.getPrev().getPoint().getX();
    }
    
    @Override
    public double getFunctionValue(double x) {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        
        if (x < getLeftDomainBorder() - EPSILON || x > getRightDomainBorder() + EPSILON) {
            return Double.NaN;
        }
        
        // Оптимизация: начинаем поиск с последнего доступного узла
        FunctionNode currentNode = (lastAccessedNode != null && lastAccessedNode != head) 
            ? lastAccessedNode 
            : head.getNext();
        
        for (int i = 0; i < pointsCount; i++) {
            // Проверяем, не дошли ли до конца списка
            if (currentNode == head || currentNode.getNext() == head) {
                currentNode = head.getNext();
                continue;
            }
            
            FunctionNode nextNode = currentNode.getNext();
            
            double x1 = currentNode.getPoint().getX();
            double x2 = nextNode.getPoint().getX();
            
            if (x >= x1 - EPSILON && x <= x2 + EPSILON) {
                double y1 = currentNode.getPoint().getY();
                double y2 = nextNode.getPoint().getY();
                
                if (x > x1 - EPSILON && x < x1 + EPSILON) {
                    // Обновляем кэш для оптимизации будущих вызовов
                    lastAccessedNode = currentNode;
                    return y1;
                }
                if (x > x2 - EPSILON && x < x2 + EPSILON) {
                    lastAccessedNode = nextNode;
                    return y2;
                }
                
                // Обновляем кэш для оптимизации будущих вызовов
                lastAccessedNode = currentNode;
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            
            currentNode = nextNode;
        }
        
        return Double.NaN;
    }
    
    @Override
    public int getPointsCount() {
        return pointsCount;
    }
    
    @Override
    public FunctionPoint getPoint(int index) {
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.getPoint());
    }
    
    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        
        FunctionNode node = getNodeByIndex(index);
        
        // Оптимизация: прямой доступ к соседним узлам без дополнительных вызовов getNodeByIndex
        if (index == 0) {
            // Для первой точки
            FunctionNode nextNode = node.getNext();
            if (point.getX() >= nextNode.getPoint().getX() - EPSILON) {
                throw new InappropriateFunctionPointException(
                    "X координата первой точки должна быть меньше X координаты второй точки");
            }
        } else if (index == pointsCount - 1) {
            // Для последней точки
            FunctionNode prevNode = node.getPrev();
            if (point.getX() <= prevNode.getPoint().getX() + EPSILON) {
                throw new InappropriateFunctionPointException(
                    "X координата последней точки должна быть больше X координаты предыдущей точки");
            }
        } else {
            // Для средней точки
            FunctionNode prevNode = node.getPrev();
            FunctionNode nextNode = node.getNext();
            
            if (point.getX() <= prevNode.getPoint().getX() + EPSILON || 
                point.getX() >= nextNode.getPoint().getX() - EPSILON) {
                throw new InappropriateFunctionPointException(
                    "X координата средней точки должна быть между " + 
                    prevNode.getPoint().getX() + " и " + nextNode.getPoint().getX());
            }
        }
        
        node.setPoint(new FunctionPoint(point));
    }
    
    @Override
    public double getPointX(int index) {
        FunctionNode node = getNodeByIndex(index);
        return node.getPoint().getX();
    }
    
    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        
        FunctionNode node = getNodeByIndex(index);
        FunctionPoint point = new FunctionPoint(node.getPoint());
        point.setX(x);
        setPoint(index, point);
    }
    
    @Override
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        FunctionNode node = getNodeByIndex(index);
        return node.getPoint().getY();
    }
    
    @Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        FunctionNode node = getNodeByIndex(index);
        node.getPoint().setY(y);
    }
    
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // Если список пустой (хотя не должно быть по условию)
        if (pointsCount == 0) {
            FunctionNode newNode = new FunctionNode(new FunctionPoint(point));
            insertNodeAtTail(newNode);
            return;
        }
        
        FunctionNode currentNode = head.getNext();
        int insertIndex = pointsCount; // По умолчанию в конец
        
        for (int i = 0; i < pointsCount; i++) {
            double currentX = currentNode.getPoint().getX();
            
            // Проверка на дубликат
            if (Math.abs(point.getX() - currentX) < EPSILON) {
                throw new InappropriateFunctionPointException(
                    "Точка с X=" + point.getX() + " уже существует");
            }
            
            // Находим позицию для вставки
            if (point.getX() < currentX - EPSILON) {
                insertIndex = i;
                break;
            }
            
            currentNode = currentNode.getNext();
        }
        
        // Вставляем новую точку
        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.setPoint(new FunctionPoint(point));
    }
    
    @Override
    public void deletePoint(int index) {
        deleteNodeByIndex(index);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LinkedListTabulatedFunction[");
        
        FunctionNode currentNode = head.getNext();
        for (int i = 0; i < pointsCount; i++) {
            if (i > 0) sb.append(", ");
            sb.append(currentNode.getPoint().toString());
            currentNode = currentNode.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
    
    // 1. Класс элементов списка (внутренний, приватный)
    private static class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;
        
        // Конструктор для создания узла с точкой
        public FunctionNode(FunctionPoint point) {
            this.point = point;
            this.prev = this; // По умолчанию ссылается на себя
            this.next = this; // По умолчанию ссылается на себя
        }
        
        // Геттеры и сеттеры
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
}