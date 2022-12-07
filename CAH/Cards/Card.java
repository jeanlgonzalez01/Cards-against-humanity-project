package Cards;

import java.io.*;

public class Card implements Serializable {

   //attributes
   private String message;
   protected String color;
   protected int playerNumber;
   /**
    * Card constructor to set message
    * @param _message String message to card
    */
   public Card(String _message) {   
      message = _message;
      color = null;      
   } 
   /**
    * getMessage returns the card's message
    * @return message String cards message
    */
   public String getMessage() {
      return message;  
   }
   /**
    * setPlayerNumber constructor to set message
    * @param num int playerNumber 
    */
   public void setPlayerNumber(int num){
      this.playerNumber = num;
   }
   /**
    * getPlayerNumber constructor to set message
    * @return playerNumber int playerNumber
    */
   public int getPlayerNumber(){
      return this.playerNumber;
   } 
}