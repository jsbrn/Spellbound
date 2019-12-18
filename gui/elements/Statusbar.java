package gui.elements;

import gui.GUIElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.entities.Entity;
import world.entities.HumanoidEntity;
import world.entities.types.Player;

public class Statusbar extends GUIElement {

    private Color healthColor, manaColor;
    private Image image;
    private HumanoidEntity target;

    public Statusbar(HumanoidEntity target) {
        this.target = target;
        try {
            this.image = new Image("assets/gui/statusbar.png", false, Image.FILTER_NEAREST);
            this.healthColor = new Color(1f, 0f, 0f, 0.5f);
            this.manaColor = new Color(220, 25, 225, 255/2);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] getDimensions() {
        return new int[]{ image.getWidth(), image.getHeight() };
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy) {
        return false;
    }

    @Override
    public boolean onKeyDown(int key) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b) {
        b.drawImage(image, 0, 0);
        b.setColor(healthColor);
        b.fillRect(15, 7, (int)(32 * (target.getHP() / target.getMaxHP())), 3);
        b.setColor(manaColor);
        b.fillRect(15, 14, (int)(32 * (target.getMana() / target.getMaxMana())), 3);
    }

}
