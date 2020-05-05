package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.*;
import com.daemonium_exorcismus.ecs.systems.PhysicsSystem;
import com.daemonium_exorcismus.ecs.systems.PlayerInputSystem;
import com.daemonium_exorcismus.ecs.systems.RenderSystem;
import com.daemonium_exorcismus.ecs.systems.SystemBase;

import java.util.ArrayList;
import java.util.HashMap;

import com.daemonium_exorcismus.engine.graphics.*;
import com.daemonium_exorcismus.engine.utils.Vec2D;

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

    public final static int framesPerSecond   = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
    public final static double timeFrame      = 1000000000.0 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/



//    private Tile tile; /*!< variabila membra temporara. Este folosita in aceasta etapa doar pentru a desena ceva pe ecran.*/

    public Game(String title, int width, int height)
    {
        this.title = title;
        this.width = width;
        this.height = height;

        runState = false;
    }

    private void initGame()
    {
        wnd = new GameWindow(title, width, height);
        wnd.buildGameWindow();
        AssetManager.InitAssets();
        RenderManager.InitRenderManager(wnd);
        wnd.addListener(new InputManager());

        // temporary code here
        Entity player = new Entity();
        player.addComponent(new RenderComponent(true, AssetManager.assets.get(Assets.PLAYER),
                false, 1));
        player.addComponent(new KinematicBodyComponent(new Vec2D(200, 200),
                new Vec2D(64, 64), new Vec2D(0, 0)));
        player.addComponent(new PlayerControlledComponent(true));
        player.addComponent(new ColliderComponent(true, new Vec2D(0.18, 0.18),
                new Vec2D(0.18, 0)));

        Entity map = new Entity();
        map.addComponent(new RenderComponent(true, AssetManager.assets.get(Assets.MAP),
                false, 0));
        map.addComponent(new RigidBodyComponent(new Vec2D(0, 0),
                new Vec2D(wnd.getWndWidth(), wnd.getWndHeight())));

        Entity enemy = new Entity();
        enemy.addComponent(new RenderComponent(true, AssetManager.assets.get(Assets.SMALL_ENEMY),
                false, 1));
        enemy.addComponent(new KinematicBodyComponent(new Vec2D(400, 200),
                new Vec2D(64, 64), new Vec2D(0, 0)));
        enemy.addComponent(new ColliderComponent(true, new Vec2D(0.18, 0.25),
                new Vec2D(0.18, 0)));

        entities.put(player.getId(), player);
        entities.put(map.getId(), map);
        entities.put(enemy.getId(), enemy);

        SystemBase physics = new PhysicsSystem();
        SystemBase render = new RenderSystem();
        SystemBase input = new PlayerInputSystem();

        systems.add(render);
        systems.add(input);
        systems.add(physics);
    }

    public void run()
    {
        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long curentTime;                    /*!< Retine timpul curent de executie.*/

        while (runState)
        {
            curentTime = System.nanoTime();
            update(curentTime);
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
        for(SystemBase system : systems) {
            system.updateSystem(entities, newTime);
        }
    }

}

