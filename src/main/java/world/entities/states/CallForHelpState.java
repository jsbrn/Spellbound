package world.entities.states;

import assets.definitions.Definitions;
import world.entities.actions.types.SpeakAction;
import world.entities.actions.types.WaitAction;

import java.util.Random;

public class CallForHelpState extends State {

    Random rng;

    public CallForHelpState() {
        this.rng = new Random();
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void update() {
        if (getParent().getActionQueue().isEmpty()) {
            getParent().getActionQueue().queueAction(new SpeakAction(Definitions.getDialogue("help_messages").getRandomText()));
            getParent().getActionQueue().queueAction(new WaitAction(2000 + rng.nextInt(3000)));
        }
    }

    @Override
    public void onExit() {

    }

}
