package world.entities.actions;

import world.entities.Entity;

import java.util.ArrayList;

public final class ActionGroup {

    private ArrayList<Action> actions;

    public ActionGroup() {
        this.actions = new ArrayList<>();
    }

    public ActionGroup(Action a) {
        this();
        this.add(a);
    }

    public void add(Action a) {
        this.actions.add(a);
    }

    public void update() {
        if (actions.isEmpty()) return;
        if (!actions.get(0).started()) actions.get(0).start();
        actions.get(0).update();
        if (actions.get(0).finished()) actions.remove(0);
    }

    public void setParent(Entity parent) {
        for (Action a: actions) a.setParent(parent);
    }

    public boolean finished() {
        return actions.isEmpty();
    }

}
