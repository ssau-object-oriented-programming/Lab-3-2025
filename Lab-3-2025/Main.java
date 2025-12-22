import functions.*;

public class Main {
	public static void main(String[] args) {
		try {
			new ArrayTabulatedFunction(2.0, 1.0, 5);
		} catch (IllegalArgumentException ex) {
			System.out.println("Ожидаемое исключение конструктора: " + ex.getMessage());
		}

		TabulatedFunction f = new ArrayTabulatedFunction(0.0, 2.0, new double[] {0.0, 1.0, 4.0});
		System.out.println("\nСоздан ArrayTabulatedFunction, точек: " + f.getPointsCount());

		try {
			f.getPoint(10);
		} catch (FunctionPointIndexOutOfBoundsException ex) {
			System.out.println("Ожидаемое индексное исключение: " + ex.getMessage());
		}

		try {
			f.addPoint(new FunctionPoint(1.0, 2.0));
		} catch (InappropriateFunctionPointException ex) {
			System.out.println("Ожидаемое исключение при дублирующем x: " + ex.getMessage());
		}

		try {
			f.setPoint(1, new FunctionPoint(10.0, 5.0));
		} catch (Exception ex) {
			System.out.println("Ожидаемое исключение при setPoint с некорректной x: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
		}

		try {
			TabulatedFunction g = new ArrayTabulatedFunction(0.0, 1.0, new double[] {0.0, 1.0});
			System.out.println("\nСоздана короткая функция, точек: " + g.getPointsCount());
			g.deletePoint(1);
		} catch (IllegalStateException ex) {
			System.out.println("Ожидаемое IllegalStateException при deletePoint: " + ex.getMessage());
		} catch (Exception ex) {
			System.out.println("Другое исключение: " + ex);
		}

		System.out.println("\nПроверка LinkedListTabulatedFunction");
		TabulatedFunction llf = new LinkedListTabulatedFunction(0.0, 2.0, new double[] {0.0, 1.0, 4.0});
		System.out.println("LinkedList точки: " + llf.getPointsCount());
		try {
			System.out.println("f(1.5) = " + llf.getFunctionValue(1.5));
		} catch (Exception ex) {
			System.out.println("Ошибка при вычислении: " + ex);
		}
	}
}


