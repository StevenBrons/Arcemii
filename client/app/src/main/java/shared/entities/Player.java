package shared.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLInput;

import shared.general.Level;
import shared.messages.Message;

public class Player extends Entity{

    transient ObjectInputStream input;
    transient ObjectOutputStream output;

    public Player () {
    }

    public Player (ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Send message to this player.
     * @param m message
     */
    public void sendMessage(Message m) {
        try {
            output.writeObject(m);
            output.flush();
        } catch (IOException e) {
        }
    }
    public ObjectInputStream getInputStream() {
        return input;
    }
}
