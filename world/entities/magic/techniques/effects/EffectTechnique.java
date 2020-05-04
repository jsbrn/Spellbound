package world.entities.magic.techniques.effects;

import world.entities.Entity;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.List;
import java.util.stream.Collectors;

public abstract class EffectTechnique extends Technique {

    public final void affectOnce(MagicSource cast) {
        cast.getCollidingEntities().stream()
                .filter(e -> e instanceof HumanoidEntity)
                .map(e -> (HumanoidEntity)e)
                .filter(cast::affects)
                .collect(Collectors.toList())
                .forEach(he -> affectOnce(cast, he));
    }

    public final void affectContinuous(MagicSource cast) {
        cast.getCollidingEntities().stream()
                .filter(e -> e instanceof HumanoidEntity)
                .map(e -> (HumanoidEntity)e)
                .filter(cast::affects)
                .collect(Collectors.toList())
                .forEach(he -> affectContinuous(cast, he));
    }

    public abstract void affectOnce(MagicSource cast, HumanoidEntity e);
    public abstract void affectContinuous(MagicSource cast, HumanoidEntity e);

}
