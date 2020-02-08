package gui.elements;

import gui.GUIElement;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import world.entities.types.humanoids.HumanoidEntity;
import world.particles.ParticleSource;

public class Hotbar extends GUIElement {

    private Image image, selected;
    private HumanoidEntity target;

    private ParticleSource[] previews;

    public Hotbar(HumanoidEntity target) {
        this.target = target;
        this.previews = new ParticleSource[3];
        try {
            this.image = new Image("assets/gui/hotbar.png", false, Image.FILTER_NEAREST);
            this.selected = new Image("assets/gui/hotbar_selected.png", false, Image.FILTER_NEAREST);
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
    public boolean onKeyDown(int key) {
        if (key == Input.KEY_1) { target.getSpellbook().selectSpell(0); return true; }
        if (key == Input.KEY_2) { target.getSpellbook().selectSpell(1); return true; }
        if (key == Input.KEY_3) { target.getSpellbook().selectSpell(2); return true; }
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        b.drawImage(image, 0, 0);
        b.drawImage(selected, 3, 3 + (17 * target.getSpellbook().getSelectedIndex()));
        for (int i = 0; i < target.getSpellbook().getSpells().size(); i++) {
            Image icon = target.getSpellbook().getSpell(i).getIcon();
            b.drawImage(icon, 2, 2 + (i * 17));
        }
    }

}
