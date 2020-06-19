package world.entities.types;

import assets.SpellFactory;
import gui.menus.PopupMenu;
import gui.sound.SoundManager;
import gui.states.GameState;
import main.GameManager;
import org.json.simple.JSONObject;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Sound;
import world.entities.Entity;
import world.entities.actions.types.ChangeAnimationAction;
import world.entities.animations.Animation;
import world.entities.magic.techniques.Techniques;
import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;
import world.sounds.SoundEmitter;

import java.util.Random;

public class Chest extends Entity {

    public static int GOLD_LOOT = 0, CRYSTAL_LOOT = 1, DYE_LOOT = 2, TOME_LOOT = 3, ARTIFACT_LOOT = 4, KEY_LOOT = 5, RANDOM_LOOT = 6;
    private boolean looted, locked;
    private int lootType;
    private float filledChance, lootMultiplier;

    private EventListener listener;

    public Chest(float lootMultiplier, boolean locked, int lootType, float filledChance) {

        this.lootMultiplier = lootMultiplier;
        this.locked = locked;
        this.lootType = lootType;
        this.filledChance = filledChance;

        this.addSoundEmitter("locked", new SoundEmitter(1.0f, new Sound[]{SoundManager.LOCKED}, this));
        this.addSoundEmitter("loot_found", new SoundEmitter(1.0f, new Sound[]{SoundManager.FOUND_LOOT}, this));
        this.addSoundEmitter("opened", new SoundEmitter(1.0f, new Sound[]{SoundManager.DOOR_OPEN}, this));

        this.getMover().setCollidable(true);
        this.addAnimation("default", "closed", new Animation((locked ? "locked_" : "") + "chest.png", 1, 1, 16, true, false));
        this.addAnimation("default", "opening", new Animation("chest_opening.png", 3, 3, 16, false, false));
        this.addAnimation("default", "opened", new Animation("chest_opened.png", 1, 1, 16, true, false));
        this.getAnimationLayer("default").setBaseAnimation("closed");

        Chest that = this;
        this.listener = new EventListener()
            .on(EntityActivatedEvent.class.toString(), new EventHandler() {
                        @Override
                        public void handle(Event e) {
                            Random rng = new Random();
                            EntityActivatedEvent eae = (EntityActivatedEvent)e;

                            if (looted) return;

                            if (eae.getEntity().equals(that)) {
                                if (eae.getActivatedBy() instanceof HumanoidEntity) {

                                    if (locked && ((HumanoidEntity)eae.getActivatedBy()).getKeyCount() < 1) {
                                        GameManager.getGameState(GameState.GAME_SCREEN).getGUI().floatText(
                                                eae.getEntity().getLocation(),
                                                "Locked",
                                                Color.gray, 2, 500, -0.5f, false);
                                        getSoundEmitter("locked").play();
                                        return;
                                    }

                                    getSoundEmitter("opened").play();

                                    that.getAnimationLayer("default").setBaseAnimation("opened");
                                    that.getActionQueue().queueAction(new ChangeAnimationAction("default", "opening", false, true));
                                    HumanoidEntity human = (HumanoidEntity)eae.getActivatedBy();

                                    boolean empty = true;
                                    String type = "";
                                    int loot = lootType, amount = (int)(rng.nextInt(1000) * lootMultiplier);

                                    if (lootType == RANDOM_LOOT) loot = rng.nextInt(6);

                                    if (rng.nextFloat() < filledChance) {
                                        if (loot == GOLD_LOOT) { amount /= 10; human.addGold(amount, false); type = "Gold"; }
                                        if (loot == CRYSTAL_LOOT) { amount /= 20; human.addCrystals(amount); type = "Crystals"; }
                                        if (loot == DYE_LOOT) { amount /= 30; human.addDyes(amount); type = "Dyes"; }
                                        if (loot == TOME_LOOT) { amount = 1; type = "Tome"; }
                                        if (loot == ARTIFACT_LOOT) { amount = 1; human.addArtifacts(amount); type = "Strange Artifact"; }
                                        if (loot == KEY_LOOT) { amount = 1; human.addKeys(amount); type = "Key"; }
                                        empty = amount <= 0;
                                    }

                                    if (locked) ((HumanoidEntity) eae.getActivatedBy()).addKeys(-1);

                                    if (empty) {
                                        GameManager.getGameState(GameState.GAME_SCREEN).getGUI().floatText(eae.getEntity().getLocation(), "Empty", Color.gray, 2, 500, -0.5f, false);
                                    } else {
                                        GameManager.getGameState(GameState.GAME_SCREEN).getGUI().floatText(eae.getEntity().getLocation(), "+"+amount+" "+type, Color.yellow, 2, 750, -0.5f, false);
                                        getSoundEmitter("loot_found").play();
                                    }

                                    if (lootType == ARTIFACT_LOOT) {
                                        GameManager.getGameState(GameState.GAME_SCREEN).getGUI().stackModal(new PopupMenu(
                                                "Artifact Found", "",
                                                "You found a mysterious Artifact! Could this be what the Collector is looking for?",
                                                "icons/artifact.png",
                                                Color.white
                                        ));
                                        SoundManager.playSound(SoundManager.DISCOVERY);
                                    } else if (lootType == TOME_LOOT) {
                                        String newTechnique = SpellFactory.discoverRandomTechnique(human.getSpellbook(), lootMultiplier);
                                        if (newTechnique != null) {
                                            human.getSpellbook().discoverTechnique(newTechnique);
                                            GameManager.getGameState(GameState.GAME_SCREEN).getGUI().stackModal(new PopupMenu(
                                                    "New Technique",
                                                    Techniques.getName(newTechnique),
                                                    Techniques.getDescription(newTechnique),
                                                    "icons/techniques/"+newTechnique+".png",
                                                    Color.darkGray
                                            ));
                                            SoundManager.playSound(SoundManager.DISCOVERY);
                                        }
                                    }

                                    looted = true;
                                }
                            }
                        }
                    }
            );
        EventDispatcher.register(listener);

    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = super.serialize();
        serialized.put("lootMultiplier", lootMultiplier);
        serialized.put("lootType", lootType);
        serialized.put("locked", locked);
        serialized.put("looted", looted);
        serialized.put("filledChance", filledChance);
        return serialized;
    }

    @Override
    public void deserialize(JSONObject json) {
        super.deserialize(json);
        looted = (boolean)json.get("looted");
    }
}
