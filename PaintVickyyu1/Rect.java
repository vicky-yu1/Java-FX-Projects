// Vicky Yu
package PaintVickyyu1;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Rect extends ShapeClass {
	Rectangle r;
	Point[] corners = new Point[2];
	double startX;
	double startY;
	double xdif;
	double ydif;

	public Rect(hw p, Pane pane, Color c, MouseEvent me) { // make shape
		super(p, c);
		corners[0] = new Point(me);
		shape = new Rectangle();
		shape.setLayoutX(me.getX()); // set polisiton of shape
		shape.setLayoutY(me.getY());
		startX = me.getX();
		startY = me.getY();
		shape.setFill(c); // set color
		adjustPoint = new Ellipse(); // point for dragging
		adjustPoint.setFill(Color.BLACK);
		adjustPoint.setOnMouseDragged((event -> {
			adjustPoint.setCenterX(event.getX());
			adjustPoint.setCenterY(event.getY());
			draw(event);
		}));
		pane.getChildren().addAll(shape, adjustPoint); // add rect and point to screen
		move();
	}

	public Rect(hw p, Pane pane, Color c, double x, double y, double width, double height) { // loading rectangle
		super(p, c);
		shape = new Rectangle(x, y, width, height);
		shape.setFill(c);
		System.out.println("Loaded");
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

	public void setSize(int x, int y) { // set size of rectangle
		((Rectangle) shape).setWidth(x);
		((Rectangle) shape).setHeight(y);
	}

	public void draw(MouseEvent me) { // draw rectangle
		corners[1] = new Point(me);
		xdif = corners[0].xdif(corners[1]);
		ydif = corners[0].ydif(corners[1]);
		((Rectangle) shape).setWidth(xdif);
		((Rectangle) shape).setHeight(ydif);
		adjustPoint.setCenterX(me.getX()); // change position of dragged point
		adjustPoint.setCenterY(me.getY());
		adjustPoint.setRadiusX(3);
		adjustPoint.setRadiusY(3);
		shape.setFill(color);

	}

	public String loadShape() { // saving shape
		return "rect " + color + " " + startX + " " + startY + " " + xdif + " " + ydif;
	}
}
