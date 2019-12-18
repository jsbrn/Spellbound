package gui.elements;

import gui.GUIElement;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import world.entities.HumanoidEntity;

public class Hotbar extends GUIElement {

    private Image image;
    private HumanoidEntity target;

    public Hotbar(HumanoidEntity target) {
        try {
            this.image = new Image("assets/gui/hotbar.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] getDimensions() {
        return new int[]{ image.getWidth(), image.getHeight() };
    }

    @Override
    public boolean onClick(int ogx, int ogy) {
        return false;
    }

    @Override
    public boolean onKeyPress(int key) {
        if (key == Input.KEY_1) { target.getSpellbook().selectSpell(0); return true; }
        if (key == Input.KEY_2) { target.getSpellbook().selectSpell(1); return true; }
        if (key == Input.KEY_3) { target.getSpellbook().selectSpell(2); return true; }
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b) {
        b.drawImage(image, 0, 0);
    }

}
