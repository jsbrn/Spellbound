package world.entities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import world.*;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import world.entities.actions.ActionQueue;
import world.entities.animations.Animation;
import world.entities.animations.AnimationLayer;
import world.entities.states.State;
import world.entities.types.Chest;
import world.entities.types.SpikeTrap;
import world.entities.types.WishingWell;
import world.entities.types.humanoids.npcs.Collector;
import world.entities.types.humanoids.enemies.Zombie;
import world.entities.types.humanoids.enemies.Bandit;
import world.entities.types.humanoids.npcs.Civilian;
import world.entities.types.humanoids.npcs.LostCivilian;
import world.entities.types.humanoids.npcs.Roommate;
import world.events.EventDispatcher;
import world.events.event.EntityChangeRegionEvent;
import world.events.event.EntityCollisionEvent;
import world.sounds.SoundEmitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Entity {

    private Mover mover;
    private Location location;
    private State currentState;

    private String conversationStartingPoint;
    private LinkedHashMap<String, AnimationLayer> animationLayers;
    private LinkedHashMap<String, ActionQueue> actionQueues;

    private LinkedHashMap<String, SoundEmitter> soundEmitters;

    private double radius;
    private boolean isTile;
    private String name;

    private List<Entity> lastTouching;

    public Entity() {
        this.radius = 0.5f;
        this.lastTouching = new ArrayList<>();
        this.actionQueues = new LinkedHashMap<>();
        this.animationLayers = new LinkedHashMap<>();
        this.mover = new Mover();
        this.mover.setParent(this);
        this.conversationStartingPoint = "greeting";
        this.soundEmitters = new LinkedHashMap<>();
    }

    public void update() {

        List<Entity> touching = getLocation().getRegion().getEntities(getLocation().getCoordinates()[0], getLocation().getCoordinates()[1], radius);
        touching.stream().filter(e -> !lastTouching.contains(e)).forEach(e -> EventDispatcher.invoke(new EntityCollisionEvent(this, e)));
        lastTouching = touching;

        if (currentState != null) currentState.update();
        mover.update();
        for (ActionQueue queue: actionQueues.values())
            queue.update();

        for (SoundEmitter emitter: soundEmitters.values()) emitter.update();
    }

    public void addSoundEmitter(String name, SoundEmitter emitter) {
        soundEmitters.put(name, emitter);
    }

    public SoundEmitter getSoundEmitter(String name) {
        return soundEmitters.get(name);
    }

    public void clearAllActions() {
        for (ActionQueue q: actionQueues.values()) q.clearActions();
    }

    public ActionQueue getActionQueue() { return getActionQueue("default"); }

    public ActionQueue getActionQueue(String name) {
        if (actionQueues.containsKey(name)) return actionQueues.get(name);
        actionQueues.put(name, new ActionQueue(this));
        return actionQueues.get(name);
    }

    public void enterState(State state) {
        clearAllActions();
        exitState();
        currentState = state;
        if (currentState != null) {
            currentState.setParent(this);
            currentState.onEnter();
        }
    }

    public State getCurrentState() { return currentState; }

    public void exitState() {
        if (currentState != null) currentState.onExit();
        currentState = null;
    }

    public void addAnimation(String layer, String name, Animation a) {
        if (animationLayers.get(layer) == null)
            animationLayers.put(layer, new AnimationLayer());
        animationLayers.get(layer).addAnimation(name, a);
    }

    public AnimationLayer getAnimationLayer(String layer) { return animationLayers.get(layer); }
    public Collection<AnimationLayer> getAnimationLayers() { return animationLayers.values(); }

    public Mover getMover() {
        return mover;
    }

    public Location getLocation() { return location; }

    public List<Entity> getNearbyEntities(double radius) {
        return getLocation().getRegion().getEntities(
                getLocation().getCoordinates()[0],
                getLocation().getCoordinates()[1],
                radius);
    }

    public double canSee(Entity e) {
        return canSee(
            (int)e.getLocation().getCoordinates()[0],
            (int)e.getLocation().getCoordinates()[1]);
    }

    public double canSee(int wx, int wy) {
        double[] coords = getLocation().getCoordinates();
        int dist = 1;
        int range = 1 + (int)MiscMath.distance(coords[0], coords[1], wx, wy);
        double visibility = 1;
        double angle = MiscMath.angleBetween((int)coords[0], (int)coords[1], wx, wy);
        double[] offset;
        while (dist < range) {
            offset = MiscMath.getRotatedOffset(0, -dist, angle);
            byte[] tile = getLocation().getRegion().getTile((int)(coords[0] + offset[0]), (int)(coords[1] + offset[1]));
            if ((int)(coords[0] + offset[0]) == (int)wx && (int)(coords[1] + offset[1]) == (int)wy) break;
            if (Tiles.getTransparency(tile[0]) == 0 || Tiles.getTransparency(tile[1]) == 0) { visibility = 0; break; }
            visibility *= (Tiles.getTransparency(tile[0]) * Tiles.getTransparency(tile[1]));
            dist++;
        }
        return visibility;
    }

    public void moveTo(Location new_) {
        Region old = null;
        if (location != null && location.getRegion() != null) {
            location.getRegion().removeEntity(this);
            old = location.getRegion();
        }
        location = new_;
        location.getRegion().addEntity(this);
        if (!location.getRegion().equals(old))
            EventDispatcher.invoke(new EntityChangeRegionEvent(old, new_.getRegion(), this));
    }

    public void draw(float osx, float osy, float scale) {
        draw(osx, osy, scale, (location.getLookDirection() + (360 / 16)) / (360 / 8));
    }

    public void draw(float osx, float osy, float scale, int direction) {
        for (AnimationLayer animationLayer: animationLayers.values()) {
            if (animationLayer.isEnabled() == false) continue;
            Animation anim = animationLayer.getAnimationByName(animationLayer.getCurrentAnimation());
            if (anim != null)
                anim.draw(
                        isTile ? (int)osx : osx,
                        isTile ? (int)osy : osy,
                        scale,
                        direction,
                        animationLayer.getColor());
        }
    }

    public void drawDebug(float scale, Graphics g) {

        float osc[] = Camera.getOnscreenCoordinates(getLocation().getCoordinates()[0], getLocation().getCoordinates()[1], scale);

        float tosc[] = Camera.getOnscreenCoordinates(getMover().getTarget()[0], getMover().getTarget()[1], scale);

        g.setColor(Color.blue);
        g.drawOval(osc[0] - (float)(Chunk.TILE_SIZE * scale * radius), osc[1]- (float)(Chunk.TILE_SIZE * scale * radius), (int)(Chunk.TILE_SIZE * scale * radius * 2), (int)(Chunk.TILE_SIZE * scale * radius * 2));

        g.setColor(Color.cyan);
        if (!getMover().isIndependent()) {
            g.drawLine( osc[0], osc[1], tosc[0], tosc[1]);
        } else {
            g.drawLine(osc[0], osc[1], tosc[0], osc[1]);
            g.drawLine(osc[0], osc[1], osc[0], tosc[1]);
        }

        g.setColor(Color.red);
        double[] offsets = MiscMath.getRotatedOffset(0, -Chunk.TILE_SIZE * scale, location.getLookDirection());
        g.drawLine(
                osc[0],
                osc[1],
                (float)(osc[0] + offsets[0]),
                (float)(osc[1] + offsets[1]));

        g.setColor(Color.yellow);
        for (Location l: getMover().getPath()) {
            float[] losc = Camera.getOnscreenCoordinates(l.getCoordinates()[0], l.getCoordinates()[1], Window.getScale());
            g.drawRect(losc[0] - Window.getScale(), losc[1] - Window.getScale(), 2*Window.getScale(), 2*Window.getScale());
        }

        g.setColor(Color.white);
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
    public double getRadius() {
        return radius;
    }
    public void setIsTile(boolean tile) {
        isTile = tile;
    }
    public boolean isTile() {
        return isTile;
    }
    public void setConversationStartingPoint(String dialogue_id) {
        this.conversationStartingPoint = dialogue_id;
    }
    public String getConversationStartingPoint() { return conversationStartingPoint; }

    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        serialized.put("x", getLocation().getCoordinates()[0]);
        serialized.put("y", getLocation().getCoordinates()[1]);
        serialized.put("rotation", getLocation().getLookDirection());
        serialized.put("region", getLocation().getRegion().getName());
        serialized.put("dialogue", conversationStartingPoint);
        serialized.put("type", getClass().getSimpleName());
        serialized.put("anim_layers", animationLayers.entrySet().stream().map(layer -> {
            JSONObject jsonLayer = new JSONObject();
            Color color = layer.getValue().getColor();
            jsonLayer.put("name", layer.getKey());
            jsonLayer.put("current", layer.getValue().getBaseAnimation());

            JSONArray jsonColor = new JSONArray();
            jsonColor.add(color.getRed());
            jsonColor.add(color.getGreen());
            jsonColor.add(color.getBlue());
            jsonColor.add(color.getAlpha());
            jsonLayer.put("color", jsonColor);
            return jsonLayer;
        }).collect(Collectors.toList()));
        return serialized;
    }

    public void deserialize(JSONObject json) {
        conversationStartingPoint = (String)json.get("dialogue");
        for (Object layers: (JSONArray)json.get("anim_layers")) {
            JSONObject jsonLayer = (JSONObject)layers;
            String layerName = (String)jsonLayer.get("name");
            getAnimationLayer(layerName).setBaseAnimation((String)jsonLayer.get("current"));

            JSONArray jsonColor = (JSONArray)jsonLayer.get("color");
            getAnimationLayer(layerName).setColor(new Color(
                    (int)(long)jsonColor.get(0),
                    (int)(long)jsonColor.get(1),
                    (int)(long)jsonColor.get(2),
                    (int)(long)jsonColor.get(3)));
        }
    }

    public static Entity create(JSONObject json) {
        String type = (String)json.get("type");
        Entity e = null;
        switch (type) {
            case "Zombie": e = new Zombie(); break;
            case "Bandit": e = new Bandit((int)(long)json.get("level")); break;
            case "Chest": e = new Chest((float)(double)json.get("lootMultiplier"), (boolean)json.get("locked"), (int)(long)json.get("lootType"), (float)(double)json.get("filledChance")); break;
            case "Civilian": e = new Civilian(); break;
            case "LostCivilian": e = new LostCivilian(1); break;
            case "SpikeTrap": e = new SpikeTrap(); break;
            case "WishingWell": e = new WishingWell(); break;
            case "Collector": e = new Collector(); break;
            case "Roommate": e = new Roommate(); break;
            default: e = null;
        }
        if (e != null) {
            String region_name = (String)json.get("region");
            e.moveTo(new Location(World.getRegion(region_name), (double)json.get("x"), (double)json.get("y"), (int)(long)json.get("rotation")));
            e.deserialize(json);
        }
        return e;
    }


}
