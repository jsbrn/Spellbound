package gui.menus;

import gui.GUIAnchor;
import gui.elements.*;
import gui.states.MainMenuScreen;
import misc.Window;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import world.Chunk;
import world.World;
import world.magic.Spell;
import world.entities.types.humanoids.HumanoidEntity;
import world.entities.types.humanoids.Player;

import java.util.Random;

public class PlayerCustomizationMenu extends Modal {

    private Player creation;
    private TextBox nameField, seedField;
    private Button createButton;

    private Random rng;

    private int characterRotation = 4;

    public PlayerCustomizationMenu() {
        super("gui/spellcasting.png");
        this.creation = new Player();

        rng = new Random();

        nameField = new TextBox(64, 8) {
            @Override
            public boolean onKeyUp(int key) {
                return super.onKeyUp(key);
            }
        };

        seedField = new TextBox(64, 8) {
            @Override
            public boolean onKeyUp(int key) {
                return super.onKeyUp(key);
            }
        };
        seedField.setText(rng.nextInt(10000000)+"");
        nameField.setText("Player");
        createButton = new Button("Play!", 24, 8, null, true) {
            @Override
            public void onClick(int button) {
                String seed = seedField.getText().replaceAll("[^\\d]", "");
                nameField.releaseFocus();
                creation.setName(nameField.getText());
                getGUI().popModal();
                World.init(creation);
                World.generate(Integer.parseInt(seed.isEmpty() ? "0" : seed));
                World.spawnPlayer(Chunk.CHUNK_SIZE/2, Chunk.CHUNK_SIZE/2, 180, "player_home");
                World.save();
                ((MainMenuScreen)getGUI().getParent()).startGame();
            }
        };

        addChild(nameField, 8, 12, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("World Options", 5, Color.white, true, false), 8, 24, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Seed", 3, Color.white, true, false), 8, 32, GUIAnchor.TOP_LEFT);
        addChild(seedField, 8, 36, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Player Name", 5, Color.white, true, false), 8, 4, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Character Customization", 5, Color.white, true, false), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new Button("Cancel", 24, 8, null, true) {
            @Override
            public void onClick(int button) {
                getGUI().popModal();
            }
        }, 12, -6, GUIAnchor.BOTTOM_LEFT);

        addChild(createButton, 40, -6, GUIAnchor.BOTTOM_LEFT);

        //character rotation
        addChild(new Button(null, 8, 8, "icons/arrow_left.png", true) {
            @Override
            public void onClick(int button) {
                characterRotation++;
                if (characterRotation > 7) characterRotation = 0;
            }
        }, -64, 42, GUIAnchor.TOP_RIGHT);
        addChild(new Button(null, 8, 8, "icons/arrow_right.png", true) {
            @Override
            public void onClick(int button) {
                characterRotation--;
                if (characterRotation < 0) characterRotation = 7;
            }
        }, -43, 42, GUIAnchor.TOP_RIGHT);

        addChild(new Button("Randomize Hair Style", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                creation.getAnimationLayer("hair").setBaseAnimation(HumanoidEntity.HAIR_STYLES[rng.nextInt(HumanoidEntity.HAIR_STYLES.length)]);
            }
        }, 39, 56, GUIAnchor.TOP_MIDDLE);

        addChild(new Button("Randomize Hair Color", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                creation.getAnimationLayer("hair").setColor(HumanoidEntity.HAIR_COLORS[rng.nextInt(HumanoidEntity.HAIR_COLORS.length)]);
            }
        }, 39, 64, GUIAnchor.TOP_MIDDLE);

        addChild(new Button("Randomize Skin Color", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                creation.getAnimationLayer("head").setColor(HumanoidEntity.SKIN_COLORS[rng.nextInt(HumanoidEntity.SKIN_COLORS.length)]);
            }
        }, 39, 72, GUIAnchor.TOP_MIDDLE);

        addChild(new Button("Randomize Torso Color", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                Color random = new Color(rng.nextInt(255), rng.nextInt(255), rng.nextInt(255));
                creation.getAnimationLayer("torso").setColor(random);
                creation.getAnimationLayer("shirt").setColor(random);
            }
        }, 39, 80, GUIAnchor.TOP_MIDDLE);

        addChild(new Button("Randomize Shirt Style", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                creation.getAnimationLayer("shirt").setBaseAnimation(HumanoidEntity.SHIRT_STYLES[rng.nextInt(HumanoidEntity.SHIRT_STYLES.length)]);
            }
        }, 39, 88, GUIAnchor.TOP_MIDDLE);

    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        return true;
    }

    @Override
    public void onShow() {
        nameField.grabFocus();
    }

    @Override
    public void onHide() {
        nameField.releaseFocus();
    }

    public void reset(Spell template) {
        refresh();
    }

    private void refresh() {

    }

    @Override
    public void drawOver(Graphics g) {
        if (creation == null) return;
        float[] osc = getOnscreenCoordinates();
        creation.draw(osc[0] + (getDimensions()[0]*0.7f * Window.getScale()), osc[1] + (32 * Window.getScale()), Window.getScale(), characterRotation);
    }
}
