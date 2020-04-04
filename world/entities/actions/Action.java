package world.entities.actions;

import world.entities.Entity;

public abstract class Action {

    private boolean started, finished;
    private Entity parent;

    public final void setParent(Entity e) { this.parent = e; }
    public final Entity getParent() { return this.parent; }

    public abstract void onStart();
    public abstract void onCancel();
    public abstract void onFinish();
    public abstract void update();
    public abstract boolean finished();
    public final void start() { this.onStart(); started = true; }
    public final boolean started() { return started; }

}
