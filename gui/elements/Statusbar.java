package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import gui.states.GameState;
import main.GameManager;
import org.newdawn.slick.*;
import world.World;
import world.entities.types.humanoids.HumanoidEntity;
import world.events.EventDispatcher;
import world.events.event.HumanoidRespawnEvent;

public class Statusbar extends GUIElement {

    private Color healthColor, manaColor, staminaColor;
    private Image image;
    private HumanoidEntity target;

    private TextLabel healthLabel, manaLabel, staminaLabel;

    private IconLabel amuletIndicator;

    public Statusbar(HumanoidEntity target) {
        this.target = target;
        healthLabel = new TextLabel("0/0", 3, Color.white, false, false);
        manaLabel = new TextLabel("0/0", 3, Color.white, false, false);
        staminaLabel = new TextLabel("0/0", 3, Color.white, false, false);
        this.addChild(healthLabel, 5, -8, GUIAnchor.CENTER);
        this.addChild(manaLabel, 5, -1, GUIAnchor.CENTER);
        this.addChild(staminaLabel, 5, 6, GUIAnchor.CENTER);
        amuletIndicator = new IconLabel("icons/amulet.png");
        this.addChild(amuletIndicator, 56, 0, GUIAnchor.TOP_LEFT);
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
    public boolean onMouseScroll(int direction) {
        return false;
    }

    @Override
    public boolean onKeyDown(int key) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        if (key == Input.KEY_R && World.getLocalPlayer().isDead()) {
            World.init();
            World.load();
            GameManager.getGameState(GameState.GAME_SCREEN).resetGUI();
            EventDispatcher.invoke(new HumanoidRespawnEvent(World.getLocalPlayer()));
            return true;
        }
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        healthLabel.setText(target.getHP() < 1 ? "!!!" : (int)target.getHP()+"/"+(int)target.getMaxHP());
        manaLabel.setText((int)target.getMana()+"/"+(int)target.getMaxMana());
        staminaLabel.setText((int)target.getStamina()+"/"+(int)target.getMaxStamina());

        if (target.hasAmulet()) amuletIndicator.show(); else amuletIndicator.hide();

        b.drawImage(image, 0, 0);
        b.setColor(healthColor);
        b.fillRect(16, 8, (int)(32 * (target.getHP() / target.getMaxHP())), 3);
        b.setColor(manaColor);
        b.fillRect(16, 15, (int)(32 * (target.getMana() / target.getMaxMana())), 3);
        b.setColor(staminaColor);
        b.fillRect(16, 22, (int)(32 * (target.getStamina() / target.getMaxStamina())), 3);
    }

}
