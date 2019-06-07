package shared.messages;

import java.io.Serializable;

/**
 * Instances of this class are sent between the server and the client, providing getType method and ensuring all classes that are sent are of valid types
 * @author Steven Bronsveld
 */
public class Message implements Serializable {

  public static final long serialVersionUID = 1L;

  public String getType() {
    return this.getClass().getSimpleName();
  }
}
