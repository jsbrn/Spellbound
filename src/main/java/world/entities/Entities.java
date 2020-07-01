package world.entities;

import org.json.simple.JSONObject;
import world.entities.components.Component;

import java.util.*;

public class Entities {

    private static int lastEntityId = 0;
    private static HashMap<Class, LinkedHashMap<Integer, Component>> COMPONENT_MAPS = new HashMap<>();

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
        for (LinkedHashMap<Integer, Component> componentMap: COMPONENT_MAPS.values()) {
            Component c = componentMap.get(entityID);
            entity.put(c.getID(), c);
        }
        return entity;
    }

    public static void removeEntity(int entityID) {
        COMPONENT_MAPS.keySet().forEach(componentClass -> removeComponent(componentClass, entityID));
    }

    public static Component getComponent(Class componentClass, int entityID) {
        LinkedHashMap listOfComponents = COMPONENT_MAPS.get(componentClass);
        if (listOfComponents == null) return null;
        return (Component)listOfComponents.get(entityID);
    }

    public static Set<Integer> getEntitiesWith(Class... componentClasses) {
        HashSet allEntityIDs = new HashSet(COMPONENT_MAPS.values().stream()
                .map(lhm -> new ArrayList(lhm.keySet()))
                .reduce(new ArrayList(), (total, current) -> {
                    total.addAll(current);
                    return total;
                }));
        for (Class componentClass: componentClasses) {
            if (COMPONENT_MAPS.get(componentClass) == null) return new HashSet<>();
            Set<Integer> entityIDs = COMPONENT_MAPS.get(componentClass).keySet();
            allEntityIDs.retainAll(entityIDs);
        }
        return allEntityIDs;
    }

    private static void addComponent(Component component, int entityID) {
        COMPONENT_MAPS.computeIfAbsent(component.getClass(), k -> new LinkedHashMap<>());
        COMPONENT_MAPS.get(component.getClass()).put(entityID, component);
    }

    public static void removeComponent(Class componentClass, int entityID) {
        HashMap<Integer, Component> componentList = COMPONENT_MAPS.get(componentClass);
        if (componentList == null) return;
        componentList.remove(entityID);
    }

    public static void removeAll() {
        COMPONENT_MAPS.clear();
    }

}
