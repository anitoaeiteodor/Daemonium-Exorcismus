package com.daemonium_exorcismus.spawn;

import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.engine.core.Game;

import java.util.ArrayList;

public class Wave {
    private ArrayList<EntityType> enemies;
    private long delay;
    private long offset;

    public Wave(ArrayList<EntityType> enemies, long delay, long offset) {
        this.enemies = enemies;
        this.delay = delay;
        this.offset = offset;
    }

    public EntityType getEnemy() {
        EntityType enemy = enemies.get(0);
        enemies.remove(0);
        return enemy;
    }

    public boolean canSpawn(long time) {
        return (time > delay * Game.timeFrame + offset * 1000);
    }

    public boolean isFinished() {
        return enemies.isEmpty();
    }
}
