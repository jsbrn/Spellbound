package world;

import assets.Assets;
import misc.MiscMath;
import misc.Window;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.github.mathiewz.slick.Graphics;
import com.github.mathiewz.slick.Sound;
import world.entities.Entity;
import world.magic.MagicSource;
import world.events.EventDispatcher;
import world.events.EventListener;
import world.events.event.EntityMovedEvent;
import world.generators.chunk.ChunkGenerator;
import world.generators.region.RegionGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Region {

    private String name;
    private Chunk[][] chunks;
    private ChunkGenerator[][] chunkGenerators;
    private int size;

    private ArrayList<MagicSource> magic_sources;
    private ArrayList<Portal> portals;
    private ArrayList<Entity> entities;

    private Sound backgroundAmbience;

    private long time;
    private RegionGenerator generator;

    public Region(String name, int size, RegionGenerator generator) {

        this.generator = generator;
        this.name = name;
        this.size = size;

        this.time = 0;

        this.backgroundAmbience = generator.getBackgroundAmbience();

        magic_sources = new ArrayList<>();
        entities = new ArrayList<>();
        portals = new ArrayList<>();

        EventDispatcher.register(new EventListener().on(EntityMovedEvent.class.toString(), e -> {
            EntityMovedEvent event = (EntityMovedEvent) e;
            if (entities.contains(event.getEntity())) {
                removeEntity(event.getEntity());
                addEntity(event.getEntity());
            }
        }));

    }

    public void plan() {
        if (chunkGenerators != null) return;
        chunks = new Chunk[size][size];
        chunkGenerators = new ChunkGenerator[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                chunkGenerators[i][j] = generator.getChunkGenerator(i, j, size);
                chunkGenerators[i][j].setChunkX(i);
                chunkGenerators[i][j].setChunkY(j);
                Portal p = chunkGenerators[i][j].getPortal();
                if (p != null) {
                    portals.add(p);
                    p.setCoordinates(p.getCoordinates()[0] + (i * Chunk.CHUNK_SIZE),p.getCoordinates()[1] + (j * Chunk.CHUNK_SIZE));
                }
            }
        }
    }

    public Sound getBackgroundAmbience() {
        return backgroundAmbience;
    }

    public long getCurrentTime() { return time; }

    public void addEntity(Entity e) {
        double lindex = e.getLocation().getGlobalIndex();
        int index = getEntityIndex(lindex, 0, entities.size());
        entities.add(index, e);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public Entity getEntity(double wx, double wy) {
        ArrayList<Entity> found = getEntities((int)(wx - 2), (int)(wy - 2), 4, 4);
        for (int i = found.size() - 1; i >= 0; i--) {
            Entity e = found.get(i);
            if (MiscMath.pointIntersectsRect(
                    wx,
                    wy,
                    e.getLocation().getCoordinates()[0] - 0.5,
                    e.getLocation().getCoordinates()[1] - 0.5,
                    1,
                    1
                )) {
                return e;
            }
        }
        return null;
    }

    public ArrayList<Entity> getEntities(int wx, int wy, int width, int height) {
        ArrayList<Entity> subsection = new ArrayList<>();
        double minloc = MiscMath.getIndex(wx, wy, Chunk.CHUNK_SIZE * getSize());
        double maxloc = MiscMath.getIndex(wx + width, wy + height, Chunk.CHUNK_SIZE * getSize());
        int[] indices = getEntityIndices(minloc, maxloc);
        for (int i = indices[0]; i < indices[1]; i++) {
            Entity e = entities.get(i);
            if (subsection.contains(e)) continue;
            boolean intersects = MiscMath.pointIntersectsRect(
                    e.getLocation().getCoordinates()[0],
                    e.getLocation().getCoordinates()[1],
                    wx,
                    wy,
                    width,
                    height
            );
            if (intersects) subsection.add(e);
        }
        return subsection;
    }

    public List<Entity> getEntities(double wx, double wy, double radius) {
        ArrayList<Entity> entities = getEntities(
                (int)(wx - radius), (int)(wy - radius),
                (int)((radius * 2) + 1), (int)((radius * 2) + 2));
        return entities.stream().filter(e -> MiscMath.circlesIntersect(
                e.getLocation().getCoordinates()[0],
                e.getLocation().getCoordinates()[1],
                e.getRadius(),
                wx,
                wy,
                radius)).collect(Collectors.toList());
    }

    public int[] getEntityIndices(double min_location, double max_location) {
        return new int[]{
                getEntityIndex(min_location - 1, 0, entities.size()),
                getEntityIndex(max_location + 1, 0, entities.size()),
        };
    }

    public ArrayList<Entity> getEntities() { return entities; }

    private int getEntityIndex(double location, int min, int max) {

        if (entities.isEmpty()) return 0;

        int half = min + ((max-min) / 2);
        double minloc = entities.get(min).getLocation().getGlobalIndex();
        double maxloc = entities.get(max-1).getLocation().getGlobalIndex();
        double halfloc = entities.get(half).getLocation().getGlobalIndex();

        if (location >= maxloc) return max;
        if (location <= minloc) return min;
        if (max - min == 1) return min + 1;

        if (halfloc > location) {
            return getEntityIndex(location, min, half);
        } else {
            return getEntityIndex(location, half, max);
        }

    }

    public List<MagicSource> getMagicSources(double wx, double wy, double radius) {
        List<MagicSource> outer = magic_sources.stream().filter(ms -> {
            double[] coords = ms.getBody().getLocation().getCoordinates();
            return MiscMath.circlesIntersect(coords[0], coords[1], ms.getBody().getDepthRadius() + ms.getBody().getReachRadius(), wx, wy, radius);
        }).collect(Collectors.toList()),
        inner = magic_sources.stream().filter(ms -> {
            double[] coords = ms.getBody().getLocation().getCoordinates();
            return MiscMath.circlesIntersect(coords[0], coords[1], ms.getBody().getReachRadius(), wx, wy, radius);
        }).collect(Collectors.toList());;
        return outer.stream().filter(ms -> !(inner.contains(ms) && !outer.contains(ms))).collect(Collectors.toList());
    }

    public void registerPortal(Portal portal) {
        portals.add(portal);
    }

    public Portal getPortal(int index) {
        return portals.get(index);
    }

    public Portal getPortal(int wx, int wy) {
        return portals.stream().filter(portal -> {
            int[] coordinates = portal.getCoordinates();
            return coordinates[0] == wx && coordinates[1] == wy;
        }).findFirst().orElse(null);
    }

    /**
     * Find the portal that links to the portal specified.
     * @param destination Region of destination portal.
     * @param destinationName Name of destination portal.
     * @return Portal if found, null if not
     */
    public Portal findPortal(String portalName, Region destination, String destinationName) {
        for (Portal p: portals)
            if (p.getName().equals(portalName) && p.getDestination().equals(destination) && p.getDestinationName().equals(destinationName))
                return p;
        return null;
    }

    public byte[] getTile(int wx, int wy) {
        Chunk current = getChunk(wx / Chunk.CHUNK_SIZE, wy / Chunk.CHUNK_SIZE);
        if (current == null) return new byte[2];
        return current.get(wx % Chunk.CHUNK_SIZE, wy % Chunk.CHUNK_SIZE);
    }

    public ChunkGenerator getChunkGenerator(int cx, int cy) {
        if (cx < 0 || cx >= chunkGenerators.length || cy < 0 || cy >= chunkGenerators[0].length) return null;
        return chunkGenerators[cx][cy];
    }

    public boolean doesChunkExist(int cx, int cy) {
        if (chunks == null) return false;
        if (cx < 0 || cx >= chunks.length || cy < 0 || cy >= chunks[0].length) return false;
        return chunks[cx][cy] != null;
    }

    public boolean isChunkDiscovered(int cx, int cy) {
        if (!doesChunkExist(cx, cy)) return false;
        return chunks[cx][cy].wasDiscovered();
    }

    public Chunk getChunk(int cx, int cy) {
        if (chunkGenerators == null) plan();
        if (cx < 0 || cx >= chunkGenerators.length || cy < 0 || cy >= chunkGenerators[0].length) return null;
        if (chunks[cx][cy] == null) {
            boolean savedToDisk = new File(Assets.ROOT_DIRECTORY+"/world/"+name+"/"+cx+"_"+cy+".chunk").exists();
            if (!savedToDisk) {
                chunks[cx][cy] = new Chunk(cx, cy, this);
                chunks[cx][cy].generate(chunkGenerators[cx][cy], true);
            } else {
                loadChunkFromFile(cx, cy);
            }
        }
        return chunks[cx][cy];
    }

    public Chunk[][] getAdjacentChunks(int cx, int cy) {
        return new Chunk[][]{
            new Chunk[]{ getChunk( cx-1, cy-1), getChunk(cx-1, cy), getChunk(cx-1, cy+1) },
            new Chunk[]{ getChunk( cx, cy-1), getChunk(cx, cy), getChunk(cx, cy+1) },
            new Chunk[]{ getChunk( cx+1, cy-1), getChunk(cx+1, cy), getChunk(cx+1, cy+1) }
        };
    }

    public String getName() { return name; }

    public void addMagicSource(MagicSource magicSource) {
        magic_sources.add(magicSource);
    }

    public void update() {

        time += MiscMath.getConstant(1000, 1 / World.getTimeMultiplier());

        for (int i = magic_sources.size() - 1; i >= 0; i--) {
            MagicSource magicSource = magic_sources.get(i);
            magicSource.update();
            if (magicSource.getBody().isEmpty() && !magicSource.getBody().isSpawning()) magic_sources.remove(i);
        }

        int radius = 1;
        int[] pchcoords = World.getLocalPlayer().getLocation().getChunkCoordinates();
        for (int j = -radius; j <= radius; j++) {
            for (int i = -radius; i <= radius; i++) {
                int cx = pchcoords[0] + i;
                int cy = pchcoords[1] + j;
                Chunk adj = getChunk(cx, cy);
                if (adj != null) adj.update();
            }
        }

    }

    public void draw(float scale, Graphics g) {
        int[] pchcoords = World.getLocalPlayer().getLocation().getChunkCoordinates();
        float[] oscoords = Camera.getOnscreenCoordinates(0, 0, scale);

        float chunk_size = Chunk.CHUNK_SIZE * Chunk.TILE_SIZE * Window.getScale();
        int radius = 2;

        for (int pass = 0; pass < 2; pass++) {
            for (int cj = -radius; cj <= radius; cj++) {
                for (int ci = -radius; ci <= radius; ci++) {
                    int cx = pchcoords[0] + ci;
                    int cy = pchcoords[1] + cj;
                    Chunk adj = getChunk(cx, cy);
                    if (adj == null) continue;
                    float osx = oscoords[0] + (cx * chunk_size), osy = oscoords[1] + (cy * chunk_size);
                    if (pass == 0) {
                        adj.drawBase(osx, osy, scale);
                    } else {
                        adj.drawTop(osx, osy, scale);
                    }
                }
            }
        }
        Assets.PARTICLE.startUse();
        for (int i = 0; i < magic_sources.size(); i++) magic_sources.get(i).draw(oscoords[0], oscoords[1], scale);
        Assets.PARTICLE.endUse();
    }

    public void drawDebug(float scale, Graphics g) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.drawDebug(scale, g);
        }
        for (MagicSource magicSource: magic_sources) magicSource.getBody().drawDebug(0, 0, scale, g);

    }

    public int getSize() { return size; }

    private void saveChunk(int cx, int cy) {
        JSONObject chunk = new JSONObject();
        JSONArray jsonEntities = new JSONArray();
        jsonEntities.addAll(
                getEntities(cx * Chunk.CHUNK_SIZE, cy * Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE)
                .stream()
                .map(e -> e.serialize()).collect(Collectors.toList())
        );
        chunk.put("entities", jsonEntities);
        chunk.put("discovered", chunks[cx][cy].wasDiscovered());
        try {
            File regionFolder = new File(Assets.ROOT_DIRECTORY+"/world/"+name+"/");
            regionFolder.mkdirs();
            Files.write(new File(regionFolder.getAbsolutePath()+"/"+cx+"_"+cy+".chunk").toPath(), chunk.toJSONString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAllChunks() {
        if (chunks == null) return;
        for (int cx = 0; cx < chunks.length; cx++)
            for (int cy = 0; cy < chunks[0].length; cy++)
                if (chunks[cx][cy] != null) saveChunk(cx, cy);
    }

    public void loadSavedChunks() {
        if (chunks == null) return;
        for (int cx = 0; cx < chunks.length; cx++)
            for (int cy = 0; cy < chunks[0].length; cy++)
                loadChunkFromFile(cx, cy);
    }

    private void loadChunkFromFile(int cx, int cy) {
        String url = Assets.ROOT_DIRECTORY+"/world/"+name+"/"+cx+"_"+cy+".chunk";
        if (!new File(url).exists()) return;
        chunks[cx][cy] = new Chunk(cx, cy, this);
        chunks[cx][cy].generate(chunkGenerators[cx][cy], false);
        try {
            JSONObject chunk = (JSONObject)new JSONParser().parse(Assets.read(url, false));
            chunks[cx][cy].setDiscovered((boolean)chunk.get("discovered"));
            JSONArray jsonEntities = (JSONArray)chunk.get("entities");
            jsonEntities
                    .stream()
                    .forEach(json -> {
                        JSONObject jsonObject = (JSONObject)json;
                        Entity e = Entity.create(jsonObject);
                        if (e == null) return;
                        //e.moveTo(new Location(this, (double)jsonObject.get("x"), (double)jsonObject.get("y")));
                    });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name;
    }

}
