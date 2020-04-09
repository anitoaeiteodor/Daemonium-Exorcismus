package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.engine.core.GameWindow;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.daemonium_exorcismus.engine.graphics.*;

public class Game implements Runnable
{
    private GameWindow      wnd;
    private boolean         runState;
    private Thread          gameThread;
    private BufferStrategy  bs;

    private String          title;
    private int             width;
    private int             height;

    private Graphics        g;

    // temporary fields under this
    private SpriteSheet cobble;
    private SpriteSheet carpet;


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
        /// Se incarca toate elementele grafice (dale)
//        Assets.Init();

        cobble = new SpriteSheet(ImageLoader.LoadImage("D:/dev/Daemonium-Exorcismus/assets/tiles/environment/cobblestone64x64.png"));
        carpet = new SpriteSheet(ImageLoader.LoadImage("D:/dev/Daemonium-Exorcismus/assets/tiles/environment/carpet.png"));
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
                Draw();
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

    }

    private void Draw()
    {
        bs = wnd.GetCanvas().getBufferStrategy();
        if(bs == null)
        {
            try
            {
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        /// operatie de desenare
        // ...............
//        Tile.grassTile.Draw(g, 0 * Tile.TILE_WIDTH, 0);
//        Tile.soilTile.Draw(g, 1 * Tile.TILE_WIDTH, 0);
//        Tile.waterTile.Draw(g, 2 * Tile.TILE_WIDTH, 0);
//        Tile.mountainTile.Draw(g, 3 * Tile.TILE_WIDTH, 0);
//        Tile.treeTile.Draw(g, 4 * Tile.TILE_WIDTH, 0);
//
//        g.drawRect(1 * Tile.TILE_WIDTH, 1 * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

        for (int i = 0; i * 32 < wnd.GetWndWidth(); i++)
            for(int j = 0; j * 32 < wnd.GetWndHeight(); j++)
                g.drawImage(cobble.crop((i + j) % 2, (i + j) % 2), i * 32, j * 32, null);

        for (int i = 0; i * 32 < wnd.GetWndWidth(); i++)
            g.drawImage(carpet.crop(0, 0), i * 32, wnd.GetWndHeight() / 2, null);

        // end operatie de desenare
        /// Se afiseaza pe ecran
        bs.show();

        /// Elibereaza resursele de memorie aferente contextului grafic curent (zonele de memorie ocupate de
        /// elementele grafice ce au fost desenate pe canvas).
        g.dispose();
    }
}

