package gui.elements;

import gui.GUIAnchor;
import misc.MiscMath;
import org.newdawn.slick.Color;
import world.entities.magic.Spell;
import world.entities.magic.techniques.Techniques;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.ArrayList;

public class SpellcraftingMenu extends Modal {

    private HumanoidEntity target;
    private ArrayList<String> categories;
    private int currentCategory;
    private TextLabel categoryLabel;
    private TextLabel techniqueName, techniqueDescription;
    private ArrayList<Button> buttons;
    private TextBox nameField;
    private Canvas canvas;

    private TextLabel crystalCost, dyesCost, manaCost;
    private Button createButton;

    private Spell spell;

    public SpellcraftingMenu(HumanoidEntity target) {
        super("spellcasting.png");
        this.target = target;
        this.buttons = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.spell = new Spell();

        canvas = new Canvas(16, 16, 4);
        nameField = new TextBox(64, 8) {
            @Override
            public boolean onKeyUp(int key) {
                refreshRequirements();
                return super.onKeyUp(key);
            }
        };
        crystalCost = new TextLabel("0 Crystals", 4, Color.cyan, true);
        dyesCost = new TextLabel("0 Dyes", 4, Color.red, true);
        manaCost = new TextLabel("0 Mana", 4, Color.pink.darker(0.1f), true);
        categoryLabel = new TextLabel("...", 4, Color.gray, false);
        techniqueName = new TextLabel("-", 5, Color.black, false);
        techniqueDescription = new TextLabel("", 4, 100, 4, Color.gray, false);
        createButton = new Button("Create!", 24, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                spell.setColor(canvas.getColor());
                spell.setPixels(canvas.getGrid());
                spell.setName(nameField.getText());
                target.getSpellbook().addSpell(spell);
                target.addCrystals(-spell.getCrystalCost());
                getGUI().popModal();
                return true;
            }
        };

        addChild(nameField, 8, 12, GUIAnchor.TOP_LEFT);
        addChild(canvas, 8, 26, GUIAnchor.TOP_LEFT);
        addChild(crystalCost, 8, -32, GUIAnchor.BOTTOM_LEFT);
        addChild(dyesCost, 8, -26, GUIAnchor.BOTTOM_LEFT);
        addChild(manaCost, 8, -20, GUIAnchor.BOTTOM_LEFT);
        addChild(new TextLabel("New Spell", 5, Color.white, true), 8, 4, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Discovered Techniques", 5, Color.white, true), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new Button("Cancel", 24, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                getGUI().popModal(); return true;
            }
        }, 12, -6, GUIAnchor.BOTTOM_LEFT);

        addChild(createButton, 40, -6, GUIAnchor.BOTTOM_LEFT);

        addChild(new Button(null, 8, 8, "icons/arrow_left.png", true) {
            @Override
            public boolean onClick(int button) {
                currentCategory = (int)MiscMath.clamp(currentCategory-1, 0, categories.size()-1);
                refreshTechniquesPanel();
                return true;
            }
        }, -21, 16, GUIAnchor.TOP_RIGHT);
        addChild(new Button(null, 8, 8, "icons/arrow_right.png", true) {
            @Override
            public boolean onClick(int button) {
                currentCategory = (int)MiscMath.clamp(currentCategory+1, 0, categories.size()-1);
                refreshTechniquesPanel();
                return true;
            }
        }, -9, 16, GUIAnchor.TOP_RIGHT);
        addChild(categoryLabel, 84, 16, GUIAnchor.TOP_LEFT);
        addChild(techniqueName, 84, -32, GUIAnchor.BOTTOM_LEFT);
        addChild(techniqueDescription, 85, 100, GUIAnchor.TOP_LEFT);
    }

    private void refreshTechniquesPanel() {
        categories = Techniques.getAllCategories();
        categoryLabel.setText(categories.get(currentCategory));
        String[] techniques = Techniques.getAll();
        for (int i = buttons.size() - 1; i > -1; i--) { removeChild(buttons.get(i)); buttons.remove(i); }
        int p = 0;
        for (int i = 0; i < techniques.length; i++) {
            String technique = techniques[i];
            System.out.println(technique);
            if (target.getSpellbook().hasTechnique(techniques[i])
                    && Techniques.getCategory(technique).equals(categories.get(currentCategory))) {
                System.out.println("Target has "+techniques[i]);
                Button chooseButton = new Button(null, 16, 16, "icons/techniques/" + technique.toLowerCase() + ".png", true) {
                    @Override
                    public boolean onClick(int button) {
                        if (spell.hasTechnique(technique)) {
                            spell.removeTechnique(technique);
                            setToggled(false);
                        } else {
                            spell.addTechnique(technique);
                            setToggled(true);
                        }
                        refreshRequirements();
                        return true;
                    }

                    @Override
                    public boolean onMouseMoved(int ogx, int ogy) {
                        if (!mouseIntersects()) return false;
                        techniqueName.setText(Techniques.getName(technique));
                        techniqueDescription.setText(Techniques.getDescription(technique));
                        return true;
                    }
                };
                buttons.add(chooseButton);
                chooseButton.setToggled(spell.hasTechnique(technique));
                addChild(chooseButton, ((p % 5) * 18), 19 + 8 + ((p / 5) * 19), GUIAnchor.TOP_MIDDLE);
                p++;
            }
        }
    }

    private void refreshRequirements() {
        this.crystalCost.setText(target.getCrystalCount()+"/"+spell.getCrystalCost()+" Crystals");
        this.dyesCost.setText(target.getDyeCount()+"/0 Dyes");
        this.manaCost.setText((int)target.getMana()+"/"+spell.getManaCost()+" Mana");
        this.createButton.setEnabled(
                target.getCrystalCount() >= spell.getCrystalCost()
                && !nameField.getText().trim().isEmpty()
                && !spell.isEmpty());
    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        techniqueName.setText("");
        techniqueDescription.setText("");
        return true;
    }

    @Override
    public void onShow() {
        super.onShow();
        reset();
    }

    private void reset() {
        this.spell = new Spell();
        refresh();
    }

    private void refresh() {
        refreshRequirements();
        this.nameField.setText(spell.getName());
        this.canvas.reset();
        refreshTechniquesPanel();
    }

}
