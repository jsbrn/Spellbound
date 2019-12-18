package world.entities.animations;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Animation {

    private Image sprite;
    private int frame_count, original_fps, fps;
    private float frame_width, frame_height;
    private long start_time;
    private boolean loop;

    public Animation(String image, int frame_count, int fps) {
        try {
            this.frame_count = frame_count;
            this.original_fps = fps;
            this.fps = original_fps;
            this.start_time = 0;
            this.sprite = new Image("assets/animations/"+image, false, Image.FILTER_NEAREST);
            this.frame_width = this.sprite.getWidth() / (float)frame_count;
            this.frame_height = this.sprite.getHeight();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private int getFrame() {
        int mills = (int)((System.currentTimeMillis() - start_time) % 1000);
        int frame = (mills / (1000 / (frame_count * fps))) % frame_count;
        return frame;
    }

    public int loopCount() {
        long frame_duration = 1000 / fps;
        long time_since_start = System.currentTimeMillis() - start_time;
        return (int)(time_since_start / frame_duration);
    }

    public boolean finished() { return !loop && loopCount() > 0; }

    public void reset() {
        this.start_time = System.currentTimeMillis();
    }

    public void draw(float ex, float ey, float scale) {
        int frame = getFrame();
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
