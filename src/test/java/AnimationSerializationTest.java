import assets.Assets;
import org.json.simple.JSONArray;
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
        JSONArray animations = (JSONArray)Assets.json("animations/humanoid/torso/animations.json", true);
        animJSON = (JSONObject)animations.get(0);
        animatorJSON = (JSONObject)Assets.json("components/humanoid_animator.json", true);
    }

    @Test
    void testAnimationDeserialization() {
        Animation.deserialize(animJSON);
    }

    @Test
    void testAnimator() {
        AnimatorComponent ac = new AnimatorComponent();
        ac.deserialize(animatorJSON);
        Assertions.assertEquals(animatorJSON.toJSONString(), ac.serialize().toJSONString());
    }

}
