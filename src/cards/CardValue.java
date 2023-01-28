package cards;

public enum CardValue {
	ZERO,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,STOP,REVERSE,PLUSTWO,PLUSFOUR,COLOR;
	
	/**
	 * 
	 * Generate random card value.
	 * 
	 * @return Card value.
	 * 
	 * */
	
	public static CardValue getRandomValue() {
		return values()[(int) (Math.random() * values().length)];
	}
	public static CardValue getRandomValueNumber() {
		return values()[(int) (Math.random() * (values().length - 5))];
	}
	public static CardValue getRandomValueAction() {
		return values()[(int) (Math.random() * ((values().length - 2) - 10) + 10)];
	}
	public static CardValue getRandomValueBlack() {
		return values()[(int) (Math.random() * (values().length - 13) + 13)];
	}
}
