package gui.menus;

import assets.Assets;
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
    private TextLabel gold, crystals, dyes, tomes, artifacts, keys;

    private ArrayList<Button> spellButtons;
    private int selectedSpell;
    private Button createButton, copyButton, combineButton, destroyButton, moveUpButton;

    public Journal(HumanoidEntity target, SpellcraftingMenu spellcraftingMenu) {
        super("assets/gui/spellbook_bg.png");
        this.target = target;
        this.selectedSpell = -1;
        this.spellButtons = new ArrayList<>();
        addChild(new TextLabel("Inventory", 5, Color.black, false), 12, 4, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Your Spells", 5, Color.black, false), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new IconLabel("icons/gold.png"), 16, 16, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/crystal.png"), 16, 40, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/dyes.png"), 16, 64, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/tome.png"), 48, 16, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/artifact.png"), 48, 40, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/key.png"), 48, 64, GUIAnchor.TOP_LEFT);
        gold = new TextLabel(target.getGoldCount()+"", 8, Color.white, true);
        crystals = new TextLabel(target.getCrystalCount()+"", 8, Color.white, true);
        dyes = new TextLabel(target.getDyeCount()+"", 8, Color.white, true);
        tomes = new TextLabel(target.getTomeCount()+"", 8, Color.white, true);
        artifacts = new TextLabel(target.getArtifactCount()+"", 8, Color.white, true);
        keys = new TextLabel(target.getKeyCount()+"", 8, Color.white, true);
        addChild(gold, 24, 24, GUIAnchor.TOP_LEFT);
        addChild(crystals, 24, 48, GUIAnchor.TOP_LEFT);
        addChild(dyes, 24, 72, GUIAnchor.TOP_LEFT);
        addChild(tomes, 56, 24, GUIAnchor.TOP_LEFT);
        addChild(artifacts, 56, 48, GUIAnchor.TOP_LEFT);
        addChild(keys, 56, 72, GUIAnchor.TOP_LEFT);
        createButton = new Button("+", 8, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                spellcraftingMenu.reset(null);
                getGUI().stackModal(spellcraftingMenu);
                return true;
            }
        };
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
        addChild(createButton, -12, 4, GUIAnchor.TOP_RIGHT);
    }

    private void refresh() {
        gold.setText(target.getGoldCount()+"");
        crystals.setText(target.getCrystalCount()+"");
        dyes.setText(target.getDyeCount()+"");
        tomes.setText(target.getTomeCount()+"");
        artifacts.setText(target.getArtifactCount()+"");
        keys.setText(target.getKeyCount()+"");
        createButton.setEnabled(target.getSpellbook().getSpells().size() < 9);
        spellButtons.stream().forEach(this::removeChild);
        spellButtons.clear();
        for (int i = 0; i < target.getSpellbook().getSpells().size(); i++) {
            Spell spell = target.getSpellbook().getSpell(i);
            int index = i;
            Button button = new Button(null, 16, 16, null, true) {
                @Override
                public boolean onClick(int button) {
                    if (button == 0) {
                        if (selectedSpell == index) selectedSpell = -1;
                        else selectedSpell = index;
                        refresh();
                    } else if (button == Input.MOUSE_MIDDLE_BUTTON) {
                        selectedSpell = index;
                        Spell exporting = target.getSpellbook().getSpell(selectedSpell);
                        Assets.write(Assets.ROOT_DIRECTORY+"/exported/"+exporting.getName()+".json", exporting.serialize().toJSONString());
                    }
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
    public boolean onKeyUp(int key) {
        if (key == Input.KEY_TAB) {
            getGUI().popModal();
            return true;
        }
        return super.onKeyUp(key);
    }

    @Override
    public void onShow() {
        World.setPaused(true);
        refresh();
    }

    @Override
    public void onHide() {
        World.setPaused(false);
    }
}
