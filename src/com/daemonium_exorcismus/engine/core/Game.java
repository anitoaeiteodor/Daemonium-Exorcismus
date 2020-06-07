package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.Constants;
import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.factory.EntityFactory;
import com.daemonium_exorcismus.ecs.systems.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import com.daemonium_exorcismus.engine.graphics.*;
import com.daemonium_exorcismus.engine.input.KeyboardManager;
import com.daemonium_exorcismus.engine.input.MouseManager;
import com.daemonium_exorcismus.engine.utils.Vec2D;
import com.daemonium_exorcismus.spawn.Spawner;
import com.daemonium_exorcismus.spawn.Wave;

public class Game implements Runnable
{
    private GameWindow      wnd;
    private boolean         runState;
    private Thread          gameThread;

    private String          title;
    private int             width;
    private int             height;

    private HashMap<String, Entity> entities = new HashMap<>();
    private ArrayList<SystemBase> systems = new ArrayList<>();
    private Map map;

    public final static int ticksPerSecond = 60;
    public final static double timeFrame      = 1000000000.0 / ticksPerSecond;


    public Game(String title, int width, int height)
    {
        this.title = title;
        this.width = width;
        this.height = height;

        runState = false;
    }

    private Spawner spawner;

    private void initGame()
    {
        wnd = new GameWindow(title, width, height);
        wnd.buildGameWindow();

        AssetManager.InitAssets();
        RenderManager.InitRenderManager(wnd);
        wnd.addListeners(new KeyboardManager(), new MouseManager());

        // temporary code here

        map = new Map(entities, systems, wnd);

        EntityFactory factory = new EntityFactory();

        Entity player = factory.getEntity(EntityType.PLAYER, new Vec2D(200, 200), true);
        Entity enemy = factory.getEntity(EntityType.REGULAR_ENEMY, new Vec2D(400, 200), true);
        Entity enemy2 = factory.getEntity(EntityType.REGULAR_ENEMY, new Vec2D(500, 200), true);
        Entity enemy3 = factory.getEntity(EntityType.REGULAR_ENEMY, new Vec2D(500, 300), true);
        Entity skull = factory.getEntity(EntityType.SKULL, new Vec2D(200, 250), true);
        Entity crate = factory.getEntity(EntityType.CRATE, new Vec2D(400, 450), true);
        Entity column = factory.getEntity(EntityType.COLUMN, new Vec2D(200, 470), true);
        Entity mediumEnemy = factory.getEntity(EntityType.MEDIUM_ENEMY, new Vec2D(600, 500), true);
        Entity bigEnemy = factory.getEntity(EntityType.HEAVY_ENEMY, new Vec2D(600, 400), true);

        entities.put(player.getId(), player);
//        entities.put(enemy.getId(), enemy);
//        entities.put(enemy2.getId(), enemy2);
//        entities.put(enemy3.getId(), enemy3);
        entities.put(skull.getId(), skull);
        entities.put(crate.getId(), crate);
        entities.put(column.getId(), column);
//        entities.put(mediumEnemy.getId(), mediumEnemy);
//        entities.put(bigEnemy.getId(), bigEnemy);


        spawner = new Spawner(Constants.SPAWN_A);
        ArrayList<EntityType> types = new ArrayList<>();
        types.add(EntityType.REGULAR_ENEMY);
        types.add(EntityType.REGULAR_ENEMY);
        types.add(EntityType.REGULAR_ENEMY);
        types.add(EntityType.MEDIUM_ENEMY);
        types.add(EntityType.MEDIUM_ENEMY);
        types.add(EntityType.MEDIUM_ENEMY);
        Wave wave = new Wave(types, 10, 0);
        spawner.setWave(wave);

        SystemBase physics = new PhysicsSystem();
        SystemBase render = new RenderSystem();
        SystemBase input = new PlayerInputSystem();
        SystemBase enemyAI = new EnemySystem();

        systems.add(render);
        systems.add(input);
        systems.add(physics);
        systems.add(enemyAI);
    }

    public void run()
    {
        long currentTime;

        while (runState)
        {
            currentTime = System.nanoTime();
            update(currentTime);
        }

    }

    public synchronized void startGame()
    {
        if(!runState)
        {
            initGame();
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }


    public synchronized void stopGame()
    {
        if(runState)
        {
            runState = false;
            try
            {
                gameThread.join();
            }
            catch(InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void update(long newTime)
    {
        if (KeyboardManager.keysPressed.contains(KeyEvent.VK_F)) {
            map.setLoadNextArea(true);
        }
        for(SystemBase system : systems) {
            system.updateSystem(entities, newTime);
        }
        map.update(newTime);
        spawner.update(entities, newTime);
    }

}

