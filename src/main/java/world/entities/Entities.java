package world.entities;

import org.json.simple.JSONObject;
import world.entities.components.Component;
import world.entities.components.LocationComponent;

import java.util.HashMap;

public class Entities {

    private static HashMap<Class, HashMap<Integer, Component>> COMPONENT_MAPS = new HashMap<>();

    public static void addEntity(JSONObject object) {
        //deserialize the components from the json and add them to the lists
    }

    public static void removeEntity(int entityID) {
        COMPONENT_MAPS.keySet().forEach(componentClass -> removeComponent(componentClass, entityID));
    }

    public static Component getComponent(Class componentClass, int entityID) {
        return COMPONENT_MAPS.get(componentClass).get(entityID);

    }

    private static void addComponent(Component component, int entityID) {
        COMPONENT_MAPS.computeIfAbsent(component.getClass(), k -> new HashMap<>());
        COMPONENT_MAPS.get(component.getClass()).put(entityID, component);
    }

    private static void removeComponent(Class componentClass, int entityID) {
        HashMap<Integer, Component> componentList = COMPONENT_MAPS.get(componentClass);
        componentList.remove(entityID);
    }

}
