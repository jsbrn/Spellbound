package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.entities.types.humanoids.HumanoidEntity;

public class Statusbar extends GUIElement {

    private Color healthColor, manaColor, staminaColor;
    private Image image;
    private HumanoidEntity target;

    private TextLabel healthLabel, manaLabel, staminaLabel;

    public Statusbar(HumanoidEntity target) {
        this.target = target;
        healthLabel = new TextLabel("0/0", 3, Color.white, false);
        manaLabel = new TextLabel("0/0", 3, Color.white, false);
        staminaLabel = new TextLabel("0/0", 3, Color.white, false);
        this.addChild(healthLabel, 5, -8, GUIAnchor.CENTER);
        this.addChild(manaLabel, 5, -1, GUIAnchor.CENTER);
        this.addChild(staminaLabel, 5, 6, GUIAnchor.CENTER);
        try {
            this.image = new Image("assets/gui/statusbar.png", false, Image.FILTER_NEAREST);
            this.healthColor = new Color(1f, 0f, 0f, 0.5f);
            this.manaColor = new Color(220, 25, 225, 255/2);
            this.staminaColor = new Color(50, 225, 60, 0.5f);
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
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {

        healthLabel.setText((int)target.getHP()+"/"+(int)target.getMaxHP());
        manaLabel.setText((int)target.getMana()+"/"+(int)target.getMaxMana());
        staminaLabel.setText((int)target.getStamina()+"/"+(int)target.getMaxStamina());

        b.drawImage(image, 0, 0);
        b.setColor(healthColor);
        b.fillRect(16, 8, (int)(32 * (target.getHP() / target.getMaxHP())), 3);
        b.setColor(manaColor);
        b.fillRect(16, 15, (int)(32 * (target.getMana() / target.getMaxMana())), 3);
        b.setColor(staminaColor);
        b.fillRect(16, 22, (int)(32 * (target.getStamina() / target.getMaxStamina())), 3);
    }

}
