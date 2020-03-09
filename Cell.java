
/*	The Cell Class
		Holds the data for each "tile" of the board
*/

public class Cell {
	private boolean isDiscovered;	//has user clicked on this tile
	private boolean isFlagged;	//has user placed a flag on this tile
	private int value;	/* value is initialized in the 'SetTiles()' method in Board class
							-1 = mine, 0 = empty, 1-8 = # of surrounding mines
						*/
	
	public Cell() {
	}
	
	//Getters
	public int getValue() {
		return value;
	}
	public boolean isDiscovered() {
		return isDiscovered;
	}
	public boolean isFlagged() {
		return isFlagged;
	}
	
	//Setters
	public void setValue(int value) {
		this.value = value;
	}
	public void setMine() {
		this.value = -1;
	}
	public void addValue() {
		this.value++;
	}
	public void discover() {
		this.isDiscovered = true;
	}
	public void flag() {
		this.isFlagged = true;
	}
	public void unFlag() {
		this.isFlagged = false;
	}
}