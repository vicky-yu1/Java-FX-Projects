// Vicky Yu
package PaintVickyyu1;

import PaintVickyyu1.hw.Mode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Circ extends ShapeClass {
	double xPos;
	double yPos;
	double xRadius;
	double yRadius;
	Pane pane_global;

	public Circ(hw p, Pane pane, Color c, MouseEvent me) {
		super(p, c);
		originalX = me.getX();
		originalY = me.getY();
		shape = new Ellipse(10, 10);
		((Ellipse) shape).setCenterX(me.getX()); // set position of ellipse
		xPos = me.getX();
		yPos = me.getY();
		((Ellipse) shape).setCenterY(me.getY());
		shape.setFill(color);
		adjustPoint = new Ellipse(); // make point for draggnig
		adjustPoint.setFill(Color.BLACK);
		adjustPoint.setOnMouseDragged((event -> {
			adjustPoint.setCenterX(event.getX());
			adjustPoint.setCenterY(event.getY());
			draw(event);
		}));
		pane.getChildren().addAll(shape, adjustPoint); // add shape and point onto screen
		pane_global = pane;

		move();
	}

	public Circ(hw p, Pane pane, Color c, double x, double y, double width, double height) { // loading ellipse from
																								// file
		super(p, c);
		shape = new Ellipse(x, y, width, height);
		shape.setFill(color);
		if (this.getModeResize()) {
			adjustPoint = new Ellipse();
			adjustPoint.setFill(Color.BLACK);
			adjustPoint.setOnMouseDragged((event -> {
				adjustPoint.setCenterX(event.getX());
				adjustPoint.setCenterY(event.getY());
				draw(event);
			}));
		}
		pane.getChildren().addAll(shape, adjustPoint);
		move();
	}

	public void draw(MouseEvent me) { // drawing shape
		double xdif = Math.abs(originalX - me.getX());
		double ydif = Math.abs(originalY - me.getY());
		xRadius = xdif;
		yRadius = ydif;
		((Ellipse) shape).setRadiusX(xdif);
		((Ellipse) shape).setRadiusY(ydif);
		adjustPoint.setCenterX(xPos + xRadius);
		adjustPoint.setCenterY(yPos + yRadius);
		adjustPoint.setRadiusX(3);
		adjustPoint.setRadiusY(3);
		shape.setFill(color);
	}

	public String loadShape() { // for saving file
		return "ellipse " + color + " " + xPos + " " + yPos + " " + xRadius + " " + yRadius;
	}
}