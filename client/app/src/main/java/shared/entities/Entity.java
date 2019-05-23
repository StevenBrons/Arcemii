package shared.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;

public class Entity implements Serializable {
    public List<RenderItem> getRenderItem(){
        List<RenderItem> result = new ArrayList<>();
        result.add(new RenderItem("fallback",0,0,0.0,0.0));
        return result;
    }
}
