package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction{
    private FunctionNode head = new FunctionNode(null); // голова списка
    private int pointcount; // количество точек
    private static class FunctionNode{
        private FunctionPoint point; // точка функции
        private FunctionNode prev; // ссылка на предыдущий узел
        private FunctionNode next; // ссылка на следующий узел

        public FunctionNode(FunctionPoint point){
            this.point = point; // устанавливаем точку
            this.prev = null; // предыдущий узел пока null
            this.next = null; // следующий узел пока null
        }

        public FunctionPoint getPoint(){
            return point; // возвращаем точку
        }
        public void setPoint(FunctionPoint point){
            this.point = point; // устанавливаем новую точку
        }
        public FunctionNode getPrev(){
            return prev; // возвращаем предыдущий узел
        }
        public void setPrev(FunctionNode prev){
            this.prev = prev; // устанавливаем предыдущий узел
        }
        public FunctionNode getNext(){
            return next; // возвращаем следующий узел
        }
        public void setNext(FunctionNode next){
            this.next = next; // устанавливаем следующий узел
        }
    }
    public FunctionNode getNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        FunctionNode node = head; // начинаем с головы
        if(index < pointcount/2){
            for(int i =0; i<=index; i++){
                node = node.getNext(); // идем вперед по списку
            }
        }
        else{
            for(int i =pointcount-1; i >=index; i--){
                node = node.getPrev(); // идем назад по списку
            }
        }
        return node; // возвращаем найденный узел
    }
    public FunctionNode addNodeToTail(FunctionPoint point){
        FunctionNode newNode = new FunctionNode(point); // создаем новый узел
        FunctionNode tail = head.getPrev(); // получаем хвост списка

        newNode.setPrev(tail); // новый узел указывает на хвост
        newNode.setNext(head); // новый узел указывает на голову
        tail.setNext(newNode); // хвост указывает на новый узел
        head.setPrev(newNode); // голова указывает на новый узел

        pointcount ++; // увеличиваем счетчик точек
        return newNode; // возвращаем новый узел
    }
    public FunctionNode addNodeByIndex(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index > pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        if(index == pointcount){
            return addNodeToTail(point); // добавляем в конец
        }

        FunctionNode curNode = getNodeByIndex(index); // получаем текущий узел
        FunctionNode newNode = new FunctionNode(point); // создаем новый узел
        FunctionNode prevNode = curNode.getPrev(); // получаем предыдущий узел

        newNode.setPrev(prevNode); // новый узел указывает на предыдущий
        newNode.setNext(curNode); // новый узел указывает на текущий
        prevNode.setNext(newNode); // предыдущий указывает на новый
        curNode.setPrev(newNode); // текущий указывает на новый

        pointcount++; // увеличиваем счетчик точек
        return newNode; // возвращаем новый узел
    }
    public FunctionNode deleteNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        FunctionNode delNode = getNodeByIndex(index); // узел для удаления
        FunctionNode prevNode = delNode.getPrev(); // предыдущий узел
        FunctionNode nextNode = delNode.getNext(); // следующий узел

        prevNode.setNext(nextNode); // предыдущий указывает на следующий
        nextNode.setPrev(prevNode); // следующий указывает на предыдущий

        pointcount--; // уменьшаем счетчик точек
        return delNode; // возвращаем удаленный узел
    }
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if(leftX >= rightX || pointsCount<2){
            throw new IllegalArgumentException("Левая граница больше левой или количество точек меньше 2");
        }

        this.head = new FunctionNode(null); // создаем голову
        head.setPrev(head); // голова указывает сама на себя
        head.setNext(head); // голова указывает сама на себя
        this.pointcount = 0; // начинаем с нуля точек
        // вычисляем шаг между точками
        double step = (rightX-leftX)/(pointsCount-1);

        for(int i =0; i < pointsCount; i++){
            // вычисляем x координату для каждой точки
            double x = leftX + i * step;
            // создаем точку с вычисленным x и y=0
            addNodeToTail(new FunctionPoint(x, 0));
        }
    }
    // конструктор создающий функцию с заданными значениями y
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        if(leftX >= rightX || values.length <2){
            throw new IllegalArgumentException("Левая граница больше левой или количество точек меньше 2");
        }
        this.head = new FunctionNode(null); // создаем голову
        head.setPrev(head); // голова указывает сама на себя
        head.setNext(head); // голова указывает сама на себя
        this.pointcount = 0; // начинаем с нуля точек
        // вычисляем шаг между точками
        double step = (rightX-leftX)/(values.length-1);

        for(int i =0; i < values.length; i++){
            // вычисляем x координату
            double x = leftX + i * step;
            // создаем точку с вычисленным x и заданным y
            addNodeToTail(new FunctionPoint(x, values[i]));
        }
    }
    public double getLeftDomainBorder(){
        return getNodeByIndex(0).getPoint().getX(); // x первой точки
    }
    public double getRightDomainBorder(){
        return getNodeByIndex(pointcount-1).getPoint().getX(); // x последней точки
    }
    // вычислить значение функции в заданной точке x
    public double getFunctionValue(double x){
        // если точек нет возвращаем не число
        if(pointcount == 0){
            return Double.NaN;
        }

        // проверяем что x находится в области определения
        if(x >= this.getLeftDomainBorder() && x <= this.getRightDomainBorder()){
            // ищем точку с точно таким же x
            for(int i = 0; i < pointcount; i++){
                if(Math.abs(getNodeByIndex(i).getPoint().getX() - x) < 1e-10){
                    // если нашли возвращаем соответствующий y
                    return getNodeByIndex(i).getPoint().getY();
                }
            }
            // если точного совпадения нет ищем интервал для интерполяции
            for(int i = 0; i < pointcount-1; i++){
                double x1 = getNodeByIndex(i).getPoint().getX();
                double x2 = getNodeByIndex(i+1).getPoint().getX();

                // проверяем попадает ли x в текущий интервал
                if(x >= x1 && x <= x2){
                    double y1 = getNodeByIndex(i).getPoint().getY();
                    double y2 = getNodeByIndex(i+1).getPoint().getY();

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
        return pointcount; // возвращаем количество точек
    }
    // получить копию точки по указанному индексу
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // возвращаем копию чтобы защитить исходные данные
        return new FunctionPoint(getNodeByIndex(index).getPoint().getX(), getNodeByIndex(index).getPoint().getY());
    }
    // заменить точку по указанному индексу
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        // проверяем валидность индекса
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // проверяем что новый x не нарушает порядок точек
        // слева от текущей точки x должен быть меньше
        // справа от текущей точки x должен быть больше
        if ((index > 0 && point.getX() <= getNodeByIndex(index-1).getPoint().getX()) || (index < pointcount - 1 && point.getX() >= getNodeByIndex(index+1).getPoint().getX())){
            throw new InappropriateFunctionPointException("Точка не входит в интервал");
        }

        // создаем новую точку чтобы избежать ссылочной зависимости
        getNodeByIndex(index).setPoint(new FunctionPoint(point));
    }
    // получить координату x точки по индексу
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >=pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        return getNodeByIndex(index).getPoint().getX(); // возвращаем x точки
    }
    // установить новую координату x для точки по индексу
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        // проверяем валидность индекса
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // проверяем что новый x сохраняет порядок точек
        if ((index > 0 && x <= getNodeByIndex(index-1).getPoint().getX()) || (index < pointcount - 1 && x >= getNodeByIndex(index+1).getPoint().getX())){
            throw new InappropriateFunctionPointException("Точка не входит в интервал");
        }
        getNodeByIndex(index).getPoint().setX(x); // меняем x точки
    }
    // получить координату y точки по индексу
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        return getNodeByIndex(index).getPoint().getY(); // возвращаем y точки
    }
    // установить новую координату y для точки по индексу
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{
        // проверяем валидность индекса
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        getNodeByIndex(index).getPoint().setY(y); // меняем y точки
    }
    // удалить точку по указанному индексу
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException{
        // проверяем можно ли удалить точку
        // должно быть минимум 2 точки и индекс должен быть валидным
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        if(pointcount < 3){
            throw new IllegalStateException("Точка не входит в интервал");
        }
        deleteNodeByIndex(index); // удаляем узел
    }
    // добавить новую точку в функцию
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        int ins_index= 0;
        // ищем позицию для вставки чтобы сохранить возрастающий порядок по x
        while (ins_index < pointcount && getNodeByIndex(ins_index).getPoint().getX() < point.getX()) {
            ins_index++;
        }
        // если точка с таким x уже существует выходим
        if (ins_index < pointcount && Math.abs(getNodeByIndex(ins_index).getPoint().getX() - point.getX()) < 1e-10) {
            throw new InappropriateFunctionPointException("Совпадают Х");
        }

        // вставляем новую точку
        addNodeByIndex(ins_index, point);
    }
}