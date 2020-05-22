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
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadingScreen extends GameState {

    private Thread downloadThread, loadSoundsThread;
    private long bytesDownloaded, downloadSize;
    private boolean finishedDownload;
    private TextLabel title, progress;

    public LoadingScreen() {
        this.downloadThread = new Thread() {
            @Override
            public void run() {
                bytesDownloaded = 0;
                try (BufferedInputStream in = new BufferedInputStream(new URL("https://spellbound.openode.io/files/classic/sounds.zip").openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(Assets.ASSETS_DIRECTORY +"/sounds/temp.zip")) {

                    File temp = new File(Assets.ASSETS_DIRECTORY+"/sounds/temp.zip");
                    temp.deleteOnExit();

                    byte dataBuffer[] = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                        bytesDownloaded += 1024;
                    }
                    new ZipFile(Assets.ASSETS_DIRECTORY+"/sounds/temp.zip").extractAll(Assets.ASSETS_DIRECTORY+"/sounds/");
                    temp.delete();
                    finishedDownload = true;
                    loadSoundsThread.start();
                } catch (IOException e) {
                    // handle exception
                }
            }
        };
        this.loadSoundsThread = new Thread() {
            @Override
            public void run() {
                progress.setText("");
                SoundManager.load("");
                GameManager.switchTo(GameState.MAIN_MENU, true);
            }
        };
    }

    private long getFileSize(String fileUrl) throws IOException {

        URL oracle = new URL(fileUrl);

        HttpURLConnection yc = (HttpURLConnection) oracle.openConnection();

        long fileSize = 0;
        try {
            // retrieve file size from Content-Length header field
            fileSize = Long.parseLong(yc.getHeaderField("Content-Length"));
        } catch (NumberFormatException nfe) {
        }

        return fileSize;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        super.init(gc, game);
        if (new File(Assets.ASSETS_DIRECTORY+"/sounds/").listFiles().length == 0) {
            downloadSounds();
        } else {
            finishedDownload = true;
            loadSoundsThread.start();
            loadAssets();
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (finishedDownload && !downloadThread.isAlive()) loadAssets();
        //if (downloadFailed) GameManager.switchTo(GameState.MAIN_MENU);
        progress.setText((bytesDownloaded/1024/1024)+"/"+(downloadSize/1024/1024)+" MB");
        if (finishedDownload) progress.hide();
    }

    @Override
    public int getID() {
        return GameState.LOADING_SCREEN;
    }

    @Override
    public void addGUIElements(GUI gui) {
        title = new TextLabel("Connecting to asset server...", 5, Color.white, false, false);
        gui.addElement(title, 0, 0, GUIAnchor.CENTER);
        progress = new TextLabel("", 4, Color.white, false, false);
        gui.addElement(progress, 0, 8, GUIAnchor.CENTER);
    }

    private void downloadSounds() {
        try {
            downloadSize = getFileSize("https://spellbound.openode.io/files/classic/sounds.zip");
            title.setText("Downloading assets...");
            downloadThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAssets() {
        title.setText("Preparing assets...");
        Assets.load();
        Definitions.load();
    }

    @Override
    public void onEnter() {

    }

}
