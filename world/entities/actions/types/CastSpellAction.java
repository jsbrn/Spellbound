package world.entities.actions.types;

import world.entities.magic.Spell;
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
            Spell selected = parent.getSpellbook().getSelectedSpell();
            if (selected == null) return;
            if (parent.getMana() >= selected.getManaCost()) {
                parent.getSpellbook().cast(wx, wy);
                parent.addMana(-selected.getManaCost());
            }
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return true;
    }

    public String toString() { return "CastSpell("+wx+", "+wy+")"; }

}
