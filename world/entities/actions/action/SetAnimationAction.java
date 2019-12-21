package world.entities.actions.action;

import world.entities.actions.Action;

public class SetAnimationAction extends Action {

    private String anim;
    private boolean wait;

    public SetAnimationAction(String name, boolean waitUntilFinished) {
        this.anim = name;
        this.wait = waitUntilFinished;
    }

    @Override
    public void onStart() {
        getParent().setAnimation(anim);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return !wait || getParent().getAnimation(anim).finished();
    }

    public String toString() { return "SetAnimation("+anim+", "+wait+")[started = "+started()+", finished = "+finished()+"]"; }

}
