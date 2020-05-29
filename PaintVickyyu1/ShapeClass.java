// Vicky Yu
package PaintVickyyu1;

import PaintVickyyu1.hw.Mode;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public abstract class ShapeClass {
	hw p;
	Color color;
	Shape shape;
	Dist distance = new Dist();
	double originalX;
	double originalY;
	boolean makeNew = false;
	int state = 0;
	Ellipse adjustPoint;
	boolean modeResize = false; // is the mode currently RESIZE

	public ShapeClass(hw p, Color c) {
		this.p = p;
		color = c;
	}

	public void move() {
		shape.setOnMousePressed(me -> {
			if (p.getMode() == Mode.RESIZE) { // move shape around screen
				select();
				double num = shape.getLayoutX() - me.getSceneX();
				distance.setX(num);
				distance.setY(shape.getLayoutY() - me.getSceneY());
				shape.setCursor(Cursor.MOVE);
				changeColor(p.getSelectedColor());
				modeResize = true;
			}
		});
		shape.setOnMouseDragged(me -> { // move shape around screen
			if (p.getMode() == Mode.RESIZE) {
				shape.setCursor(Cursor.CLOSED_HAND);
				shape.setLayoutX(me.getSceneX() + distance.getX());
				shape.setLayoutY(me.getSceneY() + distance.getY());
				adjustPoint.setCenterX(me.getSceneX() + distance.getX());
				adjustPoint.setCenterY(me.getSceneY() + distance.getY());

			}
		});

		shape.setOnMouseReleased(me -> {
			if (p.getMode() == Mode.RESIZE) {
				shape.setCursor(Cursor.CLOSED_HAND);
				modeResize = false;
			}
		});

	}

	public void select() {
		p.selectShape(this);
	}

	public void delete() {
		p.getPane().getChildren().remove(shape);
	}

	public void changeColor(Color selectedColor) {
		color = selectedColor;
		if (shape.equals("line")) {
			shape.setStroke(this.color);
		} else {
			shape.setFill(this.color);
		}
	}

	public boolean getModeResize() {
		return modeResize;
	}

	public Ellipse getadjustPoint() {
		return adjustPoint;
	}

	public abstract void draw(MouseEvent me); // implement functions in specific children

	public abstract String loadShape();
}

class Dist {
	double x;
	double y;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double num) {
		x = num;
	}

	public void setY(double num) {
		y = num;
	}
}