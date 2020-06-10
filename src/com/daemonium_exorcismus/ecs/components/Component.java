package com.daemonium_exorcismus.ecs.components;

/**
 * Super class of all components. It only stores the name of the component.
 */
abstract public class Component {
    protected String name;

    public String getName() {
        return name;
    }
}
