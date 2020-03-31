package misc;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import world.Chunk;

import java.io.File;
import java.util.Calendar;

public class Window {

    //the name of the window
    public static final String WINDOW_TITLE = "Spellbound - Demo";
    //create a window object
    public static AppGameContainer WINDOW_INSTANCE;

    private static int last_width = 0, last_height = 0;

    public static void toggleFullScreen() throws SlickException {
        if (Display.isFullscreen()) {
            Window.WINDOW_INSTANCE.setDisplayMode(last_width, last_height, false);
        } else {
            try {
                last_width = Window.getWidth();
                last_height = Window.getHeight();
                Window.WINDOW_INSTANCE.setDisplayMode(1920, 1080, false);
                Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isFullScreen() {
        return WINDOW_INSTANCE.isFullscreen();
    }

    public static void takeScreenshot(Graphics g) {
        try {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int hour = (int)(System.currentTimeMillis() / 1000 / 60 / 60) % 24;
            int minute = (int)(System.currentTimeMillis() / 1000 / 60) % 60;
            int second = (int)(System.currentTimeMillis() / 1000) % 60;
            int mills = (int)(System.currentTimeMillis() % 1000);
            Image scrn = new Image(Window.getWidth(), Window.getHeight());
            String folder = System.getProperty("user.home") + "/Desktop/";
            String file_url = folder + "Spellbound-" + year + "-" + month + "-" + day+"-"+hour+"-"+minute+"-"+second+"-"+mills;
            g.copyArea(scrn, 0, 0);
            //make screenshots folder
            if (new File(System.getProperty("user.home") + "/Desktop/").exists() == false) {
                new File(System.getProperty("user.home") + "/Desktop/").mkdir();
            }
            ImageOut.write(scrn, file_url + ".png");
            System.out.println("Saved screenshot to " + file_url + ".png");
        } catch (SlickException ex) {

        }
    }

    public static float getScale() {
        return (float)(Window.getHeight() / (Chunk.CHUNK_SIZE * Chunk.TILE_SIZE)) + 1;
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
