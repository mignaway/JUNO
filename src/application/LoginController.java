package application;

import java.io.IOException;
import java.util.stream.Collectors;

import audio.CustomAudioManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import user.Player;
import user.Users;

public class LoginController {
	@FXML
	TextField loginUsername;
	@FXML
	Label loginErrorLabel;
	@FXML
	TextFlow loginAvailableUsersLabel;
	
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	/**
	 * "Welcome to juno" Audio
	 */
	public CustomAudioManager junoTag = new CustomAudioManager();
	
	/**
	 * Initialize Login scene
	 */
	
	@FXML
	public void initialize() {
		String usernames = Users.getListOfUsers()
								.stream()
								.filter(player -> !player.getIsBot())
								.map(player -> player.getNickname())
								.collect(Collectors.joining(", "));
		Text text = new Text(usernames);
		text.setStyle("-fx-fill: white; -fx-font-weight: bold; -fx-font-family: Arial; -fx-font-size: 18px");
		loginAvailableUsersLabel.getChildren().add(text);			
//		for (String s : ) {			
//		}
	}
	
	/**
	 * Login action button. Check if there are any players that match the entered username.
	 * @param event
	 * @throws IOException
	 */
	
	public void login(ActionEvent event) throws IOException {
		
		String username = loginUsername.getText();
		Player sessionUser = Users.getListOfUsers().stream().filter(player -> player.getNickname().equals(username)).findAny().orElse(null);
		
		if (sessionUser != null) {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
			root = loader.load();
			
			DashboardController dashboardController = loader.getController();
			dashboardController.loadUser(sessionUser);
			
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			scene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());
			stage.show();
			junoTag.playAudio("audio/juno_tag_master_no_intro.wav",false,.4);
			
		} else {
			loginErrorLabel.setPrefHeight(45);
			loginErrorLabel.setVisible(true);
			loginErrorLabel.setText("User not found, see available users");
		}
		
	}
}
