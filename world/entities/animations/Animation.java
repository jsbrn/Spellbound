package world.entities.animations;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.Chunk;
import world.World;

public class Animation {

    private Image sprite;
    private Color filter;
    private int frame_count, original_fps, fps;
    private float frame_width, frame_height;
    private long start_time;
    private boolean loop, directional;

    public Animation(String image, int fps, int frame_count, int frame_height, boolean loops, boolean directional, Color filter) {
        try {
            this.frame_count = frame_count;
            this.original_fps = fps;
            this.fps = original_fps;
            this.start_time = World.getCurrentTime();
            this.sprite = new Image("assets/animations/"+image, false, Image.FILTER_NEAREST);
            this.frame_width = this.sprite.getWidth() / (float)frame_count;
            this.frame_height = frame_height;
            this.loop = loops;
            this.directional = directional;
            this.filter = filter;
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private int getFrame() {
        int mills = (int)((World.getCurrentTime() - start_time) % 1000);
        int frame = (mills / (1000 / (frame_count * fps))) % frame_count;
        return frame;
    }

    public int loopCount() {
        long frame_duration = 1000 / fps;
        long time_since_start = World.getCurrentTime() - start_time;
        return (int)(time_since_start / frame_duration);
    }

    public boolean loops() { return loop; }

    public boolean finished() { return !loop && loopCount() > 0; }

    public void setColor(Color color) { filter = color; }

    public void reset() {
        this.start_time = World.getCurrentTime();
    }

    public void draw(float ex, float ey, float scale, int direction) {
        int frame = getFrame();
        sprite.startUse();
        float y_offset = -(frame_height * scale), x_offset = -0.5f * scale * (float)Chunk.TILE_SIZE;
        int src_y = (int)(!directional ? 0 : direction * frame_height);
        sprite.drawEmbedded(
                ex + x_offset,
                ey + y_offset,
                ex + (frame_width * scale) + x_offset,
                ey + (frame_height * scale) + y_offset,
                frame * frame_width,
                src_y,
                (frame + 1) * frame_width,
                src_y + frame_height,
                filter);
        sprite.endUse();
    }

}
