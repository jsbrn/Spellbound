package gui.states;

import assets.Assets;
import assets.definitions.Definitions;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.TextLabel;
import gui.sound.SoundManager;
import main.GameManager;
import net.lingala.zip4j.ZipFile;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class LoadingScreen extends GameState {

    private Thread loadSoundsThread;
    private int bytesDownloaded;
    private TextLabel progress;

    public LoadingScreen() {
        this.loadSoundsThread = new Thread() {
            @Override
            public void run() {
                bytesDownloaded = 0;
                try (BufferedInputStream in = new BufferedInputStream(new URL("https://spellbound.openode.io/files/classic/sounds.zip").openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(Assets.ASSETS_DIRECTORY +"/sounds/temp.zip")) {
                    byte dataBuffer[] = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                        bytesDownloaded += 1024;
                        progress.setText((bytesDownloaded/1024/1024)+" MB");
                    }
                    new ZipFile(Assets.ASSETS_DIRECTORY+"/sounds/temp.zip").extractAll(Assets.ASSETS_DIRECTORY+"/sounds/");
                    new File(Assets.ASSETS_DIRECTORY+"/sounds/temp.zip").delete();
                    finish();
                } catch (IOException e) {
                    // handle exception
                }
            }
        };
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        super.init(gc, game);
        Assets.load();
        Definitions.load();
        if (new File(Assets.ASSETS_DIRECTORY+"/sounds/").listFiles().length == 0) {
            loadSoundsThread.start();
        } else {
            finish();
        }
    }

    @Override
    public int getID() {
        return GameState.LOADING_SCREEN;
    }

    @Override
    public void addGUIElements(GUI gui) {
        gui.addElement(new TextLabel("Downloading assets...", 5, Color.white, false, false), 0, 0, GUIAnchor.CENTER);
        progress = new TextLabel("", 4, Color.white, false, false);
        gui.addElement(progress, 0, 8, GUIAnchor.CENTER);
    }

    private void finish() {
        SoundManager.load("");
        GameManager.switchTo(GameState.MAIN_MENU);
    }

    @Override
    public void onEnter() {

    }

}
