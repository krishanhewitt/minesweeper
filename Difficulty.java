
/* The Difficulty Class
		An object that the board uses to scale size according to the difficulty selected
		
*/

public class Difficulty {
	private int xAxis;	//size of xAxis
	private int yAxis;	//size of yAxis
	private int numOfMines; //number of mines to place

	public Difficulty(int xAxis, int yAxis, int numOfMines) {
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.numOfMines = numOfMines;
	}
	public Difficulty() {}

	//Getters
	public int getXAxis() {
		return xAxis;
	}
	public int getYAxis() {
		return yAxis;
	}
	public int getNumOfMines() {
		return numOfMines;
	}
	
	//Setters
	public void setXAxis(int xAxis) {
		this.xAxis = xAxis;
	}
	public void setYAxis(int yAxis) {
		this.yAxis = yAxis;
	}
	public void setNumOfMines(int numOfMines) {
		this.numOfMines = numOfMines;
	}
	public String toString() {
		return "xAxis: " + xAxis + " yAxis: " + yAxis + " Number of Mines: " + numOfMines;
	}
}
