package world.entities;

import org.json.simple.JSONObject;
import world.entities.components.Component;

import java.util.HashMap;

public class Entities {

    private static int lastEntityId = 0;
    private static HashMap<Class, HashMap<Integer, Component>> COMPONENT_MAPS = new HashMap<>();

    public static int createEntity(JSONObject object) {
        int newId = lastEntityId + 1;
        //deserialize the components from the json and add them to the lists
        for (Object key: object.keySet()) {
            Component component = Component.create((String)key, (JSONObject)object.get(key));
            addComponent(component, newId);
        }
        lastEntityId = newId;
        return newId;
    }

    public static JSONObject serializeEntity(int entityID) {
        JSONObject entity = new JSONObject();
        for (HashMap<Integer, Component> componentMap: COMPONENT_MAPS.values()) {
            Component c = componentMap.get(entityID);
            entity.put(c.getID(), c);
        }
        return entity;
    }

    public static void removeEntity(int entityID) {
        COMPONENT_MAPS.keySet().forEach(componentClass -> removeComponent(componentClass, entityID));
    }

    public static Component getComponent(Class componentClass, int entityID) {
        HashMap listOfComponents = COMPONENT_MAPS.get(componentClass);
        if (listOfComponents == null) return null;
        return (Component)listOfComponents.get(entityID);
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
