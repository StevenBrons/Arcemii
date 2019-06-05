package shared.messages;

/**
 * Used to send to the server if the player is ready in lobby or not.
 * @author Bram Pulles
 */
public class ReadyMessage extends Message {

	private boolean ready;

	public ReadyMessage(boolean ready){
		this.ready = ready;
	}

	public boolean isReady(){
		return ready;
	}
}
