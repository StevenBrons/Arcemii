package shared.general;

import java.io.Serializable;
import java.util.ArrayList;

import shared.entities.Player;

public class Party implements Serializable {

  public static final long serialVersionUID = 1L;

  private ArrayList<Player> players = new ArrayList<>();

  Party () {

  }

}
