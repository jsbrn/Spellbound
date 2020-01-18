package world.entities.actions.action;

import world.entities.actions.Action;
import world.entities.types.humanoids.HumanoidEntity;

public class WaitAction extends Action {

    private long start;
    private int mills;

    public WaitAction(int mills) {
        this.mills = mills;
    }

    @Override
    public void onStart() {
        this.start = System.currentTimeMillis();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return System.currentTimeMillis() >= start + mills;
    }

    public String toString() { return "Wait("+mills+")"; }

}
