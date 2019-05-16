package shared.abilities;

import java.io.Serializable;

public abstract class Ability implements Serializable {

    public static final long serialVersionUID = 1L;

    abstract String getName();
}
