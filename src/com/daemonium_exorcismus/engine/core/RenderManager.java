package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.*;
import com.daemonium_exorcismus.ecs.components.physics.ColliderComponent;
import com.daemonium_exorcismus.ecs.components.physics.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.physics.RigidBodyComponent;
import com.daemonium_exorcismus.ecs.systems.HUDSystem;
import com.daemonium_exorcismus.engine.graphics.AssetManager;
import com.daemonium_exorcismus.engine.graphics.Assets;
import com.daemonium_exorcismus.engine.input.KeyboardManager;
import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * Class responsible for drawing to the canvas.
 */
public class RenderManager {
    private GameWindow wnd;
    private static RenderManager instance;

    private boolean fadeToBlack;
    private int alpha;

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


    public void drawTutorial() {
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

        g.drawImage(AssetManager.assets.get(Assets.TUT_SCREEN), 0, 0,
                wnd.getWndWidth(), wnd.getWndHeight(), null);

        bs.show();
        g.dispose();
    }

    public void drawGameOver(Assets asset) {
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

        g.drawImage(AssetManager.assets.get(asset), 0, 0,
                wnd.getWndWidth(), wnd.getWndHeight(), null);

        bs.show();
        g.dispose();
    }

    public void drawMenu(Assets asset) {
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

        g.drawImage(AssetManager.assets.get(asset), 0, 0,
                wnd.getWndWidth(), wnd.getWndHeight(), null);

        bs.show();
        g.dispose();
    }

    public void drawPauseMenu(Assets asset) {
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

        g.drawImage(AssetManager.assets.get(asset), wnd.getWndWidth() / 2 - 250, wnd.getWndHeight() / 2 - 300,
                500, 600, null);

        bs.show();
        g.dispose();
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

            if(render != null && body != null && render.isVisible()) {
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

                if (KeyboardManager.keysPressed.contains(KeyEvent.VK_F1)) {
                    // debug info for collision
                    ColliderComponent collider = (ColliderComponent) entity.getComponent(ComponentNames.COLLIDER);
                    if (collider != null) {
                        g.setColor(Color.YELLOW);
                        int sizeX = (int) body.getSize().getPosX();
                        int sizeY = (int) body.getSize().getPosY();
                        g.drawRect((int) (body.getPos().getPosX() + collider.getOffsetFirst().getPosX() * sizeX),
                                (int) (body.getPos().getPosY() + collider.getOffsetFirst().getPosY() * sizeY),
                                (int) (sizeX - collider.getOffsetSecond().getPosX() * sizeX
                                        - collider.getOffsetFirst().getPosX() * sizeX),
                                (int) (sizeY - collider.getOffsetSecond().getPosY() * sizeY
                                        - collider.getOffsetFirst().getPosY() * sizeY));
                    }
                }
            }
        }

        Font font = new Font("Monospaced", Font.PLAIN, 24);
        g.setFont(font);
        g.setColor(new Color(232, 230, 24));
        g.drawString(String.format("HEALTH: %d", HUDSystem.HealthValue),
                (int)HUDSystem.HealthPosition.getPosX(), (int)HUDSystem.HealthPosition.getPosY());
        g.drawString(String.format("TIME: %d", (int)HUDSystem.ScoreValue),
                (int)HUDSystem.ScorePosition.getPosX(), (int)HUDSystem.ScorePosition.getPosY());


        if (fadeToBlack && alpha < 2550) {
            alpha += 1;
        } else if (!fadeToBlack && alpha > 0) {
            alpha -= 1;
        }

        Color color = new Color(0, 0, 0, alpha / 10);
        g.setColor(color);
        g.fillRect(0, 0, wnd.getWndWidth(), wnd.getWndHeight());

        bs.show();
        g.dispose();
    }

    public boolean isFadeToBlack() {
        return fadeToBlack;
    }

    public void setFadeToBlack(boolean fadeToBlack) {
        this.fadeToBlack = fadeToBlack;
    }
}
