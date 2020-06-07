package com.daemonium_exorcismus.ecs.factory;

import com.daemonium_exorcismus.EntityProperties;
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
                EntityProperties.HeavyEnemy.SIZE, Vec2D.ZERO));
        enemy.addComponent(new ColliderComponent(true, EntityProperties.HeavyEnemy.COLLIDER_OFFSET_FIRST,
                EntityProperties.HeavyEnemy.COLLIDER_OFFSET_SECOND));
        enemy.addComponent(new HealthComponent(EntityProperties.HeavyEnemy.HEALTH));
        enemy.addComponent(new ShootingComponent(EntityProperties.HeavyEnemy.SHOOTER_TYPE,
                EntityProperties.HeavyEnemy.RELOAD_TIME));
        return enemy;
    }


    private Entity createMediumEnemy(Vec2D position, boolean isVisible) {
        Entity enemy = new Entity(EntityType.MEDIUM_ENEMY);
        enemy.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.MEDIUM_ENEMY),
                false, 2));
        enemy.addComponent(new KinematicBodyComponent(position,
               EntityProperties.MediumEnemy.SIZE,Vec2D.ZERO));
        enemy.addComponent(new ColliderComponent(true, EntityProperties.MediumEnemy.COLLIDER_OFFSET_FIRST,
                EntityProperties.MediumEnemy.COLLIDER_OFFSET_SECOND));
        enemy.addComponent(new HealthComponent(EntityProperties.MediumEnemy.HEALTH));
        enemy.addComponent(new ShootingComponent(EntityProperties.MediumEnemy.SHOOTER_TYPE,
                EntityProperties.MediumEnemy.RELOAD_TIME));
        return enemy;
    }

    private Entity createEnemyProj(Vec2D position, boolean isVisible) {
        Entity proj = new Entity(EntityType.ENEMY_PROJ);
        proj.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.ENEMY_PROJ),
                false, 2));
        proj.addComponent(new KinematicBodyComponent(position,
                EntityProperties.Projectile.SIZE, Vec2D.ZERO));
        proj.addComponent(new ColliderComponent(true, EntityProperties.Projectile.COLLIDER_OFFSET_FIRST,
                EntityProperties.Projectile.COLLIDER_OFFSET_SECOND));
        return proj;
    }

    private Entity createPlayerProj(Vec2D position, boolean isVisible) {
        Entity proj = new Entity(EntityType.PLAYER_PROJ);
        proj.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.PLAYER_PROJ),
                false, 2));
        proj.addComponent(new KinematicBodyComponent(position,
                EntityProperties.Projectile.SIZE, Vec2D.ZERO));
        proj.addComponent(new ColliderComponent(true, EntityProperties.Projectile.COLLIDER_OFFSET_FIRST,
                EntityProperties.Projectile.COLLIDER_OFFSET_SECOND));
        return proj;
    }

    private Entity createEnemy(Vec2D position, boolean isVisible) {
        Entity enemy = new Entity(EntityType.REGULAR_ENEMY);
        enemy.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.REGULAR_ENEMY),
                false, 2));
        enemy.addComponent(new KinematicBodyComponent(position,
                EntityProperties.RegularEnemy.SIZE, Vec2D.ZERO));
        enemy.addComponent(new ColliderComponent(true, EntityProperties.RegularEnemy.COLLIDER_OFFSET_FIRST,
                EntityProperties.RegularEnemy.COLLIDER_OFFSET_SECOND));
        enemy.addComponent(new HealthComponent(EntityProperties.RegularEnemy.HEALTH));
        enemy.addComponent(new ShootingComponent(EntityProperties.RegularEnemy.SHOOTER_TYPE,
                EntityProperties.RegularEnemy.RELOAD_TIME));
        return enemy;
    }

    private Entity createSkull(Vec2D position, boolean isVisible) {
        Entity skull = new Entity(EntityType.SKULL);
        skull.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.SKULL),
                false, 1));
        skull.addComponent(new RigidBodyComponent(position, EntityProperties.Skull.SIZE));
        return skull;
    }

    private Entity createColumn(Vec2D position, boolean isVisible) {
        Entity column = new Entity(EntityType.COLUMN);
        column.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.COLUMN),
                false, 2));
        column.addComponent(new RigidBodyComponent(position, EntityProperties.Column.SIZE));
        column.addComponent(new ColliderComponent(true, EntityProperties.Column.COLLIDER_OFFSET_FIRST,
                EntityProperties.Column.COLLIDER_OFFSET_SECOND));
        return column;
    }

    private Entity createCrate(Vec2D position, boolean isVisible) {
        Entity crate = new Entity(EntityType.CRATE);
        crate.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.CRATE),
                false, 2));
        crate.addComponent(new RigidBodyComponent(position, EntityProperties.Crate.SIZE));
        crate.addComponent(new ColliderComponent(true, EntityProperties.Crate.COLLIDER_OFFSET_FIRST,
                EntityProperties.Crate.COLLIDER_OFFSET_SECOND));
        return crate;
    }

    private Entity createPlayer(Vec2D position, boolean isVisible) {
        Entity player = new Entity(EntityType.PLAYER);
        player.addComponent(new RenderComponent(isVisible, AssetManager.assets.get(Assets.PLAYER),
                false, 2));
        player.addComponent(new KinematicBodyComponent(position,
                EntityProperties.Player.SIZE, Vec2D.ZERO));
        player.addComponent(new PlayerControlledComponent(true));
        player.addComponent(new ColliderComponent(true, EntityProperties.Player.COLLIDER_OFFSET_FIRST,
                EntityProperties.Player.COLLIDER_OFFSET_SECOND));
        player.addComponent(new HealthComponent(EntityProperties.Player.HEALTH));

        return player;
    }
}
