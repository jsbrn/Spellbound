package world.entities.systems;

import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import misc.Window;
import world.Camera;
import world.entities.Entities;
import world.entities.components.HitboxComponent;
import world.entities.components.LocationComponent;

public class RenderSystem {

    public static void draw(int entityID, Graphics g) {
        HitboxComponent hitbox = (HitboxComponent)Entities.getComponent(HitboxComponent.class, entityID);
        LocationComponent location = (LocationComponent) Entities.getComponent(LocationComponent.class, entityID);
        float[] osc = Camera.getOnscreenCoordinates(location.getLocation().getCoordinates()[0], location.getLocation().getCoordinates()[1], Window.getScale());
        g.setColor(Color.blue);
        g.drawOval((float)(osc[0] - hitbox.getRadius()), (float)(osc[1] - hitbox.getRadius()), (float)(hitbox.getRadius() * 2), (float)(hitbox.getRadius() * 2));
    }

}
