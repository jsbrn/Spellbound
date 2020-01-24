package gui.elements;

import gui.GUIAnchor;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.Chunk;
import world.entities.magic.Spell;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.ArrayList;

public class Journal extends Modal {

    private HumanoidEntity target;
    private ArrayList<Label> spellLabels;

    public Journal(HumanoidEntity target) {
        super("spellbook_bg.png");
        this.target = target;
        this.spellLabels = new ArrayList<>();
        addChild(new Label("Player", 5, Color.black), 12, 4, GUIAnchor.TOP_LEFT);
        addChild(new Label("Your Spells", 5, Color.black), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new Button("Open inventory", 36, 8) {
            @Override
            public boolean onClick(int button) {
                return true;
            }
        }, 12, -10, GUIAnchor.BOTTOM_LEFT);
        addChild(new Button("Spellcrafting", 32, 8) {
            @Override
            public boolean onClick(int button) {
                return true;
            }
        }, -12, -10, GUIAnchor.BOTTOM_RIGHT);
    }

    private void updateSpellsList() {
        for (Label l: spellLabels) removeChild(l);
        int spellCount = target.getSpellbook().getSpells().size();
        for (int i = 0; i < spellCount; i++) {
            Label l = new Label(target.getSpellbook().getSpell(i).getName(), 4, Color.gray);
            spellLabels.add(l);
            addChild(l, 24, 14 + (i * 5), GUIAnchor.TOP_MIDDLE);
        }

    }

    @Override
    public void drawOver(Graphics g) {
        if (spellLabels.size() != target.getSpellbook().getSpells().size()) updateSpellsList();
        target.draw(
                (float)(getCoordinates()[0] + Chunk.TILE_SIZE * 1.25) * Window.getScale(),
                (float)(getCoordinates()[1] + Chunk.TILE_SIZE*2) * Window.getScale(),
                Window.getScale(), 3);

    }
}
