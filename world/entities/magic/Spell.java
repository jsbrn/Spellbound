package world.entities.magic;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.Region;
import world.World;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.TechniqueName;

import java.util.ArrayList;

public class Spell {

    private String name;
    private ArrayList<TechniqueName> techniques;

    private Image icon;
    private boolean[][] pixels;
    private Color color;

    public Spell() {
        this.name = "Unnamed Spell";
        this.techniques = new ArrayList<>();
        this.color = Color.white;
        this.pixels = new boolean[1][1];
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void addTechnique(TechniqueName technique) { this.techniques.add(technique); }
    public void removeTechnique(TechniqueName technique) { this.techniques.remove(technique); }
    public boolean hasTechnique(TechniqueName techniqueName) { return techniques.contains(techniqueName); }

    private ArrayList<Technique> loadTechniques() {
        ArrayList<Technique> loaded = new ArrayList<>();
        for (TechniqueName techniqueName: techniques) loaded.add(Technique.create(techniqueName));
        return loaded;
    }

    public void cast(double wx, double wy, Entity caster) {
        //maybe add something to catch the spell-level techniques (like spawn patterns and chaining)
        MagicSource cast = new MagicSource(wx, wy, caster, loadTechniques(), color);
        World.getRegion().addMagicSource(cast);
    }

    public void setColor(Color c) { this.color = c; }
    public void setPixels(boolean[][] grid) { this.pixels = grid; }

    public Image getIcon() {
        if (icon != null) return icon;
        try {
            icon = new Image(pixels.length, pixels[0].length);
            Graphics b = icon.getGraphics();
            b.setColor(color);
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[0].length; j++) {
                    if (pixels[i][j]) b.fillRect(i, j, 1, 1);
                }
            }
            return icon;
        } catch (SlickException e) {
            e.printStackTrace();
        }
        return null;
    }

}
