package misc;

import assets.Assets;
import assets.Settings;
import com.github.mathiewz.slick.AppGameContainer;
import com.github.mathiewz.slick.Image;
import com.github.mathiewz.slick.SlickException;
import com.github.mathiewz.slick.imageout.ImageOut;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import world.Chunk;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Window {

    //the name of the window
    public static final String WINDOW_TITLE = "Spellbound - Alpha Candidate";
    //create a window object
    public static AppGameContainer WINDOW_INSTANCE;

    public static void toggleFullScreen() {
        try {
            if (WINDOW_INSTANCE.isFullscreen()) {
                WINDOW_INSTANCE.setFullscreen(false);
                WINDOW_INSTANCE.setDisplayMode((int) (getScreenWidth() * 0.75), (int) (getScreenHeight() * 0.75), false);
            } else {
                DisplayMode fs = Window.getAllDisplayModes().get(Settings.getInt("resolution"));
                WINDOW_INSTANCE.setDisplayMode(fs.getWidth(), fs.getHeight(), false);
                Display.setDisplayModeAndFullscreen(fs);
            }
        } catch (SlickException | LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void setFullscreen(boolean fs) {
        if (fs != Window.isFullScreen()) toggleFullScreen();
    }

    public static List<DisplayMode> getAllDisplayModes() {
        List<DisplayMode> modes = null;
        try {
            DisplayMode desktop = Display.getDesktopDisplayMode();
            modes = Arrays.stream(Display.getAvailableDisplayModes())
                    .filter(m -> m.getFrequency() == desktop.getFrequency() && m.getBitsPerPixel() == desktop.getBitsPerPixel() && m.isFullscreenCapable())
                    .sorted(Comparator.comparingInt(DisplayMode::getWidth))
                    .collect(Collectors.toList());
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        modes.add(0, Display.getDesktopDisplayMode());
        return modes;
    }

    public static boolean wasResized() {
        if (Display.getWidth() != Window.getWidth() || Display.getHeight() != Window.getHeight()) {
            return true;
        }
        return false;
    }

    public static boolean isFullScreen() {
        return WINDOW_INSTANCE.isFullscreen();
    }

    public static void takeScreenshot() {
        try {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int hour = Calendar.getInstance().get(Calendar.HOUR);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            int second = Calendar.getInstance().get(Calendar.SECOND);
            int mills = Calendar.getInstance().get(Calendar.MILLISECOND);
            Image scrn = new Image(Window.getWidth(), Window.getHeight());
            String folder = Assets.ROOT_DIRECTORY+"/screenshots/";
            String file_url = folder + year + "-" + month + "-" + day+" ("+System.currentTimeMillis()+")";
            WINDOW_INSTANCE.getGraphics().copyArea(scrn, 0, 0);
            //make screenshots folder
            new File(folder).mkdirs();
            ImageOut.write(scrn, file_url + ".png");
            System.out.println("Saved screenshot to " + file_url + ".png");
        } catch (SlickException ex) {

        }
    }

    public static float getScale() {
        return (float)(Window.getHeight() / (Chunk.CHUNK_SIZE * Chunk.TILE_SIZE)) + 2;
    }

    public static int getFPS() {
        return WINDOW_INSTANCE.getFPS();
    }

    public static int getScreenWidth() {
        return Display.getDesktopDisplayMode().getWidth();
    }

    public static int getScreenHeight() {
        return Display.getDesktopDisplayMode().getHeight();
    }

    public static float getY() {
        return (float) Display.getY();
    }

    public static float getX() {
        return (float) Display.getX();
    }

    public static int getWidth() {
        return WINDOW_INSTANCE.getWidth();
    }

    public static int getHeight() {
        return WINDOW_INSTANCE.getHeight();
    }

    public static void setResizable(boolean resizable) {
        Display.setResizable(resizable);
    }

    public static void setMouseGrabbed(boolean grabbed) {
        WINDOW_INSTANCE.setMouseGrabbed(grabbed);
    }

}
