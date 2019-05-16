package shared.general;

import java.io.Serializable;
import java.util.ArrayList;

import shared.entities.Player;
import shared.messages.Message;
import shared.messages.UpdatePartyMessage;

public class Party implements Serializable {

  public static final long serialVersionUID = 1L;

  private ArrayList<Player> players = new ArrayList<>();

  Party () {
  }

  void addPlayer(Player player) {
    if (!players.contains(player)) {
      players.add(player);
    }
    messageAll(new UpdatePartyMessage(-1,null)); //params todo
  }

  void messageAll(Message message) {
    for (Player p : players) {
      p.sendMessage(message);
    }
  }

}
