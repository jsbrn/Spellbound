package world.entities.types;

import org.newdawn.slick.Input;
import world.Chunk;
import world.World;
import world.entities.HumanoidEntity;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.CastSpellAction;
import world.entities.actions.action.SetAnimationAction;
import world.entities.animations.Animation;
import world.entities.magic.Spell;
import world.entities.magic.Spellbook;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.TechniqueName;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.KeyDownEvent;
import world.events.event.KeyUpEvent;
import world.events.event.MouseReleaseEvent;

public class Player extends HumanoidEntity {

    private Spellbook spellbook;

    public Player() {

        super();

        this.setMaxHP(10);
        this.setMaxMana(10);
        this.setHP(10);
        this.setMana(5);

        this.addAnimation("idle", new Animation("player_idle.png", 2, 1));
        this.addAnimation("walking", new Animation("player_walking.png", 4, 3));
        this.addAnimation("casting", new Animation("player_casting.png", 5, 4));
        this.setAnimation("idle");

        this.spellbook = new Spellbook(this);
        Spell testSpell = new Spell();
        testSpell.addTechnique(Technique.create(TechniqueName.PROPEL));
        testSpell.addTechnique(Technique.create(TechniqueName.RADIATE));
        this.spellbook.addSpell(testSpell);

        Player that = this;
        EventDispatcher.register(new EventListener()
                .on(MouseReleaseEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        MouseReleaseEvent mce = (MouseReleaseEvent)e;
                        ActionGroup actions = new ActionGroup();
                        if (spellbook.getParent().getMana() >= 1) {
                            actions.add(new SetAnimationAction("casting", true));
                            actions.add(new CastSpellAction(mce.getX(), mce.getY()));
                            actions.add(new SetAnimationAction("idle", false));
                            spellbook.getParent().queueActions(actions);
                        }
                    }
                })
                .on(KeyUpEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        int key = ((KeyUpEvent)e).getKey();
                        if (key == Input.KEY_W || key == Input.KEY_A || key == Input.KEY_S || key == Input.KEY_D)
                            that.skipCurrentAction();
                            that.queueAction(new SetAnimationAction("idle", false));
                    }
                })
                .on(KeyDownEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        int key = ((KeyDownEvent)e).getKey();
                        int dx = 0, dy = 0;
                        if (key == Input.KEY_W) {
                            dy = -1;
                        } else if (key == Input.KEY_A) {
                            dx = -1;
                        } else if (key == Input.KEY_S) {
                            dy = 1;
                        } else if (key == Input.KEY_D) {
                            dx = 1;
                        }

                        if ((dx != 0 || dy != 0) && World.getPlayer().getActionQueue().isEmpty()) {
                            that.queueAction(new SetAnimationAction("walking", false));
                            that.move(dx, dy, Chunk.CHUNK_SIZE);
                        }

                    }
                })
        );

    }

    public Spellbook getSpellbook() { return this.spellbook; }

}
