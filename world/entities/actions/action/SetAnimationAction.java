package world.entities.actions.action;

import world.entities.actions.Action;

public class SetAnimationAction extends Action {

    private String anim;

    public SetAnimationAction(String name) {
        this.anim = name;
    }

    @Override
    public void start() {
        getParent().setAnimation(anim);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return true;
    }

}
