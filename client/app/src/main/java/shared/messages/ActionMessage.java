package shared.messages;

import java.util.ArrayList;

import shared.abilities.Ability;

public class ActionMessage extends Message {

  ArrayList<Ability> actions;

  public ActionMessage(ArrayList<Ability> actions) {
    this.actions = actions;
  }

  public ArrayList<Ability> getActions() {
    return actions;
  }
}
