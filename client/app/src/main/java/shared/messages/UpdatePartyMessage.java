package shared.messages;

import java.util.ArrayList;

import shared.entities.Player;

public class UpdatePartyMessage extends Message{

  private int partyId;
  private ArrayList<Player> players;

  public UpdatePartyMessage(int partyId, ArrayList<Player> players) {
    this.partyId = partyId;
    this.players = players;
  }

  int getPartyId() {
    return partyId;
  }

  ArrayList<Player> getPlayers() {
    return players;
  }
}
