package server;

import java.util.ArrayList;

import shared.entities.Player;
import shared.messages.Message;

public class ServerGameHandler {

  ArrayList<Player> players = new ArrayList<>();

  ServerGameHandler() {

  }

  public void addPlayer(final Player player) {
    if (!players.contains(player)) {
      players.add(player);
    }
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            Message m = (Message) player.getInputStream().readObject();
            handlePlayerInput(m, player);
          } catch (Exception e) {
          }
        }
      }
    });
    thread.start();
  }

  private void handlePlayerInput(Message m, Player player) {
    switch (m.getType()) {
      case "CreatePartyMessage":
        System.out.println("Jeee");
        break;
    }
  }
}
