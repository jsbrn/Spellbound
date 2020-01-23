package gui.elements;

import gui.GUIAnchor;
import org.newdawn.slick.Color;

public class Journal extends Modal {

    public Journal() {
        super("spellbook_bg.png");
        addChild(new Label("Create a spell", 5, Color.black), 12, 4, GUIAnchor.TOP_LEFT);
        addChild(new Button("Click here!", 32, 8) {
            @Override
            public boolean onClick(int button) {
                return true;
            }
        }, 12, 24, GUIAnchor.TOP_LEFT);
    }

}
