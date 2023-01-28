package cards;

public enum CardColor {
	RED,GREEN,BLUE,YELLOW,BLACK;
	
	/**
	 * 
	 * Generate random card color.
	 * 
	 * @return Card color except black
	 * 
	 * */
	
	public static CardColor getRandomColor() {
		return values()[(int) (Math.random() * values().length - 1)]; // -1 avoid choosing black as random color
	}
}
