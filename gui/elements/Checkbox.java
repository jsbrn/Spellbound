package gui.elements;

import assets.Assets;
import gui.GUIAnchor;
import org.newdawn.slick.Color;

import javax.xml.soap.Text;

public class Checkbox extends Button {

    private TextLabel label;

    public Checkbox(String label) {
        super(null, 8, 8, null, true);
        this.label = new TextLabel(label, 5, Color.white, true, false);
        addChild(this.label, 0, 0, GUIAnchor.RIGHT_MIDDLE);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{8 + label.getDimensions()[0] + 2, 8};
    }

    @Override
    public void onClick(int button) {
        setToggled(!isToggled());
    }

}