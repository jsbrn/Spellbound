package world.entities.ai.actions.types;

import assets.Settings;
import gui.sound.SoundManager;
import misc.Location;
import misc.MiscMath;
import world.Portal;
import world.World;
import world.entities.ai.actions.Action;
import world.events.EventDispatcher;
import world.events.event.EntityActivatedEvent;
import world.events.event.EntityChangeRegionEvent;

public class ActivateAction extends Action {

    @Override
    public void onStart() {
        Entity parent = getParent();
        Location location = parent.getLocation();
        double[] offset = MiscMath.getRotatedOffset(0, -1, location.getLookDirection());
        double[] activateCoords = new double[]{location.getCoordinates()[0] + offset[0], location.getCoordinates()[1] + offset[1]};

        Portal origin = location.getRegion().getPortal((int)activateCoords[0], (int)activateCoords[1]);
        System.out.println("Searching for portal at "+activateCoords[0]+", "+activateCoords[1]);
        if (origin != null) {

            if (origin.isEntranceDirectional()
                    && !(MiscMath.clamp(origin.getExitDirection()[0], -1, 1) == -offset[0]
                    && MiscMath.clamp(origin.getExitDirection()[1], -1, 1) == -offset[1])) return;

            System.out.println("Found portal leading to "+origin.getDestination()+", "+origin.getDestinationName());
            origin.getDestination().plan();
            Portal destination = origin.getDestination().findPortal(origin.getDestinationName(), location.getRegion(), origin.getName());
            System.out.println("Destination portal is "+destination);
            parent.getMover().stop();
            parent.moveTo(new Location(
                    origin.getDestination(),
                    destination.getCoordinates()[0] + 0.5 + (0.75 * destination.getExitDirection()[0]),
                    destination.getCoordinates()[1] + 0.5 + (0.75 * destination.getExitDirection()[1])));
            SoundManager.playSound(origin.getSound(), 1.0f, parent.getLocation());
            parent.getActionQueue().queueAction(new MoveAction(
                    destination.getCoordinates()[0] + 0.5 + (destination.getExitDirection()[0]),
                    destination.getCoordinates()[1] + 0.5 + (destination.getExitDirection()[1]),
                    false,
                    true
            ) {
                @Override
                public void onFinish() {
                    super.onFinish();
                    if (Settings.getBoolean("autosave")) World.save();
                }
            });
            EventDispatcher.invoke(new EntityChangeRegionEvent(destination.getDestination(), origin.getDestination(), parent));
            return;

        }

        Entity at = location.getRegion().getEntity(activateCoords[0], activateCoords[1]);
        if (at != null) EventDispatcher.invoke(new EntityActivatedEvent(at, getParent()));

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return true;
    }

}
