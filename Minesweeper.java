import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Minesweeper extends Application {
	private static Difficulty d = new Difficulty(); 	//an object that the board uses to scale size
	private static boolean diffSelected = false;	//prevents the board from being intialized to the default size if another difficulty has been selected
	private static Board b; 	//global board instance

	public static void main(String[] args) {
		launch(args);
	}

	//initializes javaFX and starts the game
	public void start(Stage theStage) {
		try {
			if (!diffSelected) {
				d = new Difficulty(8, 8, 10);	//default size of board if user hasn't specified one yet
			}
			b = new Board(d);

			VBox vb = new VBox();
			GridPane gridPane = new GridPane();
			gridPane.setAlignment(Pos.CENTER);
			BorderPane borderPane = new BorderPane();
			borderPane.setPadding(new Insets(10));

			HBox top = new HBox(25);
			theStage.setTitle("Minesweeper");
			theStage.setScene(new Scene(borderPane));
			top.setStyle("-fx-border-color:blue");
			top.setPadding(new Insets(12, 12, 12, 12));

			Label ones = new Label();
			Label tens = new Label();
			Label hundreds = new Label();
			
			ones.setStyle("-fx-padding: -10;");
			tens.setStyle("-fx-padding: -10;");
			hundreds.setStyle("-fx-padding: -10;");
			
			top.getChildren().add(hundreds);
			top.getChildren().add(tens);
			top.getChildren().add(ones);

			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> increment(ones, tens, hundreds))); 	//creates an object that will call an increment function every second
			timeline.setCycleCount(Animation.INDEFINITE);

			ImageView pic = new ImageView(new Image("res/smileface.png"));
			Button face = new Button("", pic);
			face.setMinSize(50, 50);
			face.setMaxSize(50, 50);
			face.setPadding(Insets.EMPTY);
			top.getChildren().add(face);

			Label mineCount = new Label(String.format("%03d", b.getMinesLeft()));
			mineCount.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
			top.getChildren().add(mineCount);

			top.setStyle(
					"-fx-background-color: #bfbfbf; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width:3; -fx-border-radius: 0.001;");
			gridPane.setStyle(
					"-fx-background-color: #B0C6DA; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width:3; -fx-border-radius: 0.001;");
			borderPane.setStyle(
					"-fx-background-color: #bfbfbf; -fx-border-color:  #fafafa #787878 #787878 #fafafa; -fx-border-width:3; -fx-border-radius: 0.001;");

			borderPane.setTop(top);
			borderPane.setCenter(gridPane);

			MenuBar menuBar = new MenuBar();
			Menu mainMenu = new Menu("Menu");
			Menu diff = new Menu("Difficulty");
			MenuItem highScore = new MenuItem("High Scores");

			// HBox scores = new HBox(25);

			// Label bgn = new Label("Beginner");
			// bgn.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			// scores.getChildren().add(bgn);
			//
			// Label med = new Label("Medium");
			// med.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			// scores.getChildren().add(med);
			//
			// Label hrd = new Label("Hard");
			// hrd.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			// scores.getChildren().add(hrd);
			//
			//
			// vb.getChildren().add(scores);
			//
			// HBox scoreName = new HBox(25);
			// scoreName.getChildren().addAll(new Label("Name: "));
			// vb.getChildren().add(scoreName);

			menuBar.getMenus().add(mainMenu);
			vb.getChildren().add(menuBar);
			vb.getChildren().add(top);
			borderPane.setTop(vb);

			mainMenu.getItems().add(diff);
			MenuItem easy = new MenuItem("Beginner");
			diff.getItems().add(easy);
			MenuItem medium = new MenuItem("Intermediate");
			diff.getItems().add(medium);
			MenuItem hard = new MenuItem("Expert");
			diff.getItems().add(hard);

			mainMenu.getItems().add(highScore);
			highScore.setOnAction(e -> {
				DisplayHighScores display = new DisplayHighScores();
			});

			face.setOnAction(e -> {
				timeline.stop();
				theStage.close();
				start(theStage);
			});

			easy.setOnAction(e -> {
				d = new Difficulty(8, 8, 10);
				diffSelected = true;
				b.setDiffLevel(0);
				timeline.stop();
				theStage.close();
				start(theStage);
			});

			medium.setOnAction(e -> {
				d = new Difficulty(16, 16, 40);
				diffSelected = true;
				b.setDiffLevel(1);
				timeline.stop();
				theStage.close();
				start(theStage);
			});

			hard.setOnAction(e -> {
				d = new Difficulty(32, 16, 99);
				diffSelected = true;
				b.setDiffLevel(2);
				timeline.stop();
				theStage.close();
				start(theStage);
			});

			MSButton[][] buttonArray; = new MSButton[d.getXAxis()][d.getYAxis()];
			handleEvents(buttonArray, face, mineCount, timeline);

			for (int i = 0; i < d.getXAxis(); i++) {
				for (int j = 0; j < d.getYAxis(); j++) {
					gridPane.add(buttonArray[i][j], i, j);
				}
			}
			theStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//most of the game logic is controlled inside this method
	private static void handleEvents(MSButton[][] buttonArray, Button face, Label mineCount, Timeline timeline) {
		for (int row = 0; row < buttonArray.length; row++) {
			for (int col = 0; col < buttonArray[row].length; col++) {
				final int x = row;
				final int y = col;
				MSButton tile = new MSButton(x, y);
				tile.setGraphic(new ImageView(new Image("res/cover.png")));
				buttonArray[x][y] = tile;
				
				tile.setOnMousePressed(e -> {
					if (!b.getGameOver())
						face.setGraphic(new ImageView(new Image("res/oface.png")));
				});

				tile.setOnMouseReleased(e -> {
					if (!b.getGameOver())
						face.setGraphic(new ImageView(new Image("res/smileface.png")));
				});

				try {
					tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							MouseButton click = event.getButton();
							if (click == MouseButton.PRIMARY && !b.getGameOver()) {
								if (!b.getFirstClick()) {	//if first click of the game, retry board generation until first click location is NOT a mine
									do {
										b = new Board(d);
									} while (b.getCell(x, y).getValue() != 0);
									b.firstClickMade();
									displayMineField(b.getCells());
								}
								timeline.play();	//start game timer
								if (b.getCell(x, y).getValue() == -1 && b.getCell(x, y).isFlagged() == false) { 	//user clicks mine
									loseGame(buttonArray, face, timeline);
									b.setGameOver(true);
									buttonArray[x][y].setGraphic(new ImageView(new Image("res/redmine.png")));
								} else if (b.getCell(x, y).getValue() >= 0 && b.getCell(x, y).isFlagged() == false) {
									if (b.getCell(x, y).isDiscovered() == false) {
										floodFill(x, y, buttonArray);
										b.getCell(x, y).discover();
										b.displayNumbers(buttonArray[x][y], x, y);
										if (b.getHiddenTiles() == b.getMinesLeft()) {	//if the number of unclicked tiles == number of mines the user has won
											winGame(face, timeline);
											b.setGameOver(true);
										}
									}
								}
								if (b.getCell(x, y).getValue() == 0 && b.getCell(x, y).isFlagged() == false) {
									floodFill(x, y, buttonArray);
								}
							} else if (click == MouseButton.SECONDARY && !b.getGameOver()) {	//user flags tile
								tile.state++;
								tile.state %= 2;
								if (b.getCell(x, y).isDiscovered() == false) {
									switch (tile.state) {
									case 0:
										tile.setGraphic(new ImageView(new Image("res/cover.png")));
										b.getCell(x, y).unFlag();
										b.addMine();
										mineCount.setText("0" + Integer.toString(b.getMinesLeft()));
										if (b.getMinesLeft() < 10) {
											mineCount.setText("00" + Integer.toString(b.getMinesLeft()));
										}
										break;
									case 1:
										if (b.getMinesLeft() > 0) {
											tile.setGraphic(new ImageView(new Image("res/flag.png")));
											b.getCell(x, y).flag();
											b.removeMine();
											mineCount.setText(Integer.toString(b.getMinesLeft()));
											if (b.getMinesLeft() < 10)
												mineCount.setText("00" + Integer.toString(b.getMinesLeft()));
										}
										break;
									}
								}
							}
						}
					});
				} catch (Exception f) {
					System.out.println(f + "Handle Events Method");
				}
			}
			System.out.println();
		}
	}
	
	//increments and updates the timer label
	private void increment(Label onesLabel, Label tensLabel, Label hundredsLabel) {
		b.addTime();
		int ones = b.getTime() % 10;
		int tens = b.getTime() / 10 % 10;
		int hundreds = b.getTime() / 100;

		onesLabel.setGraphic(new ImageView(new Image("res/digits/" + ones + ".png")));
		tensLabel.setGraphic(new ImageView(new Image("res/digits/" + tens + ".png")));
		hundredsLabel.setGraphic(new ImageView(new Image("res/digits/" + hundreds + ".png")));

	}
	
	//displays board value array in console (used for development and debugging)
	private static void displayMineField(Cell[][] mineField) {
		System.out.println("Display Mine Field One");
		for (int row = 0; row < mineField.length; row++) {
			for (int col = 0; col < mineField[row].length; col++) {
				System.out.print(mineField[col][row].getValue() + "  ");
			}
			System.out.println();
		}
		System.out.println();
	}

	//recursivly reveals tiles that are empty and adjacent to an empty tile
	private static void floodFill(int x, int y, MSButton[][] btn) {
		try {

			if (!isValid(x, y))
				return;
			if (b.getCell(x, y).isDiscovered() == true)
				return;

			if (isValid(x, y)) {
				if (b.getCell(x, y).getValue() == 0) {
					b.displayNumbers(btn[x][y], x, y);
					b.removeHiddenTile();
					b.getCell(x, y).discover();

					floodFill(x - 1, y - 1, btn);
					floodFill(x - 1, y, btn);
					floodFill(x - 1, y + 1, btn);
					floodFill(x, y - 1, btn);
					floodFill(x, y + 1, btn);
					floodFill(x + 1, y - 1, btn);
					floodFill(x + 1, y, btn);
					floodFill(x + 1, y + 1, btn);
				}
				if (b.getCell(x, y).getValue() > 0) {
					b.displayNumbers(btn[x][y], x, y);
					b.removeHiddenTile();
					b.getCell(x, y).discover();
				}
			}
		} catch (Exception e) {
			System.out.println(e + " Flood fill method");
		}
	}

	//returns a boolean based on if the location passed in is within the game board
	private static boolean isValid(int col, int row) {
		if (row < 0 || col < 0 || row >= d.getYAxis() || col >= d.getXAxis()) {
			return false;
		}
		return true;
	}

	//changes face graphic and freezes game at current state
	private static void winGame(Button face, Timeline timeline) {
		timeline.stop();
		face.setGraphic(new ImageView(new Image("res/winface.png")));
		System.out.println("Score: " + b.getTime());
		// check if score is lower than high score, if it is then newHighScore()
	//	newHighScore();
	}

	//changes face graphic, reveals all remaining mines, and freezes game at current state
	private static void loseGame(MSButton[][] buttonArray, Button face, Timeline timeline) {
		timeline.stop();
		for (int i = 0; i < d.getXAxis(); i++) {
			for (int j = 0; j < d.getYAxis(); j++) {
				if (b.getCell(i, j).getValue() == -1) {
					buttonArray[i][j].setGraphic(new ImageView(new Image("res/-1.png")));
				}
				if (buttonArray[i][j].state == 1 && b.getCell(i, j).getValue() != -1) {
					buttonArray[i][j].setGraphic(new ImageView(new Image("res/misflagged.png")));
				}
			}
		}
		face.setGraphic(new ImageView(new Image("res/deadface.png")));
		System.out.println("Score: " + b.getTime());
	}

	//part of the experimental high score system I never finished implemeting
	private static void newHighScore() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Score");
		dialog.setHeaderText("Enter your name:");
		dialog.setContentText("Name:");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> {
			HighScore hs = new HighScore(name, b.getTime(), b.getDiffLevel());
			try {
				hs.saveHighScore(hs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}