package shared.messages;


import java.util.ArrayList;
import shared.entities.Entity;

/**
 * This class can be used to verify the connection between the client and server.
 * @author Bram Pulles
 */
public class GameUpdateMessage extends Message {

	private ArrayList<Entity> changes;
	private long createTime;

	public GameUpdateMessage(ArrayList<Entity> changes){
		this.changes = changes;
		createTime = System.currentTimeMillis();
	}

	public ArrayList<Entity> getChanges() {
		return changes;
	}

	public long getCreateTime() {
		return createTime;
	}
}
