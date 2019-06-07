package shared.messages;

import android.util.Log;

import java.util.ArrayList;

import shared.abilities.Ability;

/**
 * A message containing all actions a player wants to execute
 * @author Steven Bronsveld
 */
public class ActionMessage extends Message {

  ArrayList<Ability> actions;

  /**
   *
   * @param actions The actions to send to the server
   */
  public ActionMessage(ArrayList<Ability> actions) {
    if (actions == null) {
      this.actions = new ArrayList<>();
    } else {
      this.actions = new ArrayList<>(actions);
    }
  }

  public ArrayList<Ability> getActions() {
    return actions;
  }

  @Override
  public String toString() {
    return getType() + ": " + getActions();
  }
}
