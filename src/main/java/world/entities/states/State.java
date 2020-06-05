package world.entities.states;

import world.entities.Entity;

public abstract class State {

    private Entity parent;

    public abstract void onEnter();
    public abstract void update();
    public abstract void onExit();

    public final void setParent(Entity e) {
        parent = e;
    }

    public final Entity getParent() { return parent; }

}
