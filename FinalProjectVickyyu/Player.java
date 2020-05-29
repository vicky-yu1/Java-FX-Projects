package FinalProjectVickyyu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import MaxwellVickyyu.Ball2;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Player extends ImageView {
	ImageView imageView;
	int jumpHeight;
	int speed;
	Group group;
	Scene scene;
	private EventHandler<KeyEvent> keyEventHandler;
	LinkedList<Obstacles> list;
	
	public Player(Scene s, Group g, int sizeX, int sizeY, LinkedList<Obstacles> listObstacles) {
		scene = s;
		speed = 10;
		jumpHeight = 150;
		group = g;
		list = listObstacles;
		try {
			setImage(new Image(new FileInputStream("src/FinalProjectVickyyu/BlueToad.png"))); // importing player image
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setFitHeight(100); // setting size of image
		setPreserveRatio(true);
		setX(50);
		setY(sizeY - 100);
		g.getChildren().add(this);
		keyEventHandler = new EventHandler<KeyEvent>() { // player movements
            public void handle(final KeyEvent keyEvent) {
				Bounds boundsInScene = localToScene(getBoundsInLocal());
				if(boundsInScene.getMinY() >= 350) { // can't move past traffic cone
					if (keyEvent.getCode() == KeyCode.SPACE) {
					    jump(imageView, jumpHeight); 
					}
				}
				if(keyEvent.getCode() == KeyCode.RIGHT) {
					if(getX() < 350) { //can't move past traffic cone
						setX(getX() + speed);
					}
				}
				else if(keyEvent.getCode() == KeyCode.LEFT) {
					if(getX() > 10) { // can't move off screen
						setX(getX() - speed);
					}
					
				}
            }
		};
		 s.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
	}
	
	public void jump(ImageView iv, int jumpHeight) {
		TranslateTransition translation = new TranslateTransition(Duration.millis(500), this);
		translation.interpolatorProperty().set(Interpolator.SPLINE(.1, .1, .7, .7));
		translation.setByY(-jumpHeight);
		translation.setAutoReverse(true); // player goes back down
		translation.setCycleCount(2);
		translation.play();
	}

	public int getHeight() {
		return jumpHeight;
	}
	public int getSpeed() {
		return speed;
	}
	public void gameOver() { // remove ability to move
		scene.removeEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
	}
	public void startAgain() { // resetting variables
		scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
		speed = 10;
		jumpHeight = 150;
	}
	public void decreaseSpeed() {
		speed -= 2;
		Random rand = new Random();
		if(rand.nextInt(2) % 2 == 0) { // 50% probability of reduced jump height
			jumpHeight -= (rand.nextInt(20) + 10);
		}
	}
	public void increaseSpeed() {
		speed += 3;
		jumpHeight += 15;
	}
}
