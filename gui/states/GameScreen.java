package gui.states;

import gui.GUI;
import gui.GUIAnchor;
import gui.elements.*;
import gui.menus.Journal;
import gui.menus.PauseMenu;
import gui.menus.SpellcraftingMenu;
import misc.MiscMath;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import world.World;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.HumanoidDeathEvent;
import world.events.event.HumanoidRespawnEvent;


public class GameScreen extends GameState {

    public GameScreen() {
        super();
    }

    @Override
    public int getID() {
        return GameState.GAME_SCREEN;
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        World.setTimeMultiplier(gc.getInput().isKeyDown(Input.KEY_LSHIFT) ? 0.5 : 1);
        MiscMath.DELTA_TIME = (int)(delta * World.getTimeMultiplier());
        World.update();
    }

    @Override
    public void addGUIElements(GUI gui) {
        if (World.getLocalPlayer() == null) return;
        SpellcraftingMenu spellcasting = new SpellcraftingMenu(World.getLocalPlayer());
        Journal spellbook = new Journal(World.getLocalPlayer(), spellcasting);

        PauseMenu pauseMenu = new PauseMenu();
        gui.addElement(new CameraViewport() {
            @Override
            public boolean onKeyUp(int key) {
                if (key == Input.KEY_ESCAPE) {
                    gui.stackModal(pauseMenu);
                    return true;
                }
                return super.onKeyUp(key);
            }
        }, 0, 0, GUIAnchor.TOP_LEFT);
        gui.addElement(new Statusbar(World.getLocalPlayer()), 2, 2, GUIAnchor.TOP_LEFT);
        gui.addElement(new Hotbar(World.getLocalPlayer()), 2, 38, GUIAnchor.TOP_LEFT);
        gui.addElement(pauseMenu, 0,0, GUIAnchor.CENTER);
        pauseMenu.hide();
        gui.addElement(new Button(null, 16, 16, "spellbook.png", false) {
            @Override
            public boolean onKeyUp(int key) {
                if (key == Input.KEY_TAB) {
                    gui.stackModal(spellbook);
                    return true;
                }
                return false;
            }
            @Override
            public boolean onClick(int button) {
                gui.stackModal(spellbook);
                return true;
            }
        }, 4, 94, GUIAnchor.TOP_LEFT);

        MiniMap miniMap = new MiniMap();
        gui.addElement(miniMap, -2, 2, GUIAnchor.TOP_RIGHT);

        TextLabel deathMessage = new TextLabel("Press R to continue", 4, Color.white, true);
        EventDispatcher.register(new EventListener()
            .on(HumanoidDeathEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    HumanoidDeathEvent hde = (HumanoidDeathEvent)e;
                    if (hde.getHumanoid().equals(World.getLocalPlayer()))
                        deathMessage.show();
                }
            })
            .on(HumanoidRespawnEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    HumanoidRespawnEvent hde = (HumanoidRespawnEvent)e;
                    if (hde.getHumanoid().equals(World.getLocalPlayer()))
                        deathMessage.hide();
                }
            })
        );
        deathMessage.hide();
        gui.addElement(deathMessage, 0, 16, GUIAnchor.CENTER);

        gui.addElement(spellbook, 0, 0, GUIAnchor.CENTER);
        gui.addElement(spellcasting, 0, 0, GUIAnchor.CENTER);
        spellbook.hide();
        spellcasting.hide();

        gui.setSpeechBubble();
    }

    @Override
    public void keyReleased(int key, char c) {
        super.keyReleased(key, c);
        if (key == Input.KEY_F7) World.save();
    }

    @Override
    public void onResize(int width, int height) {

    }
}
