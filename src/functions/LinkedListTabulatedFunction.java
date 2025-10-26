package functions;

/*
 * Исключения:
 *  IllegalArgumentException: некорректные параметры конструкторов
 *  FunctionPointIndexOutOfBoundsException: неверный индекс точки
 *  InappropriateFunctionPointException: нарушен порядок по x или повтор x
 *  IllegalStateException: попытка удалить точку при количестве < 3
 */
public class LinkedListTabulatedFunction implements TabulatedFunction {

    //Внутренний элемент двусвязного циклического списка
    // Хранит одну точку и ссылки на соседей
    // Извне ссылки на элементы не выдаются

    private static class FunctionNode {
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;

        FunctionNode() {
        }

        FunctionNode(FunctionPoint p) {
            this.point = p;
        }
    }

    // Данные в head не хранятся. Он упрощает вставки и удаления
    private final FunctionNode head;


    private int size; // Тенкущее количество значимых точек

    // Конструкторы списка

    //Пустой список ^ head указывает сам на себя
    public LinkedListTabulatedFunction() {
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        size = 0;
    }

    //  Функция с равномерной разметкой по x на отрезке [leftX; rightX] все y = 0
    // Бросает IllegalArgumentException при rightX<=leftX или pointsCount<2.

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this();
        if (rightX <= leftX) {
            throw new IllegalArgumentException("leftX must be < rightX");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("pointsCount must be >= 2");
        }
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail(new FunctionPoint(x, 0.0));
        }
    }

    //Функция с равномерной разметкой по x на [leftX; rightX], значения y берутся из массива values
    //Бросает IllegalArgumentException если rightX <= leftX или values == null или values.length < 2

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        this();
        if (rightX <= leftX) {
            throw new IllegalArgumentException("leftX must be < rightX");
        }
        if (values == null || values.length < 2) {
            throw new IllegalArgumentException("values length must be >= 2");
        }
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail(new FunctionPoint(x, values[i]));
        }
    }

    // Внутренние операции списка

    //Возвращает элемент списка по индексу [0..size-1].
    //Идем от начала или от конца. Выбираем сторону, которая ближе к индексу.
    //Бросает FunctionPointIndexOutOfBoundsException при неверном индексе.

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("index=" + index);
        }
        if (index < size / 2) {
            FunctionNode cur = head.next;
            for (int i = 0; i < index; i++) cur = cur.next;
            return cur;
        } else {
            FunctionNode cur = head.prev;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
            return cur;
        }
    }

    // Добавляет новый элемент в конец последовательности точек.
    // Вставка идет перед фиктивной головой. Возвращает добавленный элемент.
    // Сохраняется копия точки для инкапсуляции.

    private FunctionNode addNodeToTail(FunctionPoint p) {
        FunctionNode last = head.prev;
        FunctionNode node = new FunctionNode(new FunctionPoint(p)); // копия (инкапсуляция)

        node.prev = last;
        node.next = head;
        last.next = node;
        head.prev = node;

        size++;
        return node;
    }


    // Вставляет новый элемент на позицию index [0..size].
    // index == size означает вставку в конец.
    // Возвращает добавленный элемент. Сохраняется копия точки.
    // Бросает FunctionPointIndexOutOfBoundsException при неверном индексе.


    private FunctionNode addNodeByIndex(int index, FunctionPoint p) {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException("index=" + index);
        }
        if (index == size) {
            return addNodeToTail(p);
        }
        FunctionNode at = getNodeByIndex(index);
        FunctionNode before = at.prev;
        FunctionNode node = new FunctionNode(new FunctionPoint(p)); // копия

        node.prev = before;
        node.next = at;
        before.next = node;
        at.prev = node;

        size++;
        return node;
    }


    // Удаляет элемент по индексу [0..size-1].
    // Возвращает удаленный элемент. Границы валидируются внутри getNodeByIndex.

    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode at = getNodeByIndex(index);
        at.prev.next = at.next;
        at.next.prev = at.prev;
        size--;
        // обнулим ссылки удалённого узла
        at.next = null;
        at.prev = null;
        return at;
    }

    // Методы табулированной функции (аналогично массивной версии)

    @Override
    public int getPointsCount() {
        return size;
    }

    @Override
    public double getLeftDomainBorder() {
        return (size == 0) ? Double.NaN : head.next.point.getX();
    }

    @Override
    public double getRightDomainBorder() {
        return (size == 0) ? Double.NaN : head.prev.point.getX();
    }


    // Значение функции в точке x
    // Если x вне [левый x; правый x], возвращается NaN
    // Если x совпадает с одной из абсцисс, возвращается соответствующее y
    // Иначе используется линейная интерполяция по ближайшему отрезку

    @Override
    public double getFunctionValue(double x) {
        if (size == 0) return Double.NaN;

        double left = head.next.point.getX();
        double right = head.prev.point.getX();
        if (x < left || x > right) return Double.NaN;

        // точное попадание
        FunctionNode cur = head.next;
        for (int i = 0; i < size; i++) {
            if (x == cur.point.getX()) return cur.point.getY();
            cur = cur.next;
        }

        // поиск интервала [x0, x1] и линейная интерполяция
        cur = head.next;
        for (int i = 0; i < size - 1; i++) {
            double x0 = cur.point.getX();
            double y0 = cur.point.getY();
            double x1 = cur.next.point.getX();
            double y1 = cur.next.point.getY();
            if (x >= x0 && x <= x1) {
                return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
            }
            cur = cur.next;
        }
        return Double.NaN;
    }


    // Возвращает копию точки по индексу
    // Копия защищает внутреннюю структуру от внешних изменений
    @Override
    public FunctionPoint getPoint(int index) {
        FunctionNode n = getNodeByIndex(index); // сам проверит границы
        return new FunctionPoint(n.point);
    }


    // Заменяет точку по индексу на переданную
    // Сохраняется копия точки
    // Требование: новая абсцисса x должна строго лежать между соседями
    // Получим InappropriateFunctionPointException при нарушении порядка

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) return;
        FunctionNode node = getNodeByIndex(index);
        double newX = point.getX();

        if (index > 0) {
            double leftX = node.prev.point.getX();
            if (!(newX > leftX)) {
                throw new InappropriateFunctionPointException("x must be > left neighbor");
            }
        }
        if (index < size - 1) {
            double rightX = node.next.point.getX();
            if (!(newX < rightX)) {
                throw new InappropriateFunctionPointException("x must be < right neighbor");
            }
        }
        node.point = new FunctionPoint(point); // копия
    }

    @Override
    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }


    // Меняет абсциссу x точки по индексу
    // Правила: для первой точки x < x соседа справа, для последней x > x соседа слева,
    // для средней точки x строго между соседями.
    // Исключение InappropriateFunctionPointException при нарушении этих правил

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        if (size == 1) {
            node.point.setX(x);
            return;}
        if (index == 0) {
            double nextX = node.next.point.getX();
            if (x >= nextX) throw new InappropriateFunctionPointException("x must be < right neighbor");
            node.point.setX(x);
            return;}
        if (index == size - 1) {
            double prevX = node.prev.point.getX();
            if (x <= prevX) throw new InappropriateFunctionPointException("x must be > left neighbor");
            node.point.setX(x);
            return;}
        double leftX = node.prev.point.getX();
        double rightX = node.next.point.getX();
        if (x <= leftX || x >= rightX) {
            throw new InappropriateFunctionPointException("x must be strictly between neighbors");}
        node.point.setX(x);}

    @Override
    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    @Override
    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }


    // Удаляет точку по индексу
    // Минимально допустимое количество точек равно 3
    // Получаем IllegalStateException если текущее количество меньше 3

    @Override
    public void deletePoint(int index) {
        if (size < 3) {
            throw new IllegalStateException("cannot delete when points count < 3");
        }
        deleteNodeByIndex(index);
    }

    // Добавляет новую точку и сохраняет порядок по x
    // Находит позицию вставки как первый элемент, у которого x текущего элемента >= x новой точки
    // При совпадении абсцисс бросает InappropriateFunctionPointException
    // Внутрь вставляется копия переданной точки
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) return;

        double x = point.getX();

        int insertIndex = 0;
        FunctionNode cur = head.next;
        while (insertIndex < size && cur.point.getX() < x) {
            cur = cur.next;
            insertIndex++;
        }
        // дубликат x запрещён
        if (insertIndex < size && cur.point.getX() == x) {
            throw new InappropriateFunctionPointException("duplicate x");
        }
        // вставка копии точки в найденную позицию
        addNodeByIndex(insertIndex, point);
    }
}
