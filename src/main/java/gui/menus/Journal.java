package gui.menus;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Input;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.IconLabel;
import gui.elements.Modal;
import gui.elements.TextLabel;
import gui.sound.SoundManager;
import network.MPClient;
import world.World;
import world.entities.Entities;
import world.entities.components.SpellbookComponent;
import world.magic.Spell;

import java.util.ArrayList;

public class Journal extends Modal {

    private SpellbookComponent spellbook;

    private ArrayList<Button> spellButtons;
    private int selectedSpell;
    private Button createButton, copyButton, combineButton, destroyButton, moveUpButton;

    public Journal(Integer target, SpellcraftingMenu spellcraftingMenu) {
        super("gui/spellbook_bg.png");
        this.spellbook = (SpellbookComponent) MPClient.getWorld().getEntities().getComponent(SpellbookComponent.class, target);
        this.selectedSpell = -1;
        this.spellButtons = new ArrayList<>();
        addChild(new TextLabel("Inventory", 5, Color.black, false, false), 12, 4, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Your Spells", 5, Color.black, false, false), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new IconLabel("icons/gold.png"), 16, 16, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/crystal.png"), 48, 16, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/dyes.png"), 16, 42, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/artifact.png"), 16, 68, GUIAnchor.TOP_LEFT);
        addChild(new IconLabel("icons/key.png"), 48, 42, GUIAnchor.TOP_LEFT);
        createButton = new Button("+", 8, 8, null, true) {
            @Override
            public void onClick(int button) {
                spellcraftingMenu.reset(null);
                getGUI().stackModal(spellcraftingMenu);
            }
        };
        createButton.setTooltipText("Create a new spell");
        copyButton = new Button("Copy", 16, 8, null, true) {
            @Override
            public void onClick(int button) {
                spellcraftingMenu.reset(spellbook.getSpell(selectedSpell));
                getGUI().stackModal(spellcraftingMenu);
            }
        };
        destroyButton = new Button("Destroy", 20, 8, null, true) {
            @Override
            public void onClick(int button) {
                if  (selectedSpell >= 0) {
                    //target.addCrystals(spellbook.getSpell(selectedSpell).getCrystalCost() / 2);
                    //target.addDyes((int)(spellbook.getSpell(selectedSpell).getDyeCost() * 0.75));
                    spellbook.getSpells().remove(selectedSpell);
                }
                if (selectedSpell >= spellbook.getSpells().size())
                    selectedSpell = spellbook.getSpells().size() - 1;
                refresh();
            }
        };
        moveUpButton = new Button(null, 8, 8, "icons/arrow_up.png", true) {
            @Override
            public void onClick(int button) {
                if (selectedSpell == 0) return;
                Spell selected = spellbook.getSpell(selectedSpell);
                if (selected == null) return;
                spellbook.getSpells().remove(selected);
                spellbook.getSpells().add(selectedSpell-1, selected);
                selectedSpell--;
                refresh();
            }
        };
        addChild(copyButton, 91, -10, GUIAnchor.BOTTOM_LEFT);
        addChild(destroyButton, 109, -10, GUIAnchor.BOTTOM_LEFT);
        addChild(moveUpButton, 131, -10, GUIAnchor.BOTTOM_LEFT);
        addChild(createButton, -12, 4, GUIAnchor.TOP_RIGHT);
    }

    private void refresh() {
        createButton.setEnabled(spellbook.getSpells().size() < 9);
        spellButtons.stream().forEach(this::removeChild);
        spellButtons.clear();
        for (int i = 0; i < spellbook.getSpells().size(); i++) {
            Spell spell = spellbook.getSpell(i);
            int index = i;
            Button button = new Button(null, 16, 16, null, true) {
                @Override
                public void onClick(int button) {
                    if (button == 0) {
                        if (selectedSpell == index) selectedSpell = -1;
                        else selectedSpell = index;
                        refresh();
                    } else if (button == Input.MOUSE_MIDDLE_BUTTON) {
                        selectedSpell = index;
                        Assets.write(Assets.ROOT_DIRECTORY+"/exported/"+spell.getName()+".json", spell.serialize().toJSONString());
                    }
                    destroyButton.setTooltipText("Destroy spell to learn techniques and harvest resources (+"+(spell.getCrystalCost()/2)+" Crystals, +"+(int)(spell.getDyeCost()*0.75)+" Dyes)");
                }
            };
            spellButtons.add(button);
            button.setTooltipText(
                    spell.getName()+"" +
                    "\n"+"-------------------"+"" +
                    "\nCost: "+spell.getManaCost()+" Mana" +
                    "\nVolatility: "+(int)(spell.getVolatility()*100)+"%");
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
        refresh();
        SoundManager.playSound(SoundManager.PAGE_TURN);
    }

}
