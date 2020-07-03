package gui.sound;

import assets.Assets;
import com.github.mathiewz.slick.Sound;
import misc.Location;
import org.lwjgl.openal.AL;
import world.World;
import events.Event;
import events.EventDispatcher;
import events.EventHandler;
import events.EventListener;
import events.event.CastFailedEvent;
import events.event.EntityChangeRegionEvent;
import events.event.HumanoidDamageEvent;
import events.event.HumanoidDeathEvent;

public class SoundManager {

    private static float BASE_VOLUME_MULTIPLIER = 0.2f;

    public static int IDLE = 0, COMBAT = 1;

    public static Sound CLICK, DEATH, DOOR_OPEN, SPIKED, DISCOVERY,
            FOREST_AMBIENCE, DUNGEON_AMBIENCE,
            PAGE_TURN, WOOD_STOVE, FOUND_LOOT, FAILED_CAST, LOCKED,
            ZOMBIE_IDLE_1, ZOMBIE_IDLE_2, ZOMBIE_DEATH, ZOMBIE_PAIN, ZOMBIE_BITE,
            MAGIC_CAST, MAGIC_POSITIVE, MAGIC_DASH, IMPACT_1, IMPACT_2, IMPACT_3, FOOTSTEP;

    public static Sound MUSIC_IDLE_1, MUSIC_IDLE_2, MUSIC_IDLE_3, MUSIC_IDLE_4, MUSIC_IDLE_5,
        MUSIC_COMBAT_1, MUSIC_COMBAT_2, MUSIC_COMBAT_3, MUSIC_COMBAT_4, MUSIC_COMBAT_5;

    private static EventListener listener;
    private static Sound backgroundAmbience, currentSong;

    private static long lastContextSwitch, lastMusicTime;
    private static int context;

    public static void load(boolean loadMusic) {

        if (loadMusic) {
            MUSIC_IDLE_1 = Assets.loadSound("music/ActThree.ogg");
            MUSIC_IDLE_2 = Assets.loadSound("music/Azimuth.ogg");
            MUSIC_IDLE_3 = Assets.loadSound("music/Azimuth.ogg");
            MUSIC_IDLE_4 = Assets.loadSound("music/TheGreatUnknown.ogg");
            MUSIC_IDLE_5 = Assets.loadSound("music/Transcend.ogg");
            MUSIC_COMBAT_1 = Assets.loadSound("music/HighTension.ogg");
            MUSIC_COMBAT_2 = Assets.loadSound("music/IntenseSuspense.ogg");
            MUSIC_COMBAT_3 = Assets.loadSound("music/ModernCombat.ogg");
            MUSIC_COMBAT_4 = Assets.loadSound("music/NightRunner.ogg");
            MUSIC_COMBAT_5 = Assets.loadSound("music/SuspenseAction.ogg");
        }

        CLICK = Assets.loadSound("click.aif");
        DEATH = Assets.loadSound("death.ogg");
        SPIKED = Assets.loadSound("spiked.ogg");
        DISCOVERY = Assets.loadSound("discovery.ogg");
        PAGE_TURN = Assets.loadSound("page_turn.ogg");
        DOOR_OPEN = Assets.loadSound("door_open.ogg");
        FOREST_AMBIENCE = Assets.loadSound("forest_ambience.ogg");
        DUNGEON_AMBIENCE = Assets.loadSound("dungeon_ambience.ogg");
        WOOD_STOVE = Assets.loadSound("wood_stove.ogg");
        FOUND_LOOT = Assets.loadSound("found_loot.ogg");
        FAILED_CAST = Assets.loadSound("failed_cast.ogg");
        LOCKED = Assets.loadSound("locked.ogg");
        ZOMBIE_IDLE_1 = Assets.loadSound("zombie_idle_1.ogg");
        ZOMBIE_IDLE_2 = Assets.loadSound("zombie_idle_2.ogg");
        ZOMBIE_DEATH = Assets.loadSound("zombie_death.ogg");
        ZOMBIE_PAIN = Assets.loadSound("zombie_pain.ogg");
        ZOMBIE_BITE = Assets.loadSound("zombie_bite.ogg");
        MAGIC_CAST = Assets.loadSound("cast1.ogg");
        MAGIC_POSITIVE = Assets.loadSound("effects_increase.ogg");
        MAGIC_DASH = Assets.loadSound("magic_dash.ogg");
        IMPACT_1 = Assets.loadSound("impact1.ogg");
        IMPACT_2 = Assets.loadSound("impact2.ogg");
        IMPACT_3 = Assets.loadSound("impact3.ogg");
        FOOTSTEP = Assets.loadSound("footstep.ogg");
    }

    public static void registerEvents() {
        listener = new EventListener()
                .on(HumanoidDeathEvent.class, new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        HumanoidDeathEvent hde = (HumanoidDeathEvent)e;
                        if (hde.getHumanoid().equals(World.getLocalPlayer())) {
                            playSound(DEATH);
                            switchContext(IDLE, true);
                        }
                    }
                })
                .on(HumanoidDamageEvent.class, new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        HumanoidDamageEvent hde = (HumanoidDamageEvent)e;
                        if (hde.getHumanoid().equals(World.getLocalPlayer())) {
                            if (context == IDLE) stopMusic();
                            switchContext(COMBAT, false);
                        }
                    }
                })
                .on(EntityChangeRegionEvent.class, new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        EntityChangeRegionEvent ecre = (EntityChangeRegionEvent)e;
                        if (ecre.getEntity().equals(World.getLocalPlayer())) {
                            setBackground(ecre.getTo().getBackgroundAmbience());
                        }
                    }
                })
                .on(CastFailedEvent.class, new EventHandler() {
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

    public static void update() {
        if (context == COMBAT) {
            if (System.currentTimeMillis() > lastContextSwitch + (90 * 1000)) {
                lastMusicTime = 0;
                switchContext(IDLE, true);
            } else if (!isPlayingMusic()) playRandomCombatSong();
        } else {
            if (System.currentTimeMillis() > lastMusicTime + (60*1000*5)) {
                lastMusicTime = System.currentTimeMillis();
                playRandomIdleSong();
            }
        }
    }

    public static boolean isPlayingMusic() {
        return currentSong != null && currentSong.playing();
    }

    public static void cleanup() {
        AL.destroy();
    }

    private static void playRandomCombatSong() {
        Sound[] combats = new Sound[]{
                MUSIC_COMBAT_1,
                MUSIC_COMBAT_2,
                MUSIC_COMBAT_3,
                MUSIC_COMBAT_4,
                MUSIC_COMBAT_5,
        };
        changeMusic(combats[(int)(Math.random() * combats.length)]);
    }

    private static void playRandomIdleSong() {
        Sound[] idles = new Sound[]{
                MUSIC_IDLE_1,
                MUSIC_IDLE_2,
                MUSIC_IDLE_3,
                MUSIC_IDLE_4,
                MUSIC_IDLE_5,
        };
        changeMusic(idles[(int)(Math.random() * idles.length)]);
    }

    private static void changeMusic(Sound sound) {
        if (currentSong != null && currentSong.playing()) currentSong.stop();
        currentSong = sound;
        if (sound != null) sound.playAt(1.0f, 0.6f * BASE_VOLUME_MULTIPLIER, 0, 0, 0);
    }

    public static void setBackground(Sound s) {
        if (backgroundAmbience != null) backgroundAmbience.stop();
        backgroundAmbience = s;
        if (backgroundAmbience != null) backgroundAmbience.loop(1.0f, 0.7f * BASE_VOLUME_MULTIPLIER);
    }

    private static void switchContext(int c, boolean stopMusic) {
        if (c != context && stopMusic) stopMusic();
        context = c;
        lastContextSwitch = System.currentTimeMillis();
    }

    public static void stopAmbience() {
        if (backgroundAmbience != null) backgroundAmbience.stop();
    }

    public static void resumeAmbience() {
        if (backgroundAmbience != null && !backgroundAmbience.playing()) backgroundAmbience.loop(1.0f, 0.5f * BASE_VOLUME_MULTIPLIER);
    }

    public static void stopMusic() {
        if (currentSong != null) currentSong.stop();
    }

    public static void playSound(Sound s) {
        if (s != null) s.playAt(1.0f, BASE_VOLUME_MULTIPLIER, 0, 0, 0);
    }

    public static void playSound(Sound s, float volumeMultiplier, Location location) {
        /*if (s == null) return;
        if (!location.getRegion().equals(World.getLocalPlayer().getLocation().getRegion())) return;
        System.out.println("Playing sound "+s);
        double xDist = location.getCoordinates()[0] - Camera.getLocation()[0];
        double yDist = location.getCoordinates()[1] - Camera.getLocation()[1];
        double dist = location.distanceTo(Camera.getLocation()[0], Camera.getLocation()[1]);
        float audioX = (int)(xDist / 6) * 0.05f;
        s.playAt((float)MiscMath.random(0.8, 1.2), BASE_VOLUME_MULTIPLIER * volumeMultiplier * (1 - (float)MiscMath.clamp((dist / 4) / 24, 0, 1)), audioX, (float)yDist / 6, 0);*/
    }

}
