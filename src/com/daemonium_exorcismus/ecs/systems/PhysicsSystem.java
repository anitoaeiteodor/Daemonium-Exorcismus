package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.Constants;
import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.HealthComponent;
import com.daemonium_exorcismus.ecs.components.RenderComponent;
import com.daemonium_exorcismus.ecs.components.physics.ColliderComponent;
import com.daemonium_exorcismus.ecs.components.physics.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.physics.RigidBodyComponent;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is where we handle collisions, applying damage, sliding etc.
 */
public class PhysicsSystem extends SystemBase {

    public PhysicsSystem(long oldTime) {
        super(oldTime);
        this.name = SystemNames.PHYSICS;
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

        ArrayList<Entity> toRemove = new ArrayList<>();

        for (int i = 0; i < physicsObjects.size(); i++) {
            for (int j = 0; j < physicsObjects.size(); j++) {
                Entity entityA = physicsObjects.get(i);
                Entity entityB = physicsObjects.get(j);

                if (i == j) {
                    continue;
                }

                if (!checkCollision(entityA, entityB)) {
                    continue;
                }

                // if two bullets collide, they pass trough each other
                if (entityA.getType() == EntityType.ENEMY_PROJ && entityB.getType() == EntityType.PLAYER_PROJ
                    || entityA.getType() == EntityType.PLAYER_PROJ && entityB.getType() == EntityType.ENEMY_PROJ
                    || entityA.getType() == EntityType.ENEMY_PROJ && entityB.getType() == EntityType.ENEMY_PROJ) {
                    continue;
                }

                // if it's a bullet, don't bother checking axis collision

                if (entityA.getType() == EntityType.PLAYER_PROJ) {
//                    System.out.println("A bullet collided with a " + entityB.getType());
                    if (entityB.getType() == EntityType.REGULAR_ENEMY
                            || entityB.getType() == EntityType.MEDIUM_ENEMY
                            || entityB.getType() == EntityType.HEAVY_ENEMY) {
                        HealthComponent hc = (HealthComponent) entityB.getComponent(ComponentNames.HEALTH);
                        RenderComponent rc = (RenderComponent) entityB.getComponent(ComponentNames.RENDER);
                        rc.setFlashing(true);
                        hc.takeDamage(Constants.PLAYER_PROJ_DAMAGE);
                        if (hc.isDead()) {
                            toRemove.add(entityB);
                        }
                    }
                    toRemove.add(entityA);
                    continue;
                }

                // if player collided with an enemy projectile, take damage

                if (entityA.getType() == EntityType.ENEMY_PROJ) {
                    if (entityB.getType() == EntityType.PLAYER) {
//                        System.out.println("Player collided with enemy proj");
                        HealthComponent hc = (HealthComponent) entityB.getComponent(ComponentNames.HEALTH);
                        RenderComponent rc = (RenderComponent) entityB.getComponent(ComponentNames.RENDER);
                        hc.takeDamage(Constants.ENEMY_PROJ_DAMAGE);
                        rc.setFlashing(true);
                        if (hc.isDead()) {
                            toRemove.add(entityB);
                        }
                    }
                    toRemove.add(entityA);
                    continue;
                }

                if (isEnemy(entityA) && isEnemy(entityB)
                    || isEnemy(entityA) && entityB.getType() == EntityType.PLAYER
                    || isEnemy(entityB) && entityA.getType() == EntityType.PLAYER) {
                    KinematicBodyComponent kbA = (KinematicBodyComponent) entityA.getComponent(ComponentNames.KINEMATIC_BODY);
                    KinematicBodyComponent kbB = (KinematicBodyComponent) entityB.getComponent(ComponentNames.KINEMATIC_BODY);
                    kbA.setVelocity(kbA.getVelocity().scale(-1));
                    kbB.setVelocity(kbB.getVelocity().scale(-1));
                    applyVelocity(entityA);
                    applyVelocity(entityB);
                    continue;
                }

                Vec2D xAxis = new Vec2D(1, 0);
                Vec2D yAxis = new Vec2D(0, 1);

                KinematicBodyComponent kBodyA = (KinematicBodyComponent)
                        entityA.getComponent(ComponentNames.KINEMATIC_BODY);

                if (kBodyA == null) {
                    continue;
                }

                boolean xCollision = isCollidingOnAxis(entityA, entityB, xAxis);
                boolean yCollision = isCollidingOnAxis(entityA, entityB, yAxis);

                // only slide if entity is player / mob

                revertVelocity(entityA);

                if (xCollision && !yCollision) {
//                    System.out.println("Collision on x axis");
                    kBodyA.setVelocity(kBodyA.getVelocity().mul(yAxis));
                } else if (!xCollision && yCollision) {
//                    System.out.println("Collision on y axis");
                    kBodyA.setVelocity(kBodyA.getVelocity().mul(xAxis));
                } else {
                    kBodyA.setVelocity(kBodyA.getVelocity().mul(Vec2D.ZERO));
                }

                applyVelocity(entityA);
            }
        }

        for (Entity end : toRemove) {
            entityList.remove(end.getId());
        }

    }

    /**
     * Simple method that checks if the entity is an enemy.
     * @param entity entity to be checked
     * @return true if enemy else false
     */
    private boolean isEnemy(Entity entity) {
        return (entity.getType() == EntityType.REGULAR_ENEMY
                || entity.getType() == EntityType.MEDIUM_ENEMY
                || entity.getType() == EntityType.HEAVY_ENEMY);
    }

    /**
     * Method used to enable sliding.
     * @param entity entity to be checked
     * @param other other entity
     * @param dir collision axis
     * @return true if collision on axis else false
     */
    private boolean isCollidingOnAxis(Entity entity, Entity other, Vec2D dir) {
        // entity is implementing kinematic body component
        revertVelocity(entity);

        KinematicBodyComponent entityComponent = (KinematicBodyComponent)
                entity.getComponent(ComponentNames.KINEMATIC_BODY);

        Vec2D prevPos = entityComponent.getPos();
        entityComponent.setPos(entityComponent.getPos().add(entityComponent.getVelocity().mul(dir)));
        boolean result = checkCollision(entity, other);
        entityComponent.setPos(prevPos);

        applyVelocity(entity);
        return result;
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

    /**
     * Simple check for collision
     * @param entity entity to be checked
     * @param other other entity
     * @return true if collided else false
     */
    private boolean checkCollision(Entity entity, Entity other) {
        Rectangle entityRect = getRigidBodyRectangle(entity);
        Rectangle otherRect = getRigidBodyRectangle(other);

        return entityRect.intersects(otherRect);
    }

    private Rectangle getRigidBodyRectangle(Entity entity) {
        RigidBodyComponent entityComponent = (RigidBodyComponent) entity.getComponent(ComponentNames.KINEMATIC_BODY);
        if (entityComponent == null) {
            entityComponent = (RigidBodyComponent) entity.getComponent(ComponentNames.RIGID_BODY);
        }

        ColliderComponent collisionComponent = (ColliderComponent) entity.getComponent(ComponentNames.COLLIDER);
        if (collisionComponent == null) {
            System.err.println("[ERROR]: A physics entity does not have implement the collider component!");
            return new Rectangle();
        }

        int sizeX = (int)entityComponent.getSize().getPosX();
        int sizeY = (int)entityComponent.getSize().getPosY();
        int posX  = (int)entityComponent.getPos().getPosX();
        int posY  = (int)entityComponent.getPos().getPosY();

        return new Rectangle((int) (posX + collisionComponent.getOffsetFirst().getPosX() * sizeX),
                (int) (posY + collisionComponent.getOffsetFirst().getPosY() * sizeY),
                (int) (sizeX - collisionComponent.getOffsetSecond().getPosX() * sizeX
                                        - collisionComponent.getOffsetFirst().getPosX() * sizeX),
                (int) (sizeY - collisionComponent.getOffsetSecond().getPosY() * sizeY
                                        - collisionComponent.getOffsetFirst().getPosY() * sizeY));

    }
}
