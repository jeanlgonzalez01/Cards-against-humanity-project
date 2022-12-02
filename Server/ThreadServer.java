package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;

import Cards.Deck;
import Cards.Hand;
import Cards.Deck.Color;

public class ThreadServer implements Runnable{

	private Socket socket;

	private int playerId;
	
	private Object threadObject = null;

	protected int playerNumber;
	
	private ArrayList <String> playersNames = new ArrayList<String>();

	private Vector <ObjectOutputStream> players = new Vector <ObjectOutputStream> ();

	private int turn = 1;

	public ThreadServer(Socket socket, int playerId) {
		this.socket = socket;
		this.playerId = playerId;
	}

	public int getPlayerID() {
		return playerId;
	}

	@Override
	public void run() {
		InputStream in;
		OutputStream out;
		String playerMessage;
		String playerName = null;

		try {
			out = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			in = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(in);

			synchronized(players) {
				players.add(oos);
			}

			Deck whiteDeck = new Deck(Color.WHITE);
			whiteDeck.shuffle();

			synchronized(players) {
				for(int i = 0; i < 7; i++) {
					Hand hand = new Hand();
					hand.addCard(whiteDeck.dealCard());
				}
			}
			oos.writeObject((Integer) playerId);
			oos.flush();
			
			while((threadObject = ois.readObject()) != null) {
				if(threadObject instanceof String) {
					if(playerName == null) {
						playerName = (String)threadObject;
						playersNames.add(playerName);
						System.out.printf("Player Number: %d : %s :", playerId, playerName);
					}
					else {
						playerMessage = (String)threadObject;
						System.out.println("Client Message: " + playerMessage);
						for(ObjectOutputStream player: players) {
							player.writeObject("Player number: " + playerId + " : " + playerName + " : " + playerMessage);
							player.flush();
						}
					}
				}
			}
		}
		catch( SocketException se ) { 
			System.out.println("Client Disconnected...");
			playerName = null; 
		}
		catch( IOException e ) { 
			System.out.println("Player : " + playerId + " disconnected...");
			playerName = null;  
		}
		catch(ClassNotFoundException exception){
            System.out.println("Class Not Found...");
         }
	}

}
