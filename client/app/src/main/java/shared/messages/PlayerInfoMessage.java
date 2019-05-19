package shared.messages;

/**
 * This class can be used to send a new name for a player to the server.
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
