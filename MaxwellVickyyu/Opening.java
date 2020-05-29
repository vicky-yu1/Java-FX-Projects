package MaxwellVickyyu;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Opening extends Rectangle {
	double y;
	public Opening() {
		super(247, 250, 6, 100);
		y = 250;
		setFill(Color.BLUE);
	}
	public void setInvisible(double pos) { //door opens
		setFill(Color.BLACK);
		move(pos);
	}
	public void setVisible() { //door closes
		setFill(Color.BLUE);
	}
	public void move(double pos) {
		setY(pos);
	}
	
	

}
