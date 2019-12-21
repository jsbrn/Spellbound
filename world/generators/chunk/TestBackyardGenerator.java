package world.generators.chunk;

import world.Region;
import world.RegionLink;
import world.World;
import world.generators.region.PlayerHomeRegionGenerator;

import java.util.Random;

public class TestBackyardGenerator extends ChunkGenerator {

    private Random rng;

    public TestBackyardGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte[][] generateBase(int size) {
        byte[][] base = new byte[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                base[i][j] = 1;
            }
        }
        return base;
    }

    @Override
    public byte[][] generateObjects(int size) {
        byte[][] top = new byte[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                top[i][j] = (byte)(Math.random() > 0.6 ? (Math.random() < 0.2 ? 4 : 3) : 0);
            }
        }
        top[5][5] = 5; //house :)
        top[6][5] = 6;
        top[7][5] = 7;
        return top;
    }

    @Override
    public RegionLink[][] generateLinks(int size) {
        RegionLink[][] links = new RegionLink[size][size];

        Region player_home = new Region("player_home", 1, new PlayerHomeRegionGenerator());
        World.addRegion(player_home);
        links[6][5] = new RegionLink(player_home, 0, 0, 6, 0, 0, 1);
        return links;
    }

}
