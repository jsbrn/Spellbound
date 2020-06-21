package world.entities.ai.states;

import world.entities.Entity;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.List;
import java.util.stream.Collectors;

public class PatrolState extends IdleState {

    private int radius;

    public PatrolState(int radius) {
        this.radius = radius;
    }

    public void update() {
        super.update();

        List<Entity> nearby = getParent().getNearbyEntities(radius).stream().filter(e -> {
            if (!(e instanceof HumanoidEntity) || getParent().canSee(e) < 0.5f) return false;
            HumanoidEntity he = ((HumanoidEntity)e);
            return !he.isDead() && !he.isAlliedTo((HumanoidEntity)getParent());
        }).collect(Collectors.toList());

        if (nearby.isEmpty()) return;
        getParent().enterState(new AttackState(nearby.get(0), 1, 8, 4));
    }

}
