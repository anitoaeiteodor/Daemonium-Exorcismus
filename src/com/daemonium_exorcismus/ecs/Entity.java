package com.daemonium_exorcismus.ecs;

import com.daemonium_exorcismus.ecs.components.Component;

import java.util.HashMap;

public class Entity {

    private String id;
    private EntityType type;
    private HashMap<String, Component> components = new HashMap<>();

    public Entity(EntityType type) {
        id = String.format("%f", Math.random());
        this.type = type;
    }

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

    public Component getComponent(String name) {
        return components.getOrDefault(name, null);
    }
}
