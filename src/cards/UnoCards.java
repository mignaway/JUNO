package cards;

import java.util.Random;
import java.util.UUID;

public class UnoCards {
	/*
	 * STATIC FACTORY DESIGN PATTERN
	 */
	
	/**
	 * Generate UNO card with different probabilities
	 * @return Card
	 */
	
	public static Card generateCard() {
		String uuid = UUID.randomUUID().toString();
		Random rand = new Random();	
        int cardType = rand.nextInt(108);
        // normal card
        if (cardType < 76) {
        	return new Card(CardValue.getRandomValueNumber(),CardColor.getRandomColor(),uuid);
        } // action card
        else if (cardType < 100) {
        	return new Card(CardValue.getRandomValueAction(),CardColor.getRandomColor(),uuid);
        } // wild card
        else {
        	return new Card(CardValue.getRandomValueBlack(),CardColor.BLACK,uuid);
        }
        
	}
	
	/**
	 * 
	 * Return image filename of given card (Visual Effect).
	 * 
	 * @param card Card
	 * @return Card image filename
	 * 
	 */
	public static String getCardFile(Card card) {
		return (card.getValue().name() + "_" + card.getColor().name()).toLowerCase();
	}
}
