package application;

import java.io.IOException;

import audio.CustomAudioManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import user.Player;
public class DashboardController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	Label nicknameLabel;
	@FXML
	ImageView playerAvatarImage;
	@FXML
	Label gamesPlayedLabel;
	@FXML
	Label gamesWonLabel;
	@FXML
	Label gamesLostLabel;
	@FXML
	Label playerLevelLabel;
	@FXML
	ProgressBar playerPointExperienceBar;
	
	@FXML
	Pane gameSelectPaneOne;
	@FXML
	Pane gameSelectPaneTwo;
	@FXML
	Pane gameSelectPaneThree;
	@FXML
	ImageView gameSelectImageOne;
	@FXML
	ImageView gameSelectImageTwo;
	@FXML
	ImageView gameSelectImageThree;
	
	@FXML
	Button gameSelectButtonOne;
	@FXML
	Button gameSelectButtonTwo;
	@FXML
	Button gameSelectButtonThree;
	
	
	/**
	 * Player session / User logged
	 */
	
	public Player sessionPlayer;
	
	/**
	 * Game modality selected.<p>
	 * 1v1: gameSelectPaneOne<p>
	 * 2v2: gameSelectPaneTwo<p>
	 * 1v4: gameSelectPaneThree<p>
	 */
	
	public String modalitySelected;
	
	/**
	 * Audio selecting modality sound
	 */
	
	public CustomAudioManager switchModalitySound = new CustomAudioManager();
	/**
	 * Audio game theme
	 */
	public static CustomAudioManager gameTheme = new CustomAudioManager();

	/**
	 * Manage the animation of game panes
	 * @param event
	 */
	
	public void gameSelectPaneAndAnimation(MouseEvent event) {
		// GAME SELECTION AND SOUND EFFECT
		Pane vbox = (Pane) event.getSource();
		modalitySelected = vbox.getId();
		switchModalitySound.playAudio("audio/draw_card_sound.wav", false, 0.5);
		
		// ANIMATION
		gameSelectButtonOne.setVisible(false);
		gameSelectButtonTwo.setVisible(false);
		gameSelectButtonThree.setVisible(false);
		gameSelectImageOne.setOpacity(.4);
		gameSelectImageTwo.setOpacity(.4);
		gameSelectImageThree.setOpacity(.4);
		gameSelectButtonOne.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 0));
		gameSelectButtonOne.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 0));
		gameSelectButtonTwo.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 0));
		gameSelectButtonOne.setPrefHeight(0);
		gameSelectButtonTwo.setPrefHeight(0);
		gameSelectButtonThree.setPrefHeight(0);
		gameSelectButtonOne.setPadding(new Insets(0));
		gameSelectButtonTwo.setPadding(new Insets(0));
		gameSelectButtonThree.setPadding(new Insets(0));
		
		Pane hoverPane = (Pane) event.getSource();
		switch(hoverPane.getId()) {
			case "gameSelectPaneOne":
				gameSelectButtonOne.setPrefHeight(40);
				gameSelectButtonOne.setPadding(new Insets(10));
				gameSelectButtonOne.setVisible(true);
				gameSelectButtonOne.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));
				gameSelectImageOne.setOpacity(1);
				break;
			case "gameSelectPaneTwo":
				gameSelectButtonTwo.setPrefHeight(40);
				gameSelectButtonTwo.setPadding(new Insets(10));
				gameSelectButtonTwo.setVisible(true);
				gameSelectButtonTwo.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));
				gameSelectImageTwo.setOpacity(1);
				break;
			case "gameSelectPaneThree":
				gameSelectButtonThree.setPrefHeight(40);
				gameSelectButtonThree.setPadding(new Insets(10));
				gameSelectButtonThree.setVisible(true);
				gameSelectButtonThree.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));
				gameSelectImageThree.setOpacity(1);
				break;
		}
	}
	
	/**
	 * Initialize Player session and statistics on login
	 * @param sessionUser
	 */
	
	public void loadUser(Player sessionUser) {
		sessionPlayer = sessionUser;
		nicknameLabel.setText(sessionUser.getNickname());
		
		// Avatar Circle Shape
		
		Circle clip = new Circle(50,50,50);
		playerAvatarImage.setImage(sessionUser.getAvatar());
		playerAvatarImage.setClip(clip);
		
		
		
		gamesPlayedLabel.setText(Integer.toString(sessionUser.getGamesPlayed()));
		gamesWonLabel.setText(Integer.toString(sessionUser.getGamesWon()));
		gamesLostLabel.setText(Integer.toString(sessionUser.getGamesLost()));
		playerLevelLabel.setText(Integer.toString(sessionUser.getPlayerLevel()));
		playerPointExperienceBar.setProgress((double) sessionUser.getPointExperience()/100);
	}
	
	/**
	 * Logout and back to login page
	 * @param event
	 * @throws IOException
	 */
	
	public void logout(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	/**
	 * Initialize game scene and switch audio theme.
	 * @param event
	 * @throws IOException
	 */
	
	public void startGame(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Game.fxml"));
		root = loader.load();
		

		// Set up Scene
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		scene.getStylesheets().add(getClass().getResource("/game.css").toExternalForm());
		
		// Set up GameController
		GameController gameController = loader.getController();
		gameController.setScene(scene);
		gameController.setPlayerPlaying(sessionPlayer);
		gameController.setGameModality(modalitySelected);
		Main.maintheme.pauseAudio();
		gameTheme.playAudio("audio/game_soundtrack.wav", true, .4);
		stage.show();
	}
	
	/**
	 * Initialize edit profile scene.
	 * @param event
	 * @throws IOException
	 */
	
	public void editProfile(MouseEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProfile.fxml"));
		root = loader.load();
		

		// Set up Scene
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		
		// Set up GameController
		EditProfileController editProfileController = loader.getController();
		editProfileController.loadUser(sessionPlayer);
		
		stage.show();
	}
}
