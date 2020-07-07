package world.entities.components.magic;

import com.github.mathiewz.slick.Color;
import misc.Location;
import misc.MiscMath;
import network.MPServer;
import org.json.simple.JSONObject;
import world.Tiles;
import world.entities.Entities;
import world.entities.components.Component;
import world.entities.components.LocationComponent;
import world.events.event.MagicDepletedEvent;
import world.events.event.MagicImpactEvent;
import world.entities.components.magic.techniques.Technique;
import world.entities.components.magic.techniques.Techniques;
import world.particles.ParticleSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MagicSourceComponent extends Component {

    private ArrayList<Technique> techniques;

    public boolean hasTechnique(String technique) {
        return techniques.stream().anyMatch(t -> t.getID().equals(technique));
    }

    public int getLevel(String technique) {
        List<Technique> matches = techniques.stream().filter(t -> t.getID().equals(technique)).collect(Collectors.toList());
        return matches.isEmpty() ? 0 : matches.get(0).getLevel();
    }

    /*public List<Integer> getCollidingEntities() {
        List<Integer> inner = body.getLocation().getRegion().getEntityIDs(
                body.getLocation().getCoordinates()[0],
                body.getLocation().getCoordinates()[1],
                body.getReachRadius()
        ), outer = body.getLocation().getRegion().getEntityIDs(
                body.getLocation().getCoordinates()[0],
                body.getLocation().getCoordinates()[1],
                body.getDepthRadius()
        );
        return outer.stream().filter(e -> !(inner.contains(e) && !outer.contains(e))).collect(Collectors.toList());
    }*/

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public JSONObject serialize() {
        return null;
    }

    @Override
    public void deserialize(JSONObject object) {

    }

    @Override
    public String getID() {
        return null;
    }
}
