package gui.states;

import assets.Assets;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.IconLabel;
import gui.elements.TextLabel;
import gui.menus.PlayerCustomizationMenu;
import gui.menus.PopupMenu;
import gui.sound.SoundManager;
import main.GameManager;
import misc.Window;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import world.World;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SettingsScreen extends GameState {

    public SettingsScreen() {
        super("assets/gui/title_bg.png");
    }

    @Override
    public int getID() {
        return GameState.SETTINGS_SCREEN;
    }

    @Override
    public void addGUIElements(GUI gui) {
        gui.addElement(new TextLabel("Settings", 5, Color.white, true, false), 0, 8, GUIAnchor.TOP_MIDDLE);
        gui.addElement(new Button("Cancel", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                GameManager.switchTo(GameState.MAIN_MENU, false);
            }
        }, -4, -8, GUIAnchor.BOTTOM_MIDDLE);

        List<DisplayMode> modes = Window.getAllDisplayModes();
        for (int i = 0; i < modes.size(); i++) {
            DisplayMode mode = modes.get(i);
            gui.addElement(
                    new TextLabel(mode.toString()+(mode.isFullscreenCapable() ? " (FS)" : ""),
                            4, Color.white, true, false),
                    0, 16 + (6 * i), GUIAnchor.TOP_MIDDLE);
        }
    }

    @Override
    public void onEnter() {
        //load and apply settings to elements
    }

}
