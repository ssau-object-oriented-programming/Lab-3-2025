package functions;

public class FunctionPoint{
	
	private double x;
	private double y;
	
	public FunctionPoint( double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public FunctionPoint(FunctionPoint point){
		this.x = point.x;
		this.y = point.y;
	}
	
	public FunctionPoint(){
		this.x = 0;
		this.y = 0;
	}
	
	// Геттеры и сеттеры для чтения и изменения данных
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}

	@Override
public String toString() {
    // Для обычных чисел используем стандартное форматирование
    // Для специальных значений (Infinity, NaN, очень большие/малые) - прямое преобразование
    if (Double.isInfinite(x) || Double.isInfinite(y) || 
        Double.isNaN(x) || Double.isNaN(y) ||
        Math.abs(x) > 1e10 || Math.abs(x) < 1e-10 ||
        Math.abs(y) > 1e10 || Math.abs(y) < 1e-10) {
        
        // Для специальных и экстремальных значений
        return "(" + x + ", " + y + ")";
    } else {
        // Для обычных чисел - красивое форматирование
        return String.format("(%.2f, %.2f)", x, y);
    }
}
}
