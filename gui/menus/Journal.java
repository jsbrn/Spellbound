package gui.menus;

import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.IconLabel;
import gui.elements.Modal;
import gui.elements.TextLabel;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import world.Chunk;
import world.World;
import world.entities.magic.Spell;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.ArrayList;

public class Journal extends Modal {

    private HumanoidEntity target;
    private TextLabel gold, crystals, dyes;

    private ArrayList<Button> spellButtons;
    private int selectedSpell;
    private Button copyButton, combineButton, destroyButton, moveUpButton;

    public Journal(HumanoidEntity target, SpellcraftingMenu spellcraftingMenu) {
        super("spellbook_bg.png");
        this.target = target;
        this.selectedSpell = -1;
        this.spellButtons = new ArrayList<>();
        addChild(new TextLabel("Player", 5, Color.black, false), 12+16, 8, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Your Spells", 5, Color.black, false), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new IconLabel("icons/gold.png"), 32, 16, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/crystal.png"), 32, 32, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/dyes.png"), 32, 48, GUIAnchor.TOP_LEFT);
        gold = new TextLabel(target.getGoldCount()+"", 8, Color.gray, false);
        crystals = new TextLabel(target.getCrystalCount()+"", 8, Color.gray, false);
        dyes = new TextLabel(target.getDyeCount()+"", 8, Color.gray, false);
        addChild(gold, 50, 20, GUIAnchor.TOP_LEFT);
        addChild(crystals, 50, 36, GUIAnchor.TOP_LEFT);
        addChild(dyes, 50, 52, GUIAnchor.TOP_LEFT);
        addChild(new Button("New Spell...", 32, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                spellcraftingMenu.reset(null);
                getGUI().stackModal(spellcraftingMenu);
                return true;
            }
        }, 12, -10, GUIAnchor.BOTTOM_LEFT);
        copyButton = new Button("Copy", 16, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                spellcraftingMenu.reset(target.getSpellbook().getSpell(selectedSpell));
                getGUI().stackModal(spellcraftingMenu);
                return true;
            }
        };
        destroyButton = new Button("Destroy", 20, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                if  (selectedSpell >= 0) {
                    target.addCrystals(target.getSpellbook().getSpell(selectedSpell).getCrystalCost() / 4);
                    target.getSpellbook().getSpells().remove(selectedSpell);
                }
                if (selectedSpell >= target.getSpellbook().getSpells().size())
                    selectedSpell = target.getSpellbook().getSpells().size() - 1;
                refresh();
                return true;
            }
        };
        moveUpButton = new Button(null, 8, 8, "icons/arrow_up.png", true) {
            @Override
            public boolean onClick(int button) {
                if (selectedSpell == 0) return false;
                Spell selected = target.getSpellbook().getSpell(selectedSpell);
                if (selected == null) return false;
                target.getSpellbook().getSpells().remove(selected);
                target.getSpellbook().getSpells().add(selectedSpell-1, selected);
                selectedSpell--;
                refresh();
                return true;
            }
        };
        addChild(copyButton, 91, -10, GUIAnchor.BOTTOM_LEFT);
        addChild(destroyButton, 109, -10, GUIAnchor.BOTTOM_LEFT);
        addChild(moveUpButton, 131, -10, GUIAnchor.BOTTOM_LEFT);
    }

    private void refresh() {
        gold.setText(target.getGoldCount()+"");
        crystals.setText(target.getCrystalCount()+"");
        dyes.setText(target.getDyeCount()+"");

        spellButtons.stream().forEach(this::removeChild);
        spellButtons.clear();
        for (int i = 0; i < target.getSpellbook().getSpells().size(); i++) {
            Spell spell = target.getSpellbook().getSpell(i);
            int index = i;
            Button button = new Button(null, 16, 16, null, true) {
                @Override
                public boolean onClick(int button) {
                    if (selectedSpell == index) selectedSpell = -1; else selectedSpell = index;
                    refresh();
                    return true;
                }
            };
            spellButtons.add(button);
            button.setIcon(spell.getIcon());
            button.setIconFilter(spell.getColor());
            button.setToggled(i == selectedSpell);
            addChild(button, 91 + (18 * (i % 3)), 16 + (18 * (i / 3)), GUIAnchor.TOP_LEFT);
        }

        copyButton.setEnabled(selectedSpell >= 0);
        destroyButton.setEnabled(selectedSpell >= 0);
        moveUpButton.setEnabled(selectedSpell >= 0);

    }

    @Override
    public void onShow() {
        refresh();
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
