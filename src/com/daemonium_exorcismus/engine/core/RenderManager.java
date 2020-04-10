package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.Component;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.RenderComponent;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class RenderManager {
    private GameWindow wnd;
    private static RenderManager instance;

    private RenderManager(GameWindow window) {
        this.wnd = window;
    }

    public static RenderManager GetInstance() {
        return instance;
    }

    public static void InitRenderManager(GameWindow window) {
        if (instance != null) {
            System.out.println("Render manager already initialized.");
        }
        else {
            instance = new RenderManager(window);
        }
    }

    public void Draw(ArrayList<Entity> entities)
    {
        BufferStrategy bs = wnd.GetCanvas().getBufferStrategy();

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

        assert bs != null;
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        for (Entity entity : entities) {
            RenderComponent render = (RenderComponent) entity.GetComponents().get(ComponentNames.RENDER);
            g.drawImage(render.GetSpriteSheet().crop(0, 0), 100, 100, null);
        }

//        for (int i = 0; i * 32 < wnd.GetWndWidth(); i++)
//            for(int j = 0; j * 32 < wnd.GetWndHeight(); j++)
//                g.drawImage(cobble.crop((i + j) % 2, (i + j) % 2), i * 32, j * 32, null);
//
//        for (int i = 0; i * 32 < wnd.GetWndWidth(); i++)
//            g.drawImage(carpet.crop(0, 0), i * 32, wnd.GetWndHeight() / 2, null);

        bs.show();
        g.dispose();
    }
}
