package gui.menus;

import com.github.mathiewz.slick.Color;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.Modal;
import gui.elements.TextBox;
import gui.elements.TextLabel;
import network.MPClient;
import network.MPServer;
import world.entities.components.magic.Spell;

import java.util.Random;

public class ServerSelectMenu extends Modal {

    private TextBox hostField;
    private Button connectButton, soloButton;

    private Random rng;

    public ServerSelectMenu() {
        super("gui/popup.png");

        rng = new Random();

        hostField = new TextBox(72, 8);

        hostField.setText("127.0.0.1");
        connectButton = new Button("Connect", 24, 8, null, true) {
            @Override
            public void onClick(int button) {
                MPClient.init();
                MPClient.join(hostField.getText());
                hostField.releaseFocus();
            }
        };

        soloButton = new Button("Play Solo", 24, 8, null, true) {
            @Override
            public void onClick(int button) {
                MPServer.init();
                MPServer.launch(0, true);
                MPClient.init();
                MPClient.join(hostField.getText());
                hostField.releaseFocus();
            }
        };

        addChild(hostField, 10, 16, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Single Player", 5, Color.darkGray, false, false), 10, 38, GUIAnchor.TOP_LEFT);
        addChild(new TextLabel("Join Game", 5, Color.darkGray, false, false), 10, 8, GUIAnchor.TOP_LEFT);
        //addChild(new TextLabel("Character Customization", 5, Color.white, true, false), 24, 4, GUIAnchor.TOP_MIDDLE);
        addChild(new Button("Cancel", 24, 8, null, true) {
            @Override
            public void onClick(int button) {
                getGUI().popModal();
            }
        }, 10, -10, GUIAnchor.BOTTOM_LEFT);

        addChild(connectButton, 10, 28, GUIAnchor.TOP_LEFT);
        addChild(soloButton, 10, 48, GUIAnchor.TOP_LEFT);

    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        return true;
    }

    @Override
    public void onShow() {
        hostField.grabFocus();
    }

    @Override
    public void onHide() {
        hostField.releaseFocus();
    }

    public void reset(Spell template) {
        refresh();
    }

    private void refresh() {

    }

}
