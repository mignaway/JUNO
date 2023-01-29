package application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cards.Card;
import cards.CardColor;
import cards.UnoCards;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import user.Player;
import user.Users;

public class Game {

	/**
	 * Controller to manage visual effects
	 */
	private GameController gameController;
	
	/**
	 * ArrayList of players that are currently playing
	 */
	
	ArrayList<Player> playersInGame = new ArrayList<Player>();
	
	/**
	 * Player session
	 */
	Player sessionPlayer;
	
	/**
	 * Last card throwed
	 */
	Card lastCard;
	
	/**
	 * Player draw turn
	 */
	int drawTurn = 0;
	
	/**
	 * Player turn
	 */
	int playerTurn = 0;
	
	/**
	 * Default delay between turns in milliseconds
	 */
	
	int defaultDelay_ms = 2000;
	
	/**
	 * Game direction
	 * True -> Clockwise
	 * False -> Counterclockwise
	 */
	
	boolean gameDirection = true;
	
	/**
	 * Tells if game is ready and set up
	 * (Needed for animation to complete)
	 */
	
	boolean gameStarted = false;
	
	/**
	 * Status of player clicked UNO Button
	 */
	boolean playerUnoStatus = false;
	
	/**
	 * Session player ally in case of 2v2
	 */
	
	Player playerMate = null;
	
	/** 
	 * Player currently choosing color (pause the game if true)
	 */
	boolean currentlyChoosingColor = false;
	
	/**
	 * Private instance of Game
	 */
	
	private static Game gameInstance;
	
	/**
	 * [SINGLETON DESIGN PATTERN]
	 * Game constructor. Used here the singleton design pattern to avoid creating multiple instance of game while player is already playing.
	 * 
	 * @param gameController - it's needed to control visual effects
	 */
	
	private Game(GameController gameController) {
		this.gameController = gameController;
	}
	
	/**
	 * (Singleton) Get Game instance
	 * @param gameController
	 * @return Game Instance
	 */
	
	public static Game getInstance(GameController gameController) {
		if (gameInstance == null) {
			gameInstance = new Game(gameController);
		}
		return gameInstance;
	}
	
	/**
	 * Set game instance to null.<p>
	 * Used when exiting or game is finished.
	 */
	
	public void removeInstance() {
		gameInstance = null;
	}
	
	/**
	 * Start and initialize game by setting the first card and dealing seven cards to all players
	 */
	
	public void startGame() {
		setupFirstCard();
		initializeSevenCard();
		gameController.updateVisualOpacityOtherHands(playersInGame.get(0));
		// game ready
		gameStarted = true;
	}
	
	/**
	 * Stop the game when <b>finished</b> and give players win
	 * (In case of manual exit, exitGame() in GameController.java will take care of it)
	 * @param winner
	 */
	
	public void stopGame(Player winner) {
		int expGained = 0;
		ArrayList<Player> winners = new ArrayList<Player>();
		boolean result;
		gameStarted = false;
		sessionPlayer.setGamesPlayed(sessionPlayer.getGamesPlayed() + 1);
		// check if you won or your bot did
		if(!winner.getIsBot() || winner.equals(playerMate)) {
			result = true;
			winners.add(sessionPlayer);
			if(gameController.gameModality.equals("gameSelectPaneTwo")) winners.add(playerMate);
			try{
				expGained = (int) Math.floor(100 * (1d/sessionPlayer.getPlayerLevel()));
		    } 
			catch (ArithmeticException e) {
				System.out.println ("Initlial player level must be > 0, Can't be divided by Zero " + e);
			}
			sessionPlayer.setPointExperience(sessionPlayer.getPointExperience() + expGained);
			
			sessionPlayer.setGamesWon(sessionPlayer.getGamesWon() + 1);	
		} // enemies won 
		else {
			result = false;
			if(gameController.gameModality.equals("gameSelectPaneTwo")) {
				List<Player> otherWinner = playersInGame.stream().filter(p -> p != sessionPlayer).filter(p -> p != playerMate).collect(Collectors.toList());
				winners.addAll(otherWinner);
			} else {
				winners.add(winner);
			}	
			expGained = 0;
			sessionPlayer.setGamesLost(sessionPlayer.getGamesLost() + 1);
		}
		gameController.gameOverShowVisually(result, winners,expGained,sessionPlayer.getPlayerLevel());
		Users.updateUserFile(sessionPlayer, null);
	}
	
	
	// GAME PROCESS METHODS
	
	/**
	 * 
	 * Method that manage user card clicking. 
	 * After checking that player can actually throw his card it updates "last card" (or table card) and then skip player turn.
	 * 
	 * @param p - Player instance
	 * @param c - Player selected cards
	 * 
	 * */
	
	public void playerSelectCard(Player p, Card c) {
		if(checkPlayerTurn(p)) {
			if(isCardCastable(c)) {
				// visual 
				playerThrowCard(p,c);
				// change table card
				updateLastCard(c);
				checkSpecialCard(c);
				
				if(currentlyChoosingColor == false) DelayAndSkipTurn(defaultDelay_ms);
			}
		}
	}

	
	/**
	 * Throw the card from the hand, called only once it is verified that the player has that card in the hand.
	 * It also check if it's the last cardplayer's hand with <b>gameOverCheck()</b>.
	 * 
	 * @param p Player Instance
	 * @param c Card to throw
	 */
	
	public void playerThrowCard(Player p, Card c) {
		p.removeCardInGame(c);
		gameController.removeCardVisual(p,c);
		if(!checkUnoButton(p)) {
			for(int i = 0; i<2; i++) {					
				addCardToPlayer(p);
			}
		} 
		if(gameOverCheck(p)) {
			stopGame(p);			
		}
	}
	
	/**
	 * 
	 * Method that manage player drawing. 
	 * After checking that player can actually draw, it generates a new card that will be added to player's hand.
	 * 
	 * @param p Player instance
	 * 
	 * 
	 * */
	
	public void playerDrawCard(Player p) {
		// Check player turn
		if(checkPlayerTurn(p)) {
			// Can player draw? If he has already drown don't let draw.
			if(canPlayerDraw()) {
				gameController.cardDrawImage.setOpacity(.5);
				Card generatedCard = UnoCards.generateCard();
				p.addCardInGame(generatedCard);
				gameController.playerDrawCardVisual(p, generatedCard);
				// Increase draw turn because you can only draw once.
				increaseDrawTurn();
				gameController.setSkipButton(true);
				
				// If player did just draw check if he can throw something out of his hands, if not skip turn to the other player/bot
				// else: if player just wait until he throw or skip, if bot throw valid card.
				if(!canPlayerThrow(p)) {
					DelayAndSkipTurn(defaultDelay_ms);
				} else {
					checkCycleForBot();
				}
			}
		}
	}
	
	
	/**
	 * 
	 * Method that loop through bot action until it's player turn,
	 * only then the loop will stop executing, waiting for user action 
	 * 
	 * */
	
	public void checkCycleForBot() {
		// when game finish break the cycle
		if(gameStarted) {
			if (playersInGame.get(playerTurn).getIsBot()) {
				executeBotAI(playersInGame.get(playerTurn));
			}
		}
	}
	
	/**
	 *
	 * Manage single bot turn.
	 * If there are not available cards then draw, the loop will execute this function again to throw the drown card.<br>
	 * like this: playerDrawCard() -> checkCycleForBot() -> this<br>
	 * if he draws a valid card then throw it else skip the turn
	 * 
	 * @param p - Player instance
	 * 
	 * */
	
	public void executeBotAI(Player p) {
		ArrayList<Card> availableCards = p.getCardsInGame().stream().filter(c -> isCardCastable(c)).collect(Collectors.toCollection(ArrayList::new));
		if (availableCards.size() > 0) {
			Card randomChoice = availableCards.get((int)(Math.random()* availableCards.size()));
			playerSelectCard(p,randomChoice);
		} else {
			// If he has already drawn the card then skip the turn
			if(canPlayerDraw()) {
				playerDrawCard(p);								
			} else {
				DelayAndSkipTurn(defaultDelay_ms);
			}
		}	
	}
	
	/**
	 * 
	 * Method to manage to manage speed between turns
	 * Create a thread that wait given ms and then skip the turn
	 * 
	 * @param ms
	 */
	
	public void DelayAndSkipTurn(int ms) {
		
		if(playerTurn == drawTurn) increaseDrawTurn();
		increasePlayerTurn();
		gameController.setSkipButton(false);
		
		
		
		gameController.updateVisualOpacityOtherHands(playersInGame.get(playerTurn));
		if(!playersInGame.get(playerTurn).getIsBot()) {
			playerUnoStatus = false;
			checkSessionPlayerUnoButtonVisual();
			if(canPlayerDraw()) {
				gameController.cardDrawImage.setOpacity(1);
			}
		} else {
			playerUnoStatus = true;
			gameController.cardDrawImage.setOpacity(.5);
		}
		
		Thread thread = new Thread() {
		    public void run() {
		        try {
		            Thread.sleep(ms);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		        Platform.runLater(()->{
		        	checkCycleForBot();
		        });   
		    }
		};
		thread.start();
	}


	/**
	 * Increase player turn based on gameDirection
	 */
	
	public void increasePlayerTurn() {
		if(gameDirection) {
			playerTurn = (playerTurn + 1) % playersInGame.size();
		} else {
			playerTurn = (playerTurn - 1) == -1 ? playersInGame.size() - 1 : playerTurn - 1;
		}
	}
	
	/**
	 * Increase player draw turn based on gameDirection
	 */
	
	public void increaseDrawTurn() {
		if(gameDirection) {
			drawTurn = (drawTurn + 1) % playersInGame.size();
		} else {
			drawTurn = (drawTurn - 1) == -1 ? playersInGame.size() - 1 : drawTurn - 1;
		}
	}
	
	
	
	/**
	 * Method that manage to update table/last card. 
	 * It also call the gameController method "updateLastCardVisual" that is responsible for visual lastCard updating.
	 */
	
	public void updateLastCard(Card c) {
		lastCard = c;
		gameController.updateLastCardVisual(c);
	}
	
	/**
	 * 
	 * Get the next player based on gameDirection
	 * 
	 * @return Player
	 * 
	 */
	
	public Player getNextPlayer() {
		if(gameDirection) {
			return playersInGame.get((playerTurn + 1) % playersInGame.size());
		} else {
			return playersInGame.get((playerTurn - 1) == -1 ? playersInGame.size() - 1 : playerTurn - 1);
		}
	}
	
	/**
	 * It opens the visual color chooser for letting user choose the color after he has thrown COLOR/PLUSFOUR card
	 */
	
	public void openColorChooser() {
		gameController.colorChooserPane.setDisable(false);
		gameController.colorChooserPane.setVisible(true);
	}
	
	/**
	 * 
	 * Method that manage to check if UNO button can be clicked and show it visually
	 * 
	 */
	
	public void checkSessionPlayerUnoButtonVisual() {
		if(checkSessionPlayerHasTwoCards() && canPlayerThrow(sessionPlayer)) {
			gameController.unoPlayerButton.setOpacity(1);
		} else {
			gameController.unoPlayerButton.setOpacity(0.5);
		}
	}

	/**
	 * 
	 * Block next player turn. Used with special cards
	 * 
	 */
	
	
	public void blockNextTurn() {
		if(playerTurn == drawTurn) increaseDrawTurn();
		increasePlayerTurn();
	}
	
	// CHECK METHODS
	
	/**
	 * 
	 * Check if user can do actions by controlling if it's his turn
	 * 
	 * @param p Player instance
	 * @return Is player turn?
	 * 
	 */
	
	public boolean checkPlayerTurn(Player p){
		return p == playersInGame.get(playerTurn);
	}
	
	/**
	 * 
	 * Check if card can be throwed based on lastCard
	 * 
	 * @param card Card
	 * @return Is card castable?
	 * 
	 */
	
	public boolean isCardCastable(Card card) {
		return (card.getColor().equals(lastCard.getColor()) || 
				card.getValue().equals(lastCard.getValue()) || 
				card.getColor().equals(CardColor.BLACK)) || 
				lastCard.getColor().equals(CardColor.BLACK);
	}
	
	/**
	 * 
	 * Method that is responsible for checking special custom card effect.
	 * 
	 * @param c Card
	 * 
	 */
	
	public void checkSpecialCard(Card c) {
		// COLOR,PLUSFOUR - to do
		switch(c.getValue().toString()) {
			case "STOP":
				blockNextTurn();
				break;
			case "REVERSE":
				gameDirection ^= true;
				gameController.reverseCardAnimation();
				
				// this allow to return the turn to the same player who throwed the reverse card in case it's a 1v1
				if(playersInGame.size() == 2) {
					blockNextTurn();
				}	
				
				break;
			case "PLUSTWO":
				for(int i = 0; i<2; i++) {					
					addCardToPlayer(getNextPlayer());
				}
				blockNextTurn();
				break;
			case "PLUSFOUR":
				for(int i = 0; i<4; i++) {					
					addCardToPlayer(getNextPlayer());
				}
				if(playersInGame.get(playerTurn).getIsBot()) {
					gameController.changeLastCardColor(CardColor.getRandomColor());
				} else {
					currentlyChoosingColor = true;
					openColorChooser();					
				}
				blockNextTurn();
				break;
			case "COLOR":
				if(playersInGame.get(playerTurn).getIsBot()) {
					gameController.changeLastCardColor(CardColor.getRandomColor());
				} else {
					currentlyChoosingColor = true;
					openColorChooser();					
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * 
	 * Check if the player can draw
	 * 
	 * @return Can player draw?
	 * 
	 */
	
	public boolean canPlayerDraw() {
		return playerTurn == drawTurn;
	}
	
	/**
	 * 
	 * Check if player has valid card in his hand
	 * 
	 * @param p Player instance
	 * @return Can player throw?
	 * 
	 */
	
	public boolean canPlayerThrow(Player p) {
		return p.getCardsInGame().stream().anyMatch(c -> isCardCastable(c));
	}
	
	/**
	 * 
	 * Check if the current player (based on playerTurn value) can manually skip the turn. 
	 * Player can only skip manually if he already drawn a card and he has valid card to throw in his hand
	 * 
	 * @return Can player manually skip?
	 * 
	 */
	
	public boolean canCurrentUserSkip() {
		return playerTurn == drawTurn - 1;
	}
	
	/**
	 * 
	 * Check if the game is finished, if the player has no card in his hand then he won
	 * 
	 * @param p Player Instance
	 * @return Player has empty hand?
	 * 
	 */
	
	public boolean gameOverCheck(Player p) {
		return p.getCardsInGame().isEmpty();
	}
	
	/**
	 * Check if player has one card left and pressed UNO before throwing the 2nd one
	 * 
	 * @param p
	 * @return Player has left with one card and clicked uno button
	 */
	
	public boolean checkUnoButton(Player p) {
		if(p.getCardsInGame().size() == 1) {
			return playerUnoStatus;
		}
		return true;
	}
	
	/**
	 * Check if sessionPlayer has two cards in hand, 
	 * this is needed to check if the player can actually press the UNO button.
	 * 
	 * @return Session player has two cards in hand
	 */
	
	public boolean checkSessionPlayerHasTwoCards() {
		return sessionPlayer.getCardsInGame().size() == 2;
	}
	
	// INITIALIZE METHODS
	
	/**
	 * 
	 * Give a card to the player
	 * 
	 * @param player Player to give card
	 * 
	 */
	
	public void addCardToPlayer(Player player) {
		Card generatedCard = UnoCards.generateCard();
		player.addCardInGame(generatedCard);
		gameController.playerDrawCardVisual(player, generatedCard);
	}
	
	/**
	 * Give 7 card to anyone in game
	 */
	
	public void initializeSevenCard(){
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	playersInGame.forEach(p -> {
					addCardToPlayer(p);
				});
		    }
		}));
		timeline.setCycleCount(7);
		timeline.play();
	}
	
	/**
	 * Add player to the game
	 */
	
	public void addPlayersInGame(Player p) {
		playersInGame.add(p);
	}
	
	/**
	 * Generate uno card (not black) and set it as the last card.
	 */
	
	public void setupFirstCard() {
		Card generatedCard;
		do {
			generatedCard = UnoCards.generateCard();
		}
		while(generatedCard.getColor().name().equals("BLACK"));
		updateLastCard(generatedCard);
	}
}
