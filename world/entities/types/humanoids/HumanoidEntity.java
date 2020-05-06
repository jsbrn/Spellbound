package world.entities.types.humanoids;

import gui.states.GameState;
import main.GameManager;
import misc.MiscMath;
import org.json.simple.JSONObject;
import org.newdawn.slick.Color;
import world.entities.Entity;
import world.entities.animations.Animation;
import world.entities.magic.Spellbook;
import world.events.EventDispatcher;
import world.events.event.HumanoidDeathEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HumanoidEntity extends Entity {

    public static Color[]
            SKIN_COLORS = new Color[]{
                new Color(230, 210, 155),
                new Color(110, 90, 72)
            },
            HAIR_COLORS = new Color[]{
                new Color(152, 103, 18),
                new Color(179, 172, 63),
                new Color(180, 90, 65),
                new Color(50, 30, 45),
                new Color(35, 35, 35),
                new Color(100, 100, 100)
            };
    public static String[] HAIR_STYLES = new String[]{
        "bald",
        "balding",
        "short",
        "long",
        "flat",
        "mohawk",
        "ponytail",
        "cone"
    }, SHIRT_STYLES = new String[]{
        "undershirt",
        "dirty"
    }, SPECIAL_HAIR_STYLES = new String[]{
        "hood"
    };

    private Spellbook spellbook;
    private double hp, mana, stamina, max_hp, max_mana, max_stamina;

    private boolean isDead, hasAmulet;
    private String allegiance;
    private int crystals, gold, dyes, keys, artifacts;

    public HumanoidEntity() {

        super();

        Random rng = new Random();

        this.allegiance = "default";
        this.setMaxHP(100);
        this.setMaxMana(Integer.MAX_VALUE);
        this.hp = 100;
        this.setMana(Integer.MAX_VALUE);
        this.setMaxStamina(100);

        this.spellbook = new Spellbook(this);

        this.addAnimation("torso", "default", new Animation("humanoid/torso_idle.png", 2, 1, 16, true, true));

        for (String shirt: SHIRT_STYLES)
            this.addAnimation("shirt", shirt, new Animation("humanoid/cosmetics/"+shirt+".png", 1, 32, 32, Color.white));

        this.addAnimation("arms", "default", new Animation("humanoid/arms_idle.png", 2, 1, 16, true, true));
        this.addAnimation("arms", "walking", new Animation("humanoid/arms_walking.png", 2, 4, 16, true, true));
        this.addAnimation("arms", "casting", new Animation("humanoid/arms_casting.png", 4, 3, 16, false, true));
        this.addAnimation("arms", "falling", new Animation("humanoid/arms_falling.png", 1, 1, 16, true, true));
        this.addAnimation("arms", "pushed", new Animation("humanoid/arms_pushed.png", 6, 3, 16, false, true));
        this.addAnimation("legs", "default", new Animation("humanoid/legs_idle.png", 2, 1, 16, true, true));
        this.addAnimation("legs", "walking", new Animation("humanoid/legs_walking.png", 2, 4, 16, true, true));
        this.addAnimation("legs", "falling", new Animation("humanoid/legs_falling.png", 1, 1, 16, true, true));
        this.addAnimation("legs", "pushed", new Animation("humanoid/legs_pushed.png", 6, 3, 16, false, true));
        this.addAnimation("legs", "dying", new Animation("humanoid/legs_dying.png", 4, 3, 16, false, false));
        this.addAnimation("legs", "dead", new Animation("humanoid/legs_dead.png", 1, 1, 16, false, false));
        this.addAnimation("head", "default", new Animation("humanoid/head_idle.png", 2, 1, 16, true, true));
        this.addAnimation("head", "talking", new Animation("humanoid/head_talking.png", 3, 2, 16, true, true));

        this.addAnimation("torso", "dying", new Animation("humanoid/torso_dying.png", 4, 3, 16, false, false));
        this.addAnimation("torso", "dead", new Animation("humanoid/torso_dead.png", 1, 1, 16, false, false));
        this.addAnimation("arms", "dying", new Animation("humanoid/arms_dying.png", 4, 3, 16, false, false));
        this.addAnimation("arms", "dead", new Animation("humanoid/arms_dead.png", 1, 1, 16, false, false));
        this.addAnimation("head", "dying", new Animation("humanoid/head_dying.png", 4, 3, 16, false, false));
        this.addAnimation("head", "dead", new Animation("humanoid/head_dead.png", 1, 1, 16, false, false));
        this.addAnimation("arms", "dead", new Animation("humanoid/arms_dead.png", 1, 1, 16, false, false));
        this.addAnimation("legs", "dying", new Animation("humanoid/legs_dying.png", 4, 3, 16, false, false));
        this.addAnimation("legs", "dead", new Animation("humanoid/legs_dead.png", 1, 1, 16, false, false));
        this.addAnimation("head", "dying", new Animation("humanoid/head_dying.png", 4, 3, 16, false, false));
        this.addAnimation("head", "dead", new Animation("humanoid/head_dead.png", 1, 1, 16, false, false));

        ArrayList<String> hairs = new ArrayList<>();
        hairs.addAll(Arrays.asList(HAIR_STYLES));
        hairs.addAll(Arrays.asList(SPECIAL_HAIR_STYLES));
        for (String hair: hairs)
            this.addAnimation("hair", hair, new Animation("humanoid/cosmetics/"+hair+".png", 1, 32, 32, Color.white));

        getAnimationLayer("hair").setBaseAnimation(HAIR_STYLES[rng.nextInt(HAIR_STYLES.length)]);
        getAnimationLayer("shirt").setBaseAnimation(SHIRT_STYLES[rng.nextInt(SHIRT_STYLES.length)]);

        Color shirt = new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat());
        getAnimationLayer("torso").setColor(shirt);
        getAnimationLayer("arms").setColor(shirt.darker());
        getAnimationLayer("legs").setColor(Color.black);
        getAnimationLayer("head").setColor(new Color(SKIN_COLORS[rng.nextInt(SKIN_COLORS.length)]));
        getAnimationLayer("hair").setColor(new Color(HAIR_COLORS[rng.nextInt(HAIR_COLORS.length)]));
        getAnimationLayer("shirt").setColor(shirt.brighter());

    }

    @Override
    public void update() {
        if (isDead()) return;
        super.update();
        addMana(MiscMath.getConstant(max_mana, 20));
        addStamina(MiscMath.getConstant(max_stamina, 7.5));
    }

    public String getAllegiance() { return allegiance; }
    public void setAllegiance(String allegiance) { this.allegiance = allegiance; }
    public boolean isAlliedTo(HumanoidEntity e) { return e.allegiance.equals(allegiance); }

    public void setHasAmulet(boolean hasAmulet) {
        this.hasAmulet = hasAmulet;
    }

    public boolean hasAmulet() {
        return hasAmulet;
    }

    public double getHP() { return hp; }
    public double getMana() { return mana; }
    public double getStamina() { return stamina; }
    public double getMaxHP() { return max_hp; }
    public double getMaxMana() { return max_mana; }
    public double getMaxStamina() { return max_stamina; }

    public int getCrystalCount() { return crystals; }
    public int getGoldCount() { return gold; }
    public int getDyeCount() { return dyes; }
    public int getKeyCount() { return keys; }
    public int getArtifactCount() { return artifacts; }

    public void addGold(int gold, boolean verbose) {
        this.gold += gold;
        if (verbose) {
            GameManager.getGameState(GameState.GAME_SCREEN).getGUI().floatText(getLocation(), (gold > 0 ? "+" : "")+gold+" Gold", Color.yellow, 1, 1000, -1, false);
        }
    }
    public void addCrystals(int crystals) { this.crystals += crystals; }
    public void addDyes(int dyes) { this.dyes += dyes; }
    public void addKeys(int keys) { this.keys += keys; }
    public void addArtifacts(int artifacts) { this.artifacts += artifacts; }

    public void setMana(double mana) { this.mana = MiscMath.clamp(mana, 0, max_mana); }
    public void addMana(double amount) { setMana(mana + amount); }
    public void setHP(double amount, boolean verbose) {
        int oldHP = (int)hp;
        this.hp = MiscMath.clamp(amount, 0, max_hp);
        if ((int)hp != oldHP && verbose) {
            int diff = (int)hp - oldHP;
            GameManager.getGameState(GameState.GAME_SCREEN).getGUI().floatText(
                    getLocation(),
                    diff > 0 ? "+"+diff : ""+diff,
                    diff > 0 ? Color.green : Color.red,
                    4,
                    500, 0, true);
        }
        if (!isDead && hp == 0) {
            EventDispatcher.invoke(new HumanoidDeathEvent(this));
            isDead = true;
            setIsTile(true);
            clearAllActions();
            enterState(null);
            getAnimationLayer("torso").stackAnimation("dying");
            getAnimationLayer("arms").stackAnimation("dying");
            getAnimationLayer("legs").stackAnimation("dying");
            getAnimationLayer("head").stackAnimation("dying");
            getAnimationLayer("torso").setBaseAnimation("dead");
            getAnimationLayer("arms").setBaseAnimation("dead");
            getAnimationLayer("legs").setBaseAnimation("dead");
            getAnimationLayer("head").setBaseAnimation("dead");
            getAnimationLayer("hair").setEnabled(false);
            getAnimationLayer("shirt").setEnabled(false);
        }
    }
    public void addHP(double amount, boolean verbose) { setHP(hp + amount, verbose); }
    public void setStamina(double amount) { this.stamina = MiscMath.clamp(amount, 0, max_stamina); }
    public void addStamina(double amount) { setStamina(stamina + amount); }

    public void setMaxHP(double amount) { this.max_hp = amount; }
    public void setMaxMana(double amount) { this.max_mana = amount; }
    public void setMaxStamina(double amount) { this.max_stamina = amount; }

    public void resurrect() {
        if (isDead) {
            setHP(max_hp, true);
            setMana(max_mana);
            setStamina(0); //probably out of breath if you come back from the dead.
            clearAllActions();
            setIsTile(false);
            getAnimationLayer("torso").setBaseAnimation("default");
            getAnimationLayer("arms").setBaseAnimation("default");
            getAnimationLayer("legs").setBaseAnimation("default");
            getAnimationLayer("head").setBaseAnimation("default");
            getAnimationLayer("hair").setEnabled(true);
            getAnimationLayer("shirt").setEnabled(true);
            isDead = false;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public Spellbook getSpellbook() { return spellbook; }
    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = super.serialize();
        serialized.put("hp", getHP());
        serialized.put("max_hp", getMaxHP());
        serialized.put("mana", getMana());
        serialized.put("max_mana", getMaxMana());
        serialized.put("stamina", getStamina());
        serialized.put("max_stamina", getMaxStamina());
        serialized.put("gold", getGoldCount());
        serialized.put("crystals", getCrystalCount());
        serialized.put("dyes", getDyeCount());
        serialized.put("artifacts", getArtifactCount());
        serialized.put("keys", getKeyCount());
        serialized.put("is_dead", isDead);
        serialized.put("has_amulet", hasAmulet);
        serialized.put("spellbook", getSpellbook().serialize());
        return serialized;
    }

    @Override
    public void deserialize(JSONObject json) {
        super.deserialize(json);
        setHP((double)json.get("hp"), false);
        setMaxHP((double)json.get("max_hp"));
        setMana((double)json.get("mana"));
        setMaxMana((double)json.get("max_mana"));
        setStamina((double)json.get("stamina"));
        setMaxStamina((double)json.get("max_stamina"));
        addGold((int)(long)json.get("gold"), false);
        addCrystals((int)(long)json.get("crystals"));
        addDyes((int)(long)json.get("dyes"));
        addKeys((int)(long)json.get("keys"));
        setHasAmulet((boolean)json.get("has_amulet"));
        getSpellbook().deserialize((JSONObject)json.get("spellbook"));
    }
}
