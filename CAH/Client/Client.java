package Client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import Cards.BlackCard;
import Cards.WhiteCard;
/**
 * Client is a class that will connect to a Server and play Cards Against Humanity
 */
public class Client extends JFrame{

   private JPanel jpCenter;
   private JPanel jpNorth;
   private JPanel messsageBoard;
   private JPanel scoreBoard;

   private JButton firstCard;
   private JButton secondCard;
   private JButton thirdCard;
   private JButton fourthCard;
   private JButton fifthCard;
   private JButton sixthCard;
   private JButton seventhCard;
   private JButton eighthCard;
   private JButton blackCard;
   private JButton sendConnection;
   private JButton connect;
   
   private JTextField playerOne;
   private JTextField playerTwo;
   private JTextField playerThree;
   private JTextField playerFour;
   
   private JTextArea server;
   private JTextArea client;
   
   private JMenuBar menuBar;
   private JMenu menuFile;
   private JMenuItem newItem;
   private JMenuItem exitFrame;

   private Socket cs = null;
   private OutputStream out = null;
   private ObjectOutputStream oos = null;
   private InputStream in = null;
   private ObjectInputStream ois = null;
   
   private String IP = "25.4.139.171";
   private final int PORT = 16789;
   
   private String clientName;
   private int winningPlayer;
   private int myNumber;
   private int turnCount;
   private ArrayList <WhiteCard> whiteDeck = new ArrayList <WhiteCard>();
  
   public Client()
   {
      setTitle("Cards Against Humanity");
      setLayout(new BorderLayout());
      
      Dimension cardDimen = new Dimension(165, 180);
      Font cardFont = new Font("Helvetica Neue", Font.BOLD, 14);
      
      menuBar = new JMenuBar();
      setJMenuBar(menuBar);
      menuFile = new JMenu("File");
      menuBar.add(menuFile);
      newItem = new JMenuItem("New Game");
      menuFile.add(newItem);
      exitFrame = new JMenuItem("Exit");
      menuFile.add(exitFrame);
         
      //white card 
      firstCard = new JButton();
      firstCard.setFont(cardFont);
      firstCard.setPreferredSize(cardDimen);
      firstCard.setBackground(Color.WHITE);
      firstCard.setOpaque(true);
      firstCard.setBorderPainted(false);
      firstCard.setEnabled(false);
   
      //white card 2
      secondCard = new JButton();
      secondCard.setFont(cardFont);
      secondCard.setPreferredSize(cardDimen);
      secondCard.setBackground(Color.WHITE);
      secondCard.setOpaque(true);
      secondCard.setBorderPainted(false);
      secondCard.setEnabled(false);
   
      //white card 3
      thirdCard = new JButton();
      thirdCard.setFont(cardFont);
      thirdCard.setPreferredSize(cardDimen);
      thirdCard.setBackground(Color.WHITE);
      thirdCard.setOpaque(true);
      thirdCard.setBorderPainted(false);
      thirdCard.setEnabled(false);
   
      //white card 4
      fourthCard = new JButton();
      fourthCard.setFont(cardFont);
      fourthCard.setPreferredSize(cardDimen);
      fourthCard.setBorderPainted(false);
      fourthCard.setBackground(Color.WHITE);
      fourthCard.setOpaque(true);
      fourthCard.setEnabled(false);
   
      //white card 5
      fifthCard = new JButton();
      fifthCard.setFont(cardFont);
      fifthCard.setPreferredSize(cardDimen);
      fifthCard.setBorderPainted(false);
      fifthCard.setBackground(Color.WHITE);
      fifthCard.setOpaque(true);
      fifthCard.setEnabled(false);
      
      //black card
      blackCard = new JButton("Black Card");
      blackCard.setFont(cardFont);
      blackCard.setBackground(Color.BLACK);
      blackCard.setForeground(Color.WHITE);
      blackCard.setPreferredSize(cardDimen);
      blackCard.setOpaque(true);
      blackCard.setBorderPainted(false);
      blackCard.setEnabled(true);
      
      //other players cards (for cardmaster)
      sixthCard = new JButton("");
      sixthCard.setFont(cardFont);
      sixthCard.setPreferredSize(cardDimen);
      sixthCard.setEnabled(false);
      
      seventhCard = new JButton("");
      seventhCard.setFont(cardFont);
      seventhCard.setPreferredSize(cardDimen);
      seventhCard.setEnabled(false);
      
      eighthCard = new JButton("");
      eighthCard.setFont(cardFont);
      eighthCard.setPreferredSize(cardDimen);
      eighthCard.setEnabled(false);
      
      
      
      scoreBoard = new JPanel(new GridLayout(0,2));
      playerOne = new JTextField(3);
      playerOne.setText("0");
      playerOne.setEnabled(false);
      playerTwo = new JTextField(3);
      playerTwo.setText("0");
      playerTwo.setEnabled(false);
      playerThree = new JTextField(3);
      playerThree.setText("0");
      playerThree.setEnabled(false);
      playerFour = new JTextField(3);
      playerFour.setText("0");
      playerFour.setEnabled(false);
      connect = new JButton("Connect");
      
      scoreBoard.add(new JLabel("Player 1:", JLabel.RIGHT));
      scoreBoard.add(playerOne);
      scoreBoard.add(new JLabel("Player 2:", JLabel.RIGHT));
      scoreBoard.add(playerTwo);
      scoreBoard.add(new JLabel("Player 3:", JLabel.RIGHT));
      scoreBoard.add(playerThree);
      scoreBoard.add(new JLabel("Player 4:", JLabel.RIGHT));
      scoreBoard.add(playerFour);
      scoreBoard.add(new JLabel("\n", JLabel.RIGHT));
      scoreBoard.add(connect);
      
      jpNorth = new JPanel(new FlowLayout());
      jpCenter = new JPanel (new FlowLayout());
      messsageBoard = new JPanel(new BorderLayout());
      
      add(jpNorth, BorderLayout.NORTH);
      add(jpCenter,BorderLayout.CENTER);
      add(messsageBoard,BorderLayout.SOUTH);

      jpNorth.add(blackCard);
      jpNorth.add(sixthCard);
      jpNorth.add(seventhCard);
      jpNorth.add(eighthCard);
      jpNorth.add(scoreBoard);
   
      jpCenter.add(firstCard);
      jpCenter.add(secondCard);
      jpCenter.add(thirdCard);
      jpCenter.add(fourthCard);
      jpCenter.add(fifthCard); 
       
   
      //Chat Client
      
      //adds text area
      server = new JTextArea(15,40);
      server.setEditable(false);
      //adds line wrap
      server.setLineWrap(true);
      //adds scrollable
      JScrollPane scroll = new JScrollPane (server);
      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      messsageBoard.add(scroll, BorderLayout.CENTER);
      
      //client text area to send to server
      client = new JTextArea(6,20);
      //line wrap
      client.setLineWrap(true);

      
      connect.addActionListener(
            new ActionListener(){
               public void actionPerformed(ActionEvent ae) {
               
                 // IP = JOptionPane.showInputDialog(connect, "Enter the server's IP address:");
                  try{
                     //Socket
                     cs = new Socket(IP, PORT);
                     
                     //Output Stream
                     out = cs.getOutputStream();
                     oos = new ObjectOutputStream(out);
                     
                     //Input Stream
                     in = cs.getInputStream();
                     ois = new ObjectInputStream(in);
                     
                     //asks the user for a user name and sends to server
                     if(clientName == null){
                        clientName = JOptionPane.showInputDialog(sendConnection,"Please enter a username");
                        oos.writeObject(clientName); 
                        oos.flush();             
                     }

                     connect.setEnabled(false);
                     
                     //Thread
                     ClientThread ct = new ClientThread(cs);
                     ct.start();
                     
                     //handles chat strings from client to server
                     sendConnection.addActionListener(
                           new ActionListener(){
                              public void actionPerformed(ActionEvent ae) {
                                 try{
                                    //Writes message from server and appends
                                    if(client.getText().length() > 0){
                                       String clientMsg = client.getText();
                                       oos.writeObject(clientMsg);
                                       oos.flush();
                                       client.setText("");
                                    }   
                                 }
                                 catch (IOException oie) {
                                    System.out.println("IO Exception");
                                 }                                 
                              }	
                           });   
                  } 
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
    * main method that calls the ChatClient constructor
    * @param args Main Arguments
    */
   public static void main(String [] args) {
      
      new Client();
      
   }
   /**
    * ClientThread is the thread that keeps the GUI updated
    */
   class ClientThread extends Thread implements ActionListener {
      
      //attributes
      Socket cs;
      String clientMessage;
      int playerNum;
      
      
      //constructor
      /**
       * ClientThread Constructor that takes a socket to bind on
       * @param cs Socket binding socket
       */     
      public ClientThread(Socket cs){
         this.cs = cs;
         
         //set the action listeners for the white card buttons
         firstCard.addActionListener(this);
         secondCard.addActionListener(this);
         thirdCard.addActionListener(this);
         fourthCard.addActionListener(this);
         fifthCard.addActionListener(this);
         sixthCard.addActionListener(this);
         seventhCard.addActionListener(this);
         eighthCard.addActionListener(this);         
      
      }
      /**
       * Run method that starts the thread for the client
       */  
      public void run() {
         try{
            //Always updates 
            while(true){
               Object readIn = ois.readObject();
               
               //if it's a string, it is chat. append to chat area
               if (readIn instanceof String) {
                  clientMessage = (String) readIn;
                  //change to append to text area
                  server.append(clientMessage+"\n");
               }
               //if it is integer
               else if (readIn instanceof Integer) {
                  //if turn count is zero, then this is going to be the player number
                  if (turnCount == 0) {
                     myNumber = (Integer) readIn;
                     if (myNumber == 1) {
                        server.append("**You are player number " + myNumber + "**\nYou will be the CardMaster for the first turn.");
                     }
                     else {
                        server.append("**You are player number " + myNumber + "**\n");
                     }
                     turnCount++;
                     if (turnCount != myNumber) {
                        firstCard.setEnabled(true);
                        secondCard.setEnabled(true);
                        thirdCard.setEnabled(true);
                        fourthCard.setEnabled(true);
                        fifthCard.setEnabled(true);
                     }    
                  }
                  //if the turn count isnt zero, then it is the number 
                  //of the winning player for that round
                  else if (turnCount > 0) {
                     winningPlayer = (Integer) readIn;
                     int currScore;
                     
                     //if the winning player is 1, add one to their score
                     if (winningPlayer == 1) {
                        currScore = Integer.parseInt(playerOne.getText());
                        currScore++;
                        playerOne.setText("" + currScore); 
                        resetBoard();
                     }
                     //if the winning player is 2
                     else if (winningPlayer == 2) {
                        currScore = Integer.parseInt(playerTwo.getText());
                        currScore++;
                        playerTwo.setText("" + currScore);
                        resetBoard();                         
                     }                  
                     //if the winning player is 3
                     else if (winningPlayer == 3) {
                        currScore = Integer.parseInt(playerThree.getText());
                        currScore++;
                        playerThree.setText("" + currScore);
                        resetBoard();     
                     }  
                     //if the winning player is 4
                     else if (winningPlayer == 4) {
                        currScore = Integer.parseInt(playerFour.getText());
                        currScore++;
                        playerFour.setText("" + currScore);
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
                  blackCard.setText("<html><center>" + cardmsg + "</center></html>");
               }
               //if its a whiteCard  
               else if (readIn instanceof WhiteCard) {
                  WhiteCard test = (WhiteCard) readIn;
                  //for testing
                  //System.out.println(test.getMessage() + test.getPlayerNumber());
                  //go thru the buttons and see which ones are empty
                  
                  //if empty, set the text
                  if (firstCard.getText().equals("")) {
                     setWhiteText(firstCard, readIn);
                  }
                  else if (secondCard.getText().equals("")) {
                     setWhiteText(secondCard, readIn);
                  }
                  else if (thirdCard.getText().equals("")) {
                     setWhiteText(thirdCard, readIn);
                  }
                  else if (fourthCard.getText().equals("")) {
                     setWhiteText(fourthCard, readIn);
                  }
                  else if (fifthCard.getText().equals("")) {
                     setWhiteText(fifthCard, readIn);
                  }
                  else if(sixthCard.getText().equals("")) {
                     sixthCard.setText(test.getMessage());
                     whiteDeck.add(test);
                     if (turnCount == myNumber) {
                        sixthCard.setEnabled(true);
                     }
                  }
                  else if(seventhCard.getText().equals("")) {
                     seventhCard.setText(test.getMessage());
                     whiteDeck.add(test);
                     if (turnCount == myNumber) {
                        seventhCard.setEnabled(true);
                     }
                  }
                  else if(eighthCard.getText().equals("")) {
                     eighthCard.setText(test.getMessage());
                     whiteDeck.add(test);
                     if (turnCount == myNumber) {
                        eighthCard.setEnabled(true); 
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
            if (choice == firstCard) {
            WhiteCard wc = new WhiteCard(firstCard.getText());
            wc.setPlayerNumber(playerNum);
            Object o = wc;
            try {
               oos.writeObject(o);
               oos.flush();
               firstCard.setText("");
               disable(); 
            }
            catch (IOException ioe) {
               System.out.println("IO Exception");
            }     
         }
         else if (choice == secondCard) {
            WhiteCard wc = new WhiteCard(secondCard.getText());
            wc.setPlayerNumber(playerNum);
            Object o = wc;
            try {
               oos.writeObject(o);
               oos.flush();
               secondCard.setText("");
                disable(); 
            }
            catch (IOException ioe) {
               System.out.println("IO Exception");
            }   
         }
         else if (choice == thirdCard) {
            WhiteCard wc = new WhiteCard(thirdCard.getText());
            wc.setPlayerNumber(playerNum);
            Object o = wc;
            try {
               oos.writeObject(o);
               oos.flush();
               thirdCard.setText("");
                disable(); 
            }
            catch (IOException ioe) {
               System.out.println("IO Exception");
            }    
         }
         else if (choice == fourthCard) {
            WhiteCard wc = new WhiteCard(fourthCard.getText());
            wc.setPlayerNumber(playerNum);
            Object o = wc;
            try {
               oos.writeObject(o);
               oos.flush();
               fourthCard.setText("");
                disable(); 
            }
            catch (IOException ioe) {
               System.out.println("IO Exception");
            }   
         }
         else if (choice == fifthCard) {
            WhiteCard wc = new WhiteCard(fifthCard.getText());
            wc.setPlayerNumber(playerNum);
            Object o = wc;
            try {
               oos.writeObject(o);
               oos.flush();
               fifthCard.setText(""); 
                disable();
            }
            catch (IOException ioe) {
               System.out.println("IO Exception");
            }
         }
         //if 6-8, send the card to the server as the winning card
         else if (choice == sixthCard) {
            if (sixthCard.getText().equals("")) {
               return;
            }
            for (WhiteCard thisCard : whiteDeck) {
               if (thisCard.getMessage().equals(sixthCard.getText())) {
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
         else if (choice == seventhCard) {
            if(seventhCard.getText().equals("")) {
               return;
            }
            for (WhiteCard thisCard : whiteDeck) {
               if (thisCard.getMessage().equals(seventhCard.getText())) {
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
         else if (choice == eighthCard) {
            if (eighthCard.getText().equals("")) {
               return;
            }
            for (WhiteCard thisCard : whiteDeck) {
               if (thisCard.getMessage().equals(eighthCard.getText())) {
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
         firstCard.setEnabled(false);
         secondCard.setEnabled(false);
         thirdCard.setEnabled(false);
         fourthCard.setEnabled(false);
         fifthCard.setEnabled(false);
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
         sixthCard.setText("");
         seventhCard.setText("");
         eighthCard.setText("");
         
         //change turn count
         if (turnCount == 4) {
            turnCount = 1;
         }
         else {
            turnCount++;  
         }
         
         //if the player isnt the cardmaster, enable their cards
         if (turnCount != myNumber) {
            firstCard.setEnabled(true);
            secondCard.setEnabled(true);
            thirdCard.setEnabled(true);
            fourthCard.setEnabled(true);
            fifthCard.setEnabled(true); 
            sixthCard.setEnabled(false);
            seventhCard.setEnabled(false);
            eighthCard.setEnabled(false);  
         }
         if (turnCount == myNumber) {
            sixthCard.setEnabled(true);
            seventhCard.setEnabled(true);
            eighthCard.setEnabled(true);
            JOptionPane.showMessageDialog(null, "You are the CardMaster for this turn.");
         }
      }
   
   }
}