package world.entities.pathfinding;

import assets.definitions.Definitions;
import assets.definitions.Tile;
import assets.definitions.TileDefinition;
import misc.Location;
import misc.MiscMath;
import world.Chunk;
import world.Region;
import world.generators.chunk.interiors.InteriorRoomGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class LocalPathFinder {

    private static HashMap<Integer, Node> nodeCache;
    private static Node startNode, targetNode;
    private static ArrayList<Node> open, closed;

    public static LinkedList<Location> findPath(Location start, int targetX, int targetY) {

        LinkedList<Location> path = new LinkedList<>();
        nodeCache = new HashMap<>();
        open = new ArrayList<>();
        closed = new ArrayList<>();

        startNode = getNode((int)start.getCoordinates()[0], (int)start.getCoordinates()[1], start.getRegion());
        targetNode = getNode(targetX, targetY, start.getRegion());
        open.add(startNode);

        if (startNode.equals(targetNode) || targetNode.getDScore() == Integer.MAX_VALUE) return path;

        while (!open.contains(targetNode)) {
            Node current = getBestOpen();
            ArrayList<Node> adjToBest = getAdjacentNodes(current);
            if (adjToBest.isEmpty()) break;
            open.remove(current);
            closed.add(current);
            for (Node adj: adjToBest) {
                if (closed.contains(adj)) continue;
                if (!open.contains(adj)) {
                    open.add(adj);
                    adj.setParent(current);
                } else {
                    Node old_parent = adj.getParent();
                    int old_G = adj.getFinalGScore();
                    adj.setParent(current);
                    if (adj.getGScore() > old_G) {
                        adj.setParent(old_parent);
                    }
                }
            }
        }

        //build the path
        for (Node n = targetNode; n != null; n = n.getParent()) {
            path.add(0, new Location(start.getRegion(), n.getX() + 0.5, n.getY() + 0.5));
        }

        return path;
    }

    private static Node getBestOpen() {
        double bestScore = Integer.MAX_VALUE;
        Node bestNode = null;
        for (Node n: open) {
            n.setHScore(getManhattanDistance(n, targetNode));
            if (n.getFScore() <= bestScore) {
                bestScore = n.getFScore();
                bestNode = n;
            }
        }
        return bestNode;
    }

    private static ArrayList<Node> getAdjacentNodes(Node node) {
        ArrayList<Node> adjacent = new ArrayList<>();
        if (node == null) return adjacent;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Math.abs(i) - Math.abs(j) == 0) continue;
                Node adj = getNode(node.getX() + i, node.getY() + j, node.getRegion());
                if (adj.getDScore() == Integer.MAX_VALUE) continue;
                adjacent.add(adj);
            }
        }
        return adjacent;
    }

    private static Node getNode(int wx, int wy, Region region) {
        Node adj = new Node(wx, wy, region);
        if (nodeCache.containsKey(adj.getLocationIndex())) {
            return nodeCache.get(adj.getLocationIndex());
        } else {
            nodeCache.put(adj.getLocationIndex(), adj);
            return adj;
        }
    }

    private static int getManhattanDistance(Node a, Node b) {
        return Math.abs((b.getX() - a.getX())) + Math.abs((b.getY() - a.getY()));
    }

}

class Node {

    private Node parent;
    private Region region;
    private int[] coordinates;
    private int G, H;

    public Node(int wx, int wy, Region region) {
        this.coordinates = new int[]{wx, wy};
        this.region = region;
        this.G = 1;
    }

    public int getX() { return coordinates[0]; }
    public int getY() { return coordinates[1]; }

    private boolean isOutOfBounds() {
        int max = region.getSize() * Chunk.CHUNK_SIZE;
        return coordinates[0] < 0 || coordinates[1] < 0
                || coordinates[0] > max || coordinates[1] > max;
    }

    public void setGScore(int g) { this.G = g; }
    public void setHScore(int h) { this.H = h; }

    public double getDScore() {
        byte[] tile = region.getTile(coordinates[0], coordinates[1]);
        TileDefinition base = Definitions.getTile(tile[0]);
        TileDefinition top = Definitions.getTile(tile[1]);
        if (base.collides() || top.collides() || tile[0] == Tile.AIR || isOutOfBounds()) return Integer.MAX_VALUE;
        return (int)(1 / (base.getSpeedMultiplier() * top.getSpeedMultiplier()));
    }

    public int getFinalGScore() { return (int)(G * getDScore()) + (parent != null ? parent.getGScore() : 0); }
    public int getGScore() { return G; }

    public int getFScore() { return getFinalGScore() + H; }

    public Node getParent() { return parent; }
    public void setParent(Node parent) { this.parent = parent; }

    public Region getRegion() { return region; }
    public int getLocationIndex() { return (int)MiscMath.getIndex(coordinates[0], coordinates[1],Chunk.CHUNK_SIZE * region.getSize()); }

    public String toString() { return "Node(X="+getX()+", Y = "+getY()+", G = "+G+", H = "+H+",F = "+getFScore()+")"; }

}