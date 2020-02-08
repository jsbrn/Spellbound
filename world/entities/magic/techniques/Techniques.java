package world.entities.magic.techniques;

import assets.CSVReader;

public class Techniques {

    private static CSVReader reader;

    public static void load() {
        reader = new CSVReader("definitions/techniques.csv", ';');
    }

    public static String getName(String id) {
        return reader.get("Name", id);
    }

    public static int getManaCost(String id) { return reader.getInteger("ManaCost", id); }

}
