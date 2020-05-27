package com.daemonium_exorcismus.ecs.factory;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.*;
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
            case ENEMY:
                return createEnemy(position, isVisible);
            case PLAYER_PROJ:
                return createPlayerProj(position, isVisible);
        }
        System.out.println("[ERROR]: Entity type not found!");
        return null;
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
        Entity enemy = new Entity(EntityType.ENEMY);
        enemy.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.SMALL_ENEMY),
                false, 2));
        enemy.addComponent(new KinematicBodyComponent(position,
                new Vec2D(64, 64), new Vec2D(0, 0)));
        enemy.addComponent(new ColliderComponent(true, new Vec2D(0.25, 0.35),
                new Vec2D(0.25, 0.15)));
        enemy.addComponent(new HealthComponent(100));
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
        player.addComponent(new HealthComponent(100));

        return player;
    }
}
