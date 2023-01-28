package application;
	
import audio.CustomAudioManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import user.Users;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	/**
	 * Login/Dashboard Audio theme
	 */
	public static CustomAudioManager maintheme = new CustomAudioManager();
	
	/**
	 * Load user and bot.
	 * @param args
	 */
	public static void main(String[] args) {	
		Users.loadLocalUsers();
		Users.generate(); // generate users = {bot1,bot2,bot3}
		launch(args);
	}

	/**
	 * On JavaFx App start load login page
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
		Scene login = new Scene(root,Color.BLACK);
		
		Image logo = new Image("logo.png");
		
		stage.getIcons().add(logo);
		stage.setTitle("Uno Game");
		stage.setScene(login);
		stage.setResizable(false);
		stage.show();
		
		maintheme.playAudio("audio/dashboard_soundtrack.wav",true,.2);
	}
}
