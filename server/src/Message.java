package server;

import java.io.Serializable;

public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String type;
	private Object content;

	Message(String type, Object content) {
		assert(type.equals(type.toUpperCase()));
		assert(content instanceof Serializable);
		this.type = type;
		this.content = content;
	}
	
	String getType() {
		return type;
	}
	
	Object getContent() {
		return content;
	}

	public static Message getSwitchMessage(Room r) {
		return new Message("SWTICH", r.getName());
	}
	
}
