package world;

import assets.Assets;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.generators.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Chunk {

    public static final int TILE_SIZE = 16;
    public static final int CHUNK_SIZE = 13;

    private Region region;
    private int[] coordinates;

    private byte[][] base;
    private byte[][] top;

    private Color mapColor;

    public Chunk(int x, int y, Region region) {
        this.region = region;
        this.coordinates = new int[]{x, y};
        this.base = new byte[CHUNK_SIZE][CHUNK_SIZE];
        this.top = new byte[CHUNK_SIZE][CHUNK_SIZE];
    }

    protected void generate(ChunkGenerator generator, boolean spawnEntities) {
        generator.setChunkX(coordinates[0]);
        generator.setChunkY(coordinates[1]);
        mapColor = generator.getColor();
        base = generator.getTiles(false);
        top = generator.getTiles(true);

        for (int j = 0; j < CHUNK_SIZE; j++) {
            for (int i = 0; i < CHUNK_SIZE; i++) {
                int wx = (coordinates[0] * Chunk.CHUNK_SIZE) + i, wy = (coordinates[1] * Chunk.CHUNK_SIZE) + j;
                Entity e = generator.getEntity(i, j);
                if (e != null && spawnEntities) e.moveTo(new Location(region, wx + 0.5, wy + 0.5));
            }
        }

    }

    public void update() {
        ArrayList<Entity> entities = region
                .getEntities((coordinates[0] * CHUNK_SIZE), (coordinates[1] * CHUNK_SIZE), CHUNK_SIZE, CHUNK_SIZE);
        for (int i = entities.size() - 1; i >= 0; i--) entities.get(i).update();
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

    public Color getMapColor() {
        return mapColor;
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
        Location player_location = World.getLocalPlayer().getLocation();
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

                ArrayList<Entity> entities = region.getEntities((coordinates[0] * CHUNK_SIZE) + i, (coordinates[1] * CHUNK_SIZE) + j, 1, 1);
                int pass = 0;
                for (int p = 0; p < 2; p++) {
                    int finalP = p;
                    for (Entity e : entities.stream().filter(e -> finalP == 0 ? e.isTile() : !e.isTile()).collect(Collectors.toList())) {
                        float[] eosc = Camera.getOnscreenCoordinates(e.getLocation().getCoordinates()[0], e.getLocation().getCoordinates()[1], scale);
                        int padding = (int) (4 * Chunk.TILE_SIZE * scale);
                        if (MiscMath.pointIntersectsRect(
                                eosc[0], eosc[1],
                                -padding, -padding,
                                Window.getWidth() + (2 * padding), Window.getHeight() + (2 * padding)))
                            e.draw(eosc[0], eosc[1], scale);
                    }
                }

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
                g.setColor(new Color(0, 0, 0, 1-(float)World.getLocalPlayer().canSee(
                        (coordinates[0] * Chunk.CHUNK_SIZE) + i,
                        (coordinates[1] * Chunk.CHUNK_SIZE) + j)));
                g.fillRect(osx + (i * TILE_SIZE * scale), osy + (j * TILE_SIZE * scale), TILE_SIZE * scale, TILE_SIZE * scale);
            }
        }
        g.setColor(Color.white);
        g.setLineWidth(1);
    }

    public String debug() {
        return "cx: "+coordinates[0]+", cy: "+coordinates[1];
    }

}
