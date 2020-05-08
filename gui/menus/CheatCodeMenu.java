package gui.menus;

import assets.Assets;
import gui.GUIAnchor;
import gui.elements.*;
import gui.states.GameScreen;
import gui.states.GameState;
import main.GameManager;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import world.Camera;
import world.World;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.humanoids.npcs.Collector;

import java.util.Random;

public class CheatCodeMenu extends Modal {

    private static TextBox input;

    public CheatCodeMenu() {
        super(Assets.getImage("assets/gui/popup_small.png"));
        addChild(new TextLabel("ENTER CHEAT CODE", 6, Color.black, false, false), 0, 8, GUIAnchor.TOP_MIDDLE);
        input = new TextBox(64, 8) {
            @Override
            public boolean onKeyDown(int key) {
                if (key == Input.KEY_ENTER) {
                    checkCode();
                    getGUI().popModal();
                }
                return super.onKeyDown(key);
            }
        };
        addChild(input, 0, 0, GUIAnchor.CENTER);
        addChild(new Button("Let's cheat!", 32, 8, null, true) {
            @Override
            public void onClick(int button) {
                checkCode();
                getGUI().popModal();
            }
        }, 0, -12, GUIAnchor.BOTTOM_MIDDLE);
    }

    private void checkCode() {
        String code = input.getText();
        if (code.equals("6mdm")) {
            World.getLocalPlayer().activateGodMode();
        }
        if (code.equals("speedrun")) {
            World.getLocalPlayer().addArtifacts(5);
        }
        if (code.equals("slomo")) {
            World.setTimeMultiplier(0.5f);
        }
        if (code.equals("freelunch")) {
            spawnChest();
        }
        if (code.equals("magicman")) {
            World.getLocalPlayer().getSpellbook().discoverAllTechniques();
        }
        if (code.equals("xray")) {
            for (int i = 0; i < World.getRegion().getSize(); i++)
                for (int j = 0; j < World.getRegion().getSize(); j++)
                World.getRegion().getChunk(i, j).setDiscovered(true);
        }
        if (code.equals("lockpick")) {
            World.getLocalPlayer().addKeys(10);
        }
        if (code.equals("freecam")) {
            Camera.setManualMode(true);
        }
        if (code.equals("fixedcam")) {
            Camera.setManualMode(false);
        }
        if (code.equals("cinematic")) {
            Camera.setSpeed(5);
        }
        if (code.equals("gui")) {
            ((GameScreen)GameManager.getGameState(GameState.GAME_SCREEN)).toggleHUD();
        }
    }

    private void spawnChest() {
        double[] playerCoords = World.getLocalPlayer().getLocation().getCoordinates();
        Entity lootChest = new Chest(2.0f, false, Chest.RANDOM_LOOT, 1.0f);
        Location player = World.getLocalPlayer().getLocation();
        lootChest.moveTo(new Location(
                player.getRegion(),
                playerCoords[0] + 1,
                playerCoords[1],
                (int) MiscMath.random(0, 360)));

    }

    @Override
    public boolean onKeyDown(int key) {
        return true;
    }

    @Override
    public void onShow() {
        input.grabFocus();
        World.setPaused(true);
    }

    @Override
    public void onHide() {
        input.releaseFocus();
        World.setPaused(false);
    }
}
