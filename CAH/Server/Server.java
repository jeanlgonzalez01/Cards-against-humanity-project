package Server;
import java.net.*;
import java.io.*;
import java.util.*;

//import Cards.BlackCard;
import Cards.CardsList;
import Cards.BlackCard;

public class Server {
   
   //Array of PrintWriters
   private Vector <ObjectOutputStream> allClients = new Vector <ObjectOutputStream>(); //Players + CardMaster
   private Vector <BlackCard> blackDeck  = new Vector<BlackCard>(); 

   private CardsList cl = new CardsList();//use to get random cards for the players
   private final int MAX_PLAYER_COUNT = 4;
   private int numPlayers;
   
   /**
    * main method that calls the ChatServer constructor
    * @param args Main Arguments
    */
   public static void main(String [] args) {
      new Server();
   }
   /**
    * ChatServer constructor that opens the ServerSocket and the Socket and runs the ThreadServer
    */
   public Server()
   {
      ServerSocket ss = null;
      try {
         ss = new ServerSocket(16789);
         Socket cs = null;
         System.out.println("Server Running...");
         System.out.println("Accepting Clients..");
         
         //sends 5 black cards to the ThreadClients
         synchronized(allClients){
            for(int x = 0 ; x < 5; x++){
               String bc = cl.sendBlack();
               blackDeck.add(new BlackCard(bc) );
            }
         }
         int playerID = 1;
         //starts playerID off as 1   
         while(numPlayers < MAX_PLAYER_COUNT){     
            cs = ss.accept();
            numPlayers++;
            ThreadServer ths = new ThreadServer(cs, playerID, blackDeck);
            for(BlackCard c : blackDeck){
               System.out.println(c.getMessage());
            }
            ths.start();
            playerID++;
         } // end while
      }     
      catch( BindException be ) {
         System.out.println("Server already running on this computer, stopping.");
      } 
      catch( IOException ioe ) {
         System.out.println("Connection Failure");
      }
   }
  
}