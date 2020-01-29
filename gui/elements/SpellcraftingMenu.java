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

    private Spell spell;

    public SpellcraftingMenu(HumanoidEntity target) {
        super("spellcasting.png");
        this.target = target;
        this.buttons = new ArrayList<>();
        this.spell = new Spell();
        addChild(new TextBox(60, 8), 8, 12, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("New Spell", 5, Color.white, true), 8, 4, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Discovered Techniques", 5, Color.white, true), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new Button("Cancel", 32, 8, null, true) {
            @Override
            public boolean onClick(int button) {
                getGUI().popModal(); return true;
            }
        }, 12, -10, GUIAnchor.BOTTOM_LEFT);
    }

    @Override
    public boolean onKeyUp(int key) {
        refresh();
        return super.onKeyUp(key);
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        return super.onMouseRelease(ogx, ogy, button);
    }

    private void refreshButtons() {
        if (buttons.size() == target.getSpellbook().getTechniqueCount()) return;
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

    private void refresh() {
        refreshButtons();
    }

}
