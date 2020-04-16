package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.RigidBodyComponent;
import com.daemonium_exorcismus.engine.core.Game;

import java.util.ArrayList;
import java.util.HashMap;

public class PhysicsSystem extends SystemBase {

    public PhysicsSystem() {
        this.name = SystemNames.PHYSICS;
        oldTime = 0;
    }

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        if(newTime - oldTime < Game.timeFrame)
            return;

        oldTime = newTime;

        ArrayList<Entity> physicsObjects = new ArrayList<>();

        for (String key : entityList.keySet()) {
            if (entityList.get(key).hasComponent(ComponentNames.KINEMATIC_BODY)) {
                physicsObjects.add(entityList.get(key));
            }
        }

        for (Entity entity : physicsObjects) {
            applyVelocity(entity);
        }
    }

    private void applyVelocity(Entity entity) {
        KinematicBodyComponent comp = ((KinematicBodyComponent) entity.getComponent(ComponentNames.KINEMATIC_BODY));
        comp.setPos(comp.getPos().add(comp.getVelocity()));
    }
}
