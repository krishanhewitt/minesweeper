
/* javaFX button class
		The actual button the user will click in-game
		Loads images from file into an ImageView property for later		
*/

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MSButton extends Button{
	int state; // 0 means blank, 1 means flagged
	int row;
	int col;
	ImageView imageCover, imageFlag, bombs;

	public MSButton(int row, int col) {
		this.row = row;
		this.col = col;
		state = 0;
		double size = 30;
		setMinWidth(size);
		setMaxWidth(size);
		setMinHeight(size);
		setMaxHeight(size);
		bombs = new ImageView(new Image("res/-1.png"));
		imageCover = new ImageView(new Image("res/cover.png"));
		imageFlag = new ImageView(new Image("res/flag.png"));

		imageCover.setFitWidth(size);
		imageCover.setFitHeight(size);
		imageFlag.setFitWidth(size);
		imageFlag.setFitHeight(size);
		bombs.setFitWidth(size);
		bombs.setFitHeight(size);

		setGraphic(imageCover);
	}

	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
}