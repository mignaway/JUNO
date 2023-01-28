package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import audio.CustomAudioManager;
import cards.Card;
import cards.CardColor;
import cards.CardValue;
import cards.UnoCards;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import user.Player;
import user.Users;

public class GameController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	ImageView cardDrawImage;
	@FXML
	ImageView lastCardThrowed;
	
	
	// Main Player
	@FXML 
	HBox playerCardBox;
	@FXML 
	VBox firstPlayerInfo;
	@FXML
	ImageView firstPlayerAvatar;
	@FXML
	ImageView unoPlayerButton;
	@FXML
	ImageView skipPlayerButton;
	
	
	// 2nd Player Elements
	@FXML
	HBox secondCardBox;
	@FXML 
	VBox secondPlayerInfo;
	@FXML
	ImageView secondPlayerAvatar;
	@FXML
	Label secondPlayerNickname;
	
	// 3rd Player Elements
	@FXML
	HBox thirdCardBox;
	@FXML 
	VBox thirdPlayerInfo;
	@FXML
	ImageView thirdPlayerAvatar;
	@FXML
	Label thirdPlayerNickname;
	
	// 4th Player Elements
	@FXML
	HBox fourthCardBox;
	@FXML 
	VBox fourthPlayerInfo;
	@FXML
	ImageView fourthPlayerAvatar;
	@FXML
	Label fourthPlayerNickname;
	
	// Black Card Choose Color
	@FXML
	Circle choosecolor_red;
	@FXML
	Circle choosecolor_blue;
	@FXML
	Circle choosecolor_green;
	@FXML
	Circle choosecolor_yellow;
	@FXML
	Pane colorChooserPane;
	
	@FXML
	Pane animationImagePane;
	@FXML
	ImageView reverseLeftArrow;
	@FXML
	ImageView reverseRightArrow;
	
	// Game Over
	@FXML
	Pane gameOverPane;
	@FXML
	Label gameOverWinnerLabel;
	@FXML
	Label gameOverExperienceLabel;
	@FXML
	Label gameOverCurrentLevelLabel;
	@FXML
	Label gameOverResultLabel;
	@FXML
	Button gameOverReturnButton;
	
	/**
	 * Audio game effect
	 */

	public CustomAudioManager gameAudioEffect = new CustomAudioManager();
	
	/**
	 * Player session
	 */
	private Player sessionPlayer;
	/**
	 * Game modality
	 */
	public String gameModality;
	
	/**
	 * Game instance
	 */
	Game game = Game.getInstance(this);
	
	
	/**
	 * Initialize all visual effect based on modality and start the game instance
	 * @param modality
	 */
	
	public void initializeModality(String modality) {
		// Clearing Table
		playerCardBox.getChildren().clear();
		secondCardBox.getChildren().clear();
		thirdCardBox.getChildren().clear();
		fourthCardBox.getChildren().clear();
		
		// Setup Player
		sessionPlayer.assignCardBox("playerCardBox");
		game.addPlayersInGame(sessionPlayer);
		game.sessionPlayer = sessionPlayer;
		firstPlayerAvatar.setImage(sessionPlayer.getAvatar());
		Circle firstAvatarClip = new Circle(40,40,40);
		firstPlayerAvatar.setClip(firstAvatarClip);
		firstPlayerInfo.setVisible(true);
				
		ArrayList<Player> otherPlayers = Users.getListOfUsers().stream().filter(p -> !p.getNickname().equals(sessionPlayer.getNickname())).filter(p -> p.getIsBot()).collect(Collectors.toCollection(ArrayList::new));
		if (modality.equals("gameSelectPaneOne")) {
			Player randomizedOpponent = otherPlayers.get((int)(Math.random()*otherPlayers.size()));
			randomizedOpponent.assignCardBox("thirdCardBox");
			game.addPlayersInGame(randomizedOpponent);
			thirdPlayerAvatar.setImage(randomizedOpponent.getAvatar());
			Circle thirdAvatarClip = new Circle(40,40,40);
			thirdPlayerAvatar.setClip(thirdAvatarClip);
			thirdPlayerNickname.setText(randomizedOpponent.getNickname());
			thirdPlayerInfo.setVisible(true);
			thirdCardBox.setVisible(true);
		} else {
			
			// Setup Ally
			Player secondPlayer = otherPlayers.get(0);
			secondPlayer.assignCardBox("secondCardBox");
			game.addPlayersInGame(secondPlayer);
			// Set second Player as mate in game
			if(modality.equals("gameSelectPaneTwo")) {
				game.playerMate = secondPlayer;
				secondPlayerAvatar.setImage(new Image("bot_ally.png",80, 80, false, true));
			} else {				
				secondPlayerAvatar.setImage(secondPlayer.getAvatar());
			}
			Circle secondAvatarClip = new Circle(40,40,40);
			secondPlayerAvatar.setClip(secondAvatarClip);
			secondPlayerNickname.setText(secondPlayer.getNickname());
			secondPlayerInfo.setVisible(true);
			secondCardBox.setVisible(true);
			
			// Setup Enemy 3
			Player thirdPlayer = otherPlayers.get(1);
			thirdPlayer.assignCardBox("thirdCardBox");
			game.addPlayersInGame(thirdPlayer);
			thirdPlayerAvatar.setImage(thirdPlayer.getAvatar());
			Circle thirdAvatarClip = new Circle(40,40,40);
			thirdPlayerAvatar.setClip(thirdAvatarClip);
			thirdPlayerNickname.setText(thirdPlayer.getNickname());
//			thirdPlayerNickname.setTextFill(Color.rgb(223, 49, 49));
			thirdPlayerInfo.setVisible(true);
			thirdCardBox.setVisible(true);
			
			
			// Setup Enemy 4
			Player fourthPlayer = otherPlayers.get(2);
			fourthPlayer.assignCardBox("fourthCardBox");
			game.addPlayersInGame(fourthPlayer);
			fourthPlayerAvatar.setImage(fourthPlayer.getAvatar());
			Circle fourthAvatarClip = new Circle(40,40,40);
			fourthPlayerAvatar.setClip(fourthAvatarClip);
			fourthPlayerNickname.setText(fourthPlayer.getNickname());
//			fourthPlayerNickname.setTextFill(Color.rgb(223, 49, 49));
			fourthPlayerInfo.setVisible(true);
			fourthCardBox.setVisible(true);
		}
		choosecolor_red.setOnMouseClicked(event -> changeLastCardColor(CardColor.RED));
		choosecolor_blue.setOnMouseClicked(event -> changeLastCardColor(CardColor.BLUE));
		choosecolor_green.setOnMouseClicked(event -> changeLastCardColor(CardColor.GREEN));
		choosecolor_yellow.setOnMouseClicked(event -> changeLastCardColor(CardColor.YELLOW));
		
		// start after setup
		game.startGame();
	}
	
	/**
	 * Deck mouse click draw action
	 * @param event
	 * @throws IOException
	 */
	
	public void drawCardAction(MouseEvent event) throws IOException {
		game.playerDrawCard(sessionPlayer);
	}
	
	/**
	 * Add card to player hand (Visually) 
	 * @param p Player
	 * @param generatedCard Card
	 */
	
	public void playerDrawCardVisual(Player p,Card generatedCard) {
		gameAudioEffect.playAudio("audio/draw_card_sound.wav", false, 0.5);
		// Setting Up CardImage
		ImageView unocard = new ImageView();
		String cardImgResource;
		
		// Substitute back card to other opponent and scale down cards
		if(p.getCardBox().equals("playerCardBox")) {
			cardImgResource = UnoCards.getCardFile(generatedCard);
		} else if (p.getCardBox().equals("secondCardBox") && gameModality.equals("gameSelectPaneTwo")){
			cardImgResource = UnoCards.getCardFile(generatedCard);
			unocard.setFitWidth(92);
			unocard.setFitHeight(144);
		} else {
			cardImgResource = "back";
			unocard.setFitWidth(92);
			unocard.setFitHeight(144);
		}
		unocard.setImage(new Image(GameController.class.getResource("/cards/" + cardImgResource + ".png").toString()));
		
		// Container for negative margin and selection of card
		VBox cardContainer = new VBox();
		cardContainer.setId(generatedCard.getUUID());
		
		TranslateTransition enteringTransition = new TranslateTransition();
		enteringTransition.setDuration(Duration.millis(200));
		enteringTransition.setFromY(cardContainer.getLayoutY() + 30);
		enteringTransition.setToY(cardContainer.getLayoutY());
		enteringTransition.setNode(cardContainer);
		enteringTransition.play();
		
		// Card Action event only if are players' cards
		if(p.equals(sessionPlayer)) {
			cardContainer.getStyleClass().add("cardContainer");
			cardContainer.setOnMousePressed(event -> {
				
				// Add MouseClick Action to Card
				game.playerSelectCard(p,generatedCard);
			});
			cardContainer.setOnMouseExited(event -> {
				TranslateTransition transition = new TranslateTransition();
				transition.setDuration(Duration.millis(200));
				transition.setToY(cardContainer.getLayoutY());
				transition.setNode(cardContainer);
				transition.play();
			});
			cardContainer.setCursor(Cursor.HAND);
		}	
		cardContainer.getChildren().add(unocard);
		
		switch(p.getCardBox()) {
			case "playerCardBox":
				playerCardBox.getChildren().addAll(cardContainer);
				updateCardsMargin(playerCardBox);
				break;
			case "secondCardBox":
				secondCardBox.getChildren().addAll(cardContainer);
				updateCardsMargin(secondCardBox);
				break;
			case "thirdCardBox":
				thirdCardBox.getChildren().addAll(cardContainer);
				updateCardsMargin(thirdCardBox);
				break;
			case "fourthCardBox":
				fourthCardBox.getChildren().addAll(cardContainer);
				updateCardsMargin(fourthCardBox);
				break;
		}
	}
	
	/**
	 * 
	 * Visually enhances the current player's hand for the turn
	 * 
	 * @param p
	 */
	
	public void updateVisualOpacityOtherHands(Player p) {
		playerCardBox.setOpacity(0.3);
		secondCardBox.setOpacity(0.3);
		thirdCardBox.setOpacity(0.3);
		fourthCardBox.setOpacity(0.3);
		switch(p.getCardBox()) {
			case "playerCardBox":
				playerCardBox.setOpacity(1);
				break;
			case "secondCardBox":
				secondCardBox.setOpacity(1);
				break;
			case "thirdCardBox":
				thirdCardBox.setOpacity(1);
				break;
			case "fourthCardBox":
				fourthCardBox.setOpacity(1);
				break;
		}
	}
	
	/**
	 * Increase/decrease spacing between card improving space scene optimization
	 * @param cardBox Player Card Box
	 */
	
	public void updateCardsMargin(HBox cardBox) {
		// Increase or decrease space from cards based on number of cards in hand
		cardBox.getChildren().forEach(c -> {
			VBox container = (VBox) c;
			int reduceMarginFromCard = -5*cardBox.getChildren().size();
			reduceMarginFromCard = reduceMarginFromCard >= -75 ? reduceMarginFromCard : -75	;
			container.setMargin((ImageView) container.getChildren().get(0), new Insets(0, 0, 0, reduceMarginFromCard));
		});
	}
	
	/**
	 * Update the last card image
	 * @param generatedCard Card
	 */
	
	public void updateLastCardVisual(Card generatedCard) {
		gameAudioEffect.playAudio("audio/draw_card_sound.wav", false, 0.5);
		lastCardThrowed.setImage(new Image(getClass().getResource("/cards/" + UnoCards.getCardFile(generatedCard) + ".png").toString()));
		System.out.println("update visual called here!");
	}
	
	/**
	 * remove card from player hand
	 * @param p Player
	 * @param card Card
	 */
	
	public void removeCardVisual(Player p, Card card) {
		switch(p.getCardBox()) {
		case "playerCardBox":
			playerCardBox.getChildren().remove(getRemoveCardIndex(playerCardBox,card));
			updateCardsMargin(playerCardBox);
			break;
		case "secondCardBox":
			secondCardBox.getChildren().remove(getRemoveCardIndex(secondCardBox,card));
			updateCardsMargin(secondCardBox);
			break;
		case "thirdCardBox":
			thirdCardBox.getChildren().remove(getRemoveCardIndex(thirdCardBox,card));
			updateCardsMargin(thirdCardBox);
			break;
		case "fourthCardBox":
			fourthCardBox.getChildren().remove(getRemoveCardIndex(fourthCardBox,card));
			updateCardsMargin(fourthCardBox);
			break;
		}
	}
	
	/**
	 * Change last card color and update image. It's only for black cards like COLOR or PLUSFOUR
	 * @param color Color
	 */
	
	public void changeLastCardColor(CardColor color) {
		game.lastCard.setColor(color);
		colorChooserPane.setDisable(true);
		colorChooserPane.setVisible(false);
		// change plusfour color card image
		if(game.lastCard.getValue().equals(CardValue.COLOR)) {	
			lastCardThrowed.setImage(new Image(getClass().getResource("/cards/color_" + color.toString().toLowerCase() + ".png").toString()));
		} else if (game.lastCard.getValue().equals(CardValue.PLUSFOUR)) {
			lastCardThrowed.setImage(new Image(getClass().getResource("/cards/plusfour_" + color.toString().toLowerCase() + ".png").toString()));
		}
		if(game.currentlyChoosingColor == true) {
			game.currentlyChoosingColor = false;
			game.DelayAndSkipTurn(game.defaultDelay_ms);
		}
	}
	
	/**
	 * Activate/Deactivate visually the manual skip button.
	 * @param bool Status
	 */
	
	public void setSkipButton(boolean bool) {
		if(bool) skipPlayerButton.setOpacity(1); else skipPlayerButton.setOpacity(.5);
	}
	
	/**
	 * Manual skip button action.
	 */
	
	public void manualSkipTurn() {
		if(game.canCurrentUserSkip()) {
			skipPlayerButton.setOpacity(.5);
			game.DelayAndSkipTurn(game.defaultDelay_ms);
		}
	}
	
	/**
	 * Get the index of player's card to be removed, needed to get end remove visually the card.
	 * @param cardBox Card Box
	 * @param card Card to remove
	 * @return Index of removed card
	 */
	
	public int getRemoveCardIndex(HBox cardBox,Card card) {
		int index = 0;
		for(int i=0;i<cardBox.getChildren().size();i++) {
			VBox child = (VBox) cardBox.getChildren().get(i);
			if(child.getId().equals(card.getUUID())) {
				break;
			} else {
				index++;				
			}
		}
		return index;
	}
	
	/**
	 * (Visual Effect) Change direction arrows image based on <b>gameDirection</b>.
	 */
	
	public void reverseCardAnimation() {
		ScaleTransition reverseLeftAnimation = new ScaleTransition(Duration.millis(500),reverseLeftArrow);
		ScaleTransition reverseRightAnimation = new ScaleTransition(Duration.millis(500),reverseRightArrow);
		if (game.gameDirection) {
			reverseLeftAnimation.setToY(1);
			reverseRightAnimation.setToY(1);
		} else { 
			reverseLeftAnimation.setToY(-1);
			reverseRightAnimation.setToY(-1);
		}
		reverseLeftAnimation.play();
		reverseRightAnimation.play();
	}
	
	/**
	 * (Visual Effect) Shows winner/lose screen
	 * 
	 * @param result Boolean that tells if user won or not
	 * @param winners List of winner(s)
	 * @param expGained Experience gained from this game
	 * @param playerLevel New player current level
	 */
	
	public void gameOverShowVisually(boolean result,ArrayList<Player> winners,int expGained,int playerLevel) {
		
		gameOverPane.setVisible(true);
		String winnerToText = winners.stream().map(p -> p.getNickname()).collect(Collectors.joining(","));
		gameOverWinnerLabel.setText(winnerToText);
		gameOverExperienceLabel.setText("+" + expGained);
		gameOverCurrentLevelLabel.setText(Integer.toString(playerLevel));
		if(result) {
			gameOverResultLabel.setText("WON");
			gameOverResultLabel.setStyle("-fx-text-fill: #66CF77");
			gameOverExperienceLabel.setStyle("-fx-text-fill: #66CF77");
			gameOverReturnButton.setStyle("-fx-background-color: #66CF77");
		} else {
			gameOverResultLabel.setText("LOST");
			gameOverResultLabel.setStyle("-fx-text-fill: #DF3131");
			gameOverReturnButton.setStyle("-fx-background-color: #DF3131");
		}
	}
	
	/**
	 * Handle click UNO button
	 * 
	 * @param event
	 * @throws IOException
	 */
	
	public void handleUnoButtonClick(MouseEvent event) throws IOException {
		if(game.checkSessionPlayerHasTwoCards()) {
			if(!game.playerUnoStatus) {
				game.playerUnoStatus = true;
				unoPlayerButton.setOpacity(.5);
			}
		}
	}
	
	// CONTROLLER CONNECTION
	
	/**
	 * Stop game and switch scene to dashboard. No experience will be gained if player quits before finish.
	 * @param event
	 * @throws IOException
	 */
	
	public void exitGame(ActionEvent event) throws IOException {
		game.removeInstance();
		Users.getListOfUsers().forEach(p -> {
			p.resetGame();
		});
		
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
		
		Main.maintheme.resumeAudio();
		DashboardController.gameTheme.pauseAudio();
		
	}
	
	/**
	 * Set session player
	 * @param player
	 */
	
	public void setPlayerPlaying(Player player) {
		sessionPlayer = player;
	}
	
	/**
	 * Set game modality
	 * @param value Modality
	 */
	
	public void setGameModality(String value) {
		gameModality = value;
		initializeModality(value);
	}
	
	/**
	 * Set scene
	 * @param scena Scene
	 */
	
	public void setScene(Scene scena) {
		scene = scena;
	}
}
