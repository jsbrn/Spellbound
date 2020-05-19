package gui.sound;

import assets.Assets;
import misc.Location;
import misc.MiscMath;
import org.lwjgl.openal.AL;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import world.Camera;
import world.World;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.CastFailedEvent;
import world.events.event.EntityChangeRegionEvent;
import world.events.event.HumanoidDeathEvent;

public class SoundManager {

    public static Sound CLICK, DEATH, DOOR_OPEN, SPIKED, DISCOVERY,
            FOREST_AMBIENCE, DUNGEON_AMBIENCE,
            PAGE_TURN, WOOD_STOVE, FOUND_LOOT, FAILED_CAST, LOCKED,
            ZOMBIE_IDLE_1, ZOMBIE_IDLE_2, ZOMBIE_DEATH, ZOMBIE_PAIN, ZOMBIE_BITE,
            MAGIC_CAST, MAGIC_POSITIVE, MAGIC_DASH, IMPACT_1, IMPACT_2, IMPACT_3, FOOTSTEP;

    private static EventListener listener;
    private static Sound backgroundAmbience;

    public static void load(String root) {
        CLICK = Assets.loadSound("click.wav");
        DEATH = Assets.loadSound("death.wav");
        SPIKED = Assets.loadSound("spiked.wav");
        DISCOVERY = Assets.loadSound("discovery.wav");
        PAGE_TURN = Assets.loadSound("page_turn.wav");
        DOOR_OPEN = Assets.loadSound("door_open.wav");
        FOREST_AMBIENCE = Assets.loadSound("forest_ambience.wav");
        DUNGEON_AMBIENCE = Assets.loadSound("dungeon_ambience.wav");
        WOOD_STOVE = Assets.loadSound("wood_stove.wav");
        FOUND_LOOT = Assets.loadSound("found_loot.wav");
        FAILED_CAST = Assets.loadSound("failed_cast.wav");
        LOCKED = Assets.loadSound("locked.wav");
        ZOMBIE_IDLE_1 = Assets.loadSound("zombie_idle_1.wav");
        ZOMBIE_IDLE_2 = Assets.loadSound("zombie_idle_2.wav");
        ZOMBIE_DEATH = Assets.loadSound("zombie_death.wav");
        ZOMBIE_PAIN = Assets.loadSound("zombie_pain.wav");
        ZOMBIE_BITE = Assets.loadSound("zombie_bite.wav");
        MAGIC_CAST = Assets.loadSound("cast1.wav");
        MAGIC_POSITIVE = Assets.loadSound("effects_increase.wav");
        MAGIC_DASH = Assets.loadSound("magic_dash.wav");
        IMPACT_1 = Assets.loadSound("impact1.wav");
        IMPACT_2 = Assets.loadSound("impact2.wav");
        IMPACT_3 = Assets.loadSound("impact3.wav");
        FOOTSTEP = Assets.loadSound("footstep.wav");
    }

    public static void registerEvents() {
        listener = new EventListener()
                .on(HumanoidDeathEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        HumanoidDeathEvent hde = (HumanoidDeathEvent)e;
                        if (hde.getHumanoid().equals(World.getLocalPlayer())) {
                            playSound(DEATH);
                        }
                    }
                })
                .on(EntityChangeRegionEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        EntityChangeRegionEvent ecre = (EntityChangeRegionEvent)e;
                        if (ecre.getEntity().equals(World.getLocalPlayer())) {
                            setBackground(ecre.getTo().getBackgroundAmbience());
                        }
                    }
                })
                .on(CastFailedEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        CastFailedEvent ecre = (CastFailedEvent)e;
                        if (ecre.getCaster().equals(World.getLocalPlayer())) {
                            playSound(FAILED_CAST);
                        }
                    }
                });;

        EventDispatcher.register(listener);
    }

    public static void cleanup() {
        AL.destroy();
    }

    public static void setBackground(Sound s) {
        if (backgroundAmbience != null) backgroundAmbience.stop();
        backgroundAmbience = s;
        if (backgroundAmbience != null) backgroundAmbience.loop();
    }

    public static void stop() {
        if (backgroundAmbience != null) backgroundAmbience.stop();
    }

    public static void resume() {
        if (backgroundAmbience != null && !backgroundAmbience.playing()) backgroundAmbience.loop();
    }


    public static void playSound(Sound s) {
        if (s != null) s.play();
    }

    public static void playSound(Sound s, float volumeMultiplier, Location location) {
        if (s == null) return;
        if (!location.getRegion().equals(World.getLocalPlayer().getLocation().getRegion())) return;
        double xDist = location.getCoordinates()[0] - Camera.getLocation()[0];
        double yDist = location.getCoordinates()[1] - Camera.getLocation()[1];
        double dist = location.distanceTo(Camera.getLocation()[0], Camera.getLocation()[1]);
        float audioX = (int)(xDist / 6) * 0.05f;
        s.playAt((float)MiscMath.random(0.8, 1.2), volumeMultiplier * (1 - (float)MiscMath.clamp((dist / 4) / 24, 0, 1)), audioX, (float)yDist / 6, 0);
    }

}
