package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.Constants;
import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.*;
import com.daemonium_exorcismus.ecs.components.physics.ColliderComponent;
import com.daemonium_exorcismus.ecs.components.physics.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.physics.RigidBodyComponent;
import com.daemonium_exorcismus.ecs.factory.EntityFactory;
import com.daemonium_exorcismus.ecs.systems.RenderSystem;
import com.daemonium_exorcismus.ecs.systems.SystemBase;
import com.daemonium_exorcismus.ecs.systems.SystemNames;
import com.daemonium_exorcismus.engine.graphics.AssetManager;
import com.daemonium_exorcismus.engine.graphics.Assets;
import com.daemonium_exorcismus.engine.utils.Vec2D;

import com.daemonium_exorcismus.spawn.LevelSystem;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Special system that keeps track of both the entities present as well as the systems. It does not extend the
 * SystemBase class but it provides the same functionality.
 */
public class Map {
    private HashMap<String, Entity> entities;
    private ArrayList<SystemBase> systems;
    private GameWindow wnd;

    private boolean loadNextArea = true;
    private long oldTime;

    private double UPPER_BOUND;
    private double LOWER_BOUND;
    private double LEFT_BOUND;
    private double RIGHT_BOUND;

    private final static int SKULL_SIZE_X = 64;
    private final static int SKULL_SIZE_Y = 64;

    private final static int CRATE_SIZE_X = 64;
    private final static int CRATE_SIZE_Y = 128;

    private final static int COLUMN_SIZE_X = 64;
    private final static int COLUMN_SIZE_Y = 192;

    private Vec2D[] spawnPoints = new Vec2D[4];
    private ArrayList<Rectangle> rects = new ArrayList<>();


    public Map(HashMap<String, Entity> entities, ArrayList<SystemBase> systems, GameWindow wnd, long oldTime) {
        this.entities = entities;
        this.systems = systems;
        this.wnd = wnd;
        this.oldTime = 0;

        UPPER_BOUND = (int)(wnd.getWndHeight() * 0.25);
        LOWER_BOUND = (int)(wnd.getWndHeight() * 0.90);
        LEFT_BOUND = (int)(wnd.getWndWidth() * 0.20);
        RIGHT_BOUND = (int)(wnd.getWndWidth() * 0.80);

        Entity upperBound = new Entity(EntityType.WALL);
        upperBound.addComponent(new RigidBodyComponent(new Vec2D(0, 0),
                new Vec2D(wnd.getWndWidth(), 0.15 * wnd.getWndHeight())));
        upperBound.addComponent(new ColliderComponent(true, new Vec2D(0, 0),
                new Vec2D(0, 0)));
        upperBound.addComponent(new RenderComponent(true, null, false, 1));

        Entity lowerBound = new Entity(EntityType.WALL);
        lowerBound.addComponent(new RigidBodyComponent(new Vec2D(0, wnd.getWndHeight()),
                new Vec2D(wnd.getWndWidth(), 0.05 * wnd.getWndHeight())));
        lowerBound.addComponent(new ColliderComponent(true, new Vec2D(0, 0),
                new Vec2D(0, 0)));
        lowerBound.addComponent(new RenderComponent(true, null, false, 1));

        Entity leftBound = new Entity(EntityType.WALL);
        leftBound.addComponent(new RigidBodyComponent(new Vec2D(0, 0),
                new Vec2D(wnd.getWndWidth() * 0.01, wnd.getWndHeight())));
        leftBound.addComponent(new ColliderComponent(true, Vec2D.ZERO, Vec2D.ZERO));
        leftBound.addComponent(new RenderComponent(true, null, false, 1));

        Entity rightBound = new Entity(EntityType.WALL);
        rightBound.addComponent(new RigidBodyComponent(new Vec2D(wnd.getWndWidth() * 0.99, 0),
                new Vec2D(wnd.getWndWidth() * 0.01, wnd.getWndHeight())));
        rightBound.addComponent(new ColliderComponent(true, Vec2D.ZERO, Vec2D.ZERO));
        rightBound.addComponent(new RenderComponent(true, null, false, 1));

        Entity map = new Entity(EntityType.MAP);
        map.addComponent(new RenderComponent(true, AssetManager.assets.get(Assets.MAP),
                false, 0));
        map.addComponent(new RigidBodyComponent(new Vec2D(0, 0),
                new Vec2D(wnd.getWndWidth(), wnd.getWndHeight())));

        entities.put(upperBound.getId(), upperBound);
        entities.put(lowerBound.getId(), lowerBound);
        entities.put(leftBound.getId(), leftBound);
        entities.put(rightBound.getId(), rightBound);
        entities.put(map.getId(), map);

        spawnPoints[0] = Constants.SPAWN_A;
        spawnPoints[2] = Constants.SPAWN_B;
        spawnPoints[3] = Constants.SPAWN_C;
        spawnPoints[1] = Constants.SPAWN_D;

    }

    /**
     * Update method similar to the one in SystemBase. No entity parameter because it is a part of the class.
     * @param newTime used to calculate elapsed time
     */
    public void update(long newTime) {
//        System.out.println(loadNextArea + " " + LevelSystem.canLoadNextArea + " " + killedAllEnemies());
        if (!(loadNextArea && LevelSystem.canLoadNextArea && killedAllEnemies())) {
//            loadNextArea = false;
            oldTime = newTime;
            return;
        }

        RenderSystem rs = null;
        for (SystemBase sb : systems) {
            if (sb.getName().equals(SystemNames.RENDER)) {
                rs = (RenderSystem) sb;
            }
        }

        assert rs != null;

        rs.setFading(true);

        // wait 60 frames
        if (newTime - oldTime < Game.timeFrame * 60) {
//            System.out.println("WAITING");
            return;
        }

        loadNextArea = false;
        HashMap<String, Entity> remaining = new HashMap<>();
        for (Entity ent : entities.values()) {
            if (ent.getType() != EntityType.COLUMN && ent.getType() != EntityType.CRATE
                    && ent.getType() != EntityType.SKULL) {
                remaining.put(ent.getId(), ent);
            }
            if (ent.getType() == EntityType.PLAYER) {
                KinematicBodyComponent kb = (KinematicBodyComponent) ent.getComponent(ComponentNames.KINEMATIC_BODY);
                kb.setPos(new Vec2D(wnd.getWndWidth() / 2., wnd.getWndHeight() / 2.));
            }
        }

        entities.clear();
        entities.putAll(remaining);
        addRandomObjects();


        LevelSystem.nextLevelFlag = true;
//        for (Vec2D spawnPoint : spawnPoints) {
//            EntityFactory factory = new EntityFactory();
//            Entity enemy = factory.getEntity(EntityType.REGULAR_ENEMY,
//                    spawnPoint, true);
//            entities.put(enemy.getId(), enemy);
//
//        }

        rs.setFading(false);
    }

    private void addRandomObjects() {
        int MAX_RANGE = 7;
        int nSkulls = (int)(Math.random() * MAX_RANGE);
        int nCrates = (int)(Math.random() * MAX_RANGE);
        int nColumns = (int)(Math.random() * MAX_RANGE);
        EntityFactory factory = new EntityFactory();
        rects.clear();

        int bound = Constants.TEXTURE_SIZE * 4;
        for (Vec2D spawnPoint : spawnPoints) {
            rects.add(new Rectangle((int) (spawnPoint.getPosX() - bound / 2.),
                    (int)(spawnPoint.getPosY() - bound / 2.), bound, bound));
        }
        rects.add(new Rectangle((int)(wnd.getWndWidth() / 2. - bound),
                (int)(wnd.getWndHeight() / 2. - bound),
                bound * 2, bound * 2));

        int tries = 1000;

        while (nColumns != 0) {
            int posX;
            int posY;
            Rectangle rect;

            posX = (int)Math.min(RIGHT_BOUND, Math.random() * RIGHT_BOUND + LEFT_BOUND);
            posY = (int)Math.min(LOWER_BOUND, Math.random() * LOWER_BOUND + UPPER_BOUND);
            rect = new Rectangle(posX, posY, SKULL_SIZE_X,  SKULL_SIZE_Y);

            while (occupied(rect, rects) && tries > 0) {
                posX = (int)Math.min(RIGHT_BOUND, Math.random() * RIGHT_BOUND + LEFT_BOUND);
                posY = (int)Math.min(LOWER_BOUND, Math.random() * LOWER_BOUND + UPPER_BOUND);
                rect = new Rectangle(posX, posY,
                        COLUMN_SIZE_X, COLUMN_SIZE_Y);
                tries--;
            }

            if (tries != 0) {
                rects.add(rect);
            }

            Entity column = factory.getEntity(EntityType.COLUMN, new Vec2D(posX, posY), true);
            entities.put(column.getId(), column);

            nColumns--;
        }

        while (nCrates != 0) {
            double posX;
            double posY;
            Rectangle rect;

            posX = Math.min(RIGHT_BOUND, Math.random() * RIGHT_BOUND + LEFT_BOUND);
            posY = Math.min(LOWER_BOUND, Math.random() * LOWER_BOUND + UPPER_BOUND);
            rect = new Rectangle((int)posX, (int)posY, SKULL_SIZE_X,  SKULL_SIZE_Y);

            while (occupied(rect, rects) && tries > 0) {
                posX = Math.min(RIGHT_BOUND, Math.random() * RIGHT_BOUND + LEFT_BOUND);
                posY = Math.min(LOWER_BOUND, Math.random() * LOWER_BOUND + UPPER_BOUND);
                rect = new Rectangle((int)posX, (int)posY,
                        CRATE_SIZE_X, CRATE_SIZE_Y);
                tries--;
            }

            if (tries != 0) {
                rects.add(rect);
            }

            Entity crate = factory.getEntity(EntityType.CRATE, new Vec2D(posX, posY), true);
            entities.put(crate.getId(), crate);

            nCrates--;
        }

        while (nSkulls != 0) {
            double posX;
            double posY;
            Rectangle rect;

            posX = Math.min(RIGHT_BOUND, Math.random() * RIGHT_BOUND + LEFT_BOUND);
            posY = Math.min(LOWER_BOUND, Math.random() * LOWER_BOUND + UPPER_BOUND);
            rect = new Rectangle((int)posX, (int)posY, SKULL_SIZE_X,  SKULL_SIZE_Y);

            while (occupied(rect, rects) && tries > 0) {
                posX = Math.min(RIGHT_BOUND, Math.random() * RIGHT_BOUND + LEFT_BOUND);
                posY = Math.min(LOWER_BOUND, Math.random() * LOWER_BOUND + UPPER_BOUND);
                rect = new Rectangle((int)posX, (int)posY,
                        SKULL_SIZE_X, SKULL_SIZE_Y);
                tries--;
            }

            if (tries != 0) {
                rects.add(rect);
            }

            Entity skull = factory.getEntity(EntityType.SKULL, new Vec2D(posX, posY), true);
            entities.put(skull.getId(), skull);

            nSkulls--;
        }

    }

    private boolean killedAllEnemies() {
        for (Entity ent : entities.values()) {
            if (ent.getType() == EntityType.REGULAR_ENEMY
                || ent.getType() == EntityType.MEDIUM_ENEMY
                || ent.getType() == EntityType.HEAVY_ENEMY) {
                return false;
            }
        }
        return true;
    }

    private boolean occupied(Rectangle rect, ArrayList<Rectangle> arr) {
        for (Rectangle other : arr) {
            if (other.intersects(rect)) {
                return true;
            }
        }
        return false;
    }

    public void setLoadNextArea(boolean loadNextArea) {
        this.loadNextArea = loadNextArea;
    }

    public boolean isLoadNextArea() {
        return loadNextArea;
    }
}
