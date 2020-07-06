package world.entities.systems;

import assets.Assets;
import com.github.mathiewz.slick.Graphics;
import misc.Window;
import world.Camera;
import world.Chunk;
import world.World;
import world.entities.Entities;
import world.entities.components.HitboxComponent;
import world.entities.components.LocationComponent;

public class RenderSystem {

    public static void drawEntity(Entities entities, int entityID, float scale) {
        HitboxComponent hitbox = (HitboxComponent) entities.getComponent(HitboxComponent.class, entityID);
        LocationComponent location = (LocationComponent) entities.getComponent(LocationComponent.class, entityID);
        float[] osc = Camera.getOnscreenCoordinates(
                location.getLocation().getCoordinates()[0] - hitbox.getRadius(),
                location.getLocation().getCoordinates()[1] - hitbox.getRadius(), Window.getScale());
        float renderWidth = 1; //TODO: separate entity dimensions from hitbox
        float renderHeight = 2;
        Assets.getImage("character_test.png").draw(
                osc[0], osc[1],
                renderWidth * Chunk.TILE_SIZE * scale,
                renderHeight * Chunk.TILE_SIZE * scale);
    }

    public static void drawEntityDebug(Entities entities, int entityID, float scale, Graphics g) {

    }

}
