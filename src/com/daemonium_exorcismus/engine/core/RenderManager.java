package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.*;
import com.daemonium_exorcismus.engine.utils.Vec2D;

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

    public void draw(ArrayList<Entity> entities)
    {
        BufferStrategy bs = wnd.getCanvas().getBufferStrategy();

        if(bs == null)
        {
            try
            {
                wnd.getCanvas().createBufferStrategy(3);
                return;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        assert bs != null;
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.getWndWidth(), wnd.getWndHeight());

        for (Entity entity : entities) {
            RenderComponent render = (RenderComponent) entity.getComponent(ComponentNames.RENDER);
            RigidBodyComponent body = (KinematicBodyComponent) entity.getComponent(ComponentNames.KINEMATIC_BODY);
            if (body == null) {
                body = (RigidBodyComponent) entity.getComponent(ComponentNames.RIGID_BODY);
            }

            if(render != null && body != null) {
                if (render.isFlipped()) {
                    g.drawImage(render.getSprite(),
                            (int) (body.getPos().getPosX() + body.getSize().getPosX()),
                            (int) body.getPos().getPosY(),
                            -(int) body.getSize().getPosX(),
                            (int) body.getSize().getPosY(),
                            null);
                } else {
                    g.drawImage(render.getSprite(),
                            (int) body.getPos().getPosX(),
                            (int) body.getPos().getPosY(),
                            (int) body.getSize().getPosX(),
                            (int) body.getSize().getPosY(),
                            null);
                }

                // debug info for collision
                ColliderComponent collider = (ColliderComponent) entity.getComponent(ComponentNames.COLLIDER);
                if (collider != null) {
                    g.setColor(Color.YELLOW);
                    int sizeX = (int)body.getSize().getPosX();
                    int sizeY = (int)body.getSize().getPosY();
                    g.drawRect((int)(body.getPos().getPosX() + collider.getOffsetFirst().getPosX() * sizeX),
                            (int)(body.getPos().getPosY() + collider.getOffsetFirst().getPosY() * sizeY),
                            (int)(sizeX - collider.getOffsetSecond().getPosX() * sizeX
                                    - collider.getOffsetFirst().getPosX() * sizeX),
                            (int)(sizeY - collider.getOffsetSecond().getPosY() * sizeY
                                    - collider.getOffsetFirst().getPosY() * sizeY));
                }
            }
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
