package world;

import assets.Assets;
import assets.definitions.Definitions;
import misc.MiscMath;
import org.newdawn.slick.Color;
import world.entities.Entity;
import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.ChunkType;

import java.util.ArrayList;
import java.util.HashMap;

public class Chunk {

    public static final int TILE_SIZE = 16;
    public static final int CHUNK_SIZE = 13;

    private int[] coordinates;
    private byte[][] base;
    private byte[][] top;
    private HashMap<Integer, Portal> portals;
    private ArrayList<Entity> entities;

    public Chunk(int x, int y, ChunkType type) {

        this.coordinates = new int[]{x, y};
        this.base = new byte[CHUNK_SIZE][CHUNK_SIZE];
        this.top = new byte[CHUNK_SIZE][CHUNK_SIZE];
        this.portals = new HashMap<>();

        this.entities = new ArrayList<>();

        ChunkGenerator generator = ChunkGenerator.get(type);

        for (int j = 0; j < CHUNK_SIZE; j++) {
            for (int i = 0; i < CHUNK_SIZE; i++) {
                base[i][j] = generator.getBase(i, j);
                top[i][j] = generator.getTop(i, j);
                Portal p = generator.getPortal(i, j);
                if (p != null) {
                    p.setChunk(this);
                    p.setTileCoordinates(i, j);
                    portals.put(MiscMath.getTileIndex(i, j), p);
                }
            }
        }

    }

    public Portal getPortal(int tx, int ty) {
        return portals.get(MiscMath.getTileIndex(tx, ty));
    }

    public Portal findPortalTo(Region destination, String destination_name) {
        for (Portal p: portals.values())
            if (p.getDestination().equals(destination) && p.getDestinationName().equals(destination_name))
                return p;
        return null;
    }

    public void update() {
        for (int i = entities.size() - 1; i >= 0; i--)
            entities.get(i).update();
    }

    public void set(int x, int y, byte base, byte top) {
        this.base[x][y] = base;
        this.top[x][y] = top;
    }

    public byte[] get(int x, int y) {
        if (x < 0 || y < 0 || y >= Chunk.CHUNK_SIZE || x >= Chunk.CHUNK_SIZE)
            return new byte[2];
        return new byte[]{this.base[x][y], this.top[x][y]};
    }

    public void addEntity(Entity e) { entities.add(e); }
    public void removeEntity(Entity e) { entities.remove(e); }

    public int[] getCoordinates() { return coordinates; }

    public void draw(float sx, float sy, float scale, boolean active) {
        Color filter = new Color(0.3f, 0.3f, 0.3f);
        Color translucent = new Color(1f, 1f, 1f, 0.5f);
        double[] player_coords = World.getPlayer().getLocation().getCoordinates();
        for (int j = 0; j < CHUNK_SIZE; j++) {
            Assets.TILE_SPRITESHEET.startUse();
            for (int i = 0; i < CHUNK_SIZE; i++) {
                //Assets.TILES.setFilter(Image.FILTER_NEAREST);
                float ox = sx + (i * TILE_SIZE * scale);
                float oy = sy + ((j - ((Assets.TILE_SPRITESHEET.getHeight() / TILE_SIZE) - 1)) * TILE_SIZE * scale);
                float btx = base[i][j] * TILE_SIZE;
                float ttx = top[i][j] * TILE_SIZE;

                float height = Definitions.getTile(top[i][j]).getHeight();

                boolean reveal =
                        Math.abs(player_coords[0] - i) < 1
                        && j - player_coords[1] < height
                        && j - player_coords[1] > 0.25f
                        && active
                        && Definitions.getTile(top[i][j]).peeking();

                Assets.TILE_SPRITESHEET.drawEmbedded(
                        ox,
                        oy,
                        ox + (TILE_SIZE * scale),
                        oy + (Assets.TILE_SPRITESHEET.getHeight() * scale),
                        btx,
                        0,
                        btx + TILE_SIZE,
                        Assets.TILE_SPRITESHEET.getHeight(), active ? Color.white : filter);
                Assets.TILE_SPRITESHEET.drawEmbedded(
                        ox,
                        oy,
                        ox + (TILE_SIZE * scale),
                        oy + (Assets.TILE_SPRITESHEET.getHeight() * scale),
                        ttx,
                        0,
                        ttx + TILE_SIZE,
                        Assets.TILE_SPRITESHEET.getHeight(), reveal ? translucent : (active ? Color.white : filter));
            }
            Assets.TILE_SPRITESHEET.endUse();
            for (Entity e: entities) if ((int)(e.getLocation().getCoordinates()[1] + 0.5f) == j) e.draw(sx, sy, scale);
        }
    }

    public String debug() {
        return "cx: "+coordinates[0]+", cy: "+coordinates[1]+", e: "+entities.size();
    }

}
