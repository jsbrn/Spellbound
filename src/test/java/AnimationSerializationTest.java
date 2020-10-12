import assets.Assets;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import world.entities.components.AnimatorComponent;
import world.entities.components.animations.Animation;

public class AnimationSerializationTest {

    private static JSONObject animJSON, animatorJSON;

    @BeforeEach
    void setUp() {
        animJSON = Assets.json("definitions/entities/test/exampleAnimation.json", true);
        animatorJSON = Assets.json("definitions/entities/test/exampleAnimator.json", true);
    }

    @Test
    void testAnimation() {
        Animation a = Animation.deserialize(animJSON);
        Assertions.assertEquals(animJSON.toJSONString(), a.serialize().toJSONString());
    }

    @Test
    void testAnimator() {
        AnimatorComponent ac = new AnimatorComponent();
        ac.deserialize(animatorJSON);
        Assertions.assertEquals(animatorJSON.toJSONString(), ac.serialize().toJSONString());
    }

}
