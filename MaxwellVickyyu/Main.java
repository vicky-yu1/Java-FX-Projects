package MaxwellVickyyu;

import java.util.LinkedList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {
	LinkedList<Ball2> listR;
	LinkedList<Ball2> listL;
	LinkedList<Ball2> allBalls;
	LinkedList<Integer> count;
	Random rand = new Random();
	Opening space;
	Scene s;
	int numRRed = 0;
	int numLRed = 0;
	int numRBlue = 0;
	int numLBlue = 0;
	boolean down = false;
	boolean pressed = false;
	Group root;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		root = new Group(); 
        Line line = new Line(250,0,250,500); //the wall
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(5);
        root.getChildren().add(line); 
        space = new Opening(); //the door
        root.getChildren().add(space);
		s = new Scene(root, 500, 500, Color.BLACK);
		listL = new LinkedList<Ball2>(); //left pane
		listR = new LinkedList<Ball2>(); //right pane
		allBalls = new LinkedList<Ball2>();
		count = new LinkedList<Integer>(); //keeps track of num balls of each color on each side
		count.add(numRRed);
		count.add(numRBlue);
		count.add(numLRed);
		count.add(numLBlue);
		Button addBall = new Button("Add ball"); 
		root.getChildren().add(addBall);
		addBall.setOnMouseClicked(me -> { //adds ball on click
			Ball2 newBallBlue = new Ball2(rand, Color.BLUE, true);
			listL.add(newBallBlue);
			allBalls.add(newBallBlue);
			root.getChildren().add(newBallBlue);
			Ball2 newBallRed = new Ball2(rand, Color.RED, false);
			listR.add(newBallRed);
			allBalls.add(newBallRed);
			root.getChildren().add(newBallRed);
		});
		for(int i = 0; i < 6; i++) {
			if(i % 2 == 0) { //left side
				Ball2 c_l1;
				if(i == 0 || i == 2) {
					c_l1 = new Ball2(rand, Color.BLUE, true);
				}
				else {
					c_l1 = new Ball2(rand, Color.RED, true);
				}
				count.set(2, 1); //numLRed = 1;
				count.set(3, 2); //numLBlue = 2;
				listL.add(c_l1);
				allBalls.add(c_l1);
				root.getChildren().add(c_l1);
			}
			else { // right side
				Ball2 c_l2;
				if(i == 1 || i == 3) {
					c_l2 = new Ball2(rand, Color.RED, false);
				}
				else {
					c_l2 = new Ball2(rand, Color.BLUE, false);
				}
				count.set(0, 2); //numRRed = 2;
				count.set(1, 1); //numRBlue = 1;
				listR.add(c_l2);
				allBalls.add(c_l2);
				root.getChildren().add(c_l2);
			}
		}
		stage.setScene(s);
		stage.show();
		
		Driver d = new Driver();
    	d.start();   
	}
	
	public class Driver extends AnimationTimer
    {
    	@Override
    	public void handle( long now )
    	{
    		for (Ball2 ball : listL ){  // all balls move every frame
    			ball.move();
            }
    		for (Ball2 ball : listR ){ 
    			ball.move();
            }
    		s.setOnMouseMoved(me -> { 
    			space.move(me.getY());
    		});
    		s.setOnMousePressed(me -> { //when mouse is held down, door opens
    			space.setInvisible(me.getY());
    			down = true; //flag that tracks if mouse is down
    		});
    		if(down) {
    			for (Ball2 ball : allBalls ){ 
    				if(ball.intersects(space.getBoundsInLocal())) { //door
    					System.out.println("$Y$#YI");
    					if(ball.getFlag()) {
    						if(ball.getColor() == Color.RED) {
    							int value1 = count.get(2) - 1; //numLRed
    							int value2 = count.get(0) + 1; //numRRed
    							count.set(2, value1) ;
    							count.set(0, value2) ;
    						}
    						else {
    							int value1 = count.get(1) + 1; //numRBlue
    							int value2 = count.get(3) - 1; //numLBlue
    							count.set(1, value1) ;
    							count.set(3, value2) ;
    						}
    					}
    					else { //originally in right side, going to left side
    						if(ball.getColor() == Color.RED) {
    							int value1 = count.get(2) + 1; //numLRed
    							int value2 = count.get(0) - 1; //numRRed
    							count.set(2, value1) ;
    							count.set(0, value2) ;
    						}
    						else {
    							int value1 = count.get(1) - 1; //numRBlue
    							int value2 = count.get(3) + 1; //numLBlue
    							count.set(1, value1);
    							count.set(3, value2);
    						}
    					}
    					ball.setFlag();
    				}
    				//ball.intersects(space, count); //if ball hits the door
    				checkWin(); //check if user wins game
    				if(ball.get_flag_alr_set()) {
    					ball.set_flag_alr_set();
    				}
                }
	    		s.setOnMouseDragged(me -> { //if mouse is down and mouse is dragged
	    			space.setInvisible(me.getY()); //mouse door along w/ mouse
	    		});
    		}
    		
    		s.setOnMouseReleased(me -> { //door closes when mouse released
    			space.setVisible();
    			down = false;
    		});
    	}
    }
	
	public void checkWin() { //checks if number of color balls is equal on both sides w/ no opposing color balls
		if((count.get(0) == 0 && count.get(3) == 0) || (count.get(1) == 0 && count.get(2) == 0)) { //numRRed == 0 && numLBlue == 0) || (numRBlue == 0 && numLRed == 0
			VBox winRoot = new VBox(5);
			Label label = new Label("You won!"); //win message
			label.setAlignment(Pos.CENTER); 
			winRoot.getChildren().add(label);
			winRoot.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
			winRoot.setPrefWidth(80);
			winRoot.setPrefHeight(40); 
			//centers the message
			winRoot.layoutXProperty().bind(s.widthProperty().subtract(winRoot.layoutBoundsProperty().get().getWidth()).divide(2));
			winRoot.layoutYProperty().bind(s.heightProperty().subtract(winRoot.layoutBoundsProperty().get().getHeight()).divide(2));
			root.getChildren().add(winRoot);
		}
	}
	
	
}
