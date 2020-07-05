package gui.states;

import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.GameContainer;
import com.github.mathiewz.slick.Input;
import com.github.mathiewz.slick.SlickException;
import com.github.mathiewz.slick.state.StateBasedGame;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.*;
import gui.menus.Journal;
import gui.menus.PauseMenu;
import gui.menus.SpellcraftingMenu;
import misc.MiscMath;
import network.MPClient;
import network.MPServer;
import world.Camera;
import world.World;
import world.events.Event;
import world.events.EventManager;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.ConversationEndedEvent;
import world.events.event.HumanoidDeathEvent;
import world.events.event.HumanoidRespawnEvent;
import world.events.event.NPCSpeakEvent;


public class GameScreen extends GameState {

    private boolean showHUD;

    public GameScreen()
    {
        super();
        showHUD = true;
    }

    @Override
    public int getID() {
        return GameState.GAME_SCREEN;
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        super.update(gc, sbg, delta);
        MiscMath.DELTA_TIME = delta;
        MPServer.update();
        MPClient.update();
    }

    public void toggleHUD() {
        showHUD = !showHUD;
        resetGUI();
    }

    @Override
    public void addGUIElements(GUI gui) {
        if (Camera.getTargetEntity() == null) return;
        SpellcraftingMenu spellcasting = new SpellcraftingMenu(Camera.getTargetEntity());
        Journal spellbook = new Journal(Camera.getTargetEntity(), spellcasting);
        PauseMenu pauseMenu = new PauseMenu();
        CameraViewport viewport = new CameraViewport() {
            @Override
            public boolean onKeyUp(int key) {
                if (key == Input.KEY_ESCAPE) {
                    gui.stackModal(pauseMenu);
                    return true;
                }
                return super.onKeyUp(key);
            }
        };


        if (showHUD) gui.addElement(viewport, 0, 0, GUIAnchor.TOP_LEFT);
        gui.addElement(new Statusbar(Camera.getTargetEntity()), 2, 2, GUIAnchor.TOP_LEFT);
        gui.addElement(new Hotbar(Camera.getTargetEntity()), 2, 26, GUIAnchor.TOP_LEFT);
        gui.addElement(pauseMenu, 0,0, GUIAnchor.CENTER);
        pauseMenu.hide();
        Button spellbookButton = new Button(null, 16, 16, "spellbook.png", false) {
            @Override
            public boolean onKeyUp(int key) {
                if (key == Input.KEY_TAB) {
                    gui.stackModal(spellbook);
                    return true;
                }
                return false;
            }
            @Override
            public void onClick(int button) {
                gui.stackModal(spellbook);
            }
        };
        spellbookButton.setTooltipText("Journal (TAB)");
        gui.addElement(spellbookButton, 4, 82, GUIAnchor.TOP_LEFT);

        TextLabel deathMessage = new TextLabel("Press R to continue", 4, Color.white, true, false){
            @Override
            public boolean onKeyUp(int key) {
                return key != Input.KEY_R;
            }
        };

        deathMessage.hide();
        gui.addElement(deathMessage, 0, 16, GUIAnchor.CENTER);

        gui.addElement(spellbook, 0, 0, GUIAnchor.CENTER);
        gui.addElement(spellcasting, 0, 0, GUIAnchor.CENTER);
        spellbook.hide();
        spellcasting.hide();

        gui.addElement(new TextLabel("Spellbound Alpha Build", 4, Color.white, true, false), 0, 2, GUIAnchor.TOP_MIDDLE);

        SpeechBubble speechBubble = new SpeechBubble();
        gui.addElement(speechBubble, 0, -10, GUIAnchor.BOTTOM_MIDDLE);
        speechBubble.hide();

        if (!showHUD) gui.addElement(viewport, 0, 0, GUIAnchor.TOP_LEFT);

    }

    @Override
    public void keyReleased(int key, char c) {
        super.keyReleased(key, c);
    }

    @Override
    public void onEnter() {

    }

}
