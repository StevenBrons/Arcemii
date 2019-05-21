package server.general;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplayerServer extends ArcemiiServer {

	public static final int PORT = 26194;

	public MultiplayerServer() {
		System.out.println("Starting multiplayer server...");
		Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket(PORT);
				System.out.println("Server is running at port " + PORT + ".");
				while (true) {
					Socket socket = serverSocket.accept();
					ObjectInputStream pInput = new ObjectInputStream(socket.getInputStream());
					ObjectOutputStream pOutput = new ObjectOutputStream(socket.getOutputStream());
					Client client = new Client(pInput, pOutput);
					gameHandler.addPlayer(client);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		});
		serverThread.start();
	}
}
