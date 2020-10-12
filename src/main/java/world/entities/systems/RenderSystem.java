package world.entities.systems;

import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import misc.MiscMath;
import misc.Window;
import misc.annotations.ClientExecution;
import world.Camera;
import world.Chunk;
import world.entities.Entities;
import world.entities.components.AnimatorComponent;
import world.entities.components.HitboxComponent;
import world.entities.components.LocationComponent;
import world.entities.components.animations.Animation;
import world.entities.components.animations.AnimationLayer;

public class RenderSystem {

    @ClientExecution
    public static void drawEntity(Entities entities, int entityID, float scale) {
        HitboxComponent hitbox = (HitboxComponent) entities.getComponent(HitboxComponent.class, entityID);
        LocationComponent location = (LocationComponent) entities.getComponent(LocationComponent.class, entityID);
        AnimatorComponent animator = (AnimatorComponent) entities.getComponent(AnimatorComponent.class, entityID);
        float[] osc = Camera.getOnscreenCoordinates(
                location.getLocation().getCoordinates()[0],
                location.getLocation().getCoordinates()[1], Window.getScale());

        if (animator == null) return;

        for (AnimationLayer layer: animator.getLayers()) {
            Animation a = layer.getCurrentAnimation(animator.getActiveAnimations());
            if (a == null) continue;
            int lookDir = (int)MiscMath.round(location.getLocation().getLookDirection(), 45) / 45;
            a.draw(osc[0], osc[1], scale, lookDir, Color.white);
        }
    }

    @ClientExecution
    public static void drawEntityDebug(Entities entities, int entityID, float scale, Graphics g) {
        g.setColor(Color.red);
        HitboxComponent hitbox = (HitboxComponent) entities.getComponent(HitboxComponent.class, entityID);
        LocationComponent location = (LocationComponent) entities.getComponent(LocationComponent.class, entityID);
        float[] osc = Camera.getOnscreenCoordinates(
                location.getLocation().getCoordinates()[0],
                location.getLocation().getCoordinates()[1], scale);
        g.fillRect(osc[0] - 2, osc[1] - 2, 4, 4);
        float oscRad = (float)hitbox.getRadius() * (float)Chunk.TILE_SIZE * scale;
        g.drawRect(osc[0] - oscRad, osc[1] - oscRad, oscRad * 2, oscRad * 2);
        g.setColor(Color.blue);
        double[] lineEnd = MiscMath.getRotatedOffset(0, -1, location.getLocation().getLookDirection());
        g.drawLine(osc[0], osc[1], osc[0] + ((float)lineEnd[0] * Chunk.TILE_SIZE * scale), osc[1] + ((float)lineEnd[1] * Chunk.TILE_SIZE * scale));
        g.setColor(Color.white);
        g.drawString(entityID+"", osc[0] - (0.5f * Chunk.TILE_SIZE * scale), osc[1] - (Chunk.TILE_SIZE * scale * 1.5f));
    }

}
