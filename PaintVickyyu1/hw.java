// Name: Vicky Yu
// Netid: vickyyu
// Course: ITP 368 - Programming Graphical Interface
// Spr 2020

package PaintVickyyu1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Scanner;

import Sample.GenEllipse;
import Sample.GenLine;
import Sample.GenPolygon;
import Sample.GenRectangle;
import Sample.GenShape;
import Sample.GenText;
import Sample.Point;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class hw extends Application {
	Pane root = new Pane();
	StackPane canvas = new StackPane();

	enum Mode {
		RECT, OVAL, LINE, TRIANGLE, TEXT, NONE, RESIZE
	};

	Mode mode = Mode.NONE;
	LinkedList<ShapeClass> theList;
	Point[] corners = new Point[2];
	ShapeClass t = null;
	double lineStartX;
	double lineStartY;
	ShapeClass selected;
	TextField txt;
	Color color = Color.BLACK;
	double originalX;
	double originalY;
	Scanner scan;
	ColorPicker cp;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Paint Program");
		Group menu = new Group();
		VBox options = new VBox(8); // different shape modes and save/load options
		Button resize = new Button("Resize/Move/Change Color");
		resize.setOnMouseClicked(mouseEvent -> { // mode=Mode.LINE;
			mode = Mode.RESIZE;
		});
		Button line_button = new Button("Line");
		line_button.setOnMouseClicked(mouseEvent -> { // mode=Mode.LINE;
			mode = Mode.LINE;
		});
		Button rect_button = new Button("Rectangle");
		rect_button.setOnMouseClicked(mouseEvent -> { // mode=Mode.RECT;
			mode = Mode.RECT;
		});
		Button triangle_button = new Button("Triangle");
		triangle_button.setOnMouseClicked(mouseEvent -> { // mode=Mode.TRIANGLE;
			mode = Mode.TRIANGLE;
		});
		Button oval_button = new Button("Oval");
		oval_button.setOnMouseClicked(mouseEvent -> { // mode=Mode.OVAL;
			mode = Mode.OVAL;
		});
		txt = new TextField();
		Button txt_button = new Button("Text");
		txt_button.setOnMouseClicked(mouseEvent -> { // mode=Mode.TEXT;
			mode = Mode.TEXT;
		});
		HBox textRow = new HBox(5);
		textRow.getChildren().add(txt_button);
		textRow.getChildren().add(txt);

		cp = new ColorPicker(Color.BLACK);
		cp.setOnAction((event -> {
			color = cp.getValue();
		}));

		Button save_button = new Button("Save");
		Label save = new Label();
		HBox saveInfo = new HBox();
		saveInfo.getChildren().addAll(save_button, save);
		Button load_button = new Button("Load");

		// add all options to VBox
		options.getChildren().addAll(resize, line_button, rect_button, triangle_button, oval_button, txt_button, txt,
				cp, saveInfo, load_button);
		menu.getChildren().add(options);

		// all the shapes currently on screen
		theList = new LinkedList<ShapeClass>();

		// add shapes if shape button is clicked
		root.setOnMousePressed((mI -> {
			if (mode == mode.LINE) {
				t = new LineShape(this, root, color, mI);
				theList.add(t);
			} else if (mode == mode.RECT) {
				t = new Rect(this, root, color, mI);
				theList.add(t);
			} else if (mode == mode.OVAL) {
				t = new Circ(this, root, color, mI);
				theList.add(t);
			} else if (mode == mode.TRIANGLE) { // add points and shape later by counting clicks
			} else if (mode == mode.RESIZE) {

			}
		}));
		// draw shape
		root.setOnMouseDragged((me -> {
			if (mode == mode.LINE) {
				t.draw(me);
			} else if (mode == mode.RECT) {
				t.draw(me);
			} else if (mode == mode.OVAL) {
				t.draw(me);
			} else if (mode == mode.TRIANGLE) {

			}
		}));
		root.setOnMouseReleased((me -> {
			if (mode == mode.LINE) {
				t = null;
			} else if (mode == mode.RECT) {
				t = null;
			} else if (mode == mode.OVAL) {
				t = null;
			} else if (mode == mode.TRIANGLE) {
				if (me.getClickCount() == 1) { // if first point, make new polygon/triangle
					if (t == null) {
						t = new Poly(this, root, color, me);
						theList.add(t);
					} else { // if not point, add point to polygon
						t.draw(me);
						t.getadjustPoint().setCenterX(me.getX());
						t.getadjustPoint().setCenterY(me.getY());
						t.getadjustPoint().setRadiusX(3);
						t.getadjustPoint().setRadiusY(3);
					}
				} else if (me.getClickCount() == 2 && t != null) { // stop adding points when 2+ clicks
					t = null;
				}
			}
			if (mode == mode.TEXT) { // text appear on screen
				t = new Txt(this, root, color, me, txt.getText());
			}
		}));
		save_button.setOnMouseClicked(mouseEvent -> {
			try {
				saveFile(save_button, save); // call function
			} catch (Exception e1) {
				System.out.println("File cannot be saved");
			}
		});
		load_button.setOnMouseClicked(mouseEvent -> {
			try {
				loadData(stage, load_button, save); // call function
			} catch (Exception e1) {
				System.out.println("File not found");
			}
		});

		root.getChildren().addAll(menu, canvas);
		Scene s = new Scene(root, 500, 500);
		stage.setScene(s);
		stage.show();
		s.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.BACK_SPACE) { // delete shape from screen and list
				if (mode == mode.RESIZE && selected != null) {
					selected.delete();
					theList.remove(selected);
				}
			}
		});

	}

	public void selectShape(ShapeClass shape) {
		selected = shape;
	}

	public Pane getPane() {
		return root;
	}

	public Mode getMode() {
		return mode;
	}

	public void saveFile(Button b, Label l) {
		try {
			FileWriter saveWriter = new FileWriter("vickyyuPaint.txt");
			for (ShapeClass shape : theList) { // write text so loadFile can parse through it later
				saveWriter.write(shape.loadShape() + "\n");
			}
			saveWriter.close();
			l.setText("Canvas saved at " + LocalDate.now() + " " + LocalTime.now()); // timestamp saved
			readLines();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadData(Stage s, Button b, Label l) throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("."));
		fileChooser.setTitle("Load Paint");
		File selectedFile = fileChooser.showOpenDialog(s);
		if (selectedFile != null) {
			for (ShapeClass deleteShape : theList) { // delte everything on screen first
				deleteShape.delete();
				theList.remove(deleteShape);
			}
			scan = new Scanner(selectedFile);
		}
		while (scan.hasNextLine()) // parse through text to see position/shapes to add on screen
		{
			String shap = scan.next();
			if (shap.equals("line")) {
				t = new LineShape(this, root, Color.valueOf(scan.next()), scan.nextDouble(), scan.nextDouble(),
						scan.nextDouble(), scan.nextDouble());
				theList.add(t);
				scan.nextLine();

			} else if (shap.equals("rect")) {
				t = new Rect(this, root, Color.valueOf(scan.next()), scan.nextDouble(), scan.nextDouble(),
						scan.nextDouble(), scan.nextDouble());
				theList.add(t);
				scan.nextLine();

			} else if (shap.equals("poly")) {
				t = new Poly(this, root, Color.valueOf(scan.next()));
				((Poly) t).addPoints(scan);
				theList.add(t);
				scan.nextLine();

			} else if (shap.equals("ellipse")) {
				t = new Circ(this, root, Color.valueOf(scan.next()), scan.nextDouble(), scan.nextDouble(),
						scan.nextDouble(), scan.nextDouble());
				theList.add(t);
				scan.nextLine();

			} else if (shap.equals("text")) {
				t = new Txt(this, root, Color.valueOf(scan.next()), scan.next(), scan.nextDouble(), scan.nextDouble());
				theList.add(t);
				scan.nextLine();

			}
		}
		l.setText("Canvas loaded at " + LocalDate.now() + " " + LocalTime.now()); // timestamp loaded
		scan.close();
	}

	public void readLines() { // debugging
		BufferedReader br = null;
		String strLine = "";
		try {
			br = new BufferedReader(new FileReader("vickyyuPaint.txt"));
			while ((strLine = br.readLine()) != null) {
				System.out.println(strLine);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find the file: fileName");
		} catch (IOException e) {
			System.err.println("Unable to read the file: fileName");
		}
	}

	Color getSelectedColor() {
		return cp.getValue();
	}

}
