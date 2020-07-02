package world.entities;

import org.json.simple.JSONObject;
import world.Region;
import world.entities.components.Component;
import world.events.EventDispatcher;

import java.util.*;
import java.util.stream.Collectors;

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
        return getEntitiesWith(COMPONENT_MAPS.values().stream()
                .map(lhm -> new ArrayList<Integer>(lhm.keySet()))
                .reduce(new ArrayList<Integer>(), (total, current) -> {
                    total.addAll(current);
                    return total;
                }), componentClasses);
    }

    public static Set<Integer> getEntitiesWith(ArrayList<Integer> from, Class... componentClasses) {
        HashSet<Integer> entities = new HashSet<Integer>(from);
        return entities.stream()
                .filter(e -> COMPONENT_MAPS.values().stream().allMatch(lhm -> lhm.containsKey(e)))
                .collect(Collectors.toSet());
    }

    private static void addComponent(Component component, int entityID) {
        COMPONENT_MAPS.computeIfAbsent(component.getClass(), k -> new LinkedHashMap<>());
        COMPONENT_MAPS.get(component.getClass()).put(entityID, component);
        component.setParent(entityID);
        EventDispatcher.register(component.getEventListener());
    }

    public static void removeComponent(Class componentClass, int entityID) {
        HashMap<Integer, Component> componentList = COMPONENT_MAPS.get(componentClass);
        if (componentList == null) return;
        Component removed = componentList.remove(entityID);
        if (removed != null) EventDispatcher.unregister(removed.getEventListener());
    }

    public static void removeAll() {
        COMPONENT_MAPS.values().forEach(lhm -> lhm.values().forEach(c -> EventDispatcher.unregister(c.getEventListener())));
        COMPONENT_MAPS.clear();
    }

}
