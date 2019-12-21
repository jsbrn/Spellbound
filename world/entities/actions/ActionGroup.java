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
        Action a = actions.get(0);
        if (!a.started()) a.start();
        if (a.finished()) { actions.remove(0); return; }
        a.update();
    }

    public void setParent(Entity parent) {
        for (Action a: actions) a.setParent(parent);
    }

    public boolean finished() {
        return actions.isEmpty();
    }

    public String debug() {
        String d = "";
        for (Action a: actions) d += a.toString();
        return d;
    }

}
