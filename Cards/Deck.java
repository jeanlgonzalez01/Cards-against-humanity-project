package Cards;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Deck {

	private Card[] deck;

	private int cardsUsed;

	public enum Color{
		WHITE, BLACK
	}

	public Deck(Color color) {
		try {
			BufferedReader brb = new BufferedReader(new FileReader("black.txt"));
			BufferedReader brw = new BufferedReader(new FileReader("white.txt"));
			String sentence;
			deck = new Card[100]; // Esto hay que cambiarlo por el numero de lineas en el .txt
			int cardCount = 0; // Cuantas cartas se han creado
			if(color == Color.BLACK) {
				while((sentence = brb.readLine()) != null) {
					deck[cardCount] = new Card(sentence, Color.BLACK);
					cardCount++;
				}
			}
			if(color == Color.WHITE) {
				while((sentence = brw.readLine()) != null) {
					deck[cardCount] = new Card(sentence, Color.WHITE);
					cardCount++;
				}
			}
			cardsUsed = 0;
			brb.close();
			brw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shuffle() {
		for(int i = deck.length - 1; i > 0; i--) {
			int rand = (int)(Math.random()*(i+1));
			Card temp = deck[i];
			deck[i] = deck[rand];
			deck[rand] = temp;
		}
		cardsUsed = 0;
	}

	public int cardsLeft() {
		return deck.length - cardsUsed;
	}

	public Card dealCard() {
		if(cardsUsed == deck.length)
			throw new IllegalStateException("No cards are left in the deck");
		cardsUsed++;
		return deck[cardsUsed - 1];
	}
}
