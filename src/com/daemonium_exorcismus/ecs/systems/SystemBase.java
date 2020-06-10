package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.ecs.Entity;

import java.util.HashMap;

abstract public class SystemBase {
    protected String name;
    protected long oldTime;

    public SystemBase(long oldTime) {
        this.oldTime = oldTime;
    }

    abstract public void updateSystem(HashMap<String, Entity> entityList, long newTime);

    public String getName() {
        return name;
    }
}
