package world.entities;

import org.json.simple.JSONObject;
import world.entities.components.Component;

import java.util.*;
import java.util.stream.Collectors;

public class Entities {

    private int lastEntityID = 0;
    private HashMap<Class, LinkedHashMap<Integer, Component>> COMPONENT_MAPS = new HashMap<>();

    public int createEntity(JSONObject json) {
        int newID = lastEntityID++;
        return createEntity(newID, json);
    }

    public int createEntity(int newID, JSONObject json) {
        //deserialize the components from the json and add them to the lists
        for (Object key: json.keySet()) {
            Component component = Component.create((String)key, (JSONObject)json.get(key));
            addComponent(component, newID);
        }
        return newID;
    }

    public int updateEntity(int entityID, JSONObject newData) {
        removeEntity(entityID);
        createEntity(entityID, newData);
        return entityID;
    }

    public boolean exists(int entityID) {
        return COMPONENT_MAPS.values().stream().anyMatch(lic -> lic.containsKey(entityID));
    }

    public JSONObject serializeEntity(int entityID) {
        JSONObject entity = new JSONObject();
        for (LinkedHashMap<Integer, Component> componentMap: COMPONENT_MAPS.values()) {
            Component c = componentMap.get(entityID);
            entity.put(c.getID(), c.serialize());
        }
        return entity;
    }

    public void removeEntity(int entityID) {
        COMPONENT_MAPS.keySet().forEach(componentClass -> removeComponent(componentClass, entityID));
    }

    public Component getComponent(Class componentClass, int entityID) {
        LinkedHashMap listOfComponents = COMPONENT_MAPS.get(componentClass);
        if (listOfComponents == null) return null;
        return (Component)listOfComponents.get(entityID);
    }

    public Set<Integer> getEntitiesWith(Class... componentClasses) {
        return getEntitiesWith(COMPONENT_MAPS.values().stream()
                .map(lhm -> new ArrayList<Integer>(lhm.keySet()))
                .reduce(new ArrayList<Integer>(), (total, current) -> {
                    total.addAll(current);
                    return total;
                }), componentClasses);
    }

    public Set<Integer> getEntitiesWith(ArrayList<Integer> from, Class... componentClasses) {
        HashSet<Integer> entities = new HashSet<Integer>(from);
        return entities.stream()
                .filter(e -> COMPONENT_MAPS.values().stream().allMatch(lhm -> lhm.containsKey(e)))
                .collect(Collectors.toSet());
    }

    public ArrayList<Component> getComponents(Integer entityID) {
        ArrayList<Component> components = new ArrayList<>();
        for (HashMap<Integer, Component> componentMap: COMPONENT_MAPS.values())
            if (componentMap.get(entityID) != null) components.add(componentMap.get(entityID));
        return components;
    }

    public void addComponent(Component component, int entityID) {
        COMPONENT_MAPS.computeIfAbsent(component.getClass(), k -> new LinkedHashMap<>());
        COMPONENT_MAPS.get(component.getClass()).put(entityID, component);
        component.setParent(entityID);
    }

    public void removeComponent(Class componentClass, int entityID) {
        HashMap<Integer, Component> componentList = COMPONENT_MAPS.get(componentClass);
        if (componentList == null) return;
        Component removed = componentList.remove(entityID);
    }

}
