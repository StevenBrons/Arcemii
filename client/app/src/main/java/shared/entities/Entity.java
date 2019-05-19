package shared.entities;

import java.io.Serializable;

import client.view.RenderItem;

public class Entity implements Serializable {
    public RenderItem getRenderItem(){
        return new RenderItem("fallback",0,0,0.0,0.0);
    }
}
