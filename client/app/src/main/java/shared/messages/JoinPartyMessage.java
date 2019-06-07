package shared.messages;

/**
 * This message is sent to the server containing the id of the party the player sending the message wants to join
 * @author Steven Bronsveld
 */
public class JoinPartyMessage extends Message {

  private int partyId;

  public JoinPartyMessage(int partyId) {
    this.partyId = partyId;
  }

  public int getPartyId() {
    return partyId;
  }
}
