package world;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import misc.Location;
import network.MPClient;
import network.MPServer;
import org.json.simple.JSONObject;
import world.entities.components.LocationComponent;
import world.entities.systems.RenderSystem;
import world.events.event.ChunkGeneratedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chunk {

    public static final int TILE_SIZE = 16;
    public static final int CHUNK_SIZE = 15;

    private Region region;
    private ArrayList<Integer> cachedEntities;
    private int[] coordinates;

    private byte[][] base;
    private byte[][] top;

    private boolean generated;

    public Chunk(int x, int y, Region region) {
        this.region = region;
        this.coordinates = new int[]{x, y};
        this.base = new byte[CHUNK_SIZE][CHUNK_SIZE];
        this.top = new byte[CHUNK_SIZE][CHUNK_SIZE];
        this.cachedEntities = new ArrayList<>();
    }

    public void generate() {
        if (generated) return;
        for (int i = 0; i < base.length; i++) {
            for (int j = 0; j < base[0].length; j++) {
                int[] wc = new int[]{ (coordinates[0] * CHUNK_SIZE) + i, (coordinates[0] * CHUNK_SIZE) + j };
                base[i][j] = region.getGenerator().getBase(wc[0], wc[1]);
                top[i][j] = region.getGenerator().getTop(wc[0], wc[1]);
                JSONObject entityDefinition = region.getGenerator().getEntity(wc[0], wc[1]);
                if (entityDefinition == null) continue;
                //TODO: fix chunk entity natural generation
                //int newEntityID = MPServer.getWorld().getEntities().addEntity(entityDefinition);
            }
        }
        MPServer.getEventManager().invoke(new ChunkGeneratedEvent(this));
        generated = true;
    }

    public boolean isEmpty() {
        return !generated;
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

    public int compareTo(int cx, int cy) {
        if (this.coordinates[0] > cx) return 1;
        if (this.coordinates[0] < cx) return -1;
        if (this.coordinates[1] > cy) return 1;
        if (this.coordinates[1] < cy) return -1;
        return 0;
    }

    public Region getRegion() {
        return region;
    }

    public byte[][] getBase() { return base; }
    public byte[][] getTop() { return top; }

    public void cacheEntity(int entityID) {
        if (!cachedEntities.contains(entityID)) cachedEntities.add(entityID);
    }

    public List<Integer> getCachedEntities() {
        return cachedEntities.stream().filter(eid -> {
            LocationComponent lc = (LocationComponent)region.getWorld().getEntities().getComponent(LocationComponent.class, eid);
            if (lc == null) return false;
            Location loc = lc.getLocation();
            if (loc == null) return false;
            return loc.getChunkCoordinates()[0] == coordinates[0] && loc.getChunkCoordinates()[1] == coordinates[1];
        }).collect(Collectors.toList());
    }

    public void drawBase(float osx, float osy, float scale) {
        for (int j = 0; j < CHUNK_SIZE; j++) {
            for (int i = 0; i < CHUNK_SIZE; i++) {

                float ox = osx + (i * TILE_SIZE * scale);
                float oy = osy + ((j - ((Assets.TILE_SPRITESHEET.getHeight() / TILE_SIZE) - 1)) * TILE_SIZE * scale);
                float btx = base[i][j] * TILE_SIZE;

                Assets.TILE_SPRITESHEET.startUse();
                Assets.TILE_SPRITESHEET.drawEmbedded(
                        (int)ox,
                        (int)oy,
                        (int)ox + (TILE_SIZE * scale),
                        (int)oy + (Assets.TILE_SPRITESHEET.getHeight() * scale),
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
        Location player_location = Camera.getLocation();
        double[] playerLocalCoords = player_location.getLocalCoordinates();
        int[] playerChunkCoords = player_location.getChunkCoordinates();

        for (int j = 0; j < CHUNK_SIZE; j++) {
            for (int i = 0; i < CHUNK_SIZE; i++) {

                float ox = osx + (i * TILE_SIZE * scale);
                float oy = osy + ((j - ((Assets.TILE_SPRITESHEET.getHeight() / TILE_SIZE) - 1)) * TILE_SIZE * scale);
                float ttx = top[i][j] * TILE_SIZE;

                boolean reveal =
                        (int)playerLocalCoords[0] == i
                                && playerChunkCoords[0] == coordinates[0]
                                && playerChunkCoords[1] == coordinates[1]
                                && j - playerLocalCoords[1] < Tiles.getHeight(top[i][j]) - 1
                                && j - playerLocalCoords[1] > 0
                                && Tiles.peeking(top[i][j]);

                Assets.TILE_SPRITESHEET.startUse();
                Assets.TILE_SPRITESHEET.drawEmbedded(
                        (int)ox,
                        (int)oy,
                        (int)ox + (TILE_SIZE * scale),
                        (int)oy + (Assets.TILE_SPRITESHEET.getHeight() * scale),
                        ttx,
                        0,
                        ttx + TILE_SIZE,
                        Assets.TILE_SPRITESHEET.getHeight(), reveal ? translucent : Color.white);
                Assets.TILE_SPRITESHEET.endUse();

                List<Integer> entities = region.getEntities((coordinates[0] * CHUNK_SIZE) + i, (coordinates[1] * CHUNK_SIZE) + j, 1, 1);
                for (Integer entityID: entities) RenderSystem.drawEntity(MPClient.getWorld().getEntities(), entityID, scale);

            }
        }


    }

    public void drawDebug(float osx, float osy, float scale, Graphics g) {
        for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
            for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
                g.setLineWidth(j == 0 && i == 0 ? 3 : 1);
                g.setColor(Color.white);
                g.drawLine(osx + (i * TILE_SIZE * scale), osy, osx + (i * TILE_SIZE * scale), osy + (CHUNK_SIZE * scale * TILE_SIZE));
                g.drawLine(osx, osy + (j * TILE_SIZE * scale), osx + (CHUNK_SIZE * TILE_SIZE * scale), osy + (j * TILE_SIZE * scale));
                //g.setColor(new Color(0, 0, 0, 1-(float)World.getLocalPlayer().canSee(
                //        (coordinates[0] * Chunk.CHUNK_SIZE) + i,
                //        (coordinates[1] * Chunk.CHUNK_SIZE) + j)));
                //g.fillRect(osx + (i * TILE_SIZE * scale), osy + (j * TILE_SIZE * scale), TILE_SIZE * scale, TILE_SIZE * scale);
            }
        }
        g.setColor(Color.white);
        g.setLineWidth(1);
    }

    public String debug() {
        return "cx: "+coordinates[0]+", cy: "+coordinates[1];
    }

}
