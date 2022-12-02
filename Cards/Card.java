package Cards;

import Cards.Deck.Color;

public class Card {
	
	private String message;
	protected Color cardType;
	protected int playerNumber;
	
	public Card(String message, Cards.Deck.Color color) {
		this.message = message;
		cardType = color;
	}

	public String getMessage() {
		return message;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

}
