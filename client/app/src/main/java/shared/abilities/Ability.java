package shared.abilities;

import java.io.Serializable;

import shared.entities.Entity;
import shared.general.Level;

public abstract class Ability implements Serializable {

    public static final long serialVersionUID = 1L;

    public abstract boolean execute(Level level, Entity self);

    public abstract Ability invoke(double direction);

}
