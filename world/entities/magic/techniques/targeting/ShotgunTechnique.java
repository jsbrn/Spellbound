package world.entities.magic.techniques.targeting;

import world.World;
import world.entities.Entity;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

import java.util.ArrayList;

public class ShotgunTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        ArrayList<Entity> entities = World.getRegion().getEntities(
                (int)cast.getCastCoordinates()[0] - getLevel(),
                (int)cast.getCastCoordinates()[1] - getLevel(),
                getLevel() * 2,
                getLevel() * 2);
        entities.remove(cast.getCaster());
        cast.setTargets(entities);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
