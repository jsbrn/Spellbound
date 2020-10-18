package world.entities.components.animations;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Image;
import com.github.mathiewz.slick.SlickException;
import network.MPClient;
import org.json.simple.JSONObject;

import java.io.Serializable;

public class Animation implements Serializable {

    private Image sprite;
    private String sprite_url;
    private int frame_count, fps;
    private float frame_width, frame_height;
    private long start_time;
    private boolean loop, directional;

    public Animation(String image, int fps, int frame_count, int frame_height, boolean loops, boolean directional) {
        try {
            this.frame_count = frame_count;
            this.fps = fps;
            this.start_time = MPClient.getTime();
            this.sprite_url = image;
            this.frame_height = frame_height;
            this.loop = loops;
            this.directional = directional;
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public Animation(String image, int fps, int frame_width, int frame_height, Color filter) {
        this(image, fps, -1, frame_height, false, true);
        this.frame_width = frame_width;
        this.frame_count = sprite.getWidth() / frame_width;
    }

    private int getFrame() {
        int mills = (int)((MPClient.getTime() - start_time) % 1000);
        int frame = (mills / (1000 / (frame_count * fps))) % frame_count;
        return frame;
    }

    public int loopCount() {
        long frame_duration = 1000 / fps;
        long time_since_start = MPClient.getTime() - start_time;
        return (int)(time_since_start / frame_duration);
    }

    public boolean loops() { return loop; }

    public boolean finished() { return !loop && loopCount() > 0; }

    public void reset() {
        this.start_time = MPClient.getTime();
    }

    public static Animation deserialize(JSONObject json) {
        Animation a = new Animation(
                "animations/"+(String)json.get("type")+"/"+(String)json.get("name")+".png",
                ((Number)json.get("fps")).intValue(),
                ((Number)json.get("frame_count")).intValue(),
                ((Number)json.get("frame_height")).intValue(),
                (boolean)json.getOrDefault("loop", true),
                (boolean)json.getOrDefault("directional", false)
        );
        return a;
    }

    public void draw(float ex, float ey, float scale, int direction, Color filter) {

        //initialize the sprite when drawing
        if (sprite == null) {
            this.sprite = Assets.getImage(sprite_url);
            this.frame_width = this.sprite.getWidth() / (float)frame_count;
        }

        int frame = getFrame();
        sprite.startUse();
        float y_offset = -(frame_height/2 * scale), x_offset = -(frame_width/2 * scale);
        int src_y = (int)(!directional ? 0 : direction * frame_height);
        sprite.drawEmbedded(
                (int)(ex + x_offset),
                (int)(ey + y_offset),
                (int)(ex + (frame_width * scale) + x_offset),
                (int)(ey + (frame_height * scale) + y_offset),
                frame * frame_width,
                src_y,
                (frame + 1) * frame_width,
                src_y + frame_height,
                filter);
        sprite.endUse();
    }

}
