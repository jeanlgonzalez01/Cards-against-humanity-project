package Server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import Cards.Deck;
import Cards.Deck.Color;

public class Server {
	
	private final int port = 2027;
	
	private final int noConnections = 4;
	
	private Boolean turn = true;
	
	private final int MAX_PLAYER_COUNT = 4;
	
	private int numPlayers;
	
	public void listen() {
		try {
			
			/*Deck blackDeck = new Deck(Color.BLACK);
			blackDeck.shuffle();*/
			
			ServerSocket server = new ServerSocket(port, noConnections);
			
			System.out.println("Server running...");
			System.out.println("Accepting Players...."); //Se tiene que cambiar a JOptionPane or something

			int playerID = 1;
			while(numPlayers < MAX_PLAYER_COUNT) {
				Socket client = server.accept();
				
				Runnable run = new ThreadServer(client, playerID);
				Thread thread = new Thread(run);
				thread.start();
			}
		}
		catch (BindException be) {
			System.out.println("Server already running on this computer, stopping.");
		}
		catch (IOException ioe) {
			System.out.println("Connection Failure");
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.listen();
	}
}
