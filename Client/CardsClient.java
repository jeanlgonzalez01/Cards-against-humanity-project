package Client;

//Client for Cards Against Humanity Game
//Authors: Giovannie Marrero, Jean L. González y Desireé Rodríguez
//Final Project Data Communication SICI4037
//Prof. Juan Solá 


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


//import Cards.Card;
//import Cards.Deck.Color;

public class CardsClient extends JFrame implements Serializable {
	
	private CardsClient[] deck;
    
    private CardsClient[] deck2;
	
	 private JPanel jpCenter;
	   private JPanel jpNorth;
	   private JPanel jpScore;

	   private JButton card1;
	   private JButton card2;
	   private JButton card3;
	   private JButton card4;
	   private JButton card5;
	   private JButton card6;
	   private JButton card7;
	   private JButton cBlack;
	   private JButton jbSend;
	   private JButton jbConnect;
	   
	   private JTextField player1;
	   private JTextField player2;
	   private JTextField player3;
	   private JTextField player4;
	   
	   private JTextArea jtaServer;
	   private JTextArea jtaClient;
	   
	   private JMenuBar jmb;
	   private JMenu jmFile;
	   private JMenuItem jmiNew;
	   private JMenuItem jmiExit;

	   private Socket cs = null;
	   private OutputStream out = null;
	   private ObjectOutputStream oos = null;
	   private InputStream in = null;
	   private ObjectInputStream ois = null;
	   
	   private String IP = "localhost";
	   private final int PORT = 16789;
	   
	   private String clientName;
	   private int winningPlayer;
	   private int myNumber;
	   private int turnCount;
	   
	   private ArrayList <WhiteCard> sentCards = new ArrayList <WhiteCard>();
	  
	   public CardsClient()
	   {
	      setTitle("Cards Against Humanity");
	      setLayout(new BorderLayout());
	      
	      Dimension cardDimen = new Dimension(165, 180);
	      Font cardFont = new Font("Arial", Font.BOLD, 14);
	      
	      //menu
	      jmb = new JMenuBar();
	      setJMenuBar(jmb);
	      jmFile = new JMenu("File");
	      jmb.add(jmFile);
	      jmiNew = new JMenuItem("New Game");
	      jmFile.add(jmiNew);
	      jmiExit = new JMenuItem("Exit");
	      jmFile.add(jmiExit);
	         
	      //cardmaster
	      card6 = new JButton("");
	      card6.setFont(cardFont);
	      card6.setPreferredSize(cardDimen);
	      card6.setEnabled(false);
	      
	      card7 = new JButton("");
	      card7.setFont(cardFont);
	      card7.setPreferredSize(cardDimen);
	      card7.setEnabled(false);
	      
	      
	      //score keeper
	      /*
	      *Set the scores to zero in un-editable texfields
	      */
	      jpScore = new JPanel(new GridLayout(0,2));
	      player1 = new JTextField(3);
	      player1.setText("0");
	      player1.setEnabled(false);
	      player2 = new JTextField(3);
	      player2.setText("0");
	      player2.setEnabled(false);
	      player3 = new JTextField(3);
	      player3.setText("0");
	      player3.setEnabled(false);
	      player4 = new JTextField(3);
	      player4.setText("0");
	      player4.setEnabled(false);
	      
	      jpScore.add(new JLabel("Player 1:", JLabel.RIGHT));
	      jpScore.add(player1);
	      jpScore.add(new JLabel("Player 2:", JLabel.RIGHT));
	      jpScore.add(player2);
	      jpScore.add(new JLabel("Player 3:", JLabel.RIGHT));
	      jpScore.add(player3);
	      jpScore.add(new JLabel("Player 4:", JLabel.RIGHT));
	      jpScore.add(player4);
	      
	      //Jpanel
	      jpNorth = new JPanel(new FlowLayout());
	      jpCenter = new JPanel (new FlowLayout());
	      
	      //Centralization for cards
	      add(jpNorth, BorderLayout.NORTH);
	      add(jpCenter,BorderLayout.CENTER);
	      
	      jpNorth.add(cBlack);
	      jpNorth.add(card6);
	      jpNorth.add(card7);
	      jpNorth.add(jpScore);
	   
	      jpCenter.add(card1);
	      jpCenter.add(card2);
	      jpCenter.add(card3);
	      jpCenter.add(card4);
	      jpCenter.add(card5);
	      
	      //Connection to server
	      jbConnect.addActionListener(
	        new ActionListener(){
	         public void actionPerformed(ActionEvent ae) {
	               
	                  //IP = JOptionPane.showInputDialog(jbConnect, "Enter the server's IP address:");
	                  try{
	                     //Socket
	                     cs = new Socket(IP, PORT);
	                     
	                     //Output 
	                     out = cs.getOutputStream();
	                     oos = new ObjectOutputStream(out);
	                     
	                     //Input 
	                     in = cs.getInputStream();
	                     ois = new ObjectInputStream(in);
	                     
	                     //Asks the user for a user name and sends to server
	                     if(clientName == null){
	                        clientName = JOptionPane.showInputDialog(jbSend,"Please enter a username");
	                        oos.writeObject(clientName); 
	                        oos.flush();             
	                     }
	                     

	                     jbConnect.setEnabled(false);
	                     
	                     //Thread
	                     ClientThread ct = new ClientThread(cs);
	                     ct.start();
	                     
	                  } 
	                  
	                  //All Exceptions
	                  catch(ConnectException ce ) { 
	                     System.out.println("Server is Not Online"); 
	                  }
	                  catch(UnknownHostException uhe) {
	                     System.out.println("no host");
	                     uhe.printStackTrace();
	                  }
	                  catch(IOException ioe)
	                  {
	                     System.out.println("IO error");
	                     ioe.printStackTrace();
	                  }
	                  catch( ArrayIndexOutOfBoundsException aioobe ) {
	                     System.out.println("Array Index Out of Bounds");
	                  }
	               }});
	      	setSize(900,700);
	      	setLocationRelativeTo(null);
	      	setDefaultCloseOperation(EXIT_ON_CLOSE);
	      	setVisible(true);
	   
	   }//end constructor
	   
	   
	   /**
	    * ClientThread is the thread that keeps the GUI updated
	    */
	   class ClientThread extends Thread implements ActionListener {
	      
	      //Attributes
	      Socket cs;
	      String clientMessage;
	      int playerNum;
	      
	      
	      //Constructor
	      /**
	       * ClientThread Constructor that takes a socket to bind on
	       * @param cs Socket binding socket
	       */     
	      public ClientThread(Socket cs){
	         this.cs = cs;
	         
	         //Set the action listeners for the white card buttons
	         card1.addActionListener(this);
	         card2.addActionListener(this);
	         card3.addActionListener(this);
	         card4.addActionListener(this);
	         card5.addActionListener(this);
	         card6.addActionListener(this);
	         card7.addActionListener(this);
	                 
	      
	      }
	      /**
	       * Run method that starts the thread for the client
	       */  
	      public void run() {
	         try{
	            //updates 
	            while(true){
	               Object readIn = ois.readObject();
	               
	              
	               //if it is integer
	               if (readIn instanceof Integer) {
	                  //if turn count is zero, then this is going to be the player number
	                  if (turnCount == 0) {
	                     myNumber = (Integer) readIn;
	                     if (myNumber == 1) {
	                        jtaServer.append("**You are player number " + myNumber + "**\nYou will be the CardMaster for the first turn.");
	                     }
	                     else {
	                        jtaServer.append("**You are player number " + myNumber + "**\n");
	                     }
	                     turnCount++;
	                     if (turnCount != myNumber) {
	                        card1.setEnabled(true);
	                        card2.setEnabled(true);
	                        card3.setEnabled(true);
	                        card4.setEnabled(true);
	                        card5.setEnabled(true);
	                     }    
	                  }
	                  //if the turn count isn't zero, then it is the number of the winning player for that round
	                  else if (turnCount > 0) {
	                     winningPlayer = (Integer) readIn;
	                     int currScore;
	                     
	                     //if the winning player is 1, add one to their score
	                     if (winningPlayer == 1) {
	                        currScore = Integer.parseInt(player1.getText());
	                        currScore++;
	                        player1.setText("" + currScore); 
	                        resetBoard();
	                     }
	                     //if the winning player is 2
	                     else if (winningPlayer == 2) {
	                        currScore = Integer.parseInt(player2.getText());
	                        currScore++;
	                        player2.setText("" + currScore);
	                        resetBoard();                         
	                     }                  
	                     //if the winning player is 3
	                     else if (winningPlayer == 3) {
	                        currScore = Integer.parseInt(player3.getText());
	                        currScore++;
	                        player3.setText("" + currScore);
	                        resetBoard();     
	                     }  
	                     //if the winning player is 4
	                     else if (winningPlayer == 4) {
	                        currScore = Integer.parseInt(player4.getText());
	                        currScore++;
	                        player4.setText("" + currScore);
	                        resetBoard();   
	                     }
	                  }
	               }    
	               //if the object is a Black Card  
	               else if (readIn instanceof BlackCard) {
	                  BlackCard msg = (BlackCard)readIn;
	                  String cardmsg = msg.getMessage();
	                  //call the html method and set the text to the black card
	                  cardmsg = toHtml(cardmsg, 18);
	                  cBlack.setText("<html><center>" + cardmsg + "</center></html>");
	               }
	               //if its a whiteCard  
	               else if (readIn instanceof WhiteCard) {
	                  WhiteCard test = (WhiteCard) readIn;
	                  //for testing
	                  //System.out.println(test.getMessage() + test.getPlayerNumber());
	                  //go thru the buttons and see which ones are empty
	                  
	                  //if empty, set the text
	                  if (card1.getText().equals("")) {
	                     setWhiteText(card1, readIn);
	                  }
	                  else if (card2.getText().equals("")) {
	                     setWhiteText(card2, readIn);
	                  }
	                  else if (card3.getText().equals("")) {
	                     setWhiteText(card3, readIn);
	                  }
	                  else if (card4.getText().equals("")) {
	                     setWhiteText(card4, readIn);
	                  }
	                  else if (card5.getText().equals("")) {
	                     setWhiteText(card5, readIn);
	                  }
	                  else if(card6.getText().equals("")) {
	                     card6.setText(test.getMessage());
	                     sentCards.add(test);
	                     if (turnCount == myNumber) {
	                        card6.setEnabled(true);
	                     }
	                  }
	                  else if(card7.getText().equals("")) {
	                     card7.setText(test.getMessage());
	                     sentCards.add(test);
	                     if (turnCount == myNumber) {
	                        card7.setEnabled(true);
	                     }
	                  }
	            
	               }
	            }
	         }
	         
	         catch(ConnectException ce ) { 
	            System.out.println("Server is Not Online"); 
	         }
	         catch(ClassNotFoundException cnfe) {
	            System.out.println("class not found");
	         }
	         catch(UnknownHostException uhe) {
	            System.out.println("no host");
	         }
	         catch(IOException ioe) {
	            System.out.println("Server was terminated...");
	         }
	       
	      }
	      
	      //actionPerformed      
	      public void actionPerformed(ActionEvent ae) {
	         Object choice = ae.getSource();
	         
	         //if clicked button 1-5, send to server and disable after
	            if (choice == card1) {
	            WhiteCard wc = new WhiteCard(card1.getText());
	            wc.setPlayerNumber(playerNum);
	            Object o = wc;
	            try {
	               oos.writeObject(o);
	               oos.flush();
	               card1.setText("");
	               disable(); 
	            }
	            catch (IOException ioe) {
	               System.out.println("IO Exception");
	            }     
	         }
	         else if (choice == card2) {
	            WhiteCard wc = new WhiteCard(card2.getText());
	            wc.setPlayerNumber(playerNum);
	            Object o = wc;
	            try {
	               oos.writeObject(o);
	               oos.flush();
	               card2.setText("");
	                disable(); 
	            }
	            catch (IOException ioe) {
	               System.out.println("IO Exception");
	            }   
	         }
	         else if (choice == card3) {
	            WhiteCard wc = new WhiteCard(card3.getText());
	            wc.setPlayerNumber(playerNum);
	            Object o = wc;
	            try {
	               oos.writeObject(o);
	               oos.flush();
	               card3.setText("");
	                disable(); 
	            }
	            catch (IOException ioe) {
	               System.out.println("IO Exception");
	            }    
	         }
	         else if (choice == card4) {
	            WhiteCard wc = new WhiteCard(card4.getText());
	            wc.setPlayerNumber(playerNum);
	            Object o = wc;
	            try {
	               oos.writeObject(o);
	               oos.flush();
	               card4.setText("");
	                disable(); 
	            }
	            catch (IOException ioe) {
	               System.out.println("IO Exception");
	            }   
	         }
	         else if (choice == card5) {
	            WhiteCard wc = new WhiteCard(card5.getText());
	            wc.setPlayerNumber(playerNum);
	            Object o = wc;
	            try {
	               oos.writeObject(o);
	               oos.flush();
	               card5.setText(""); 
	                disable();
	            }
	            catch (IOException ioe) {
	               System.out.println("IO Exception");
	            }
	         }
	         //if 6-8, send the card to the server as the winning card
	         else if (choice == card6) {
	            if (cardSix.getText().equals("")) {
	               return;
	            }
	            for (WhiteCard thisCard : sentCards) {
	               if (thisCard.getMessage().equals(card6.getText())) {
	                  Object send = thisCard;
	                  try {
	                     oos.writeObject(send);
	                     oos.flush();
	                  }
	                  catch(IOException ioe) {
	                     System.out.println("IOException");
	                  }
	               }
	            }  
	         }
	         else if (choice == card7) {
	            if(card7.getText().equals("")) {
	               return;
	            }
	            for (WhiteCard thisCard : sentCards) {
	               if (thisCard.getMessage().equals(card7.getText())) {
	                  Object send = thisCard;
	                  try {
	                     oos.writeObject(send);
	                     oos.flush();
	                  }
	                  catch(IOException ioe) {
	                     System.out.println("IOException");
	                  }
	               }
	            }  
	         }
	      }
	      
	      /**
	       * Disable method to disable the player's 5 cards
	       * only used when they are the cardMaster
	       */
	      public void disable() {
	         card1.setEnabled(false);
	         card2.setEnabled(false);
	         card3.setEnabled(false);
	         card4.setEnabled(false);
	         card5.setEnabled(false);
	      }
	      /** SetWhiteText gets the string from the white card, calls the toHtml method, puts it on
	        * the jButton with the html and center  tags
	        * @param jb JButton Button to set text to
	        * @param _readIn Object in which to take text from
	        */
	      public void setWhiteText(JButton jb, Object _readIn) {
	         WhiteCard wc = (WhiteCard)_readIn;
	         playerNum = wc.getPlayerNumber();
	         String wcMsg = wc.getMessage();
	         wcMsg = toHtml(wcMsg, 18);
	         System.out.println(wcMsg);
	         jb.setText("<html><center>" + wcMsg + "</center></html>");   
	      }
	      
	      /** toHtml calls the create method in a loop to add all the break tags
	       *  @param text String text that formats to the card
	       *  @param number int *magic* number that fits the card's wrap
	       *  @return text String formatted
	       */
	      public String toHtml(String text, int number) {
	         StringBuilder sb = new StringBuilder(text);
	         int loc = 0;
	         String space = " ";
	         loc = loc + number;
	         while (loc < sb.length()) {
	            createLine(loc, sb);
	            loc = loc + number;  
	         }
	         text = sb.toString();
	         return text;
	      }
	      /** createLine method to put breaks in the card strings so they are readable
	       *  @param _loc int inserts br tags
	       *  @param _sb StringBuilder where to add text
	       */
	      public void createLine(int _loc, StringBuilder _sb) {
	         if (_sb.charAt(_loc) == ' ') {
	            _sb.insert(_loc, "<br>");
	         }
	         else {
	            _loc--;
	            createLine(_loc, _sb);
	         }
	      }
	      /** resetBoard resets board after each turn */
	      public void resetBoard() {
	         //remove the potential winning cards after winner is picked
	         card6.setText("");
	         card7.setText("");
	         
	         
	         //change turn count
	         if (turnCount == 4) {
	            turnCount = 1;
	         }
	         else {
	            turnCount++;  
	         }
	         
	         //if the player isnt the cardmaster, enable their cards
	         if (turnCount != myNumber) {
	            card1.setEnabled(true);
	            card2.setEnabled(true);
	            card3.setEnabled(true);
	            card4.setEnabled(true);
	            card5.setEnabled(true); 
	            card6.setEnabled(false);
	            card7.setEnabled(false);
	            
	         }
	         if (turnCount == myNumber) {
	            card6.setEnabled(true);
	            card7.setEnabled(true);
	            JOptionPane.showMessageDialog(null, "You are the CardMaster for this turn.");
	         }
	      }
	   
	   }
	
	

}
