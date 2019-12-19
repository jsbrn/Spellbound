package world.entities.actions.action;

import world.entities.types.humanoids.HumanoidEntity;
import world.entities.actions.Action;

public class CastSpellAction extends Action {

    private double wx, wy;

    public CastSpellAction(double wx, double wy) {
        this.wx = wx;
        this.wy = wy;
    }

    @Override
    public void onStart() {
        if (getParent() instanceof HumanoidEntity) {
            HumanoidEntity parent = ((HumanoidEntity)getParent());
            if (parent.getMana() >= 1) {
                parent.getSpellbook().cast(wx, wy);
                parent.addMana(-1);
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return true;
    }

}
