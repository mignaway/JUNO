package application;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import user.Player;
import user.Users;

public class EditProfileController {
	private Stage stage;
	private Scene scene;
	private Parent root;

	/** 
	 * Player session
	 */
	private Player sessionPlayer;
	
	/**
	 * File chooser window
	 */
	
	FileChooser fileChooser = new FileChooser();
	
	/**
	 * File selected
	 */
	
	File selectedFile = null;
	
	/**
	 * Initialized (null) new image
	 */
	
	Image newImage;
	
	// navbar elements
	
	@FXML
	Label navbarPlay;
	@FXML
	Label navbarProfile;
	
	
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
	TextField usernameInput;
	@FXML
	Text editErrorLabel;
	
	
	/**
	 * Initialize Player session and statistics on login
	 * @param sessionPlayer
	 */
	
	public void loadUser(Player sessionUser) {
		sessionPlayer = sessionUser;
		nicknameLabel.setText(sessionPlayer.getNickname());
		
		// Avatar Circle Shape
		
		Circle clip = new Circle(50,50,50);
		playerAvatarImage.setImage(sessionPlayer.getAvatar());
		playerAvatarImage.setClip(clip);
		
		
		
		gamesPlayedLabel.setText(Integer.toString(sessionPlayer.getGamesPlayed()));
		gamesWonLabel.setText(Integer.toString(sessionPlayer.getGamesWon()));
		gamesLostLabel.setText(Integer.toString(sessionPlayer.getGamesLost()));
		playerLevelLabel.setText(Integer.toString(sessionPlayer.getPlayerLevel()));
		playerPointExperienceBar.setProgress((double) sessionUser.getPointExperience()/100);
	}
	
	
	/**
	 * Validate username and update <b>users.txt</b> file.
	 */
	public void validateAndUpdateUsername() {
		// username not equal
		if(!sessionPlayer.getNickname().equals(usernameInput.getText())) {
			// username already taken
			if(!Users.getListOfUsers().stream().anyMatch(p -> p.getNickname().equals(usernameInput.getText()))) {
				// check 3 to 8 alphanumeric chars
				if(usernameInput.getText().matches("^[a-zA-Z0-9]{3,8}$")) {
					Users.updateUserFile(sessionPlayer,usernameInput.getText());
					sessionPlayer.setNickname(usernameInput.getText());
					nicknameLabel.setText(usernameInput.getText());
				} else displayError("L'username non rispecchia i parametri imposti.");
			} else displayError("L'username è già stato preso, scegline un'altro.");
		} else displayError("L'username deve essere diverso dal vecchio.");
	}
	
	/**
	 * Open file chooser and if file selected set as new profile image
	 * @param event
	 */
	
	public void selectAndUploadImage(MouseEvent event) {
		configureFileChooser(fileChooser);
		selectedFile = fileChooser.showOpenDialog(stage);
		if(selectedFile != null) {
			// update viewed image
			newImage = new Image(selectedFile.getAbsolutePath(),80, 80, false, true);
			playerAvatarImage.setImage(newImage);
			sessionPlayer.setAvatar(newImage);
			try {
				Files.copy(Paths.get(selectedFile.getAbsolutePath()), Paths.get("assets/" + sessionPlayer.getAvatarFileName()), StandardCopyOption.REPLACE_EXISTING);
				Users.updateUserFile(sessionPlayer,null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Show error messages
	 * @param error Error message
	 */
	
	public void displayError(String error) {
		editErrorLabel.setText(error);
	}
	
	/**
	 * Configure file chooser accepting only png and jpg files.
	 * @param fileChooser
	 */
	
	private static void configureFileChooser(
        final FileChooser fileChooser) {      
            fileChooser.setTitle("Choose player's avatar image");                 
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
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
	 * Back to dashboard scene
	 * @param event
	 * @throws IOException
	 */
	
	public void goToDashboard(MouseEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
		root = loader.load();
		
		DashboardController dashboardController = loader.getController();
		dashboardController.loadUser(sessionPlayer);
		
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		scene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());
		stage.show();
	}
}
