package world.generators.chunk.overworld;

public class WishingWellFieldGenerator extends OpenFieldGenerator {

    public WishingWellFieldGenerator(int seed) {
        super(seed);
    }

    @Override
    public String getIcon() {
        return "animations/wishing_well.png";
    }
}
