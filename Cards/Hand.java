package Cards;

import java.util.ArrayList;

public class Hand {
	
	private ArrayList<Card> hand;

	public Hand() {
		hand = new ArrayList<Card>(7);
	}
	
	public void clear() {
		hand.clear();
	}
	
	public void addCard(Card c) {
		if(c == null)
			throw new NullPointerException("Can't add a null card to a hand.");
		hand.add(c);
	}
	
	/*public void removeCard(Card c) {
		hand.remove(c);
	}*/
	
	public void removeCard(int position) {
		if (position < 0 || position >= hand.size())
            throw new IllegalArgumentException("Position does not exist in hand: "
                    + position);
        hand.remove(position);
	}
	
	public int getCardCount() {
		return hand.size();
	}
	
	public Card getCard(int position) {
		if (position < 0 || position >= hand.size())
            throw new IllegalArgumentException("Position does not exist in hand: "
                    + position);
        return hand.get(position);
	}
}
