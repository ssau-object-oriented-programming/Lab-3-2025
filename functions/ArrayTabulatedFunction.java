package functions;

public class ArrayTabulatedFunction implements TabulatedFunction{
    private FunctionPoint[] point_mass; // массив точек функции
    private int point_count; // количество точек

    // конструктор создающий функцию с равномерно распределенными точками
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if(leftX >= rightX || pointsCount<2){
            throw new IllegalArgumentException("Левая граница больше левой или количество точек меньше 2");
        }
        // создаем массив для хранения точек
        this.point_mass = new FunctionPoint[pointsCount];
        this.point_count = pointsCount;
        // вычисляем шаг между точками
        double step = (rightX-leftX)/(pointsCount-1);

        for(int i =0; i < pointsCount; i++){
            // вычисляем x координату для каждой точки
            double x = leftX + i * step;
            // создаем точку с вычисленным x и y=0
            point_mass[i] = new FunctionPoint(x, 0);
        }
    }
    // конструктор создающий функцию с заданными значениями y
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        if(leftX >= rightX || values.length <2){
            throw new IllegalArgumentException("Левая граница больше левой или количество точек меньше 2");
        }
        // создаем массив точек размером с массив значений
        this.point_mass = new FunctionPoint[values.length];
        this.point_count = values.length;

        // вычисляем шаг между точками
        double step = (rightX-leftX)/(point_count-1);

        for(int i =0; i < point_count; i++){
            // вычисляем x координату
            double x = leftX + i * step;
            // создаем точку с вычисленным x и заданным y
            point_mass[i] = new FunctionPoint(x, values[i]);
        }
    }
    // получить левую границу области определения функции
    public double getLeftDomainBorder(){
        return this.point_mass[0].getX(); // x первой точки
    }
    // получить правую границу области определения функции
    public double getRightDomainBorder(){
        return this.point_mass[point_count-1].getX(); // x последней точки
    }
    // вычислить значение функции в заданной точке x
    public double getFunctionValue(double x){
        // если точек нет возвращаем не число
        if(point_count == 0){
            return Double.NaN;
        }

        // проверяем что x находится в области определения
        if(x >= point_mass[0].getX() && x <= point_mass[point_count-1].getX()){
            // ищем точку с точно таким же x
            for(int i = 0; i < point_count; i++){
                if(Math.abs(point_mass[i].getX() - x) < 1e-10){
                    // если нашли возвращаем соответствующий y
                    return point_mass[i].getY();
                }
            }
            // если точного совпадения нет ищем интервал для интерполяции
            for(int i = 0; i < point_count-1; i++){
                double x1 = point_mass[i].getX();
                double x2 = point_mass[i+1].getX();

                // проверяем попадает ли x в текущий интервал
                if(x >= x1 && x <= x2){
                    double y1 = point_mass[i].getY();
                    double y2 = point_mass[i+1].getY();

                    // вычисляем значение линейной интерполяции
                    return y1+(y2-y1)*(x-x1)/(x2-x1);
                }
            }
        }
        // если x вне области определения возвращаем не число
        return Double.NaN;
    }
    // получить общее количество точек в функции
    public int getPointsCount(){
        return point_count; // возвращаем количество точек
    }
    // получить копию точки по указанному индексу
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= point_count){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // возвращаем копию чтобы защитить исходные данные
        return new FunctionPoint(point_mass[index].getX(), point_mass[index].getY());
    }
    // заменить точку по указанному индексу
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        // проверяем валидность индекса
        if (index < 0 || index >= point_count) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // проверяем что новый x не нарушает порядок точек
        // слева от текущей точки x должен быть меньше
        // справа от текущей точки x должен быть больше
        if ((index > 0 && point.getX() <= point_mass[index - 1].getX()) || (index < point_count - 1 && point.getX() >= point_mass[index + 1].getX())){
            throw new InappropriateFunctionPointException("Точка не входит в интервал");
        }
        // создаем новую точку чтобы избежать ссылочной зависимости
        point_mass[index] = new FunctionPoint(point);
    }
    // получить координату x точки по индексу
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >=point_count){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        return point_mass[index].getX(); // возвращаем x точки
    }
    // установить новую координату x для точки по индексу
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        // проверяем валидность индекса
        if (index < 0 || index >= point_count) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // проверяем что новый x сохраняет порядок точек
        if ((index > 0 && x <= point_mass[index - 1].getX()) || (index < point_count - 1 && x >= point_mass[index + 1].getX())){
            throw new InappropriateFunctionPointException("Точка не входит в интервал");
        }
        point_mass[index].setX(x); // меняем x точки
    }
    // получить координату y точки по индексу
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= point_count){
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return point_mass[index].getY(); // возвращаем y точки
    }
    // установить новую координату y для точки по индексу
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{
        // проверяем валидность индекса
        if (index < 0 || index >= point_count) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        point_mass[index].setY(y); // меняем y точки
    }
    // удалить точку по указанному индексу
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException{
        // проверяем можно ли удалить точку
        // должно быть минимум 2 точки и индекс должен быть валидным
        if(index < 0 || index >= point_count){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        if(point_count < 3){
            throw new IllegalStateException("Количество точек меньще 3");
        }
        // сдвигаем все точки после удаляемой влево
        if (index < point_count - 1) {
            System.arraycopy(point_mass, index + 1, point_mass, index, point_count - index - 1);
        }
        // уменьшаем счетчик точек
        point_count--;
        // очищаем последний элемент
        point_mass[point_count] = null;
    }
    // добавить новую точку в функцию
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        int ins_index= 0;
        // ищем позицию для вставки чтобы сохранить возрастающий порядок по x
        while (ins_index < point_count && point_mass[ins_index].getX() < point.getX()) {
            ins_index++;
        }
        // если точка с таким x уже существует выходим
        if (ins_index < point_count && Math.abs(point_mass[ins_index].getX() - point.getX()) < 1e-10) {
            throw new InappropriateFunctionPointException("Совпадают X");
        }
        // проверяем нужно ли увеличивать массив
        if (point_count == point_mass.length) {
            // удваиваем размер массива
            int newcap = point_mass.length * 2;
            // если массив был пустой устанавливаем размер 1
            if (newcap == 0) {
                newcap = 1;
            }
            // создаем новый массив большего размера
            FunctionPoint[] newArray = new FunctionPoint[newcap];
            // копируем старые точки в новый массив
            System.arraycopy(point_mass, 0, newArray, 0, point_count);
            point_mass = newArray;
        }
        // сдвигаем точки чтобы освободить место для новой
        if (ins_index < point_count) {
            System.arraycopy(point_mass, ins_index, point_mass, ins_index + 1, point_count - ins_index);
        }

        // вставляем новую точку
        point_mass[ins_index] = new FunctionPoint(point);
        // увеличиваем счетчик точек
        point_count++;
    }
}