package server.general;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import shared.entities.Player;
import shared.messages.Message;

public class Client implements Serializable {

	public static final long serialVersionUID = 1L;

	private Player player = new Player();
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
			output.flush();
			output.reset();
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
