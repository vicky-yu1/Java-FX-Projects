// Vicky yu
package PaintVickyyu1;

import java.util.Scanner;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Poly extends ShapeClass {
	public Poly(hw p, Pane pane, Color c, MouseEvent me) {
		super(p, c);
		shape = new Polygon();
		((Polygon) shape).getPoints().addAll(me.getX(), me.getY()); // add all points clicked on by moes
		shape.setFill(color); // setting color
		adjustPoint = new Ellipse(); // point for dragging
		adjustPoint.setFill(Color.BLACK);
		adjustPoint.setOnMouseDragged((event -> {
			adjustPoint.setCenterX(event.getX());
			adjustPoint.setCenterY(event.getY());
			draw(event);
		}));
		pane.getChildren().addAll(shape, adjustPoint); // add point and polygon
		move();
	}

	public Poly(hw p, Pane pane, Color c) { // for loading shape
		super(p, c);
		shape = new Polygon();
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

	public void addPoints(Scanner s) { // loading file add points of polygon
		while (s.hasNextDouble()) {
			((Polygon) shape).getPoints().add(s.nextDouble());
		}
	}

	public Shape getShape() {
		return shape;
	}

	public void draw(MouseEvent me) { // draw polygon by adding point to list
		((Polygon) shape).getPoints().add(me.getX());
		((Polygon) shape).getPoints().add(me.getY());

	}

	public String loadShape() { // saving polygon
		String statementReturned = "poly " + color + " ";
		for (double num : ((Polygon) shape).getPoints()) {
			statementReturned += num + " ";
		}
		return statementReturned;
	}
}
