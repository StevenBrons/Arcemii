package shared.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import shared.messages.Message;

public class Player extends Entity{

	private String name = "Player#" + (int)(Math.random()*99999);
	private transient ObjectInputStream input;
	private transient ObjectOutputStream output;

	public Player () {
	}

	public Player (ObjectInputStream input, ObjectOutputStream output) {
		this.input = input;
		this.output = output;
	}

	/**
	 * Send message to this player.
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

	/**
	 * Set the name of the player.
	 * @param name
	 * @author Bram Pulles
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * @return name of the player.
	 * @author Bram Pulles
	 */
	public String getName(){
		return name;
	}
}
