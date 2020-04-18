package gui.menus;

import assets.Assets;
import gui.GUIAnchor;
import gui.elements.*;
import misc.MiscMath;
import org.newdawn.slick.Color;
import world.entities.magic.Spell;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.Techniques;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class SpellcraftingMenu extends Modal {

    private HumanoidEntity target;
    private ArrayList<String> categories;
    private int currentCategory;
    private TextLabel categoryLabel;
    private TextLabel techniqueName, techniqueDescription, techniqueConflicts;
    private HashMap<String, Button> buttons;
    private TextBox nameField;
    private TextLabel crystalCost, dyesCost, manaCost, volatility;
    private ColorChooser colorChooser;
    private IconChooser iconChooser;
    private Button createButton, leftButton, rightButton;

    private Spell spell;

    public SpellcraftingMenu(HumanoidEntity target) {
        super("assets/gui/spellcasting.png");
        this.target = target;
        this.buttons = new HashMap<>();
        this.categories = new ArrayList<>();
        this.spell = new Spell();

        nameField = new TextBox(64, 8) {
            @Override
            public boolean onKeyUp(int key) {
                refreshRequirements();
                return super.onKeyUp(key);
            }
        };
        crystalCost = new TextLabel("0", 8, Color.white, true);
        dyesCost = new TextLabel("0", 8, Color.white, true);
        manaCost = new TextLabel("0", 8, Color.white, true);
        volatility = new TextLabel("", 4, Color.yellow, true);
        categoryLabel = new TextLabel("...", 4, Color.gray, false);
        techniqueName = new TextLabel("-", 5, Color.black, false);
        techniqueDescription = new TextLabel("", 4, 100, 3, Color.gray, false);
        techniqueConflicts = new TextLabel("", 4, 100, 3, Color.red, false);
        createButton = new Button("Create!", 24, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                spell.setColor(colorChooser.getColor());
                spell.setIconIndex(iconChooser.getIndex());
                spell.setName(nameField.getText());
                target.getSpellbook().addSpell(spell);
                target.addCrystals(-spell.getCrystalCost());
                nameField.releaseFocus();
                getGUI().popModal();
                return true;
            }
        };

        createTechniqueButtons();

        addChild(nameField, 8, 12, GUIAnchor.TOP_LEFT);

        iconChooser = new IconChooser("assets/gui/icons/spells", 15, 2);
        colorChooser = new ColorChooser(16, 8, 4) {
            @Override
            public boolean onMousePressed(int ogx, int ogy, int button) {
                boolean clicked = super.onMousePressed(ogx, ogy, button);
                if (clicked) { spell.setColor(getColor()); iconChooser.setColor(getColor()); }
                return clicked;
            }
        };

        addChild(colorChooser, 8, 24, GUIAnchor.TOP_LEFT);
        addChild(iconChooser, 42, 24, GUIAnchor.TOP_LEFT);

        addChild(new IconLabel("icons/crystal.png"), 8, -20, GUIAnchor.BOTTOM_LEFT);
        addChild(new IconLabel("icons/dyes.png"), 28, -20, GUIAnchor.BOTTOM_LEFT);
        addChild(new IconLabel("icons/mana.png"), 48, -20, GUIAnchor.BOTTOM_LEFT);
        addChild(crystalCost, 8+8, -20, GUIAnchor.BOTTOM_LEFT);
        addChild(dyesCost, 28+8, -20, GUIAnchor.BOTTOM_LEFT);
        addChild(manaCost, 48+8, -20, GUIAnchor.BOTTOM_LEFT);
        addChild(volatility, 11, -16, GUIAnchor.BOTTOM_LEFT);
        addChild(new TextLabel("New Spell", 5, Color.white, true), 8, 4, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Discovered Techniques", 5, Color.white, true), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new Button("Cancel", 24, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                getGUI().popModal(); return true;
            }
        }, 12, -6, GUIAnchor.BOTTOM_LEFT);

        addChild(createButton, 40, -6, GUIAnchor.BOTTOM_LEFT);

        leftButton = new Button(null, 8, 8, "icons/arrow_left.png", true) {
            @Override
            public boolean onClick(int button) {
                currentCategory = (int)MiscMath.clamp(currentCategory-1, 0, categories.size()-1);
                refreshTechniquesPanel();
                return true;
            }
        };

        rightButton = new Button(null, 8, 8, "icons/arrow_right.png", true) {
            @Override
            public boolean onClick(int button) {
                currentCategory = (int)MiscMath.clamp(currentCategory+1, 0, categories.size()-1);
                refreshTechniquesPanel();
                return true;
            }
        };

        addChild(leftButton, -21, 16, GUIAnchor.TOP_RIGHT);
        addChild(rightButton, -9, 16, GUIAnchor.TOP_RIGHT);
        addChild(categoryLabel, 84, 16, GUIAnchor.TOP_LEFT);
        addChild(techniqueName, 84, -32, GUIAnchor.BOTTOM_LEFT);
        addChild(techniqueDescription, 85, 98, GUIAnchor.TOP_LEFT);
        addChild(techniqueConflicts, 85, 110, GUIAnchor.TOP_LEFT);

    }

    private void createTechniqueButtons() {
        String[] techniques = Techniques.getAll();
        for (int i = 0; i < techniques.length; i++) {
            String technique = techniques[i];
            Button chooseButton = new Button(null, 16, 16, "icons/techniques/"+technique+".png", true) {
                private TextLabel levelLabel = new TextLabel("", 4, Color.yellow, true);
                private TextLabel warningLabel = new TextLabel("!", 6, Color.red, true);
                @Override
                public boolean onClick(int button) {
                    if (button == 0) {
                        if (spell.hasTechnique(technique)) {
                            spell.addLevel(technique);
                            if (Techniques.getMaxLevel(technique) < spell.getLevel(technique)) {
                                spell.resetLevel(technique);
                                spell.removeTechnique(technique);
                                removeChild(levelLabel);
                            }
                        } else {
                            spell.addTechnique(technique);
                            addChild(levelLabel, -1, -1, GUIAnchor.TOP_RIGHT);
                        }

                        if (spell.hasTechnique(technique) && !spell.getConflicts(technique).isEmpty())
                            addChild(warningLabel, 1, -1, GUIAnchor.BOTTOM_LEFT);
                        else
                            removeChild(warningLabel);
                    } else if (button == 1) {
                        spell.removeTechnique(technique);
                        removeChild(levelLabel);
                    }

                    levelLabel.setText(Techniques.getMaxLevel(technique) > 1 ? spell.getLevel(technique)+"/"+Techniques.getMaxLevel(technique) : "");
                    setToggled(spell.hasTechnique(technique));
                    refreshRequirements();
                    return true;
                }

                @Override
                public void onShow() {
                    super.onShow();
                    levelLabel.setText(Techniques.getMaxLevel(technique) > 1 ? spell.getLevel(technique)+"/"+Techniques.getMaxLevel(technique) : "");
                    if (!spell.hasTechnique(technique)) removeChild(levelLabel); else addChild(levelLabel, -1, -1, GUIAnchor.TOP_RIGHT);
                    if (spell.hasTechnique(technique) && !spell.getConflicts(technique).isEmpty())
                        addChild(warningLabel, 1, -1, GUIAnchor.BOTTOM_LEFT);
                    else
                        removeChild(warningLabel);
                }

                @Override
                public boolean onMouseMoved(int ogx, int ogy) {

                    if (spell.hasTechnique(technique) && !spell.getConflicts(technique).isEmpty())
                        addChild(warningLabel, 1, -1, GUIAnchor.BOTTOM_LEFT);
                    else
                        removeChild(warningLabel);

                    if (!mouseIntersects()) return false;
                    techniqueName.setText(target.getSpellbook().hasTechnique(technique) ? Techniques.getName(technique) : "Unknown Technique");
                    techniqueDescription.setText(target.getSpellbook().hasTechnique(technique) ? Techniques.getDescription(technique) : "???");
                    ArrayList<String> conflicts = spell.getConflicts(technique);
                    String conflictNames = "";
                    for (int i = 0; i < conflicts.size(); i++)
                        conflictNames += Techniques.getName(conflicts.get(i)) + (i < conflicts.size()-1 ? ", " : "");
                    techniqueConflicts.setText(conflicts.isEmpty() ? "" : "Conflicts with: "+conflictNames);
                    return true;
                }
            };
            buttons.put(technique, chooseButton);
            addChild(chooseButton, 0, 0, GUIAnchor.TOP_MIDDLE);
            chooseButton.hide();
        }
    }

    private void refreshTechniquesPanel() {
        categories = Techniques.getAllCategories();
        categoryLabel.setText(categories.get(currentCategory));
        leftButton.setEnabled(currentCategory > 0);
        rightButton.setEnabled(currentCategory < categories.size() - 1);
        String[] techniques = Techniques.getAll();
        int p = 0;
        for (int i = 0; i < techniques.length; i++) {
            String technique = techniques[i];
            Button b = buttons.get(technique);
            b.hide();
            if (Techniques.getCategory(technique).equals(categories.get(currentCategory))) {
                b.show();
                b.setOffset(((p % 5) * 18), 19 + 8 + ((p / 5) * 19));
                b.setToggled(spell.hasTechnique(technique));
                b.setEnabled(Technique.createFrom(technique) != null && target.getSpellbook().hasTechnique(technique));
                b.setIcon(target.getSpellbook().hasTechnique(technique) ? Assets.getImage("assets/gui/icons/techniques/"+technique+".png") : Assets.getImage("assets/gui/icons/question_mark.png"));
                p++;
            }
        }
    }

    private void refreshRequirements() {
        this.crystalCost.setText(spell.getCrystalCost()+"");
        this.crystalCost.setColor(spell.getCrystalCost() > target.getCrystalCount() ? Color.red : Color.white);
        this.dyesCost.setText("0");
        this.manaCost.setText(spell.getManaCost()+"");
        this.manaCost.setColor(spell.getManaCost() > target.getMaxMana() ? Color.yellow : Color.white);
        this.volatility.setText(spell.getVolatility() < 0.05 ? "" : (int)(spell.getVolatility()*100)+"% Volatile");
        this.createButton.setEnabled(
                target.getCrystalCount() >= spell.getCrystalCost()
                && !nameField.getText().trim().isEmpty()
                && !spell.isEmpty());
    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        techniqueName.setText("");
        techniqueDescription.setText("");
        techniqueConflicts.setText("");
        return true;
    }

    public void reset(Spell template) {
        this.spell = template == null ? new Spell() : new Spell(template);
        this.currentCategory = 0;
        refresh();
    }

    private void refresh() {
        refreshRequirements();
        this.nameField.setText(spell.getName());
        this.iconChooser.setColor(spell.getColor());
        this.iconChooser.setIndex(spell.getIconIndex());
        this.colorChooser.setColor(spell.getColor());
        refreshTechniquesPanel();
    }

}
