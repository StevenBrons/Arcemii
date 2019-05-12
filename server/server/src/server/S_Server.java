package server;

import java.io.IOException;
import java.net.ServerSocket;

public class S_Server {

	public static final int PORT = 10011;
	
	private RoomHandler roomHandeler = new RoomHandler();
	
	S_Server() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
			while (true) {
				Player player = new Player(serverSocket.accept());
				player.switchRoom(roomHandeler.getMenuRoom());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
