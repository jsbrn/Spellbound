package world.sounds;

import gui.sound.SoundManager;
import org.newdawn.slick.Sound;
import world.entities.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class SoundEmitter {

    private Entity parent;
    private int timing, maxVariance, variance;
    private float volumeMultiplier;
    private long lastEmit;
    private ArrayList<Sound> sounds;
    private Sound currentSound;

    private boolean isActive;

    private Random rng;

    public SoundEmitter(float volumeMultiplier, Sound[] sounds, Entity parent) {
        this(0, 0, volumeMultiplier, sounds, parent);
        this.isActive = false;
    }

    public SoundEmitter(int timingMills, int variance, float volume, Sound[] sounds, Entity parent) {
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
        if (parent.getLocation().getRegion().getCurrentTime() > lastEmit + timing + variance) {
            play();
            variance = rng.nextInt(1 + maxVariance);
            lastEmit = parent.getLocation().getRegion().getCurrentTime();
        }
    }

    public void stop() {
        if (currentSound != null) currentSound.stop();
        lastEmit = parent.getLocation().getRegion().getCurrentTime();
    }

    public void play() {
        if (sounds.isEmpty()) return;
        Sound s = sounds.get(rng.nextInt(sounds.size()));
        SoundManager.playSound(s, volumeMultiplier, parent.getLocation());
        currentSound = s;
    }

    public void setActive(boolean active) {
        if (isActive == false && active == true) lastEmit = 0; //play immediately
        isActive = active;
    }
}
