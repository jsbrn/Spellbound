package world.entities.magic.techniques.effects;

import world.entities.Entity;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.List;

public abstract class EffectTechnique extends Technique {

    public void affectOnce(MagicSource cast) {
        List<Entity> colliding = cast.getCollidingEntities();
        for (Entity e: colliding) {
            if (e instanceof HumanoidEntity) affectOnce(cast, (HumanoidEntity)e);
        }
    }

    public void affectContinuous(MagicSource cast) {
        List<Entity> colliding = cast.getCollidingEntities();
        for (Entity e: colliding) {
            if (e instanceof HumanoidEntity) affectContinuous(cast, (HumanoidEntity)e);
        }
    }

    public abstract void affectOnce(MagicSource cast, HumanoidEntity e);
    public abstract void affectContinuous(MagicSource cast, HumanoidEntity e);

}
