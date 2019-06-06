package server.general;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import shared.entities.Player;
import shared.general.Party;

public class Server {

	public static final int PORT = 26193;
	private boolean running;
	private boolean starting = true;

	private ServerSocket serverSocket;
	private ServerGameHandler gameHandler;

	/**
	 * Start a new server.
	 * @param gameHandler
	 * @author Bram Pulles
	 * @author Steven Bronsveld
	 */
	public Server(final ServerGameHandler gameHandler) {
		Console.log(ConsoleTag.CONNECTION, "Starting server...");
		running = true;
		this.gameHandler = gameHandler;

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(PORT);
					Console.log(ConsoleTag.CONNECTION, "Server is running at port " + PORT + ".");

					starting = false;
					while (isRunning()) {
						Socket socket = serverSocket.accept();

						InetAddress ip = socket.getInetAddress();
						checkUniqueness(ip);

						ObjectInputStream playerInput = new ObjectInputStream(socket.getInputStream());
						ObjectOutputStream playerOutput = new ObjectOutputStream(socket.getOutputStream());
						Player player = new Player(ip, playerInput, playerOutput);
						gameHandler.addPlayer(player);
					}
				} catch (IOException e) {
					if(e.getMessage().equals("Socket closed") || e instanceof java.net.BindException){
					}else{
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	/**
	 * Stop the server thread. This thread listens for new connections from clients.
	 * @author Bram Pulles
	 */
	public synchronized void stop(){
		running = false;

		if(!serverSocket.isClosed()) {
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Check if this ip address is already in use by another player in the server.
	 * If so then this other player will be set to not unique and consequently
	 * be removed from the server.
	 * This will not be done for emulators.
	 * @param ip
	 */
	private void checkUniqueness(InetAddress ip){
		List<Party> parties = gameHandler.getParties();
		for(Party party : parties){
			for(Player player : party.getPlayers()){
				try{
					if(!player.getIp().equals(InetAddress.getLoopbackAddress()) && player.getIp().equals(ip)){
						player.setNotUnique();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return if this server is running (waiting for connections from clients).
	 * @author Bram Pulles
	 */
	private synchronized boolean isRunning(){
		return running;
	}

	/**
	 * @return if this server is starting (booting up).
	 * @author Bram Pulles
	 */
	public synchronized boolean isStarting(){
		return starting;
	}
}
