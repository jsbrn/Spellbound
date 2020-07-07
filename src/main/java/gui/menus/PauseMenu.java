package gui.menus;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.Modal;
import gui.elements.TextLabel;
import gui.sound.SoundManager;
import gui.states.GameState;
import main.GameManager;
import network.MPClient;
import network.MPServer;
import world.World;
import world.events.EventManager;

public class PauseMenu extends Modal {

    public PauseMenu() {
        super(Assets.getImage("gui/blank.png"));
        addChild(new TextLabel("Paused", 5, Color.white, true, false), 0, -48, GUIAnchor.TOP_MIDDLE);
        addChild(new Button(null, 24, 24, "icons/play.png", true) {
            @Override
            public void onClick(int button) {
                getGUI().popModal();
            }
        }, -16, 0, GUIAnchor.CENTER);

        addChild(new Button(null, 24, 24, "icons/save.png", true) {
            @Override
            public void onClick(int button) {
                //TODO: reimplement world saving / event cleanup
                MPClient.close();
                if (MPServer.isOpen()) MPServer.close();
                GameManager.switchTo(GameState.MAIN_MENU, true);
                SoundManager.stopAmbience();
                //EventManager.unregisterAll();
                SoundManager.stopMusic();
            }
        }, 16, 0, GUIAnchor.CENTER);

    }

    @Override
    public boolean onKeyDown(int key) {
        return true;
    }

    @Override
    public void onShow() {
        SoundManager.stopAmbience();
    }

    @Override
    public void onHide() {
        SoundManager.resumeAmbience();
    }
}
