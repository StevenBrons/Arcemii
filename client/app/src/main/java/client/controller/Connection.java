package client.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.general.ArcemiiServer;
import server.general.SinglePlayerServer;
import shared.messages.Message;

public class Connection {

	private static final String hostName = "10.0.2.2";
	private static final int PORT = 26194;
	public static boolean isConnected = false;

	private static ObjectInputStream input;
	private static ObjectOutputStream output;

	public Connection(final boolean singlePlayer) {
		if (isConnected) return;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Connecting to server...");
				if (singlePlayer) {
					ArcemiiServer.main(new String[]{"singleplayer"});
					input = ((SinglePlayerServer) ArcemiiServer.server).getInputStream();
					output = ((SinglePlayerServer) ArcemiiServer.server).getOutputStream();
					System.out.println("Connected to singleplayer server.");
					isConnected = true;
				} else {
					try {
						Socket clientSocket = new Socket(hostName, PORT);
						output = new ObjectOutputStream(clientSocket.getOutputStream());
						input = new ObjectInputStream(clientSocket.getInputStream());
						System.out.println("Connected to multiplayer server.");
						isConnected = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	public ObjectInputStream getInputStream() {
		return input;
	}

	private ObjectOutputStream getOutputStream() {
		System.out.println("Output stream: " + output);
		return output;
	}

	/**
	 * Send a message to the server from the client.
	 * @param msg message
	 */
	public void sendMessage(final Message msg) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					getOutputStream().writeObject(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
}
