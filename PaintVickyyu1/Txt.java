// Vicky Yu
package PaintVickyyu1;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Txt extends ShapeClass {
	double xPos;
	double yPos;
	String textPrinted;

	public Txt(hw p, Pane pane, Color c, MouseEvent me, String txt) {
		super(p, c);
		shape = new Text(txt); // make text
		((Text) shape).setX(me.getX()); // set position of text
		xPos = me.getX();
		((Text) shape).setY(me.getY());
		yPos = me.getY();
		shape.setFill(color);
		textPrinted = txt;
		adjustPoint = new Ellipse(); // draggable point
		adjustPoint.setFill(Color.BLACK);
		adjustPoint.setOnMouseDragged((event -> {
			adjustPoint.setCenterX(event.getX());
			adjustPoint.setCenterY(event.getY());
			draw(event);
		}));
		pane.getChildren().addAll(shape, adjustPoint); // add text and draggable point to screen
		move();

	}

	public Txt(hw p, Pane pane, Color c, String txt, double x, double y) { // loading text from file
		super(p, c);
		shape = new Text(txt);
		((Text) shape).setX(x);
		xPos = x;
		((Text) shape).setY(y);
		yPos = y;
		shape.setFill(color);
		adjustPoint = new Ellipse();
		adjustPoint.setFill(Color.BLACK);
		adjustPoint.setOnMouseDragged((event -> {
			adjustPoint.setCenterX(event.getX());
			adjustPoint.setCenterY(event.getY());
			draw(event);
		}));
		pane.getChildren().addAll(shape, adjustPoint);
		move();

	}

	public void draw(MouseEvent me) { // drawing text
		adjustPoint.setCenterX(me.getX());
		adjustPoint.setCenterY(me.getY());
		adjustPoint.setRadiusX(3);
		adjustPoint.setRadiusY(3);

	}

	public String loadShape() { // saving text
		return "text " + color + " " + textPrinted + " " + xPos + " " + yPos;
	}
}
