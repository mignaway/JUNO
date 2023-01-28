package cards;

public class Card {
	/**
	 * Card value
	 */
	private final CardValue value;
	/**
	 * Card color
	 */
	private CardColor color;
	/**
	 * Card unique value
	 */
	private final String uuid;
	
	/**
	 * 
	 * Card Constructor
	 * 
	 * @param value Card value
	 * @param color Card color
	 * @param uuid Unique string for identifying cards, mostly needed for visual effect
	 * 
	 */
	
	public Card(CardValue value, CardColor color, String uuid) {
		this.value = value;
		this.color = color;
		this.uuid = uuid;
	}
	
	/**
	 * Get card value.
	 * @return Card value
	 */
	
	public CardValue getValue() {
		return value;
	}
	
	/**
	 * Get card color.
	 * @return Card color
	 */
	
	public CardColor getColor() {
		return color;
	}
	
	/**
	 * Set card color, used for black cards.
	 * @param color New card color
	 */
	
	public void setColor(CardColor color) {
		this.color = color;
	}
	
	/**
	 * Get unique card value.
	 * @return uuid
	 */
	
	public String getUUID() {
		return uuid;
	}
	
	@Override
	public String toString() {
		return color + " " + value;
	}
	
}
