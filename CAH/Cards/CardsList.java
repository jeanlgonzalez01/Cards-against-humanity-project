package Cards;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CardsList {

	ArrayList<String> whiteCards = new ArrayList <String>();
	ArrayList<String> blackCards = new ArrayList <String>();     
	/** CardsList constructor adds black and white cards
	 */
	public CardsList() {
		addWhite();
		addBlack();     
	}

	/**
	 * addBlack adds all the black cards to the vector
	 */
	private void addBlack() {
		try {
			BufferedReader brb = new BufferedReader(new FileReader("./Cards/black.txt"));
			String sentence;
			while((sentence = brb.readLine()) != null) {
				blackCards.add(sentence);
			}
			brb.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * AddWhite adds all the cards to the card array
	 */
	private void addWhite() {
		try {
			BufferedReader brw = new BufferedReader(new FileReader("./Cards/white.txt"));
			String sentence;
			while((sentence = brw.readLine()) != null) {
				whiteCards.add(sentence);
			}
			brw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	/** sendWhite gets a random card, return the card and remove from list
	 * @return send String card to send to the Client
	 */
	public String sendWhite() {
		Random rand = new Random();
		int whiteCard = rand.nextInt(whiteCards.size());
		String send = whiteCards.get(whiteCard);
		whiteCards.remove(whiteCard);
		return send;    
	}
	
	/** get a random card, return and remove from list 
	 * @return send String card to send to the Client
	 */
	public String sendBlack() {
		Random rand = new Random();
		int blackCard = rand.nextInt(blackCards.size());
		String send = blackCards.get(blackCard);
		blackCards.remove(blackCard);
		return send;    
	}
}