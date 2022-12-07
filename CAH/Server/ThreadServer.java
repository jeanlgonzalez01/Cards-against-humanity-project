package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;

import Cards.BlackCard;
import Cards.CardsList;
import Cards.WhiteCard;

public class ThreadServer extends Thread 
{
	private int numPlayers;
	private CardsList cl = new CardsList();
	private Vector <ObjectOutputStream> allClients = new Vector <ObjectOutputStream>();	   
	private Vector <WhiteCard> pickedCards = new Vector<WhiteCard>(); //holds the cards picked by the players 	   
	private ArrayList <String> clientNames = new ArrayList<String>();	 
	private WhiteCard winningCard = null;	   
	private int winningPlayer;	  
	private int cardMaster = 1;
    Socket cs;
    Object to = null;
    Vector <BlackCard> blackCard;
    int playerID;

    
    public ThreadServer(Socket cs, int playerID, Vector<BlackCard> _blackCard) {
       this.cs = cs;
       blackCard = _blackCard;
       this.playerID = playerID;
    }
    /**
     * returns the playerID
     * @return playerID int
     */
    public int getPlayerID() {
       return playerID;
    }
    /**
     * run method of the thread
     */
    public void run() {
       InputStream in;
       OutputStream out;
       String message;
       String clientName = null;
       int turn = 1; //use to determine cardmaster, adds 1 after each turn
       try {
       //output
          out = cs.getOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(out);
       //input
          in = cs.getInputStream();
          ObjectInputStream ois = new ObjectInputStream(in);
          //synchronized add clients to the Vector of clients
          synchronized(allClients){
             allClients.add(oos); //adds Client to the Vector list            
          }
          //starts by sending 5 white cards to the clients
          for (int i = 0; i < 5; i++) {
             synchronized(allClients){
                String sendCard = cl.sendWhite();
                WhiteCard wc = new WhiteCard(sendCard);
                wc.setPlayerNumber(playerID);
                oos.writeObject(wc);
                oos.flush();
             }
          }
          //sends turn count 
          oos.writeObject(blackCard.get(turn));
          oos.flush();
          //sends playerID
          oos.writeObject((Integer) playerID); //writes player ID to client
          oos.flush();   
          while( (to = ois.readObject() )  != null )
          {
             //STRING SENT FROM CLIENT
             if (to instanceof String){ 
                //Sets Username
                if(clientName == null){
                   clientName = (String)to;
                   clientNames.add(clientName);
                   System.out.println("Player Number: "+ playerID + " : " + clientName + " : ");
                }
                //Username + Input
                else{
                   message = (String)to;
                   System.out.println("Client Message: "+ message);
                   for(ObjectOutputStream client: allClients){ //writes to all clients
                      client.writeObject("Player Number: "+ playerID + " : " + clientName + " : ");	//to client
                      client.flush(); //clears the remaining data in the PrintWriter
                   }
                }
             }
             else if (to instanceof WhiteCard) {
                WhiteCard wc = (WhiteCard) to;
                //if the player is not the cardMaster
                if (cardMaster == playerID ) {
                   System.out.println(cardMaster + " Cardmaster");
                   System.out.println(playerID + " PlayerID");
                   winningCard = wc;
                   winningPlayer = winningCard.getPlayerNumber();
                   turn++;
                   for(ObjectOutputStream toEveryone: allClients){
                      toEveryone.writeObject((Integer) winningPlayer);
                      toEveryone.flush(); //sends winning player
                      toEveryone.writeObject("The Card Master chose " + playerID + "'s card : " + winningCard.getMessage() );
                      toEveryone.flush();//sends winning White Card
                      toEveryone.writeObject(blackCard.get(turn)); //sends next black card in BlackCard array
                      toEveryone.flush();
                   }
                   //New Round
                   winningCard = null;
                   winningPlayer = 0;
                   //Removes cards from pickedCards
                   for(int sublackCardount = 0; sublackCardount < 3 ; ++sublackCardount){
                      pickedCards.remove(0);
                   }
                   //loops back cardmaster count back to 1
                   if(cardMaster == 4){
                      cardMaster = 1;
                   }
                   else{
                      cardMaster++;
                   }
                   System.out.println(cardMaster + " new Cardmaster");       
                }
                else{ //not the cardMaster
                   //while less than 3 cards
                   if(pickedCards.size() < 4){
                      pickedCards.add(wc);
                   }
                   System.out.println("Added : " + wc.getPlayerNumber() + "to submission");
                   synchronized(allClients){
                      String newCard = cl.sendWhite();
                      WhiteCard wc2 = new WhiteCard(newCard);
                      wc2.setPlayerNumber(playerID);
                      System.out.println("Sent to : " + wc2.getPlayerNumber() + ":"   + wc2.getMessage());
                      oos.writeObject(wc2);
                      oos.flush();
                   }
                } 
             }
             //if 3 pickedCards are collected
             if(pickedCards.size() == 3){
                for(ObjectOutputStream toEveryone: allClients){
                   for(WhiteCard cmSubs: pickedCards){
                      toEveryone.writeObject(cmSubs);
                      toEveryone.flush();
                      System.out.println("Sent card: " + cmSubs.getMessage() + " to player" + cmSubs.getPlayerNumber() );
                   }
                }
             }
             //if 4 players are connected
             if(numPlayers == 4 ){
                for(ObjectOutputStream toEveryone: allClients){
                   toEveryone.writeObject("The players currently connected: ");
                   for(String name: clientNames){
                      toEveryone.writeObject(name);
                      toEveryone.flush();
                   }
                   System.out.println("Sent name array to client");
                }
             }
          }//end while
       }//end try
       catch( SocketException se ) { 
          System.out.println("Client Disconnected...");
          clientName = null; 
       }
       catch( IOException e ) { 
          System.out.println("Player : " + playerID + " disconnected...");
          clientName = null;  
       }
       catch(ClassNotFoundException cnfe){
          System.out.println("Class Not Found...");
       }
    } // end while
 } // end class ThreadServer
