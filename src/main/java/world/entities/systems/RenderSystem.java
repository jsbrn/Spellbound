package world.entities.systems;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import misc.Window;
import misc.annotations.ClientExecution;
import world.Camera;
import world.Chunk;
import world.World;
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
                location.getLocation().getCoordinates()[0] - hitbox.getRadius(),
                location.getLocation().getCoordinates()[1] - hitbox.getRadius(), Window.getScale());

        if (animator == null) return;

        for (AnimationLayer layer: animator.getLayers()) {
            Animation a = layer.getCurrentAnimation();
            if (a == null) continue;
            a.draw(osc[0], osc[1], Window.getScale(), location.getLocation().getLookDirection(), Color.white);
        }


        Assets.getImage("gui/cursor.png").draw(
                osc[0], osc[1],
                Chunk.TILE_SIZE * scale,
                Chunk.TILE_SIZE * scale);
    }

    public static void drawEntityDebug(Entities entities, int entityID, float scale, Graphics g) {
        g.setColor(Color.red);
        HitboxComponent hitbox = (HitboxComponent) entities.getComponent(HitboxComponent.class, entityID);
        LocationComponent location = (LocationComponent) entities.getComponent(LocationComponent.class, entityID);
        float[] osc = Camera.getOnscreenCoordinates(
                location.getLocation().getCoordinates()[0],
                location.getLocation().getCoordinates()[1], Window.getScale());
        g.fillRect(osc[0] - 2, osc[1] - 2, 4, 4);
        float oscRad = (float)hitbox.getRadius() * (float)Chunk.TILE_SIZE * Window.getScale();
        g.drawRect(osc[0] - oscRad, osc[1] - oscRad, oscRad * 2, oscRad * 2);
    }

}
