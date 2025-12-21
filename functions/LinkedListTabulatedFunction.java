package functions;

// Реализация табулированной функции на основе двусвязного списка
public class LinkedListTabulatedFunction implements TabulatedFunction 
{
    private static class FunctionNode 
    {
        FunctionPoint data;
        FunctionNode prev, next;

        
        // Фиктивная голова списка (ссылается сама на себя)
        FunctionNode() 
        {
            this.data = new FunctionPoint(Double.NaN, Double.NaN);
            prev = next = this;
        }

        // Узел с данными
        FunctionNode(FunctionPoint data) 
        {
            this.data = data;
        }

        // Вставка узла перед текущим
        void insertBefore(FunctionNode node) 
        {
            node.next = this;
            node.prev = prev;
            prev.next = node;
            prev = node;
        }

        // Удаление узла из списка
        void remove() 
        {
            prev.next = next;
            next.prev = prev;
            prev = next = null;
        }
    }

    private final FunctionNode head = new FunctionNode(); // фиктивная голова
    private int size = 0;
    private FunctionNode lastAccessed = head;     // кэширование последнего доступа
    private int lastAccessedIndex = 0;

    // Проверка корректности индекса
    private boolean isValidIndex(int index) 
    {
        return index >= 0 && index < size;
    }

    // Проверка наличия точки с заданным x
    private boolean isXExists(double x) 
    {
        for (FunctionNode cur = head.next; cur != head; cur = cur.next) 
        {
            if (Double.compare(cur.data.getX(), x) == 0) return true;
        }
        return false;
    }

    // Возвращает узел по индексу с оптимизацией через кэширование
    private FunctionNode getNodeByIndex(int index) 
    {
        if (!isValidIndex(index)) 
        {
            return null;
        }
        
        int fromHead = index;
        int fromTail = size - 1 - index;
        int fromLast = Math.abs(index - lastAccessedIndex);

        FunctionNode start;
        int offset;

        // Выбираем, откуда начинать поиск — от головы, хвоста или кэша
        if (fromLast <= fromHead && fromLast <= fromTail) 
        {
            start = lastAccessed;
            offset = index - lastAccessedIndex;
        } 
        
        else if (fromHead <= fromTail) 
        {
            start = head.next;
            offset = index;
        } 
        else 
        {
            start = head.prev;
            offset = index - (size - 1);
        }

        FunctionNode cur = start;
        if (offset > 0) 
        {
            for (int i = 0; i < offset; i++) 
            {
                cur = cur.next;
                if (cur == head) i--; // пропускаем фиктивную голову
            }
        } 
        else if (offset < 0) 
        {
            for (int i = 0; i > offset; i--) 
            {
                cur = cur.prev;
                if (cur == head) i++;
            }
        }

        lastAccessed = cur;
        lastAccessedIndex = index;
        return cur;
    }

    // Линейная интерполяция (аналогично массивной версии)
    private double interpolate(double x, FunctionPoint left, FunctionPoint right) 
    {
        return (x - left.getX()) * (right.getY() - left.getY()) / (right.getX() - left.getX()) + left.getY();
    }

    // Конструктор по количеству точек
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointCount)
        throws IllegalArgumentException 
        {
            if (Double.compare(leftX, rightX) >= 0 || pointCount < 2) 
            {
                throw new IllegalArgumentException("Invalid parameters: leftX >= rightX or pointCount < 2");
            }
            
            double step = (rightX - leftX) / (pointCount - 1);
            for (int i = 0; i < pointCount; i++) 
            {
                try 
                {
                    addPoint(new FunctionPoint(leftX + step * i, 0.0));
                } 
                catch (InappropriateFunctionPointException e) 
                {
                    throw new IllegalStateException("Unexpected error during construction", e);
                }
            }
        }

    // Конструктор по массиву значений
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values)
        throws IllegalArgumentException 
        {
            if (Double.compare(leftX, rightX) >= 0 || values.length < 2) 
            {
                throw new IllegalArgumentException("Invalid parameters: leftX >= rightX or values.length < 2");
            }
            
            double step = (rightX - leftX) / (values.length - 1);
            for (int i = 0; i < values.length; i++) 
            {
                try 
                {
                    addPoint(new FunctionPoint(leftX + step * i, values[i]));
                } 
                catch (InappropriateFunctionPointException e) 
                {
                    throw new IllegalStateException("Unexpected error during construction", e);
                }
            }
        }

    @Override
    public double getLeftDomainBorder() 
    {
        return head.next.data.getX();
    }
    
    // Интерполяция в связном списке (поиск с конца для ускорения)
    @Override
    public double getFunctionValue(double x) 
    {
        if (size == 0 || Double.compare(x, head.next.data.getX()) < 0 || Double.compare(x, head.prev.data.getX()) > 0) 
        {
            return Double.NaN;
        }
        
        if (Double.compare(x, head.prev.data.getX()) == 0) 
        {
            return head.prev.data.getY();
        }
        
        for (FunctionNode cur = head.prev; cur != head; cur = cur.prev) 
        {
            if (Double.compare(x, cur.prev.data.getX()) >= 0) 
            {
                return interpolate(x, cur.prev.data, cur.data);
            }
        }
        return Double.NaN;
    }

    @Override
    public int getPointsCount()
    {
        return size;
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return new FunctionPoint(getNodeByIndex(index).data);
    }

    @Override
    public void setPoint(int index, FunctionPoint point)
        throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException 
        {
            if (!isValidIndex(index)) 
            {
                throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
            }
            
            double x = point.getX();
            FunctionNode node = getNodeByIndex(index);
            double leftBound = (index == 0) ? Double.NEGATIVE_INFINITY : node.prev.data.getX();
            double rightBound = (index == size - 1) ? Double.POSITIVE_INFINITY : node.next.data.getX();
            
            if (Double.compare(x, leftBound) > 0 && Double.compare(x, rightBound) < 0) 
            {
                node.data = new FunctionPoint(point);
            } 
            else 
            {
                throw new InappropriateFunctionPointException("x must be strictly between neighbors");
            }
        }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return getNodeByIndex(index).data.getX();
    }

    @Override
    public void setPointX(int index, double x)
        throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException 
        {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        setPoint(index, new FunctionPoint(x, getNodeByIndex(index).data.getY()));
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return getNodeByIndex(index).data.getY();
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        getNodeByIndex(index).data.setY(y);
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException 
    {
        if (!isValidIndex(index)) 
        {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        
        if (size < 3) 
        {
            throw new IllegalStateException("Cannot delete: fewer than 3 points");
        }
        
        FunctionNode node = getNodeByIndex(index);
        // Обновляем кэш, если удалили последний использованный узел
        
        if (node == lastAccessed) 
        {
            lastAccessed = (node.next != head) ? node.next : head.next;
            lastAccessedIndex = (lastAccessed == head.next) ? 0 : index;
        }
        
        node.remove();
        size--;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException 
    {
        double x = point.getX();
        if (isXExists(x)) 
        {
            throw new InappropriateFunctionPointException("Point with x=" + x + " already exists");
        }
        
        // Вставка в начало, конец или середину
        if (size == 0) 
        {
            head.insertBefore(new FunctionNode(point));
        } 
        
        else if (Double.compare(x, head.prev.data.getX()) > 0) 
        {
            head.insertBefore(new FunctionNode(point));
        } 
        else 
        {
            for (FunctionNode cur = head.next; cur != head; cur = cur.next) 
            {
                if (Double.compare(cur.data.getX(), x) > 0) 
                {
                    cur.insertBefore(new FunctionNode(point));
                    break;
                }
            }
        }
        size++;
        lastAccessed = head; // сброс кэша после модификации
    }
}