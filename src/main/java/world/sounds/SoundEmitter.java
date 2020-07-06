package world.sounds;

import com.github.mathiewz.slick.Sound;
import gui.sound.SoundManager;
import misc.Location;
import network.MPClient;
import network.MPServer;
import world.Camera;
import world.entities.Entities;
import world.entities.components.LocationComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

//TODO: convert to component with appropriate system
public class SoundEmitter {

    private Integer parent;
    private int timing, maxVariance, variance;
    private float volumeMultiplier;
    private long lastEmit;
    private ArrayList<Sound> sounds;
    private Sound currentSound;

    private boolean isActive;

    private Random rng;

    public SoundEmitter(float volumeMultiplier, Sound[] sounds, Integer parent) {
        this(0, 0, volumeMultiplier, sounds, parent);
        this.isActive = false;
    }

    public SoundEmitter(int timingMills, int variance, float volume, Sound[] sounds, Integer parent) {
        this.timing = timingMills;
        this.sounds = new ArrayList<>();
        this.volumeMultiplier = volume;
        this.maxVariance = variance;
        this.sounds.addAll(Arrays.stream(sounds).collect(Collectors.toList()));
        this.parent = parent;
        this.rng = new Random();
        this.isActive = true;
    }

    public void update() {
        if (!isActive) return;
        Location sourceLocation = ((LocationComponent) MPServer.getWorld().getEntities().getComponent(LocationComponent.class, parent)).getLocation();
        if (MPClient.getTime() > lastEmit + timing + variance) {
            play();
            variance = rng.nextInt(1 + maxVariance);
            lastEmit = MPClient.getTime();
        }
    }

    public void stop() {
        if (currentSound != null) currentSound.stop();
        Location sourceLocation = ((LocationComponent) MPServer.getWorld().getEntities().getComponent(LocationComponent.class, parent)).getLocation();
        lastEmit = MPClient.getTime();
    }

    public void play() {
        if (sounds.isEmpty()) return;
        Sound s = sounds.get(rng.nextInt(sounds.size()));
        Location sourceLocation = ((LocationComponent) MPServer.getWorld().getEntities().getComponent(LocationComponent.class, parent)).getLocation();
        //SoundManager.playSound(s, volumeMultiplier, sourceLocation);
        //TODO: send sound emit packet instead
        currentSound = s;
    }

    public void setActive(boolean active) {
        if (isActive == false && active == true) lastEmit = 0; //play immediately
        isActive = active;
    }
}
