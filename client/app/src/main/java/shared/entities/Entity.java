package shared.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import client.view.RenderItem;
import shared.abilities.Ability;
import shared.abilities.Move;
import shared.general.Level;
import shared.tiles.Tile;

public abstract class Entity implements Serializable {

    private UUID uuid;
    protected transient Move move;
    protected transient ArrayList<Ability> abilities = new ArrayList<>();
    protected transient ArrayList<Ability> actions = new ArrayList<>();
    private transient boolean changed = false;
    private transient double hitbox = 1;
    private boolean isDead = false;

    protected double xPos = 0, yPos = 0, xVel = 0, yVel = 0;

    Entity(double x, double y) {
        this.uuid = UUID.randomUUID();
        this.xPos = x;
        this.yPos = y;

        this.move = new Move();
        abilities.add(move);
    }

    /**
     * @return a fallback RenderItem for Entities that haven't defined this function themselves
     * @author Jelmer Firet
     */
    public List<RenderItem> getRenderItem(){
        List<RenderItem> result = new ArrayList<>();
        result.add(new RenderItem("fallback",(int)(Tile.WIDTH*xPos), -(int)(Tile.HEIGHT*yPos),0.0,0.0,2));
        return result;
    }

    public boolean isDead() {
        return isDead;
    }

    public void destroy() {
        isDead = true;
        setChanged(true);
    }

    public void executeAll(Level level) {
        for (Ability a : actions) {
            a.execute(level,this);
        }
    }

    /**
     * Invoke all actions for this entity (based on AI or entity mechanics)
     * @param level
     */
    public abstract void invokeAll(Level level);

    public void invoke(Ability ability) {
        this.actions.add(ability);
    }

    public double getX() {
      return this.xPos;
    }

    public double getY() {
      return this.yPos;
    }

    public double getVelX() {
      return this.xVel;
    }

    public double getVelY() {
      return this.yVel;
    }

    public ArrayList<Ability> getAbilities(){return abilities;}

    public void setPos(double x, double y) {
      this.xPos = x;
      this.yPos = y;
      setChanged(true);
    }

    public void setVel(double x, double y) {
        this.xVel = x;
        this.yVel = y;
        setChanged(true);
    }

    public boolean isChanged() {
      return changed;
    }

    public void setChanged(boolean changed) {
      this.changed = changed;
    }

    public boolean intersects(Entity e) {
      return Math.pow(e.getX() - getX(),2) + Math.pow(e.getY() - getY(),2) < Math.pow(e.getHitbox() + getHitbox(),2);
    }

    public boolean intersects(double x,double y, double width, double height) {
      return Math.pow(x - getX(),2) + Math.pow(y - getY(),2) < Math.pow(getHitbox(),2) &&
              Math.pow(x + width - getX(),2) + Math.pow(y - getY(),2) < Math.pow(getHitbox(),2) &&
              Math.pow(x - getX(),2) + Math.pow(y + height - getY(),2) < Math.pow(getHitbox(),2) &&
              Math.pow(x + width - getX(),2) + Math.pow(y + height - getY(),2) < Math.pow(getHitbox(),2);
    }

    public double getHitbox() {
      return hitbox;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Entity) {
            return this.uuid.equals(((Entity) obj).getUUID());
        }
        return false;
    }
}
