package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.RenderComponent;
import com.daemonium_exorcismus.ecs.systems.ISystem;
import com.daemonium_exorcismus.ecs.systems.RenderSystem;
import com.daemonium_exorcismus.engine.core.GameWindow;

import java.util.ArrayList;
import java.util.HashMap;

import com.daemonium_exorcismus.engine.graphics.*;

public class Game implements Runnable
{
    private GameWindow      wnd;
    private boolean         runState;
    private Thread          gameThread;

    private String          title;
    private int             width;
    private int             height;

    private HashMap<String, Entity> entities = new HashMap<>();
    private ArrayList<ISystem> systems = new ArrayList<>();



//    private Tile tile; /*!< variabila membra temporara. Este folosita in aceasta etapa doar pentru a desena ceva pe ecran.*/

    public Game(String title, int width, int height)
    {
        this.title = title;
        this.width = width;
        this.height = height;

        runState = false;
    }

    private void InitGame()
    {
        wnd = new GameWindow(title, width, height);
        wnd.BuildGameWindow();
        AssetManager.InitAssets();
        RenderManager.InitRenderManager(wnd);

        // temporary code here
        Entity player = new Entity();
        player.AddComponent(new RenderComponent(true, AssetManager.assets.get(Assets.COBBLE)));
        entities.put(player.getId(), player);

        ISystem render = new RenderSystem();
        systems.add(render);
    }

    public void run()
    {
        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long curentTime;                    /*!< Retine timpul curent de executie.*/

        final int framesPerSecond   = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        final double timeFrame      = 1000000000.0 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/

        while (runState)
        {
            curentTime = System.nanoTime();

            if((curentTime - oldTime) > timeFrame)
            {
                Update();
                oldTime = curentTime;
            }
        }

    }

    public synchronized void StartGame()
    {
        if(!runState)
        {
            InitGame();
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }


    public synchronized void StopGame()
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

    private void Update()
    {
        for(ISystem system : systems) {
            system.UpdateSystem(entities);
        }
    }

}

