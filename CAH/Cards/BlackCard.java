package Cards;

 /** BlackCard sent across the Server    */ 
public class BlackCard extends Card {
	   /** BlackCard sets the message for the card
	    *  @param _message cards text
	    */  
	   public BlackCard(String _message) {
	      super(_message);
	      color = "BLACK";
	   }
	}
