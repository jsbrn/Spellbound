package world.entities.actions.action;

import world.World;
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
        this.start = World.getCurrentTime();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return World.getCurrentTime() >= start + mills;
    }

    public String toString() { return "Wait("+mills+")"; }

}
