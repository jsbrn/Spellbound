package gui.menus;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Input;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.Modal;
import gui.elements.TextBox;
import gui.elements.TextLabel;
import world.Camera;

public class CheatCodeMenu extends Modal {

    private static TextBox input;

    public CheatCodeMenu() {
        super(Assets.getImage("gui/popup_small.png"));
        addChild(new TextLabel("ENTER CHEAT CODE", 6, Color.black, false, false), 0, 8, GUIAnchor.TOP_MIDDLE);
        input = new TextBox(64, 8) {
            @Override
            public boolean onKeyDown(int key, char c) {
                if (key == Input.KEY_ENTER) {
                    checkCode();
                    getGUI().popModal();
                }
                return super.onKeyDown(key, c);
            }
        };
        addChild(input, 0, 0, GUIAnchor.CENTER);
        addChild(new Button("Let's cheat!", 32, 8, null, true) {
            @Override
            public void onClick(int button) {
                checkCode();
                getGUI().popModal();
            }
        }, 0, -12, GUIAnchor.BOTTOM_MIDDLE);
    }

    private void checkCode() {
        String code = input.getText();
        if (code.equals("cinematic")) {
            Camera.setSpeed(5);
        }
    }

    @Override
    public boolean onKeyDown(int key, char c) {
        return true;
    }

    @Override
    public void onShow() {
        input.grabFocus();
    }

    @Override
    public void onHide() {
        input.releaseFocus();
    }
}
