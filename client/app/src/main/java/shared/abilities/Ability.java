package shared.abilities;

import java.io.Serializable;

import shared.entities.Entity;
import shared.general.Level;

public interface Ability extends Serializable {

	boolean execute(Level level, Entity self);

	default boolean available(Level level, Entity self){
		return true;
	}

	/**
	 * @return the name of this ability.
	 * @author Bram Pulles
	 */
	default String getName(){
		return "This ability has no name yet.";
	}

	/**
	 * @return a description of the ability.
	 * @author Bram Pulles
	 */
	default String getDescription(){
		return "This ability has no description yet.";
	}

	default String getId(){return "No_Id";}
}
