package misc;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;

import java.io.File;
import java.util.Calendar;

public class Window {

    //the name of the window
    public static final String WINDOW_TITLE = "A Mage's Quest";
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
            Image scrn = new Image(Window.getWidth(), Window.getHeight());
            String file_url = System.getProperty("user.home") + "/secret/screenshots/"
                    + year + "" + month + "" + day;
            g.copyArea(scrn, 0, 0);
            //make screenshots folder
            if (new File(System.getProperty("user.home") + "/secret/screenshots/").exists() == false) {
                new File(System.getProperty("user.home") + "/secret/screenshots/").mkdir();
            }
            //check if image_exists already
            if (new File(file_url + ".png").exists()) {
                int count = 2;
                while (true) {
                    if (new File(file_url + "_" + count + ".png").exists() == false) {
                        file_url += "_" + count;
                        break;
                    }
                    count++;
                }
            }
            ImageOut.write(scrn, file_url + ".png");
            System.out.println("Saved screenshot to " + file_url + ".png");
        } catch (SlickException ex) {

        }
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
