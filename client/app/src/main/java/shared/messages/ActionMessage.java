package shared.messages;

import android.util.Log;

import java.util.ArrayList;

import shared.abilities.Ability;

public class ActionMessage extends Message {

  ArrayList<Ability> actions;

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
