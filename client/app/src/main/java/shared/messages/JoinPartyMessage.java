package shared.messages;

public class JoinPartyMessage extends Message {

  private int partyId;

  public JoinPartyMessage(int partyId) {
    this.partyId = partyId;
  }

  public int getPartyId() {
    return partyId;
  }
}
