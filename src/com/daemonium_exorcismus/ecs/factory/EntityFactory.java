package com.daemonium_exorcismus.ecs.factory;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.*;
import com.daemonium_exorcismus.ecs.components.shooting.ShootingComponent;
import com.daemonium_exorcismus.ecs.components.shooting.ShooterType;
import com.daemonium_exorcismus.ecs.components.physics.ColliderComponent;
import com.daemonium_exorcismus.ecs.components.physics.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.physics.RigidBodyComponent;
import com.daemonium_exorcismus.engine.graphics.AssetManager;
import com.daemonium_exorcismus.engine.graphics.Assets;
import com.daemonium_exorcismus.engine.utils.Vec2D;

public class EntityFactory {

    public Entity getEntity(EntityType type, Vec2D position, boolean isVisible) {
        switch (type) {
            case PLAYER:
                return createPlayer(position, isVisible);
            case CRATE:
                return createCrate(position, isVisible);
            case COLUMN:
                return createColumn(position, isVisible);
            case SKULL:
                return createSkull(position, isVisible);
            case REGULAR_ENEMY:
                return createEnemy(position, isVisible);
            case MEDIUM_ENEMY:
                return createMediumEnemy(position, isVisible);
            case HEAVY_ENEMY:
                return createBigEnemy(position, isVisible);
            case PLAYER_PROJ:
                return createPlayerProj(position, isVisible);
            case ENEMY_PROJ:
                return createEnemyProj(position, isVisible);
        }
        System.out.println("[ERROR]: Entity type not found!");
        return null;
    }

    private Entity createBigEnemy(Vec2D position, boolean isVisible) {
        Entity enemy = new Entity(EntityType.HEAVY_ENEMY);
        enemy.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.BIG_ENEMY),
                false, 2));
        enemy.addComponent(new KinematicBodyComponent(position,
                new Vec2D(64, 64), new Vec2D(0, 0)));
        enemy.addComponent(new ColliderComponent(true, new Vec2D(0.25, 0.10),
                new Vec2D(0.25, 0.15)));
        enemy.addComponent(new HealthComponent(500));
        enemy.addComponent(new ShootingComponent(ShooterType.RADIAL, 50));
        return enemy;
    }


    private Entity createMediumEnemy(Vec2D position, boolean isVisible) {
        Entity enemy = new Entity(EntityType.MEDIUM_ENEMY);
        enemy.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.MEDIUM_ENEMY),
                false, 2));
        enemy.addComponent(new KinematicBodyComponent(position,
                new Vec2D(64, 64), new Vec2D(0, 0)));
        enemy.addComponent(new ColliderComponent(true, new Vec2D(0.20, 0.20),
                new Vec2D(0.25, 0.15)));
        enemy.addComponent(new HealthComponent(300));
        enemy.addComponent(new ShootingComponent(ShooterType.CONE, 40));
        return enemy;
    }

    private Entity createEnemyProj(Vec2D position, boolean isVisible) {
        Entity proj = new Entity(EntityType.ENEMY_PROJ);
        proj.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.ENEMY_PROJ),
                false, 2));
        proj.addComponent(new KinematicBodyComponent(position,
                new Vec2D(64, 64), new Vec2D(0, 0)));
        proj.addComponent(new ColliderComponent(true, new Vec2D(0.40, 0.40),
                new Vec2D(0.40, 0.40)));
        return proj;
    }

    private Entity createPlayerProj(Vec2D position, boolean isVisible) {
        Entity proj = new Entity(EntityType.PLAYER_PROJ);
        proj.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.PLAYER_PROJ),
                false, 2));
        proj.addComponent(new KinematicBodyComponent(position,
                new Vec2D(64, 64), new Vec2D(0, 0)));
        proj.addComponent(new ColliderComponent(true, new Vec2D(0.40, 0.40),
                new Vec2D(0.40, 0.40)));
        return proj;
    }

    private Entity createEnemy(Vec2D position, boolean isVisible) {
        Entity enemy = new Entity(EntityType.REGULAR_ENEMY);
        enemy.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.REGULAR_ENEMY),
                false, 2));
        enemy.addComponent(new KinematicBodyComponent(position,
                new Vec2D(64, 64), new Vec2D(0, 0)));
        enemy.addComponent(new ColliderComponent(true, new Vec2D(0.25, 0.35),
                new Vec2D(0.25, 0.15)));
        enemy.addComponent(new HealthComponent(100));
        enemy.addComponent(new ShootingComponent(ShooterType.BASIC, 30));
        return enemy;
    }

    private Entity createSkull(Vec2D position, boolean isVisible) {
        Entity skull = new Entity(EntityType.SKULL);
        skull.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.SKULL),
                false, 1));
        skull.addComponent(new RigidBodyComponent(position, new Vec2D(64, 64)));
        return skull;
    }

    private Entity createColumn(Vec2D position, boolean isVisible) {
        Entity column = new Entity(EntityType.COLUMN);
        column.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.COLUMN),
                false, 2));
        column.addComponent(new RigidBodyComponent(position, new Vec2D(64, 128)));
        column.addComponent(new ColliderComponent(true, new Vec2D(0.10, 0.65),
                new Vec2D(0.10, 0.20)));
        return column;
    }

    private Entity createCrate(Vec2D position, boolean isVisible) {
        Entity crate = new Entity(EntityType.CRATE);
        crate.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.CRATE),
                false, 2));
        crate.addComponent(new RigidBodyComponent(position, new Vec2D(64, 128)));
        crate.addComponent(new ColliderComponent(true, new Vec2D(0, 0.50),
                new Vec2D(0, 0.20)));
        return crate;
    }

    private Entity createPlayer(Vec2D position, boolean isVisible) {
        Entity player = new Entity(EntityType.PLAYER);
        player.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.PLAYER),
                false, 2));
        player.addComponent(new KinematicBodyComponent(position, new Vec2D(64, 64),
                new Vec2D(0, 0)));
        player.addComponent(new PlayerControlledComponent(true));
        player.addComponent(new ColliderComponent(true, new Vec2D(0.18, 0.24),
                new Vec2D(0.18, 0.15)));
        player.addComponent(new HealthComponent(250));

        return player;
    }
}
