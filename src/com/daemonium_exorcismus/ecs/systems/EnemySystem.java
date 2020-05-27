package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.KinematicBodyComponent;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.util.ArrayList;
import java.util.HashMap;

public class EnemySystem extends SystemBase {

    public EnemySystem() {
        name = SystemNames.ENEMY;
        oldTime = 0;
    }

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        if (newTime - oldTime < Game.timeFrame) {
            return;
        }

        oldTime = newTime;

        ArrayList<Entity> enemyList = new ArrayList<>();

        for (Entity ent : entityList.values()) {
            if (ent.getType() == EntityType.ENEMY) {
                enemyList.add(ent);
            }
        }

        for (Entity enemy : enemyList) {
            boolean changeDir = Math.random() < 0.5;
            if (changeDir) {
                Vec2D newDir = new Vec2D(Math.cos(Math.random() * Math.PI * 2),
                        Math.sin(Math.random() * Math.PI * 2)).scale(5);
                KinematicBodyComponent kb = (KinematicBodyComponent) enemy.getComponent(ComponentNames.KINEMATIC_BODY);
                kb.setVelocity(newDir);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
