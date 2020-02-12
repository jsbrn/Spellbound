package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import org.newdawn.slick.Color;
import world.entities.magic.Spell;
import world.entities.magic.techniques.Techniques;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.ArrayList;

public class SpellcraftingMenu extends Modal {

    private HumanoidEntity target;
    private ArrayList<String> categories;
    private int currentCategory;
    private ArrayList<Button> buttons;
    private TextBox nameField;
    private Canvas canvas;

    private TextLabel crystalCost, dyesCost, manaCost;

    private Spell spell;

    public SpellcraftingMenu(HumanoidEntity target) {
        super("spellcasting.png");
        this.target = target;
        this.buttons = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.spell = new Spell();

        canvas = new Canvas(16, 16, 4);
        nameField = new TextBox(64, 8);
        crystalCost = new TextLabel("0 Crystals", 4, Color.cyan, true);
        dyesCost = new TextLabel("0 Dyes", 4, Color.red, true);
        manaCost = new TextLabel("0 Mana", 4, Color.pink.darker(0.1f), true);

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
        addChild(new Button("Create!", 24, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                spell.setColor(canvas.getColor());
                spell.setPixels(canvas.getGrid());
                spell.setName(nameField.getText());
                target.getSpellbook().addSpell(spell);
                getGUI().popModal();
                return true;
            }
        }, 40, -6, GUIAnchor.BOTTOM_LEFT);
    }

    private void refreshButtons() {
        categories = Techniques.getAllCategories();
        String[] techniques = Techniques.getAll();
        for (int i = 0; i < buttons.size(); i++) { removeChild(buttons.get(i)); buttons.remove(i); }
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
                        return true;
                    }
                };
                buttons.add(chooseButton);
                chooseButton.setToggled(spell.hasTechnique(technique));
                addChild(chooseButton, ((i % 5) * 18), 19 + ((i / 5) * 19), GUIAnchor.TOP_MIDDLE);
            }
        }
    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        return false;
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
        this.nameField.setText(spell.getName());
        this.canvas.reset();
        refreshButtons();
        this.crystalCost.setText(target.getCrystalCount()+"/0 Crystals");
        this.dyesCost.setText(target.getDyeCount()+"/0 Dyes");
        this.manaCost.setText("0 Mana");
    }

}
