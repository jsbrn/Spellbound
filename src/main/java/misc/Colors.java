package misc;

import com.github.mathiewz.slick.Color;
import net.sourceforge.jnlp.util.optionparser.InvalidArgumentException;
import org.json.simple.JSONArray;

public class Colors {

    public static Color fromJSONArray(JSONArray jsonColor) {
        return new Color(
                jsonColor.size() >= 1 ? ((Number)jsonColor.get(0)).intValue() : 255,
                jsonColor.size() >= 2 ? ((Number)jsonColor.get(1)).intValue() : 255,
                jsonColor.size() >= 3 ? ((Number)jsonColor.get(2)).intValue() : 255,
                jsonColor.size() >= 4 ? ((Number)jsonColor.get(3)).intValue() : 255
        );
    }

    public static JSONArray toJSONArray(Color c) {
        JSONArray color = new JSONArray();
        color.add(c.getRed());
        color.add(c.getGreen());
        color.add(c.getBlue());
        color.add(c.getAlpha());
        return color;
    }

}
