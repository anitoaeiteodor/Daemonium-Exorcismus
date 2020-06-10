package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.DatabaseDriver;
import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.HealthComponent;
import com.daemonium_exorcismus.ecs.factory.EntityFactory;
import com.daemonium_exorcismus.ecs.systems.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import com.daemonium_exorcismus.engine.graphics.*;
import com.daemonium_exorcismus.engine.input.KeyboardManager;
import com.daemonium_exorcismus.engine.input.MouseManager;
import com.daemonium_exorcismus.engine.utils.Vec2D;
import com.daemonium_exorcismus.menu.GameState;
import com.daemonium_exorcismus.spawn.LevelInfo;
import com.daemonium_exorcismus.spawn.LevelSystem;

import javax.xml.crypto.Data;

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

    public static int highscore = 0;
    public static boolean stopGameSignal = false;
    public static boolean gameOverSignal = false;
    private boolean loadGameSignal = false;

    public final static int ticksPerSecond = 60;
    public final static double timeFrame      = 1000000000.0 / ticksPerSecond;

    public static GameState gameState = GameState.GS_MENU;

    public Game(String title, int width, int height)
    {
        this.title = title;
        this.width = width;
        this.height = height;

        runState = false;
        initGame();
    }

    private void initGame()
    {
        wnd = new GameWindow(title, width, height);
        wnd.buildGameWindow();
        wnd.addListeners(new KeyboardManager(), new MouseManager());
        AssetManager.InitAssets();
        RenderManager.InitRenderManager(wnd);
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

    public void launch() {
        Assets[] options = {Assets.MENU_PLAY, Assets.MENU_LOAD, Assets.MENU_TUT, Assets.MENU_EXIT};
        GameState[] states = {GameState.GS_PLAY, GameState.GS_LOAD, GameState.GS_TUT, GameState.GS_EXIT};

        int option = 0;

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (gameState) {
                case GS_MENU:
                    while (!KeyboardManager.keysPressed.contains(KeyEvent.VK_ENTER)) {

                        if (KeyboardManager.keysPressed.contains(KeyEvent.VK_W)) {
                            KeyboardManager.keysPressed.remove(KeyEvent.VK_W);
                            option--;
                            if (option < 0) {
                                option = options.length - 1;
                            }
                        }
                        if (KeyboardManager.keysPressed.contains(KeyEvent.VK_S)) {
                            KeyboardManager.keysPressed.remove(KeyEvent.VK_S);
                            option++;
                            if (option == options.length) {
                                option = 0;
                            }
                        }

                        RenderManager.GetInstance().drawMenu(options[option]);
                    }

                    switch (states[option]) {
                        case GS_PLAY:
                            gameState = GameState.GS_PLAY;
                            break;
                        case GS_LOAD:
                            gameState = GameState.GS_LOAD;
                            break;
                        case GS_TUT:
                            gameState = GameState.GS_TUT;
                            break;
                        case GS_EXIT:
                            gameState = GameState.GS_EXIT;
                            break;
                    }

                    break;
                case GS_PLAY:
                    startGame();
//                    try {
//                        gameThread.join();
//                        System.out.println("Back here");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    while (runState) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (stopGameSignal) {
                            stopGameSignal = false;

                            Entity player = null;
                            for (Entity ent : entities.values()) {
                                if (ent.getType() == EntityType.PLAYER) {
                                    player = ent;
                                }
                            }

                            DatabaseDriver.saveGame(LevelSystem.currentLevel, player);
                            stopGame();
                        }

                        if (gameOverSignal) {
                            stopGame();
                            gameState = GameState.GS_GAMEOVER;
                        }
                    }
                    break;
                case GS_LOAD:
                    if (DatabaseDriver.loadGame()) {
                        gameState = GameState.GS_PLAY;
                        loadGameSignal = true;
                    }
                    else {
                        gameState = GameState.GS_MENU;
                    }
                    break;
                case GS_TUT:
                    while (!KeyboardManager.keysPressed.contains(KeyEvent.VK_ESCAPE)) {
                        RenderManager.GetInstance().drawTutorial();
                    }
                    gameState = GameState.GS_MENU;
                    break;
                case GS_GAMEOVER:
                    System.out.println("Congrats!");
                    int gameOverOption = 0;
                    Assets[] gameOverOptions = { Assets.GAME_OVER_BACK, Assets.GAME_OVER_EXIT};
                    GameState[] gameOverStates = {GameState.GS_MENU, GameState.GS_EXIT};
                    Game.gameOverSignal = false;

                    while (!KeyboardManager.keysPressed.contains(KeyEvent.VK_ENTER)) {

                        if (KeyboardManager.keysPressed.contains(KeyEvent.VK_W)) {
                            KeyboardManager.keysPressed.remove(KeyEvent.VK_W);
                            gameOverOption--;
                            if (gameOverOption < 0) {
                                gameOverOption = gameOverOptions.length - 1;
                            }
                        }
                        if (KeyboardManager.keysPressed.contains(KeyEvent.VK_S)) {
                            KeyboardManager.keysPressed.remove(KeyEvent.VK_S);
                            gameOverOption++;
                            if (gameOverOption == gameOverOptions.length) {
                                gameOverOption = 0;
                            }
                        }

                        RenderManager.GetInstance().drawGameOver(gameOverOptions[gameOverOption]);
                    }
                    gameState = gameOverStates[option];
                    break;
                case GS_EXIT:
                    System.exit(0);
                    break;
            }
        }
    }

    public synchronized void startGame()
    {
        entities.clear();
        systems.clear();
        if(!runState)
        {
            map = new Map(entities, systems, wnd, System.nanoTime());
            EntityFactory factory = new EntityFactory();

            Entity player = factory.getEntity(EntityType.PLAYER, new Vec2D(wnd.getWndWidth() / 2.,
                    wnd.getWndHeight() / 2.), true);
            entities.put(player.getId(), player);

            SystemBase physics = new PhysicsSystem(System.nanoTime());
            SystemBase render = new RenderSystem(System.nanoTime());
            SystemBase input = new PlayerInputSystem(System.nanoTime());
            SystemBase enemyAI = new EnemySystem(System.nanoTime());
            LevelSystem levelSystem = new LevelSystem(LevelInfo.parseLevelInfo(), System.nanoTime());
            HUDSystem hud = new HUDSystem(System.nanoTime());

            systems.add(render);
            systems.add(input);
            systems.add(physics);
            systems.add(enemyAI);
            systems.add(levelSystem);
            systems.add(hud);
            map.setLoadNextArea(true);

            // load game code
            if (loadGameSignal) {
                loadGameSignal = false;
                HealthComponent hc = (HealthComponent) player.getComponent(ComponentNames.HEALTH);
                hc.setHealth(DatabaseDriver.PLAYER_HEALTH);
                LevelSystem.currentLevel = DatabaseDriver.LEVEL_NUM;
            }

            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }


    public synchronized void stopGame()
    {
        System.out.println("Stopping game...");
        map = null;
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

        map.update(newTime);
        for(SystemBase system : systems) {
            system.updateSystem(entities, newTime);
        }
    }

}

