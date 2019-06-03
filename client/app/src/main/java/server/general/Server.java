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

	public Server(final ServerGameHandler gameHandler) {
		System.out.println("Starting server...");
		running = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(PORT);
					System.out.println("Server is running at port " + PORT + ".");

					while (isRunning()) {
						Socket socket = serverSocket.accept();
						ObjectInputStream playerInput = new ObjectInputStream(socket.getInputStream());
						ObjectOutputStream playerOutput = new ObjectOutputStream(socket.getOutputStream());
						Player player = new Player(playerInput, playerOutput);
						gameHandler.addPlayer(player);
					}
				} catch (IOException e) {
					e.printStackTrace();
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
	}

	/**
	 * @return if this server is running (waiting for connections from clients).
	 * @author Bram Pulles
	 */
	private synchronized boolean isRunning(){
		return running;
	}
}
