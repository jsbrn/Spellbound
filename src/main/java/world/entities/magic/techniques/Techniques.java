package world.entities.magic.techniques;

import assets.CSVReader;

import java.util.ArrayList;

public class Techniques {

    private static CSVReader reader;
    private static CSVReader reader() {
        if (reader == null) reader = new CSVReader("definitions/techniques.csv", ';');
        return reader;
    }

    public static String getName(String id) {
        return reader().get("Name", id);
    }
    public static String getDescription(String id) { return reader().get("Description", id); }

    public static String getCategory(String id) { return reader().get("Category", id); }

    public static int getManaCost(String id) { return reader().getInteger("ManaCost", id); }
    public static int getCrystalCost(String id) { return reader().getInteger("CrystalCost", id); }

    public static int getMaxLevel(String id) { return reader().getInteger("MaxLevel", id); }

    public static boolean isDefault(String id) { return reader().getBoolean("Default", id);}

    public static float getRarity(String id) { return (float)reader().getDouble("Rarity", id); }

    public static String getConflictsWith(String id) { return reader().get("ConflictsWith", id); }

    public static boolean getRequiresEnergy(String id) { return reader().getBoolean("RequiresEnergy", id); }

    public static String[] getAll() {
        String[] ids = new String[reader().getRowCount() - 1];
        for (int i = 1; i < reader().getRowCount(); i++) {
            ids[i-1] = reader().get(0, i);
        }
        return ids;
    }

    public static ArrayList<String> getAllCategories() {
        ArrayList<String> cats = new ArrayList<>();
        for (int i = 1; i < reader().getRowCount(); i++) {
            String cat = reader().get(2, i);
            if (!cats.contains(cat)) cats.add(cat);
        }
        return cats;
    }

}
