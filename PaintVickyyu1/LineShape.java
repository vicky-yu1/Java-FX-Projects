// Vicky Yu
package PaintVickyyu1;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

public class LineShape extends ShapeClass {
	Line line;
	double startX;
	double endX;
	double startY;
	double endY;

	public LineShape(hw p, Pane pane, Color c, MouseEvent me) // make line
	{
		super(p, c);
		shape = new Line(me.getX(), me.getY(), me.getX(), me.getY());
		startX = me.getX();
		startY = me.getY();
		shape.setStroke(color); // set color
		adjustPoint = new Ellipse(); // point for dragging
		adjustPoint.setFill(Color.BLACK);
		adjustPoint.setOnMouseDragged((event -> {
			adjustPoint.setCenterX(event.getX());
			adjustPoint.setCenterY(event.getY());
			draw(event);
		}));
		pane.getChildren().addAll(shape, adjustPoint); // add line and point to screen
		move();
	}

	public LineShape(hw p, Pane pane, Color c, double x1, double y1, double x2, double y2) { // loading from file
		super(p, c);
		shape = new Line(x1, y1, x2, y2);
		shape.setStroke(color);
		adjustPoint = new Ellipse(); // make point for draggnig
		adjustPoint.setFill(Color.BLACK);
		adjustPoint.setOnMouseDragged((event -> {
			adjustPoint.setCenterX(event.getX());
			adjustPoint.setCenterY(event.getY());
			draw(event);
		}));
		pane.getChildren().addAll(shape, adjustPoint);
		move();
	}

	public void draw(MouseEvent me) { // drawing line
		((Line) shape).setEndX(me.getX());
		((Line) shape).setEndY(me.getY());
		endX = me.getX();
		endY = me.getY();
		adjustPoint.setCenterX(endX);
		adjustPoint.setCenterY(endY);
		adjustPoint.setRadiusX(3);
		adjustPoint.setRadiusY(3);
		shape.setFill(color);
	}

	public String loadShape() { // for saving file
		return "line " + color + " " + startX + " " + startY + " " + endX + " " + endY;
	}
}