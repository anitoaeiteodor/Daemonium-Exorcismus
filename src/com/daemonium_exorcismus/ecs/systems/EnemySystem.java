package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.Constants;
import com.daemonium_exorcismus.EntityProperties;
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

    public static final int PROJ_SPEED = Constants.ENEMY_PROJ_SPEED;

    public EnemySystem(long oldTime) {
        super(oldTime);
        name = SystemNames.ENEMY;
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
            ShootingComponent enemySC = (ShootingComponent) enemy.getComponent(ComponentNames.SHOOTING);

            if (changeDir) {
                Vec2D newDir = new Vec2D(Math.cos(Math.random() * Math.PI * 2),
                        Math.sin(Math.random() * Math.PI * 2)).scale(EntityProperties.RegularEnemy.SPEED / 2.);
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
        ShootingComponent enemySC = (ShootingComponent) enemy.getComponent(ComponentNames.SHOOTING);

        EntityFactory factory = new EntityFactory();
        Vec2D playerPos = playerKB.getPos().add(playerKB.getSize().scale(0.5));
        Vec2D velocity = playerPos.sub(enemyKB.getPos().add(enemyKB.getSize().scale(0.5)));
        velocity = velocity.scale(1. / velocity.norm());


        switch (enemySC.getType()) {
            case BASIC:
                Entity proj = factory.getEntity(EntityType.ENEMY_PROJ,
                        enemyKB.getPos().add(velocity.scale(enemyKB.getSize().scale(0.5).getPosX())), true);

                KinematicBodyComponent projKb = (KinematicBodyComponent) proj.getComponent(ComponentNames.KINEMATIC_BODY);
                projKb.setVelocity(velocity.scale(PROJ_SPEED));

                entityList.put(proj.getId(), proj);
                break;
            case CONE:
                for (int i = -1; i <= 1; i++) {
                    double angle = i * Math.PI / 6;
                    double x = Math.cos(angle) * velocity.getPosX() - Math.sin(angle) * velocity.getPosY();
                    double y = Math.sin(angle) * velocity.getPosX() + Math.cos(angle) * velocity.getPosY();

                    Vec2D newVel = new Vec2D(x, y);

                    proj = factory.getEntity(EntityType.ENEMY_PROJ,
                            enemyKB.getPos().add(newVel.scale(enemyKB.getSize().scale(0.5).getPosX())), true);

                    projKb = (KinematicBodyComponent) proj.getComponent(ComponentNames.KINEMATIC_BODY);
                    projKb.setVelocity(newVel.scale(PROJ_SPEED));

                    entityList.put(proj.getId(), proj);
                }
                break;
            case RADIAL:
                for (int i = 0; i < 8; i++) {
                    double angle = i * Math.PI / 4;
                    double x = Math.cos(angle) * velocity.getPosX() - Math.sin(angle) * velocity.getPosY();
                    double y = Math.sin(angle) * velocity.getPosX() + Math.cos(angle) * velocity.getPosY();

                    Vec2D newVel = new Vec2D(x, y);

                    proj = factory.getEntity(EntityType.ENEMY_PROJ,
                            enemyKB.getPos().add(newVel.scale(enemyKB.getSize().scale(0.8).getPosX())), true);

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
