package FinalProjectVickyyu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Background extends ImageView {
	private int BACKGROUND_WIDTH = 750;
	public Background(Scene scene, Group g) {
		FileInputStream inputstream;
		try {
			// add background to screen when game is started
			inputstream = new FileInputStream("src/FinalProjectVickyyu/Background.png");
			setImage(new Image(new FileInputStream("src/FinalProjectVickyyu/Background.png")));
			setFitWidth(BACKGROUND_WIDTH);
			setPreserveRatio(true);
			g.getChildren().add(this); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
