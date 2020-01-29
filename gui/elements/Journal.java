package gui.elements;

import gui.GUIAnchor;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import world.Chunk;
import world.World;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.ArrayList;

public class Journal extends Modal {

    private HumanoidEntity target;
    private ArrayList<TextLabel> strings;

    public Journal(HumanoidEntity target, SpellcraftingMenu spellcraftingMenu) {
        super("spellbook_bg.png");
        this.target = target;
        this.strings = new ArrayList<>();
        addChild(new TextLabel("Player", 5, Color.black, false), 12, 4, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Your Spells", 5, Color.black, false), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new IconLabel("icons/gold.png"), 32, 16, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/crystal.png"), 32, 32, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/dyes.png"), 32, 48, GUIAnchor.TOP_LEFT);
        addChild(new Button("Spellcrafting", 32, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                getGUI().stackModal(spellcraftingMenu);
                return true;
            }
        }, 12, -10, GUIAnchor.BOTTOM_LEFT);
    }

    private void refresh() {
        for (TextLabel l: strings) removeChild(l);
        int spellCount = target.getSpellbook().getSpells().size();
        for (int i = 0; i < spellCount; i++) {
            TextLabel l = new TextLabel(target.getSpellbook().getSpell(i).getName(), 4, Color.gray, false);
            strings.add(l);
            addChild(l, 24, 14 + (i * 5), GUIAnchor.TOP_MIDDLE);
        }

        TextLabel gold = new TextLabel(target.getGoldCount()+"", 8, Color.gray, false);
        TextLabel crystals = new TextLabel(target.getCrystalCount()+"", 8, Color.gray, false);
        TextLabel dyes = new TextLabel(target.getDyeCount()+"", 8, Color.gray, false);

        strings.add(gold);
        strings.add(crystals);
        strings.add(dyes);

        addChild(gold, 50, 20, GUIAnchor.TOP_LEFT);
        addChild(crystals, 50, 36, GUIAnchor.TOP_LEFT);
        addChild(dyes, 50, 52, GUIAnchor.TOP_LEFT);

    }

    @Override
    public boolean onKeyUp(int key) {
        refresh();
        return false;
    }

    @Override
    public boolean onKeyDown(int key) {
        if (key == Input.KEY_TAB || key == Input.KEY_ESCAPE) {
            World.setPaused(false);
        }
        return super.onKeyDown(key);
    }

    @Override
    public void drawOver(Graphics g) {
        target.draw(
                (float)(getCoordinates()[0] + Chunk.TILE_SIZE * 1.25) * Window.getScale(),
                (float)(getCoordinates()[1] + Chunk.TILE_SIZE*3) * Window.getScale(),
                Window.getScale() * 1, 3);

    }
}
