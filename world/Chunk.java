package world;

import assets.Assets;
import assets.definitions.Definitions;
import misc.Location;
import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.generators.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class Chunk {

    public static final int TILE_SIZE = 16;
    public static final int CHUNK_SIZE = 13;

    private int[] coordinates;
    private byte[][] base;
    private byte[][] top;
    private HashMap<Integer, Portal> portals;

    public Chunk(int x, int y, ChunkGenerator generator) {

        this.coordinates = new int[]{x, y};
        this.base = new byte[CHUNK_SIZE][CHUNK_SIZE];
        this.top = new byte[CHUNK_SIZE][CHUNK_SIZE];
        this.portals = new HashMap<>();

        for (int j = 0; j < CHUNK_SIZE; j++) {
            for (int i = 0; i < CHUNK_SIZE; i++) {
                base[i][j] = generator.getBase(i, j);
                top[i][j] = generator.getTop(i, j);
                Portal p = generator.getPortal(i, j);
                if (p != null) {
                    p.setChunk(this);
                    p.setTileCoordinates(i, j);
                    portals.put(MiscMath.getIndex(i, j, Chunk.CHUNK_SIZE), p);
                }
            }
        }

    }

    public Portal getPortal(int tx, int ty) {
        return portals.get(MiscMath.getIndex(tx, ty, Chunk.CHUNK_SIZE));
    }

    public Portal findPortalTo(Region destination, String destination_name) {
        for (Portal p: portals.values())
            if (p.getDestination().equals(destination) && p.getDestinationName().equals(destination_name))
                return p;
        return null;
    }

    public void set(int x, int y, byte base, byte top) {
        this.base[x][y] = base;
        this.top[x][y] = top;
    }

    public byte[] get(int tx, int ty) {
        if (tx < 0 || ty < 0 || ty >= Chunk.CHUNK_SIZE || tx >= Chunk.CHUNK_SIZE)
            return new byte[2];
        return new byte[]{this.base[tx][ty], this.top[tx][ty]};
    }

    public int[] getCoordinates() { return coordinates; }

    public void drawBase(float osx, float osy, float scale) {
        for (int j = 0; j < CHUNK_SIZE; j++) {
            for (int i = 0; i < CHUNK_SIZE; i++) {

                float ox = osx + (i * TILE_SIZE * scale);
                float oy = osy + ((j - ((Assets.TILE_SPRITESHEET.getHeight() / TILE_SIZE) - 1)) * TILE_SIZE * scale);
                float btx = base[i][j] * TILE_SIZE;

                Assets.TILE_SPRITESHEET.startUse();
                Assets.TILE_SPRITESHEET.drawEmbedded(
                        ox,
                        oy,
                        ox + (TILE_SIZE * scale),
                        oy + (Assets.TILE_SPRITESHEET.getHeight() * scale),
                        btx,
                        0,
                        btx + TILE_SIZE,
                        Assets.TILE_SPRITESHEET.getHeight(), Color.white);
                Assets.TILE_SPRITESHEET.endUse();
            }
        }
    }

    public void drawTop(float osx, float osy, float scale) {
        Color translucent = new Color(1f, 1f, 1f, 0.5f);
        Location player_location = World.getPlayer().getLocation();
        double[] player_coords = player_location.getLocalCoordinates();


        for (int j = 0; j < CHUNK_SIZE; j++) {
            for (int i = 0; i < CHUNK_SIZE; i++) {

                float ox = osx + (i * TILE_SIZE * scale);
                float oy = osy + ((j - ((Assets.TILE_SPRITESHEET.getHeight() / TILE_SIZE) - 1)) * TILE_SIZE * scale);
                float ttx = top[i][j] * TILE_SIZE;

                float height = Definitions.getTile(top[i][j]).getHeight();

                boolean reveal =
                        Math.abs(player_coords[0] - i) < 1
                                && j - player_coords[1] < height
                                && j - player_coords[1] > 0.25f
                                && Definitions.getTile(top[i][j]).peeking();
                Assets.TILE_SPRITESHEET.startUse();
                Assets.TILE_SPRITESHEET.drawEmbedded(
                        ox,
                        oy,
                        ox + (TILE_SIZE * scale),
                        oy + (Assets.TILE_SPRITESHEET.getHeight() * scale),
                        ttx,
                        0,
                        ttx + TILE_SIZE,
                        Assets.TILE_SPRITESHEET.getHeight(), reveal ? translucent : Color.white);
                Assets.TILE_SPRITESHEET.endUse();

                int[] range = player_location.getRegion().getEntityIndices(MiscMath.getIndex(
                        (int)((coordinates[0] * CHUNK_SIZE) + i),
                        (int)((coordinates[1] * CHUNK_SIZE) + j),
                        CHUNK_SIZE * player_location.getRegion().getSize()
                ));

                for (int eindex = range[0]; eindex < range[1]; eindex++) {
                    Entity e = player_location.getRegion().getEntities().get(eindex);
                    float[] eosc = Camera.getOnscreenCoordinates(e.getLocation().getCoordinates()[0], e.getLocation().getCoordinates()[1], scale);
                    e.draw(eosc[0], eosc[1], scale);
                }

            }
        }


    }

    public void drawEntities(float osx, float osy, float scale) {
        Location player_location = World.getPlayer().getLocation();
        for (int j = 0; j < CHUNK_SIZE; j++) {


        }
    }

    public void drawDebug(float osx, float osy, float scale, Graphics g) {
        g.setColor(Color.white);
        for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
            for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
                g.drawRect(osx + (i * TILE_SIZE * scale), osy + (j * TILE_SIZE * scale), TILE_SIZE * scale, TILE_SIZE * scale);
            }
        }
    }

    public String debug() {
        return "cx: "+coordinates[0]+", cy: "+coordinates[1];
    }

}
