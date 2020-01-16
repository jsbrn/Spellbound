package world.entities.states;

public abstract class State {

    abstract void onEnter();
    abstract void update();
    abstract void onExit();

}
