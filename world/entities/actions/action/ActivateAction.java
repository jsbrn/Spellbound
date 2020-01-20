package world.entities.actions.action;

import misc.Location;
import misc.MiscMath;
import world.Portal;
import world.entities.Entity;
import world.entities.actions.Action;

public class ActivateAction extends Action {

    @Override
    public void onStart() {
        Entity parent = getParent();
        Location location = parent.getLocation();
        double[] offset = MiscMath.getRotatedOffset(0, -1, location.getLookDirection());
        double[] coords = new double[]{location.getCoordinates()[0] + offset[0], location.getCoordinates()[1] + offset[1]};

        Portal origin = location.getRegion().getPortal((int)coords[0], (int)coords[1]);
        System.out.println("Searching for portal at "+coords[0]+", "+coords[1]);
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
                    destination.getCoordinates()[1] + 0.5 + (destination.getExitDirection()[1])
            ));
//            parent.getLocation().setLookDirection((int)MiscMath.angleBetween(
//                    parent.getLocation().getCoordinates()[0],
//                    parent.getLocation().getCoordinates()[1],
//                    parent.getLocation().getCoordinates()[0] + destination.getExitDirection()[0],
//                    parent.getLocation().getCoordinates()[0] + destination.getExitDirection()[1]
//            ));
            return;
        }

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return true;
    }

}
