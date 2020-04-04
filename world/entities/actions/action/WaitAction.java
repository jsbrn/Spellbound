package world.entities.actions.action;

import world.World;
import world.entities.actions.Action;

public class WaitAction extends Action {

    private long start;
    private int mills;

    public WaitAction(int mills) {
        this.mills = mills;
    }

    @Override
    public void onStart() {
        this.start = getParent().getLocation().getRegion().getCurrentTime();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return getParent().getLocation().getRegion().getCurrentTime() >= start + mills;
    }

    @Override
    public void onFinish() {
        
    }

    public String toString() { return "Wait("+mills+")"; }

}
