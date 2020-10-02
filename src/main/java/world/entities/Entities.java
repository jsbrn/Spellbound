package world.entities;

import misc.MiscMath;
import misc.annotations.ClientExecution;
import misc.annotations.ServerExecution;
import org.json.simple.JSONObject;
import world.entities.components.Component;

import java.util.*;
import java.util.stream.Collectors;

public class Entities {

    private HashMap<Class, LinkedHashMap<Integer, Component>> componentMaps = new HashMap<>();

    @ClientExecution
    public int putEntity(int entityID, JSONObject json) {
        //deserialize the components from the json and add them to the lists
        for (Object key: json.keySet()) {
            Component component = Component.create((String)key, (JSONObject)json.get(key));
            addComponent(component, entityID);
        }
        return entityID;
    }

    public boolean exists(int entityID) {
        return componentMaps.values().stream().anyMatch(lic -> lic.containsKey(entityID));
    }

    public JSONObject serializeEntity(int entityID) {
        JSONObject entity = new JSONObject();
        for (LinkedHashMap<Integer, Component> componentMap: componentMaps.values()) {
            Component c = componentMap.get(entityID);
            entity.put(c.getID(), c.serialize());
        }
        return entity;
    }

    public void removeEntity(int entityID) {
        componentMaps.keySet().forEach(componentClass -> removeComponent(componentClass, entityID));
    }

    public Component getComponent(Class componentClass, int entityID) {
        LinkedHashMap listOfComponents = componentMaps.get(componentClass);
        if (listOfComponents == null) return null;
        return (Component)listOfComponents.get(entityID);
    }

    public Set<Integer> getEntitiesWith(Class... componentClasses) {
        return getEntitiesWith(componentMaps.values().stream()
                .map(lhm -> new ArrayList<Integer>(lhm.keySet()))
                .reduce(new ArrayList<Integer>(), (total, current) -> {
                    total.addAll(current);
                    return total;
                }), componentClasses);
    }

    public Set<Integer> getEntitiesWith(ArrayList<Integer> from, Class... componentClasses) {
        HashSet<Integer> entities = new HashSet<Integer>(from);
        return entities.stream()
                .filter(e -> componentMaps.values().stream().allMatch(lhm -> lhm.containsKey(e)))
                .collect(Collectors.toSet());
    }

    public ArrayList<Component> getComponents(Integer entityID) {
        ArrayList<Component> components = new ArrayList<>();
        for (HashMap<Integer, Component> componentMap: componentMaps.values())
            if (componentMap.get(entityID) != null) components.add(componentMap.get(entityID));
        return components;
    }

    public void addComponent(Component component, int entityID) {
        componentMaps.computeIfAbsent(component.getClass(), k -> new LinkedHashMap<>());
        componentMaps.get(component.getClass()).put(entityID, component);
        component.setParent(entityID);
    }

    public void removeComponent(Class componentClass, int entityID) {
        HashMap<Integer, Component> componentList = componentMaps.get(componentClass);
        if (componentList == null) return;
        componentList.remove(entityID);
    }

}
