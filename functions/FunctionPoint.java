package functions;

/**
 * Класс, объекты которого описывают одну точку
 * табулированной функции
 */
public class FunctionPoint {
    // координаты точки
    private double x;
    private double y;
    /**
     * Конструктор без параметров<br>
     * Создает точку с координатами (0,0)
     */
    public FunctionPoint() {
        this.x = 0.0;
        this.y = 0.0;
    }
    /**
     * Конструктор с заданными координатами точки
     * @param x значение по оси X
     * @param y значение по оси Y
     */
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Конструктор копирования<br>
     * <i>Примечание: все же лучше использовать переопределение метода clone(), но ТЗ такое</i>
     * @param other другой объект, с которого надо скопировать координаты точки
     */
    public FunctionPoint(FunctionPoint other) {
        this.x = other.x;
        this.y = other.y;
    }

    /** Метод получения значения точки по оси X
     * @return значение по оси X
     */
    public double getX() { return this.x; }
    /** Метод получения значения точки по оси Y
     * @return значение по оси Y
     */
    public double getY() { return this.y; }

    /** Метод установки значения точки по оси X
     * @param x значение точки по оси X
     */
    public void setX(double x) { this.x = x; }
    /** Метод установки значения точки по оси Y
     * @param y значение точки по оси Y
     */
    public void setY(double y) { this.y = y; }
}