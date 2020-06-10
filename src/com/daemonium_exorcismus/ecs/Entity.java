package com.daemonium_exorcismus.ecs;

import com.daemonium_exorcismus.ecs.components.Component;

import java.util.HashMap;

/**
 * Base class for all entities in the game. By itself it has no functionality.
 */
public class Entity {

    private String id;
    private EntityType type;
    private HashMap<String, Component> components = new HashMap<>();

    public Entity(EntityType type) {
        id = String.format("%f", Math.random());
        this.type = type;
    }

    /**
     * Method used to add components to the entity.
     * @param component component to add
     */
    public void addComponent(Component component) {
        if (components.containsKey(component.getName())) {
            System.out.println("Component already attached to entity " + id);
            return;
        }
        components.put(component.getName(), component);
    }

    public void removeComponent(Component component) {
        if (!components.containsKey(component.getName())) {
            System.out.println("Component not attached to entity " + id);
            return;
        }
        components.remove(component.getName());
    }

    public boolean hasComponent(String name) {
        return components.containsKey(name);
    }

    public String getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    /**
     * Return the component if the entity has it.
     * @param name name of the component
     * @return component if present, else null
     */
    public Component getComponent(String name) {
        return components.getOrDefault(name, null);
    }
}
