package world.entities.states;

import world.entities.Entity;

public class PatrolState extends IdleState {

    private Entity target;
    private int radius;

    public PatrolState(Entity target, int radius) {
        this.radius = radius;
        this.target = target;
    }

    public void update() {
        super.update();
        if (getParent().canSee(target) > 0.5 && target.getLocation().distanceTo(getParent().getLocation()) < radius)
            getParent().enterState(new AttackState(target, 3, 8, 16));
    }

}
