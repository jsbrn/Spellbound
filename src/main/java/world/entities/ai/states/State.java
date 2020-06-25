package world.entities.ai.states;

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
