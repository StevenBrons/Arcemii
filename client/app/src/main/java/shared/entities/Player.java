package shared.entities;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLInput;

import shared.general.Level;

public class Player extends Entity{

    transient Level curLevel;
    transient ObjectInputStream input;
    transient ObjectOutputStream output;

    public Player () {

    }

    public Player (ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
    }

    public ObjectInputStream getInputStream() {
        return input;
    }
}
