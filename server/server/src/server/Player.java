package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Player {

	private String ID;
	private static long count = 1;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Room currentRoom;
	private boolean isDead = false;

	Player(Socket socket) throws IOException {
		this.ID = "" + count++;
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
		catchInput();
	}

	public void switchRoom(Room r) {
		currentRoom.onLeave(this);
		send(Message.getSwitchMessage(r));
		currentRoom = r;
	}

	public void send(Message m) {
		try {
			output.writeObject(m);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleInput(Message m) {
		currentRoom.input(this, m);
	}

	private void catchInput() {
		Thread clientThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isDead) {
					try {
						Message m = (Message) input.readObject();
						handleInput(m);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
						isDead = true;
					}
				}
			}
		});
		clientThread.start();
	}

	@Override
	public String toString() {
		return "Player#" + ID;
	}
}
