package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.ecs.Entity;

import java.util.HashMap;

/**
 * Base class for the system.
 */
abstract public class SystemBase {
    protected String name;
    protected long oldTime;

    public SystemBase(long oldTime) {
        this.oldTime = oldTime;
    }

    /**
     * This method updates the system.
     * @param entityList hashmap of all the entities currently in the game
     * @param newTime used to calculate elapsed time
     */
    abstract public void updateSystem(HashMap<String, Entity> entityList, long newTime);

    public String getName() {
        return name;
    }
}
