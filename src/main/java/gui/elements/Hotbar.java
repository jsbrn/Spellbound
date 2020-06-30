package gui.elements;

import gui.GUIElement;
import misc.MiscMath;
import com.github.mathiewz.slick.*;
import world.entities.Entities;
import world.entities.components.SpellbookComponent;
import world.particles.ParticleSource;

public class Hotbar extends GUIElement {

    private Image image, selected;

    private SpellbookComponent spellbook;

    private ParticleSource[] previews;

    public Hotbar(Integer target) {
        this.spellbook = (SpellbookComponent)Entities.getComponent(SpellbookComponent.class, target);
        this.previews = new ParticleSource[3];
        try {
            this.image = new Image("gui/hotbar.png", false, Image.FILTER_NEAREST);
            this.selected = new Image("gui/hotbar_selected.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] getDimensions() {
        return new int[]{ image.getWidth(), image.getHeight() };
    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        return false;
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        return false;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) { return false; }

    @Override
    public boolean onMouseScroll(int direction) {
        int current = spellbook.getSelectedIndex();
        int new_ = (int)MiscMath.clamp(current + direction, 0, 2);
        spellbook.selectSpell(new_);
        return true;
    }

    @Override
    public boolean onKeyDown(int key) {
        if (key == Input.KEY_1) { spellbook.selectSpell(0); return true; }
        if (key == Input.KEY_2) { spellbook.selectSpell(1); return true; }
        if (key == Input.KEY_3) { spellbook.selectSpell(2); return true; }
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        b.drawImage(image, 0, 0);
        b.drawImage(selected, 3, 3 + (17 * spellbook.getSelectedIndex()));
        for (int i = 0; i < spellbook.getSpells().size(); i++) {
            Image icon = spellbook.getSpell(i).getIcon();
            if (icon == null) continue;
            b.drawImage(icon, 2, 2 + (i * 17), spellbook.getSpell(i).getColor());
        }
    }

}
