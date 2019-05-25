package shared.messages;

/**
 * This class can be used to send a new name for a player to the server.
 * Or to request a name by the server during connection initialization.
 * @author Bram Pulles
 */
public class PlayerInfoMessage extends Message {

	private String name;

	public PlayerInfoMessage(){
	}

	public PlayerInfoMessage(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
