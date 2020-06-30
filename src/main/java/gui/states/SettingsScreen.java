package gui.states;

import assets.Settings;
import com.github.mathiewz.slick.Color;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.Checkbox;
import gui.elements.Picker;
import gui.elements.TextLabel;
import main.GameManager;
import misc.Window;
import org.lwjgl.opengl.DisplayMode;

import java.util.List;
import java.util.stream.Collectors;

public class SettingsScreen extends GameState {

    private Picker picker;
    private Checkbox vsyncToggle, autosaveToggle, fullscreenToggle;

    private boolean changedResolution;

    public SettingsScreen() {
        super("gui/title_bg.png");
    }

    @Override
    public int getID() {
        return GameState.SETTINGS_SCREEN;
    }

    @Override
    public void addGUIElements(GUI gui) {
        gui.addElement(new TextLabel("Settings", 6, Color.white, true, false), 0, 8, GUIAnchor.TOP_MIDDLE);


        List<DisplayMode> modes = Window.getAllDisplayModes();
        gui.addElement(new TextLabel("Fullscreen Resolution", 5, Color.white, true, false), 0, 32, GUIAnchor.TOP_MIDDLE);
        picker = new Picker(0, modes.size()-1, 1, 64, modes.stream().map(m -> m.toString()).collect(Collectors.toList()).toArray(new String[]{})) {
            @Override
            public void onValueChange() {
                changedResolution = true;
            }
        };
        gui.addElement(picker, 0, 40, GUIAnchor.TOP_MIDDLE);

        gui.addElement(new TextLabel("(press F11 to toggle fullscreen)", 4, Color.white, true, false), 0, 52, GUIAnchor.TOP_MIDDLE);

        fullscreenToggle = new Checkbox("Fullscreen on launch");
        gui.addElement(fullscreenToggle, 0, 72, GUIAnchor.TOP_MIDDLE);

        vsyncToggle = new Checkbox("Use VSync");
        fullscreenToggle.addChild(vsyncToggle, 0, 16, GUIAnchor.TOP_LEFT);

        autosaveToggle = new Checkbox("Auto Save");
        fullscreenToggle.addChild(autosaveToggle, 0, 32, GUIAnchor.TOP_LEFT);

        gui.addElement(new Button("Cancel", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                GameManager.switchTo(GameState.MAIN_MENU, false);
            }
        }, -36, -8, GUIAnchor.BOTTOM_MIDDLE);

        gui.addElement(new Button("Done", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                Settings.set("resolution", picker.getValue());
                Settings.set("fullscreen", fullscreenToggle.isToggled());
                Settings.set("vsync", vsyncToggle.isToggled());
                Settings.set("autosave", autosaveToggle.isToggled());
                Settings.save();

                Window.WINDOW_INSTANCE.setVSync(Settings.getBoolean("vsync"));
                if (Window.isFullScreen() && changedResolution) {
                    Window.toggleFullScreen();
                    Window.toggleFullScreen();
                }

                GameManager.switchTo(GameState.MAIN_MENU, false);
            }
        }, 36, -8, GUIAnchor.BOTTOM_MIDDLE);
    }

    @Override
    public void onEnter() {
        //load and apply settings to elements
        changedResolution = false;
        picker.setValue(Settings.getInt("resolution"));
        autosaveToggle.setToggled(Settings.getBoolean("autosave"));
        vsyncToggle.setToggled(Settings.getBoolean("vsync"));
        fullscreenToggle.setToggled(Settings.getBoolean("fullscreen"));
    }

}
