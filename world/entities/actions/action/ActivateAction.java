package world.entities.actions.action;

import misc.Location;
import misc.MiscMath;
import world.Portal;
import world.entities.Entity;
import world.entities.actions.Action;
import world.entities.types.humanoids.Player;
import world.events.EventDispatcher;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;

import java.util.ArrayList;

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
            origin.getDestination().forceLoadChunks();
            Portal destination = origin.getDestination().findPortalTo(location.getRegion(), origin.getName());
            System.out.println("Destination portal is "+destination);
            parent.getMover().stop();
            parent.moveTo(new Location(
                    origin.getDestination(),
                    destination.getCoordinates()[0] + 0.5 + (0.75 * destination.getExitDirection()[0]),
                    destination.getCoordinates()[1] + 0.5 + (0.75 * destination.getExitDirection()[1])));
            parent.queueAction(new MoveAction(
                    destination.getCoordinates()[0] + 0.5 + (destination.getExitDirection()[0]),
                    destination.getCoordinates()[1] + 0.5 + (destination.getExitDirection()[1]),
                    false,
                    true
            ));
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
