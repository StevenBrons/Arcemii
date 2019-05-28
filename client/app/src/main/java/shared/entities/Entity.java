package shared.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import client.view.RenderItem;
import shared.abilities.Ability;
import shared.abilities.Move;

public class Entity implements Serializable {

    private UUID uuid;
    protected transient Move move;
    private ArrayList<Ability> abilities = new ArrayList<>();

    Entity() {
        this.uuid = UUID.randomUUID();

        this.move = new Move();
        abilities.add(move);
    }

    public List<RenderItem> getRenderItem(){
        List<RenderItem> result = new ArrayList<>();
        result.add(new RenderItem("fallback",0,0,0.0,0.0));
        return result;
    }
}
