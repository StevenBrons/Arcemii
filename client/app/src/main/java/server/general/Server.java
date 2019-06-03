package server.general;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import shared.entities.Player;

public class Server {

	public static final int PORT = 26194;
	private boolean running;
	private boolean starting = true;

	private ServerSocket serverSocket;

	public Server(final ServerGameHandler gameHandler) {
		System.out.println("Starting server...");
		running = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(PORT);
					System.out.println("Server is running at port " + PORT + ".");

					starting = false;
					while (isRunning()) {
						Socket socket = serverSocket.accept();
						ObjectInputStream playerInput = new ObjectInputStream(socket.getInputStream());
						ObjectOutputStream playerOutput = new ObjectOutputStream(socket.getOutputStream());
						Player player = new Player(playerInput, playerOutput);
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
	 * @return if this server is running (waiting for connections from clients).
	 * @author Bram Pulles
	 */
	private synchronized boolean isRunning(){
		return running;
	}

	public synchronized boolean isStarting(){
		return starting;
	}
}
