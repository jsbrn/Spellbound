package world.entities.ai.actions;

import world.entities.Entity;

import java.util.ArrayList;

public class ActionQueue {

    private Entity parent;
    private ArrayList<ActionGroup> actionQueue;

    public ActionQueue(Entity parent) {
        this.parent = parent;
        this.actionQueue = new ArrayList<>();
    }

    public void update() {
        if (actionQueue.isEmpty()) return;
        ActionGroup action = actionQueue.get(0);
        action.update();
        if (action.finished()) actionQueue.remove(action);
    }

    public void queueActions(ActionGroup actions) {
        this.actionQueue.add(actions);
        actions.setParent(parent);
    }

    public void queueAction(Action a) {
        queueActions(new ActionGroup(a));
    }

    public Action getCurrentAction() { return actionQueue.isEmpty() ? null : actionQueue.get(0).getCurrentAction(); }

    public void cancelCurrentAction() {
        if (actionQueue.isEmpty()) return;
        actionQueue.remove(0).getCurrentAction().onCancel();
    }

    public void clearActions() {
        cancelCurrentAction();
        actionQueue.clear();
    }

    public boolean isEmpty() { return actionQueue.isEmpty(); }

}
