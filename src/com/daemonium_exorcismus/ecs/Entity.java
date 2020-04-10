package com.daemonium_exorcismus.ecs;

import com.daemonium_exorcismus.ecs.components.Component;
import com.daemonium_exorcismus.ecs.components.ComponentNames;

import java.util.HashMap;

public class Entity {

    private String id;
    private HashMap<String, Component> components = new HashMap<>();

    public Entity() {
        id = String.format("%f", Math.random());
    }

    public void AddComponent(Component component) {
        if (components.containsKey(component.GetName())) {
            System.out.println("Component already attached to entity " + id);
            return;
        }
        components.put(component.GetName(), component);
    }

    public void RemoveComponent(Component component) {
        if (!components.containsKey(component.GetName())) {
            System.out.println("Component not attached to entity " + id);
            return;
        }
        components.remove(component.GetName());
    }

    public boolean hasComponent(String name) {
        return components.containsKey(name);
    }

    public String getId() {
        return id;
    }

    public HashMap<String, Component> GetComponents() {
        return components;
    }
}
