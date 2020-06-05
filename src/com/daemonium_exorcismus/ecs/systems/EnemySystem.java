package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.shooting.ShootingComponent;
import com.daemonium_exorcismus.ecs.components.physics.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.factory.EntityFactory;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.util.ArrayList;
import java.util.HashMap;

public class EnemySystem extends SystemBase {

    public static final int PROJ_SPEED = 5;

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
        Entity player = null;

        for (Entity ent : entityList.values()) {
            if (ent.getType() == EntityType.REGULAR_ENEMY
                    || ent.getType() == EntityType.MEDIUM_ENEMY
                    || ent.getType() == EntityType.HEAVY_ENEMY) {
                enemyList.add(ent);
            }
            if (ent.getType() == EntityType.PLAYER) {
                player = ent;
            }
        }

        for (Entity enemy : enemyList) {
            boolean changeDir = Math.random() < 0.05;
            KinematicBodyComponent enemyKB = (KinematicBodyComponent) enemy.getComponent(ComponentNames.KINEMATIC_BODY);
            ShootingComponent enemySC = (ShootingComponent) enemy.getComponent(ComponentNames.AI);

            if (changeDir) {
                Vec2D newDir = new Vec2D(Math.cos(Math.random() * Math.PI * 2),
                        Math.sin(Math.random() * Math.PI * 2)).scale(2.5);
                enemyKB.setVelocity(newDir);
            }

            if (player != null && enemySC.canShoot(newTime)) {
                shoot(enemy, player, entityList);
                enemySC.setLastTime(newTime);
            }
        }
    }

    void shoot(Entity enemy, Entity player, HashMap<String, Entity> entityList) {
        KinematicBodyComponent playerKB = (KinematicBodyComponent) player.getComponent(ComponentNames.KINEMATIC_BODY);
        KinematicBodyComponent enemyKB = (KinematicBodyComponent) enemy.getComponent(ComponentNames.KINEMATIC_BODY);
        ShootingComponent enemySC = (ShootingComponent) enemy.getComponent(ComponentNames.AI);

        EntityFactory factory = new EntityFactory();
        Vec2D playerPos = playerKB.getPos().add(new Vec2D(32, 32));
        Vec2D velocity = playerPos.sub(enemyKB.getPos().add(new Vec2D(32, 32)));
        velocity = velocity.scale(1. / velocity.norm());


        switch (enemySC.getType()) {
            case BASIC:
                Entity proj = factory.getEntity(EntityType.ENEMY_PROJ,
                        enemyKB.getPos().add(velocity.scale(30)), true);

                KinematicBodyComponent projKb = (KinematicBodyComponent) proj.getComponent(ComponentNames.KINEMATIC_BODY);
                projKb.setVelocity(velocity.scale(PROJ_SPEED));

                entityList.put(proj.getId(), proj);
                break;
            case CONE:
                Entity[] projectiles = new Entity[3];

                for (int i = -1; i <= 1; i++) {
                    double angle = i * Math.PI / 3;
                    double x = Math.cos(angle) * velocity.getPosX() - Math.sin(angle) * velocity.getPosY();
                    double y = Math.sin(angle) * velocity.getPosX() + Math.cos(angle) * velocity.getPosY();

                    Vec2D newVel = new Vec2D(x, y);

                    proj = factory.getEntity(EntityType.ENEMY_PROJ,
                            enemyKB.getPos().add(newVel.scale(30)), true);

                    projKb = (KinematicBodyComponent) proj.getComponent(ComponentNames.KINEMATIC_BODY);
                    projKb.setVelocity(newVel.scale(PROJ_SPEED));

                    projectiles[i + 1] = proj;
                }

                for (Entity pr : projectiles) {
                    entityList.put(pr.getId(), pr);
                }

                break;
            case RADIAL:
                System.out.println();
                for (int i = 0; i < 8; i++) {
                    double angle = i * Math.PI / 4;
                    double x = Math.cos(angle) * velocity.getPosX() - Math.sin(angle) * velocity.getPosY();
                    double y = Math.sin(angle) * velocity.getPosX() + Math.cos(angle) * velocity.getPosY();

                    System.out.println(x + " " + y);

                    Vec2D newVel = new Vec2D(x, y);

                    proj = factory.getEntity(EntityType.ENEMY_PROJ,
                            enemyKB.getPos().add(newVel.scale(35)), true);

                    projKb = (KinematicBodyComponent) proj.getComponent(ComponentNames.KINEMATIC_BODY);
                    projKb.setVelocity(newVel.scale(PROJ_SPEED));

                    entityList.put(proj.getId(), proj);
                }

                break;
        }

    }

    @Override
    public String getName() {
        return name;
    }
}
