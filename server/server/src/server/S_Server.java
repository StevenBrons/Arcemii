package server;

import java.io.IOException;
import java.net.ServerSocket;

public class S_Server {

	public static final int PORT = 10011;
	public RoomHandler roomHandeler = new RoomHandler();
	
	S_Server() {
		Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(PORT);
					System.out.println("Server is running at port " + PORT);
					while (true) {
						Player player = new Player(serverSocket.accept());
						roomHandeler.joinRoom(player, "MENU");
						System.out.println("A new player has joined");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		});
		serverThread.start();
	}

}
