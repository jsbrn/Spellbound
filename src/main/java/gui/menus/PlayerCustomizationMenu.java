package gui.menus;

import com.github.mathiewz.slick.Color;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.Modal;
import gui.elements.TextBox;
import gui.elements.TextLabel;
import gui.states.GameState;
import gui.states.MainMenuScreen;
import main.GameManager;
import network.MPClient;
import network.MPServer;
import world.entities.components.magic.Spell;

import java.util.Random;

public class PlayerCustomizationMenu extends Modal {

    private TextBox nameField, seedField;
    private Button createButton;

    private Random rng;

    private int characterRotation = 4;

    public PlayerCustomizationMenu() {
        super("gui/spellcasting.png");

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
                int seed = Integer.parseInt(seedField.getText().replaceAll("[^\\d]", ""));
                MPServer.init();
                if (MPServer.launch(seed)) {
                    MPClient.init();
                    MPClient.join("127.0.0.1");
                }
                nameField.releaseFocus();
                //creation.setName(nameField.getText());
                //getGUI().popModal();
                //TODO: initialize server and connect to it (or something like that)
//                World.init();
//                World.generate(Integer.parseInt(seed.isEmpty() ? "0" : seed));
//                World.spawnPlayer(Chunk.CHUNK_SIZE / 2, Chunk.CHUNK_SIZE / 2, World.getRegion("player_home"));
//                World.save();

                //GameManager.getGameState(GameState.GAME_SCREEN).resetGUI();
                //GameManager.switchTo(GameState.GAME_SCREEN, true);

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

}
