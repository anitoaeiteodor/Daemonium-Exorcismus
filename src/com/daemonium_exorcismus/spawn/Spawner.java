package com.daemonium_exorcismus.spawn;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.factory.EntityFactory;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.util.ArrayList;
import java.util.HashMap;

public class Spawner {
    private Vec2D position;
    private long delayTimer;
    private long oldTime;
    private Wave wave;

    public Spawner(Vec2D position) {
        this.position = position;
    }

    public void update(HashMap<String, Entity> entities, long newTime) {
        if (wave == null) {
            return;
        }
        if (wave.isFinished()) {
            return;
        }
        if (newTime - oldTime < Game.timeFrame) {
            return;
        }
        delayTimer += newTime - oldTime;
        oldTime = newTime;

        if (wave.canSpawn(delayTimer)) {
            delayTimer = 0;
            spawn(entities, wave.getEnemy());
        }
    }

    private void spawn(HashMap<String, Entity> entities, EntityType type) {
        if (type == null) {
            return;
        }
        EntityFactory factory = new EntityFactory();
        Entity enemy = factory.getEntity(type, position, true);
        entities.put(enemy.getId(), enemy);
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public boolean isFinished() {
        if (wave == null) return true;
        return wave.isFinished();
    }
}
