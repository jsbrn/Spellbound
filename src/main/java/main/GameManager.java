package main;

import assets.Assets;
import assets.Settings;
import com.github.mathiewz.slick.AppGameContainer;
import com.github.mathiewz.slick.GameContainer;
import com.github.mathiewz.slick.SlickException;
import com.github.mathiewz.slick.state.StateBasedGame;
import com.github.mathiewz.slick.state.transition.FadeInTransition;
import com.github.mathiewz.slick.state.transition.FadeOutTransition;
import gui.sound.SoundManager;
import gui.states.*;
import misc.Window;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.lwjgl.opengl.DisplayMode;
import network.MPServer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GameManager extends StateBasedGame {

    private static GameManager instance;
    private static String mouseCursor;

    private static MPServer host;

    private static boolean loadMusic;

    public GameManager(String gameTitle) {

        super(gameTitle); //set window title to "gameTitle" string
        //add states
        addState(new GameScreen());
        addState(new MainMenuScreen());
        addState(new LoadingScreen(loadMusic));
        addState(new SettingsScreen());

        instance = this;

    }

    public static void main(String args[]) throws IOException {

        loadMusic = args.length <= 0 || Boolean.parseBoolean(args[0]);

        new File(Assets.ROOT_DIRECTORY+ "/world").mkdirs();
        new File(Assets.ASSETS_DIRECTORY+"/sounds/").mkdirs();

        Settings.load();



        //initialize the window
        try {

            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("pom.xml"));
            Window.WINDOW_TITLE = "Spellbound "+model.getVersion();

                    Window.WINDOW_INSTANCE = new AppGameContainer(new GameManager(Window.WINDOW_TITLE));
            DisplayMode desktop = Window.getAllDisplayModes().get(0);
            Window.WINDOW_INSTANCE.setDisplayMode((int)(Window.getScreenWidth() * 0.5), (int)(Window.getScreenHeight() * 0.5), false);
            Window.WINDOW_INSTANCE.setSmoothDeltas(false);
            Window.WINDOW_INSTANCE.setAlwaysRender(true);
            Window.WINDOW_INSTANCE.setVSync(true);
            Window.WINDOW_INSTANCE.setShowFPS(false);
            Window.WINDOW_INSTANCE.setUpdateOnlyWhenVisible(false);
            Window.WINDOW_INSTANCE.setIcons(new String[]{
                    "gui/icons/favicon/icon_16x16.png",
                    "gui/icons/favicon/icon_32x32.png",
                    "gui/icons/favicon/icon_48x48.png",
                    "gui/icons/favicon/icon_64x64.png",
                    "gui/icons/favicon/icon_80x80.png",
                    "gui/icons/favicon/icon_96x96.png",
                    "gui/icons/favicon/icon_112x112.png",
                    "gui/icons/favicon/icon_128x128.png"
            });
            if (Settings.getBoolean("fullscreen")) Window.toggleFullScreen();
            Window.WINDOW_INSTANCE.start();

        } catch (SlickException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        SoundManager.cleanup();

    }

    public static void switchTo(int gameState, boolean transition) {
        instance.enterState(gameState, transition ? new FadeOutTransition() : null, transition ? new FadeInTransition() : null);
        ((GameState)instance.getState(gameState)).onEnter();
    }

    public static void setMouseCursor(String ref) {
        try {
            mouseCursor = ref;
            instance.getContainer().setMouseCursor(
                    Assets.getImage(mouseCursor)
                            .getFlippedCopy(false, false)
                            .getScaledCopy(4), 0, 0);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    public static GameState getGameState(int id) { return (GameState)instance.getState(id); }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        //initialize states
        getState(GameState.GAME_SCREEN).init(gc, this);
        getState(GameState.MAIN_MENU).init(gc, this);
        getState(GameState.SETTINGS_SCREEN).init(gc, this);

        gc.setDefaultFont(Assets.getFont(14));

        //load "menu" state on startup
        this.enterState(GameState.LOADING_SCREEN);
    }

}