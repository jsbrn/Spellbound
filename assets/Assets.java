package assets;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Assets {

    public static int GAME_SCREEN = 0;
    public static Image TILES;

    public static void loadTileSprite() {
        try {
            TILES = new Image("assets/tiles.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

}
