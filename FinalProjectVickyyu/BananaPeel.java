package FinalProjectVickyyu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class BananaPeel extends ImageView  {
	int position;
	Group group;
	Player p;
	Random rand = new Random();
	int speed = rand.nextInt(2) + 2;
	int lives;
	Text t;
	boolean collided = false;
	FinalProjectVickyyu main;
	int yPos = 0;
	public BananaPeel(Group g, Player player) {
		p = player;
		Random rand = new Random();
		group = g;
		try {
			// import banana peel image
			setImage(new Image(new FileInputStream("src/FinalProjectVickyyu/BananaPeel.png")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setFitWidth(30); // setting size of banana peel
		setPreserveRatio(true);
		position = rand.nextInt(350);
		setX(position);
		setY(yPos);
		g.getChildren().add(this);
	}
	public void move(){
		yPos += speed; // falls down (top to bottom)
		this.setY(yPos);
	}
	public void collide() { 
		if (this.getBoundsInParent().intersects(p.getBoundsInParent())) {
			if(!collided) { // flag to ensure can only collide once
				collided = true;
				p.decreaseSpeed(); // move slower and possibility jump lower
			}
	    }
	}
}
