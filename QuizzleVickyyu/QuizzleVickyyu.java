package QuizzleVickyyu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class QuizzleVickyyu extends Application{
	String param1; 
	String param2;
	int rand_list;
	
	int rand_element;
	int l1_len;
	int l2_len;
	File selectedFile;
	boolean first = true;
	
	ArrayList<String> list1;
	ArrayList<String> list2;
	
	int round = 1;
	int score = 0;
	Text score_text;
	
	ArrayList<Button> disable_answers = new ArrayList<Button>();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		BorderPane borderpane = new BorderPane();
		Scene s = new Scene(borderpane, 500, 500);
		list1 = new ArrayList<String>();
		list2 = new ArrayList<String>();
		HBox hbox_top = new HBox(); // right/wrong questions
		hbox_top.setPrefWidth(stage.getWidth()); 
		borderpane.setTop(hbox_top);
		GridPane qAndA = new GridPane();
		qAndA.setAlignment(Pos.CENTER);
		borderpane.setCenter(qAndA);
		
		HBox hbox_bot = new HBox(); // load file, start, next question, score
		hbox_bot.setPrefWidth(stage.getWidth());
		borderpane.setBottom(hbox_bot);
		Boolean[] read_to_start = {false};
		Button load_button = new Button("Load");
		Button start_button = new Button("Start");
		start_button.setVisible(false);
		Button next_button = new Button("Next");
		next_button.setVisible(false);
		score_text = new Text("Score: " + score);
		hbox_bot.getChildren().addAll(load_button, start_button, next_button, score_text);
		
		load_button.setOnMouseClicked(mouseEvent -> {
			round = 1;
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setInitialDirectory(new File("."));
				fileChooser.setTitle("Load Quizzle File");
				selectedFile = fileChooser.showOpenDialog(stage);
				readFile(stage, list1, list2); //read file
				//hbox_bot.getChildren().remove(load_button);
				read_to_start[0] = true;
				start_button.setVisible(true);
				next_button.setVisible(true);
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("File not found");
			}
		});
		stage.setScene(s);
		stage.show();
		ArrayList<Label> questions = new ArrayList<Label>();
		for(int i = 0; i < 5; i++) { // question bar indicator
			Integer num = i+1;
			Label l = new Label(num.toString());
			hbox_top.getChildren().addAll(l);
			hbox_top.setHgrow(l, Priority.ALWAYS);
			l.setPrefWidth(100000);
			l.setAlignment(Pos.CENTER);
			questions.add(l);
		}
		start_button.setOnMouseClicked(mouseEvent -> { // start button
			round = 1;
			hbox_bot.getChildren().remove(start_button); // once the game starts, remove button
			if (hbox_bot.getChildren().contains(next_button) == false) { //game restarted
				hbox_bot.getChildren().add(next_button); // 
			}
			if (hbox_bot.getChildren().contains(score_text) == false) { //game restarted
				score = 0; // reinitialize score
				score_text.setText("Score: " + score);
				hbox_bot.getChildren().add(score_text);
			}
			for(int i = 0; i < questions.size(); i++) {
				questions.get(i).setBackground(Background.EMPTY);
			}
			askQuestion(borderpane, qAndA, questions, hbox_bot); //show questions
		});
		next_button.setOnMouseClicked(mouseEvent -> { // show new question and answers
			round++; //count rounds
			System.out.println("Round" + round);
			if(round < 6) { // ensure 5 questions asked
				qAndA.getChildren().clear();
				askQuestion(borderpane, qAndA, questions, hbox_bot);
			}
			else { // reset question
				qAndA.getChildren().clear();
				hbox_bot.getChildren().clear();
				hbox_bot.getChildren().add(load_button);
				hbox_bot.getChildren().add(start_button);
			}
			enable_buttons(disable_answers); //allow user to select answer
		});
	}

	public void readFile(Stage s, ArrayList<String> l1, ArrayList<String> l2) {
		Scanner fileScanner;
		try {
			fileScanner = new Scanner(selectedFile);
			fileScanner.useDelimiter(",|\\n");
			param1 = fileScanner.next(); //gets topic param1
			param2 = fileScanner.next(); //gets topic param2
			System.out.println("p1: " + param1 + "p2: " + param2);
			while(fileScanner.hasNext()) {
				if(first) {
					String i = fileScanner.next();
					l1.add(i);
					first = false;
				}
				else {
					String x = fileScanner.next();
					l2.add(x);
					first = true;
				}
			}
			l1_len = l1.size();
			l2_len = l2.size();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void askQuestion(BorderPane root, GridPane grid, ArrayList<Label> questions, HBox hbox) {
		Text text = new Text();
		Random rand = new Random();
		rand_list = rand.nextInt(2);
		String txt;
		if(rand_list == 0) { // type 1 question
			rand_element = rand.nextInt(l2_len);
			txt = "What is the " + param1 + " of " + list2.get(rand_element) + "?"; //what is the state of ____
		}
		else { // type 2 question
			rand_element = rand.nextInt(l1_len);
			txt = "What is the " + param2 + " of " + list1.get(rand_element) + "?"; //what is the capitol of ____
		}
		text.setText(txt);
		grid.add(text, 0, 0, 2, 1);
		showAnswers(grid, rand_element, questions, hbox); //show answers
	}
	public void showAnswers(GridPane pane, int rand_element, ArrayList<Label> questions, HBox hbox) {
		Random rand = new Random();
		int rand_num;
		ArrayList<Button> answers = new ArrayList<Button>(); // all answers
		if(rand_list == 0) { // type 1 questions
			String correct_answer = list1.get(rand_element);
			Button correct_but = new Button(correct_answer);
			answers.add(correct_but);
			disable_answers.add(correct_but); // user can't change answers
			correct_but.setOnMouseClicked(mouseEvent -> { // correct answer chosen
				BackgroundFill background_fill = new BackgroundFill(Color.GREEN,  
                        CornerRadii.EMPTY, Insets.EMPTY); 
				Background background = new Background(background_fill);
				correct_but.setBackground(background);
				BackgroundFill background_q_fill = new BackgroundFill(Color.GREEN,  
	                    CornerRadii.EMPTY, Insets.EMPTY);
				Background background_q = new Background(background_q_fill); 
				questions.get(round - 1).setBackground(background);
				score++;
				score_text.setText("Score: " + score);
				disable_buttons(disable_answers); // user can't change answers
			});
			for(int i = 0; i < 3; i++) { 
				rand_num = rand.nextInt(list1.size());
				String ans = list1.get(rand_num);
				Button but = new Button(ans);
				answers.add(but);
				disable_answers.add(but);  // user can't change answers
				but.setOnMouseClicked(mouseEvent -> { // wrong answer selected
					BackgroundFill background_fill_wrong = new BackgroundFill(Color.RED,  
	                        CornerRadii.EMPTY, Insets.EMPTY); 
					Background background = new Background(background_fill_wrong);
					but.setBackground(background);
					BackgroundFill background_fill_correct = new BackgroundFill(Color.BLUE,  
	                        CornerRadii.EMPTY, Insets.EMPTY); 
					Background background_correct = new Background(background_fill_correct);
					correct_but.setBackground(background_correct);
					
					BackgroundFill background_q_fill = new BackgroundFill(Color.RED,  
		                    CornerRadii.EMPTY, Insets.EMPTY);
					Background background_q = new Background(background_q_fill); 
					questions.get(round - 1).setBackground(background);
					disable_buttons(disable_answers);
				});
			}
		}
		else { // type 2 questions 
			String correct_answer = list2.get(rand_element);
			Button correct_but = new Button(correct_answer);
			answers.add(correct_but);
			disable_answers.add(correct_but);  // user can't change answers
			correct_but.setOnMouseClicked(mouseEvent -> { // correct answer selected
				BackgroundFill background_fill = new BackgroundFill(Color.GREEN,  
                        CornerRadii.EMPTY, Insets.EMPTY); 
				Background background = new Background(background_fill);
				correct_but.setBackground(background);
				BackgroundFill background_q_fill = new BackgroundFill(Color.GREEN,  
	                    CornerRadii.EMPTY, Insets.EMPTY);
				Background background_q = new Background(background_q_fill); 
				questions.get(round - 1).setBackground(background);
				score++;
				score_text.setText("Score: " + score);
				disable_buttons(disable_answers); // user can't change answers
			});
			for(int i = 0; i < 3; i++) {
				rand_num = rand.nextInt(list2.size());
				String ans = list2.get(rand_num);
				Button but = new Button(ans);
				answers.add(but);
				disable_answers.add(but); // user can't change answers
				but.setOnMouseClicked(mouseEvent -> { // wrong answer selected
					BackgroundFill background_fill = new BackgroundFill(Color.RED,  
	                        CornerRadii.EMPTY, Insets.EMPTY); 
					Background background = new Background(background_fill);
					but.setBackground(background);
					BackgroundFill background_fill_correct = new BackgroundFill(Color.BLUE,  
	                        CornerRadii.EMPTY, Insets.EMPTY); 
					Background background_correct = new Background(background_fill_correct);
					correct_but.setBackground(background_correct);
					BackgroundFill background_q_fill = new BackgroundFill(Color.RED,  
		                    CornerRadii.EMPTY, Insets.EMPTY);
					Background background_q = new Background(background_q_fill); 
					questions.get(round - 1).setBackground(background);
					disable_buttons(disable_answers); // user can't change answers
				});
			}
		}
		for(int row = 1; row < 3; row++) { // position answer options
			for(int col = 0; col < 2; col++) {
				int num = rand.nextInt(answers.size());
				GridPane.setConstraints(answers.get(num), col, row);
				pane.getChildren().add(answers.get(num));
				answers.remove(num);
			}
		}
	}
	public void disable_buttons(ArrayList<Button> buttons) { // user can no longer select answer
		for(int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setDisable(true);
		}
	}
	public void enable_buttons(ArrayList<Button> buttons) { // user can select answer
		for(int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setDisable(false);
		}
	}
}
