package functions;

public class Main {

        public static void main(String[] arg) throws InappropriateFunctionPointException {

            //создаем табулированную функцию
            TabulatedFunction func = new LinkedListTabulatedFunction(-3.5, 5, 5);
            for (int i =0; i < func.getPointsCount(); i++)
                func.setPointY(i, func.getPointX(i)*2 +2);

            System.out.println("\nФункиця: y = 2x + 2");
            func.printFunc();

            //проверка удаления точки, метод должен корректно удалить точку
            System.out.println("\nПроверка удаления точки с индексом 2");
            func.deletePoint(2);
            func.printFunc();

            //проверка добавления новой точки
            System.out.println("\nПроверка добавления точки (1, 4)");
            func.addPoint(new FunctionPoint(1.0, 4.0));
            func.printFunc();

            //проверка изменения существующей точки
            System.out.println("\nПроверка установки точки (-2, -2) на позицию с индексом 1");
            func.setPoint(1, new FunctionPoint(-2, -2));
            func.printFunc();

            //проверка вычисления значения функции
            System.out.println("\nПроверка getFunctionValue");
            for (double i = -1; i < 3; i += 1) {
                System.out.printf("(%.2f, %.2f) ", i, func.getFunctionValue(i));
                System.out.println();
            }

            System.out.println("\n\nТестирование исключений");

            //конструктор должен выбрасывать исключение при leftX > rightX
            System.out.println("\nНекорректное создание (левая граница больше правой)");
            try {
                TabulatedFunction testFunc = new ArrayTabulatedFunction(10, -3, 5);
                System.out.println("без ошибок");
            }catch (IllegalArgumentException e){
                System.out.println("Error: " + e.getMessage());
            }

            //обращение к несуществующему индексу должно вызыватэксепшн
            System.out.println("\nОбращение к точке с несуществующим индексом");
            try {
                func.getPoint(23);
                System.out.println("Без ошибок");
            }catch (FunctionPointIndexOutOfBoundsException e){
                System.out.println("Error: " + e.getMessage());
            }

            //установка точки, которая нарушает порядок возрастания X координат
            System.out.println("\nТочка нарушает порядок возрастания");
            try {
                func.setPoint(3, new FunctionPoint(10.0,2));
                System.out.println("без ошибок");
            }catch (InappropriateFunctionPointException e){
                System.out.println("Error: " + e.getMessage());
            }

            //попытка установить точку с индексом -1
            System.out.println("\nОбращение к точке с отрицательным индексом");
            try {
                func.getPoint(-1);
                System.out.println("Без ошибок");
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("Error: " + e.getMessage());
            }

            //попытка добавить точку с таким же X
            System.out.println("\nДобавление точки с уже существующим X");
            try {
                func.addPoint(new FunctionPoint(1.0, 10.0)); //х=1 уже существует
                System.out.println("без ошибок");
            } catch (InappropriateFunctionPointException e) {
                System.out.println("Error: " + e.getMessage());
            }

            // нельзя удалить точки, если после удаления останется меньше 2 точек
            try{
                TabulatedFunction testFunc2 = new ArrayTabulatedFunction(0,2,3); //создаем с тремя точками
                testFunc2.deletePoint(0);
                testFunc2.deletePoint(0);
                testFunc2.deletePoint(0);
                System.out.println("без ошибок");
            }catch (FunctionPointIndexOutOfBoundsException e){
                System.out.println("Error: " + e.getMessage());
            }


        }
    }

