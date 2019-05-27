package shared.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import client.view.RenderItem;

public class Entity implements Serializable {

    /**
     * @return a fallback RenderItem for Entities that haven't defined this function themselves
     * @author Jelmer Firet
     */
    public List<RenderItem> getRenderItem(){
        List<RenderItem> result = new ArrayList<>();
        result.add(new RenderItem("fallback",0,0,0.0,0.0));
        return result;
    }
}
