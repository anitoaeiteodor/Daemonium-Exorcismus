package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.ColliderComponent;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.RigidBodyComponent;
import com.daemonium_exorcismus.engine.core.Game;
import javafx.geometry.Rectangle2D;

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
            if (entityList.get(key).hasComponent(ComponentNames.KINEMATIC_BODY) ||
                entityList.get(key).hasComponent(ComponentNames.COLLIDER)) {
                physicsObjects.add(entityList.get(key));
            }
        }


        for (Entity entity : physicsObjects) {
            applyVelocity(entity);
        }

        ArrayList<Entity> revertVelocityObjects = new ArrayList<>();
        for (int i = 0; i < physicsObjects.size() - 1; i++) {
            for (int j = i + 1; j < physicsObjects.size(); j++) {
                if (checkCollision(physicsObjects.get(i), physicsObjects.get(j))) {
                    revertVelocityObjects.add(physicsObjects.get(i));
                    revertVelocityObjects.add(physicsObjects.get(j));
                }
            }
        }

        for (Entity entity : revertVelocityObjects) {
            revertVelocity(entity);
        }
    }

    private void revertVelocity(Entity entity) {
        KinematicBodyComponent comp = ((KinematicBodyComponent) entity.getComponent(ComponentNames.KINEMATIC_BODY));
        if (comp == null) {
            return;
        }

        comp.setPos(comp.getPos().sub(comp.getVelocity()));
    }

    private void applyVelocity(Entity entity) {
        KinematicBodyComponent comp = ((KinematicBodyComponent) entity.getComponent(ComponentNames.KINEMATIC_BODY));
        if (comp == null) {
            return;
        }
        comp.setPos(comp.getPos().add(comp.getVelocity()));
    }

    private boolean checkCollision(Entity entity, Entity other) {
        Rectangle2D entityRect = getRigidBodyRectangle(entity);
        Rectangle2D otherRect = getRigidBodyRectangle(other);

        return entityRect.intersects(otherRect);
    }

    private Rectangle2D getRigidBodyRectangle(Entity entity) {
        RigidBodyComponent entityComponent = (RigidBodyComponent) entity.getComponent(ComponentNames.KINEMATIC_BODY);
        if (entityComponent == null) {
            entityComponent = (RigidBodyComponent) entity.getComponent(ComponentNames.RIGID_BODY);
        }

        ColliderComponent collisionComponent = (ColliderComponent) entity.getComponent(ComponentNames.COLLIDER);
        if (collisionComponent == null) {
            System.err.println("[ERROR]: A physics entity does not have implement the collider component!");
            return Rectangle2D.EMPTY;
        }
        int sizeX = (int)entityComponent.getSize().getPosX();
        int sizeY = (int)entityComponent.getSize().getPosY();
        int posX = (int)entityComponent.getPos().getPosX();
        int posY = (int)entityComponent.getPos().getPosY();

        return new Rectangle2D(posX + collisionComponent.getOffsetFirst().getPosX() * sizeX,
                               posY + collisionComponent.getOffsetFirst().getPosY() * sizeY,
                            sizeX - collisionComponent.getOffsetSecond().getPosX() * sizeX
                                        - collisionComponent.getOffsetFirst().getPosX() * sizeX,
                            sizeY - collisionComponent.getOffsetSecond().getPosY() * sizeY
                                        - collisionComponent.getOffsetFirst().getPosY() * sizeY);

    }
}
