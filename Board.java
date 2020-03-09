
/*	The game board class
		Holds data relevent to game initialization/game logic
*/


import java.util.Random;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Board {
	private Cell[][] cells;			//2d array of cell objects used in game logic
	private int minesLeft;			//just the number of mines on the board, poor variable name
	private int hiddenTiles;		//keeps track of how many tiles are still unclicked as part of a win condition
	private boolean isGameOver;		//freezes game when win/loss condition is met
	private boolean firstClick;		//keeps track of first click of the game to ensure it isn't a mine
	private Integer timeCount;		//the game timer represented as an Integer used by the javaFX label
	private int diffLevel;			//variable used in high score system (never completed)

	public Board(Difficulty d) {
		this.cells = new Cell[d.getXAxis()][d.getYAxis()];
		this.minesLeft = d.getNumOfMines();
		this.hiddenTiles = d.getXAxis() * d.getYAxis();
		this.isGameOver = false;
		this.firstClick = false;
		this.timeCount = 0;
		this.diffLevel = 0;
		generateBoard();
	}
	
	
	//Getters
	public int getHiddenTiles() {
		return hiddenTiles;
	}
	
		public int getMinesLeft() {
		return minesLeft;
	}	
	
	public int getXSize() {
		return cells.length;
	}

	public int getYSize() {
		return cells[0].length;
	}

	public Cell getCell(int x, int y) {
		return cells[x][y];
	}

	public Cell[][] getCells(){
		return cells;
	}
	
	public boolean getFirstClick() {
		return firstClick;
	}
	
	public boolean getGameOver() {
		return isGameOver;
	}
	
	public Integer getTime() {
		return timeCount;
	}
	
	public int getDiffLevel() {
		return diffLevel;
	}
	
	//Setters
	public void removeHiddenTile() {
		this.hiddenTiles--;
	}

	public void removeMine() {
		this.minesLeft--;
	}
	
	public void addMine() {
		this.minesLeft++;
	}
	
	public void firstClickMade() {
		this.firstClick = true;
	}
	
	public void setGameOver(boolean gameState) {
		this.isGameOver = gameState;
	}
	
	public void addTime() {
		++this.timeCount;
	}
	
	public void setDiffLevel(int newLevel) {
		this.diffLevel = newLevel;
	}
	

	
	//creates an array of cell objects and calls the methods to initialize them
	private void generateBoard(){
		for (int i = 0; i < cells.length; i++){
			for (int j = 0; j < cells[0].length; j++) {
				cells[i][j] = new Cell();
			}
		}
		setMines();
		setTiles();
	}

	//randomly places mines on board
	public void setMines() {
		try {
			Random rand = new Random();
			int minesLeft = this.minesLeft;
			for(int i = 0; i < cells.length; i++) {
				for(int j = 0; j < cells[i].length; j++) {
					while(minesLeft > 0) {
						int randomRow = rand.nextInt(cells.length);
						int randomCol = rand.nextInt(cells[i].length);
						if(cells[randomRow][randomCol].getValue() != -1) {	//make sure current tile isn't already a mine
							cells[randomRow][randomCol].setMine();
							minesLeft--;
						}
					}
				}
			}	
		}catch(Exception e) {
			System.out.println(e + "setMines method");
		}
	}

	//calculates numbers for the board based on placement of mines
	public void setTiles() {
		// checks bounds of 8 surrounding tiles around mine and increments accordingly
		try {
			boolean topLeft = true;
			boolean topMid = true;
			boolean topRight = true;
			boolean midLeft = true;
			boolean midRight = true;
			boolean lowerLeft = true;
			boolean lowerMid = true;
			boolean lowerRight = true;

			for(int i = 0; i < cells.length; i++) {
				for(int j = 0; j < cells[i].length; j++) {					
					if(cells[i][j].getValue() == -1) {

						if(j == 0) {
							lowerLeft = false;
							midLeft = false;
							topLeft = false;
						}
						if(j == cells[i].length - 1) {
							lowerRight = false;
							midRight = false;
							topRight = false;
						}
						if(i == 0) {
							topLeft = false;
							topMid = false;
							topRight = false;
						}
						if(i == cells[j].length - 1) {
							lowerLeft = false;
							lowerMid = false;
							lowerRight = false;
						}
						if(topLeft == true) {
							if(cells[i-1][j-1].getValue() != -1)
								cells[i-1][j-1].addValue();
						}
						if(topMid == true) {
							if(cells[i-1][j].getValue() != -1)
								cells[i-1][j].addValue();
						}
						if(topRight == true) {
							if(cells[i-1][j+1].getValue() != -1)
								cells[i-1][j+1].addValue();
						}
						if(midLeft == true) {
							if(cells[i][j-1].getValue() != -1)
								cells[i][j-1].addValue();
						}
						if(midRight == true) {
							if(cells[i][j+1].getValue() != -1)
								cells[i][j+1].addValue();
						}
						if(lowerLeft == true) {
							if(cells[i+1][j-1].getValue() != -1)
								cells[i+1][j-1].addValue();
						}
						if(lowerMid == true) {
							if(cells[i+1][j].getValue() != -1)
								cells[i+1][j].addValue();
						}
						if(lowerRight == true) {
							if(cells[i+1][j+1].getValue() != -1)
								cells[i+1][j+1].addValue();
						}
						topLeft = true;
						topMid = true;
						topRight = true;
						midLeft = true;
						midRight = true;
						lowerLeft = true;
						lowerMid = true;
						lowerRight = true;	
					}
				}
			}
		}catch(Exception e) {
			System.out.println(e + " setTiles method");
		}
	}
	
	//sets the graphic for the tile based on which value it holds
	public void displayNumbers(Button btn, int x, int y) {
		try {
			switch(this.getCell(x, y).getValue()) {
			case -1:
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				btn.setGraphic(new ImageView(new Image("res/" + this.getCell(x, y).getValue() + ".png")));
				break;
			}
		}catch(Exception e) {
			System.out.println(e + "displayNumbers Method");
		}
	}
}