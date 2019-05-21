package server.general;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import shared.entities.Player;
import shared.messages.Message;

public class Client {

	private Player player;
	private transient ObjectInputStream input;
	private transient ObjectOutputStream output;

	public Client() {
	}

	public Client(ObjectInputStream input, ObjectOutputStream output) {
		this.input = input;
		this.output = output;
	}

	/**
	 * Send message to this client.
	 * @param m message
	 */
	public void sendMessage(Message m) {
		try {
			output.writeObject(m);
			output.reset();
			output.flush();
		} catch (IOException e) {
		}
	}
	public ObjectInputStream getInputStream() {
		return input;
	}

	public Player getPlayer() {
		return player;
	}


}
