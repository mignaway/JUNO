package user;

import java.util.ArrayList;

import cards.Card;
import javafx.scene.image.Image;

public class Player {
	/**
	 * Player nickname
	 */
	private String nickname, avatarFileName;
	/**
	 * Player statistics
	 */
	private int playerLevel,gamesPlayed,gamesWon,gamesLost,pointExperience;
	/**
	 * Is player a bot or a real user?
	 */
	private final boolean isBot;
	/**
	 * Player avatar image
	 */
	private Image avatar;
	
	/**
	 * Player hand.
	 */
	private ArrayList<Card> cardsInGame = new ArrayList<Card>();
	/**
	 * Position of player (Visual Effect).
	 * Card boxes are used to assign visual position to player (left,top,right)
	 */
	private String cardBox;
	
	/**
	 * 
	 * Player Constructor
	 * 
	 * @param nickname Player nickname
	 * @param avatar Player avatar
	 * @param gamesPlayed Player's played games
	 * @param gamesWon Player's won games 
	 * @param playerLevel Player level
	 * @param pointExperience Player's point experience
	 * @param isBot Is player a bot?
	 * 
	 */
	public Player(String nickname, String avatarFileName, int gamesPlayed,int gamesWon,int playerLevel,int pointExperience,boolean isBot) {
		this.nickname = nickname;
		this.avatar = avatarFileName != null ? new Image(avatarFileName, 80, 80, false, false): new Image("bot_enemy.png",80, 80, false, true);
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesPlayed - gamesWon;
		this.playerLevel = playerLevel;
		this.pointExperience = pointExperience;
		this.isBot = isBot;
		this.avatarFileName = avatarFileName;
	}
	
	/**
	 * Get player nickname.
	 * @return nickname
	 */

	public String getNickname() {
		return this.nickname;
	}
	
	/**
	 * Set Player nickname.
	 * @param nickname
	 */
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * Get Player avatar image.
	 * @return Avatar Image
	 */
	
	public Image getAvatar() {
		return this.avatar;
	}
	
	/**
	 * Set Player avatar image.
	 * @param img New avatar image
	 */
	
	public void setAvatar(Image img) {
		this.avatar = img;
	}
	
	/**
	 * Get player's played games.
	 * @return Games Played
	 */
	
	public int getGamesPlayed() {
		return this.gamesPlayed;
	}
	
	/**
	 * Set player's played games.
	 * @param x New games played value
	 */
	
	public void setGamesPlayed(int x) {
		this.gamesPlayed = x;
	}
	
	/**
	 * Get player's won games.
	 * @return Games won
	 */
	
	public int getGamesWon() {
		return this.gamesWon;
	}
	
	/**
	 * Set player's won games.
	 * @param x New games won value
	 */
	
	public void setGamesWon(int x) {
		this.gamesWon = x;
	}
	
	/**
	 * Get player's lost games.
	 * @return Games lost
	 */
	
	public int getGamesLost() {
		return this.gamesLost;
	}
	
	/**
	 * Set player's lost games.
	 * @param x New games lost value
	 */
	
	public void setGamesLost(int x) {
		this.gamesLost = x;
	}
	
	/**
	 * Get player level.
	 * @return Player level
	 */
	
	public int getPlayerLevel() {
		return this.playerLevel;
	}
	
	/**
	 * Set player level
	 * @param x New player level value
	 */
	
	public void setPlayerLevel(int x) {
		this.playerLevel = x;
	}
	
	/**
	 * Get point experience
	 * @return pointExperience
	 */
	
	public int getPointExperience() {
		return this.pointExperience;
	}
	
	/**
	 * Set point experience.ù
	 * If greater than max point experience (100) then level up and set point experience as the rest
	 * 
	 * @param exp
	 */
	
	public void setPointExperience(int exp) {
		if(exp >= 100) {
			this.playerLevel += 1;
			this.pointExperience = exp % 100;
		} else {
			this.pointExperience = exp;
		}
	}
		
	
	/**
	 * Get if player is a bot.
	 * @return Is player a bot?
	 */
	
	public boolean getIsBot() {
		return this.isBot;
	}
	
	/**
	 * Get if player is a bot.
	 * @return Is player a bot?
	 */
	
	
	public String getAvatarFileName() {
		return this.avatarFileName;
	}
	
	/**
	 * Add card in player's hand.
	 * @param card Card
	 */
	
	public void addCardInGame(Card card) {
		cardsInGame.add(card);
	}
	
	/**
	 * Remove card in player's hand.
	 * @param card Card
	 */
	
	public void removeCardInGame(Card card) {
		cardsInGame.removeIf(c -> c.getUUID().equals(card.getUUID()));
	}
	
	/**
	 * Get player's hand.
	 * @return Player hand
	 */
	
	public ArrayList<Card> getCardsInGame() {
		return cardsInGame;
	}
	
	/**
	 * Set card box (visual position)
	 * @param box Card Box
	 */
	
	public void assignCardBox(String box) {
		cardBox = box;
	}	
	
	/**
	 * Get card box (visual position)
	 * @return Card Box
	 */
	
	public String getCardBox() {
		return cardBox;
	}
	
	/**
	 * Clear player hand and remove card box
	 */
	
	public void resetGame() {
		cardsInGame.clear();
		cardBox = "";
	}
}


