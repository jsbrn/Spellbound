package world.entities.ai.actions.types;

import gui.sound.SoundManager;
import misc.MiscMath;
import world.magic.Spell;
import world.entities.types.humanoids.HumanoidEntity;
import world.entities.ai.actions.Action;
import world.events.EventDispatcher;
import world.events.event.CastFailedEvent;

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
                SoundManager.playSound(SoundManager.MAGIC_CAST, (float) MiscMath.random(0.7, 1.0), getParent().getLocation());
            } else {
                EventDispatcher.invoke(new CastFailedEvent((HumanoidEntity)getParent(), selected));
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
