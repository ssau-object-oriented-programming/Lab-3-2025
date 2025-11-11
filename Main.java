import functions.*;  //импортируем все классы из пакета functions

public class Main {
    
    //метод для вывода всех точек функции
    private static void printPoints(TabulatedFunction func) {
        System.out.print("Точки функции: [");
        for (int i = 0; i < func.getPointsCount(); i++) {
            System.out.print("(" + func.getPointX(i) + ", " + func.getPointY(i) + ")");
            if (i < func.getPointsCount() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    public static void main(String[] args) {
        //создаем массив значений y для ф-ии y=x
        double[] values = {0, 1, 2, 3, 4};  //y=x для x=0,1,2,3,4
        
        //тестирование ArrayTabulatedFunction(на массиве)
        System.out.println(" Тестирование ArrayTabulatedFunction");
        testFunction(new ArrayTabulatedFunction(0, 4, values));
        
        //тестирование LinkedListTabulatedFunction(на списке)
        System.out.println("\n Тестирование LinkedListTabulatedFunction");
        testFunction(new LinkedListTabulatedFunction(0, 4, values));
        
        //проверка исключений на корректность работы
        System.out.println("\n Проверка исключений ");
        testExceptions();
    }
    
    private static void testFunction(TabulatedFunction func) {
        //выводим информацию о ф-ии
        System.out.println("Исходная функция");
        System.out.println("Левая граница: " + func.getLeftDomainBorder());
        System.out.println("Правая граница: " + func.getRightDomainBorder());
        System.out.println("Кол-во точек: " + func.getPointsCount());
        printPoints(func);
        
        //проверяем значения в разных точках
        System.out.println(" Проверка значений функции");
        double[] testPoints = {-1, 0.5, 1.7, 2.2, 3.4, 5.5};
        for (double x : testPoints) {
            double result = func.getFunctionValue(x);//вычисляем f(x)
            System.out.println("f(" + x + ") = " + result);
        }
        
        //проверяем операции с точками(добавление, удаление, изменение)
        System.out.println("\n Проверка операций с точками");
        
        //добавляем новую точку
        System.out.println("1. Добавляем точку (2.2, 2.2):");
        try {
            func.addPoint(new FunctionPoint(2.2, 2.2));
            printPoints(func);
            System.out.println("f(2.2) = " + func.getFunctionValue(2.2)); //проверяем значение в новой точке
        } catch (InappropriateFunctionPointException e) { //если не получилось
            System.out.println("Ошибка при добавлении: " + e.getMessage());
        }
        
        //удаляем точку с индексом 1
        System.out.println("\n2. Удаляем точку с индексом 1:");
        try {
            func.deletePoint(1);
            printPoints(func);
        } catch (Exception e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }
        
        //изменяем y у точки с индексом 2
        System.out.println("\n3. Изменяем y точки с индексом 2 на 2.5:");
        try {
            func.setPointY(2, 2.5);
            printPoints(func);
        } catch (Exception e) {
            System.out.println("Ошибка при изменении: " + e.getMessage());
        }
        
        //проверяем результат после всех изменений
        System.out.println("\n Итоговый результат");
        System.out.println("f(2.2) = " + func.getFunctionValue(2.2));//знач. в точке 2.2
        printPoints(func);//все точки
    }
    //метод для проверки всех типов исключений
    private static void testExceptions() {
        //1.Левая граница > или = правой
        System.out.println("1. Неправильные границы функции (левая >= правой):");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(4, 0, new double[]{1, 2});//пытаемся создать ф-ию от 4 до 0(НЕПРАВИЛЬНУЮ)
            System.out.println("   Ошибка: должно было быть исключение!");//если дошли сюда- ошибка в коде
        } catch (IllegalArgumentException e) { //должно сработать это исключение
            System.out.println("   Получено: " + e.getMessage());
        }
        
        //2.Количество точек меньше двух
        System.out.println("2. Слишком мало точек (меньше 2):");
        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 4, new double[]{1});//пытаемся создать ф-ию с 1 точкой
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (IllegalArgumentException e) {
            System.out.println("   Получено: " + e.getMessage());
        }
        
        //3.Выход за границы набора точек
        System.out.println("3. Несуществующий индекс точки:");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 4, new double[]{0, 1, 2});//создаем функцию с 3 точками (индексы 0,1,2)
            func.getPoint(5);//хотим получить точку №5(ее нет)
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("   Получено: " + e.getMessage());
        }
        
        //4.Нарушение порядка точек
        System.out.println("4. Нарушение порядка точек по X:");
        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 10, 3);
            func.setPointX(1, 15.0); //х должен быть между 5.0 и 10.0
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("   Получено: " + e.getMessage());
        }
        
        //5.Точка с таким X уже существует
	System.out.println("5. Добавление точки с уже существующим X:");
	try {
    	    TabulatedFunction func = new ArrayTabulatedFunction(0, 2, new double[]{0, 1, 2}); // изменен интервал с [0,4] на [0,2]
    	    func.addPoint(new FunctionPoint(1.0, 5.0)); //х=1.0 уже существует
    	    System.out.println("   Ошибка: должно было быть исключение!");
	} catch (InappropriateFunctionPointException e) {
    	    System.out.println("   Получено: " + e.getMessage());
	}
        
        //6.Удаление когда точек меньше трех
        System.out.println("6. Удаление точки когда их останется слишком мало:");
        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 2, new double[]{0, 2});//создаем функцию всего с 2 точками
            func.deletePoint(0);//пытаемся удалить одну точку->останется 1(ошибка)
            System.out.println("   Ошибка: должно было быть исключение!");
        } catch (IllegalStateException e) {
            System.out.println("   Получено: " + e.getMessage());
        }
    }
}