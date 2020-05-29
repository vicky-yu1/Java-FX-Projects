package FinalProjectVickyyu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Obstacles extends ImageView {
	int position;
	Group group;
	LinkedList<Obstacles> list;
	Player p;
	Random rand = new Random();
	int speed = rand.nextInt(2) + 2;
	int lives;
	Text t;
	boolean collided = false;
	FinalProjectVickyyu main;
	public Obstacles(FinalProjectVickyyu f, Group g, LinkedList<Obstacles> listObstacles, Player player, int numLives, Text livesText) {
		p = player;
		Random rand = new Random();
		group = g;
		list = listObstacles;
		lives = numLives;
		t = livesText;
		main = f;
		try {
			setImage(new Image(new FileInputStream("src/FinalProjectVickyyu/Bomb.png"))); // importing bomb image
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setFitWidth(30);
		setPreserveRatio(true);
		position = rand.nextInt(350) + 400;
		setX(position);
		setY(400);
		g.getChildren().add(this);
	}
	
	public void move(){
		position -= speed; // move from right to left of screen
		this.setX(position);

	}
	public void collide() {
		if (this.getBoundsInParent().intersects(p.getBoundsInParent())) {
			if(!collided) { // flag to ensure can only collide once per bomb
				collided = true;
				for(Obstacles o:list) { // update # lives for each bomb
					int updatedNum = lives - 1;
					o.setLives(updatedNum);
					System.out.println(list.size() + " " + updateLives());
				}
				main.loseLife(); // player loses a life
				main.printLives(); // update text
			}
	    }
	}
	
	public int updateLives() {
		return lives;
	}
	public void setLives(int num) {
		lives = num;
	}
	public void addObstacle(Obstacles ob) {
		list.add(ob);
	}

}
