package world.entities.actions;

import world.entities.Entity;

public abstract class Action {

    private boolean finished;
    private Entity parent;

    public void setParent(Entity e) { this.parent = e; }
    public Entity getParent() { return this.parent; }

    public void setFinished(boolean f) { this.finished = true; }

    public abstract void init();
    public abstract void update();
    public abstract void stop();
    public abstract boolean finished();

}
