package shared.messages;

import java.io.Serializable;

public class Message implements Serializable {

  public static final long serialVersionUID = 1L;

  public String getType() {
    return this.getClass().getSimpleName();
  }
}
