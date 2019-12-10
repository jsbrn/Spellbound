package world.entities.animations;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Animation {

    private Image sprite;
    private int frame_count, original_fps, fps;
    private float frame_width, frame_height;
    private long startTime;

    public Animation(String image, int frame_count, int fps) {
        try {
            this.frame_count = frame_count;
            this.original_fps = fps;
            this.fps = original_fps;
            this.sprite = new Image("assets/animations/"+image, false, Image.FILTER_NEAREST);
            this.frame_width = this.sprite.getWidth() / (float)frame_count;
            this.frame_height = this.sprite.getHeight();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        this.fps = original_fps;
    }

    public void play(int fps) {
        this.startTime = System.currentTimeMillis();
        this.fps = fps;
    }

    public void draw(float ex, float ey, float scale) {
        int mills = (int)((System.currentTimeMillis() - startTime) % 1000);
        int frame = mills / (1000 / (frame_count * fps));
        sprite.startUse();
        float y_offset = -(frame_height * scale / 2);
        sprite.drawEmbedded(
                ex,
                ey + y_offset,
                ex + (frame_width * scale),
                ey + (frame_height * scale) + y_offset,
                frame * frame_width,
                0,
                (frame + 1) * frame_width,
                frame_height);
        sprite.endUse();
    }

}
