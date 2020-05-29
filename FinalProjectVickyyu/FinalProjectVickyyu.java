package FinalProjectVickyyu;

import java.awt.ScrollPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;

import com.sun.tools.javac.Main;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FinalProjectVickyyu extends Application { 
	// increased accessibility on buttons (no mouse click needed, simply press spacebar)
	Scene scene;
	Player p;
	int BACKGROUND_WIDTH = 750;
	LinkedList<Obstacles> listObstacles;
	LinkedList<BananaPeel> listBananas;
	LinkedList<Star> listStars;
	Group group;
	private long lastTime = 0;
	Text score;
	Text lives;
	int seconds = 0;
	int numLives = 5;
	Driver d;
	Group gameOverMenu;
	FinalProjectVickyyu main = this;
	public static void main( String[] args )
	{ launch(args); }

	public void start( Stage stage )
	{
		group = new Group();
		Group startMenu = new Group();
		gameOverMenu = new Group();
		int sizeX = 750;
		int sizeY = 450;
		scene = new Scene(startMenu, sizeX, sizeY, 	Color.	CORNFLOWERBLUE);
		stage.setTitle("Final Project");
		stage.setScene(scene);
		stage.show();

		Background background1 = new Background(scene, group);
//		File song = new File("src/FinalProjectVickyyu/song.mp3"); //traffic cone to indicate user cannot move pass this   
//		Media media = new Media("https://drive.google.com/file/d/1PO_QAcI_f9MTossOWYLNYEueUWn36hod/view?usp=sharing");
//		MediaPlayer mediaPlayer = new MediaPlayer(media);
//		mediaPlayer.setVolume(.3);// decimal 0-1
//		mediaPlayer.play();
		startMenu(startMenu);

		File file = new File("src/FinalProjectVickyyu/caution.png"); //traffic cone to indicate user cannot move pass this
		Image image = new Image(file.toURI().toString());
		ImageView caution = new ImageView(image);
		caution.setFitHeight(100);
		caution.setPreserveRatio(true);
		caution.setX(350);
		caution.setY(350);
		group.getChildren().addAll(caution);

		p = new Player(scene, group, sizeX, sizeY, listObstacles); // instantiating player object

		// initializing lists of obstacles and powerups
		listObstacles = new LinkedList<Obstacles>(); 
		listBananas = new LinkedList<BananaPeel>();
		listStars = new LinkedList<Star>();

		// score and number of lives text on screen
		score = new Text("Score: " + seconds);
		score.setX(550);
		score.setY(55);
		lives = new Text("Number of Lives: " + numLives);
		lives.setX(550);
		lives.setY(75);
		score.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		lives.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		group.getChildren().addAll(score, lives);
	}

	// side-scrolling background attempt
	public void setBackground(Background background1, Background background2) {
		TranslateTransition translateTransition =
				new TranslateTransition(Duration.millis(5000), background1);
		translateTransition.setFromX(0);
		translateTransition.setToX(-1 * BACKGROUND_WIDTH);
		TranslateTransition translateTransition2 =
				new TranslateTransition(Duration.millis(5000), background2);
		translateTransition2.setFromX(500);
		translateTransition2.setToX(-1 * BACKGROUND_WIDTH);
		ParallelTransition parallelTransition = 
				new ParallelTransition( translateTransition, translateTransition2 );
		parallelTransition.setCycleCount(Timeline.INDEFINITE);
		parallelTransition.play();
	}

	public class Driver extends AnimationTimer
	{
		@Override
		public void handle( long now )
		{
			if (lastTime != 0) { // timer for score (score = # seconds player survives)
				if (now > lastTime + 1_000_000_000) {
					seconds++;
					score.setText("Score: " + Integer.toString(seconds));
					lastTime = now;
				}
			} else {
				lastTime = now;
			}
			
			if(listObstacles.size() < 2) { // ensures screen always has 2 bombs
				Obstacles o = new Obstacles(main, group, listObstacles, p, numLives, lives);
				listObstacles.add(o);
			}
			if(listBananas.size() + listStars.size() < 2) { // ensures screen always has 2 bananas and/or stars
				BananaPeel bp = new BananaPeel(group, p);
				Star star = new Star(group, p);
				listBananas.add(bp);
				listStars.add(star);
			}
			Iterator<BananaPeel> iteratorBanana = listBananas.iterator(); // loop through obstacles (banana peels)
			while(iteratorBanana.hasNext()){
				BananaPeel peel = iteratorBanana.next();
				peel.move(); 
				peel.collide(); // check if player touches banana peel
				if(peel.getY() >= 500) { // remove banana peel from screen once it falls to the bottom
					group.getChildren().remove(peel);
					iteratorBanana.remove();
				}
			}
			Iterator<Star> iteratorStar = listStars.iterator(); // loop through powerups 
			while(iteratorStar.hasNext()){
				Star s = iteratorStar.next();
				s.move();
				s.collide(); // check if player touches powerup (star)
				if(s.getY() >= 500) { // remove star from screen once it falls to the bottom
					group.getChildren().remove(s);
					iteratorStar.remove();
				}
			}
			Iterator<Obstacles> iterator = listObstacles.iterator(); // loop through obstacles (bombs)
			while(iterator.hasNext()){
				Obstacles bomb = iterator.next();
				bomb.move();
				bomb.collide(); // check if player touches bomb
				if(bomb.getX() < 0) { // remove bomb from screen once it reaches left side of screen
					group.getChildren().remove(bomb); 
					iterator.remove();
				}
			}
			if(numLives <= 0) { // check number of lives
				gameOver();
			}
		}
	}

	public void loseLife() {
		numLives--;
	}
	public void printLives() {
		lives.setText("Number of Lives: " + Integer.toString(numLives));
	}
	public void startMenu(Group startMenu) {
		VBox winRoot = new VBox();
		Label label = new Label("Instructions: You have 5 lives and you may only move on the left side of the traffic cone. ");
		Label labelAvoid = new Label("Avoid: bomb (lose one life) and banana (move slower and 50% chance of decreased jump height");
		Label labelPowerUp = new Label("Power Up: star (move faster AND jump higher)");
		Label controls = new Label("Controls: Left/right arrow key to move. Space bar to jump.");
		label.setAlignment(Pos.CENTER); 
		// increase accessibility
		label.setFocusTraversable(true);
		labelAvoid.setFocusTraversable(true);
		labelPowerUp.setFocusTraversable(true);
		controls.setFocusTraversable(true);
		
		Button startButton = new Button("Start");
		KeyCombination kc = new KeyCodeCombination(KeyCode.DIGIT2,
				KeyCombination.CONTROL_DOWN);
		Mnemonic mnemonic2 = new Mnemonic(startButton, kc);
		scene.addMnemonic(mnemonic2);

		
		HBox avoid = new HBox(); // contains images of items to avoid in game
		avoid.setFocusTraversable(true);
		
		File bomb = new File("src/FinalProjectVickyyu/Bomb.png"); // add image of bomb
		Image image = new Image(bomb.toURI().toString());
		ImageView bombImg = new ImageView(image);
		bombImg.setFitHeight(50);
		bombImg.setPreserveRatio(true);
		File banana = new File("src/FinalProjectVickyyu/BananaPeel.png");  // add image of banana peel
		Image imageBanana = new Image(banana.toURI().toString());
		ImageView bananaImg = new ImageView(imageBanana);
		bananaImg.setFitHeight(50);
		bananaImg.setPreserveRatio(true);
		avoid.getChildren().addAll(bombImg, bananaImg);
		File star = new File("src/FinalProjectVickyyu/star.png"); // add image of star
		Image imageStar = new Image(star.toURI().toString());
		ImageView starImg = new ImageView(imageStar);
		starImg.setFitHeight(50);
		starImg.setPreserveRatio(true);
		starImg.setFocusTraversable(true);
		startButton.setTranslateY(30);
		// vbox is centered
		winRoot.layoutXProperty().bind(scene.widthProperty().subtract(winRoot.layoutBoundsProperty().get().getWidth()).divide(5));
		winRoot.layoutYProperty().bind(scene.heightProperty().subtract(winRoot.layoutBoundsProperty().get().getHeight()).divide(4));
		winRoot.getChildren().addAll(label, controls, labelAvoid, avoid, labelPowerUp, starImg, startButton);
		startMenu.getChildren().addAll(winRoot);
		
		// accessibility for start button in case user cannot click with mouse
		startButton.setAccessibleText("This button starts the game.");
		startButton.requestFocus();  
		System.out.println("startButton.text=" + startButton.getAccessibleText());
		startButton.setOnAction(e -> { // when start button is clicked/pressed
			d = new Driver();
			d.start(); // start animation
			scene.setRoot(group); // change scene
		});
	}
	
	public void gameOver() { // menu when game is over
		d.stop(); // stop animation
		p.gameOver(); // stop player from moving
		Rectangle rect = new Rectangle(); 
		VBox winRoot = new VBox();
		winRoot.setFillWidth(true); 
		winRoot.setPadding(new Insets(10));
		winRoot.setTranslateX(50);

		Label label = new Label("Final Score: " + seconds);
		label.setAlignment(Pos.CENTER); 

		Button startButton = new Button("Restart");
		label.setTranslateY(15);
		startButton.setTranslateY(50);
		winRoot.getChildren().addAll(label, startButton);
		
		// center rectangle and vbox on screen
		rect.layoutXProperty().bind(scene.widthProperty().subtract(winRoot.layoutBoundsProperty().get().getWidth()).divide(3));
		rect.layoutYProperty().bind(scene.heightProperty().subtract(winRoot.layoutBoundsProperty().get().getHeight()).divide(3));
		winRoot.setAlignment(Pos.CENTER);
		winRoot.layoutXProperty().bind(scene.widthProperty().subtract(winRoot.layoutBoundsProperty().get().getWidth()).divide(3));
		winRoot.layoutYProperty().bind(scene.heightProperty().subtract(winRoot.layoutBoundsProperty().get().getHeight()).divide(3));
		rect.setWidth(200);
		rect.setHeight(150);
		rect.setFill(Color.LIGHTSTEELBLUE);
		rect.setStroke(Color.PURPLE);
		rect.setStrokeWidth(5);

		group.getChildren().addAll(rect, winRoot);
		// increase accessibility for those w/o or unable to use mouse
		startButton.requestFocus(); // can restart by simply pressing spacebar
		startButton.setOnAction(e -> { // player restarts game
			seconds = 0; // resetting all variables
			numLives = 5;
			p.startAgain(); // allow key events to move player
			d.start(); // start animation
			label.setText(""); // resetting final score
			printLives(); // resetting # lives
			Iterator<BananaPeel> iteratorBanana = listBananas.iterator();
			while(iteratorBanana.hasNext()){ // remove all current banana peels from screen so they start falling from the top again
				BananaPeel peel = iteratorBanana.next();
				group.getChildren().remove(peel);
				iteratorBanana.remove();
			}
			Iterator<Star> iteratorStar = listStars.iterator();
			while(iteratorStar.hasNext()){ // remove all current stars from screen so they start falling from the top again
				Star s = iteratorStar.next();
				group.getChildren().remove(s);
				iteratorStar.remove();
			}
			group.getChildren().removeAll(rect, winRoot);
		});
	}

}
