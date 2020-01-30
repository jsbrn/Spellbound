package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import org.newdawn.slick.Color;
import world.entities.magic.Spell;
import world.entities.magic.techniques.TechniqueName;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.ArrayList;

public class SpellcraftingMenu extends Modal {

    private HumanoidEntity target;
    private ArrayList<Button> buttons;
    private TextBox nameField;
    private Canvas canvas;

    private Spell spell;

    public SpellcraftingMenu(HumanoidEntity target) {
        super("spellcasting.png");
        this.target = target;
        this.buttons = new ArrayList<>();
        this.spell = new Spell();
        nameField = new TextBox(60, 8);
        addChild(nameField, 8, 12, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("New Spell", 5, Color.white, true), 8, 4, GUIAnchor.TOP_LEFT);
        canvas = new Canvas(15, 15, 4);
        addChild(canvas, 8, 32, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Discovered Techniques", 5, Color.white, true), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new Button("Cancel", 24, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                getGUI().popModal(); return true;
            }
        }, 12, -10, GUIAnchor.BOTTOM_LEFT);
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
        }, 38, -10, GUIAnchor.BOTTOM_LEFT);
    }

    private void refreshButtons() {

        for (int i = 0; i < buttons.size(); i++) { removeChild(buttons.get(i)); buttons.remove(i); }
        int techniquesCount = TechniqueName.values().length;
        for (int i = 0; i < techniquesCount; i++) {
            TechniqueName technique = TechniqueName.values()[i];
            System.out.println(technique);
            if (target.getSpellbook().hasTechnique(TechniqueName.values()[i])) {
                System.out.println("Target has "+TechniqueName.values()[i]);
                Button chooseButton = new Button(null, 16, 16, "icons/techniques/" + technique.name().toLowerCase() + ".png", true) {
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
    public void onShow() {
        super.onShow();
        reset();
    }

    private void reset() {
        this.spell = new Spell();
        this.nameField.setText("");
        this.canvas.reset();
        refreshButtons();
    }

    private void refresh() {
        if (buttons.size() == target.getSpellbook().getTechniqueCount()) return;
        refreshButtons();
    }

}
