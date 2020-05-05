package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.RenderComponent;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.engine.core.InputManager;
import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;

public class PlayerInputSystem extends SystemBase {

    private static final int SPEED = 10;

    public PlayerInputSystem() {
        name = SystemNames.PLAYER_INPUT;
        oldTime = 0;
    }

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        if(newTime - oldTime < Game.timeFrame)
            return;

        oldTime = newTime;

        HashSet<Integer> input = InputManager.keysPressed;

        Entity player = null;

        for (String key : entityList.keySet()) {
            if(entityList.get(key).hasComponent(ComponentNames.PLAYER_CNTRL)) {
                player = entityList.get(key);
                break;
            }
        }

        double velX = 0;
        double velY = 0;

        for (Integer key : input) {
            switch (key) {
                case KeyEvent.VK_W:
                    velY -= SPEED;
                    break;
                case KeyEvent.VK_S:
                    velY += SPEED;
                    break;
                case KeyEvent.VK_D:
                    velX += SPEED;
                    break;
                case KeyEvent.VK_A:
                    velX -= SPEED;
                    break;
            }
        }
        Vec2D velocity = new Vec2D(velX, velY);

        assert player != null;
        if (!player.hasComponent(ComponentNames.KINEMATIC_BODY)) {
            System.err.println("Player does not have kinematic body component!");
        } else {
            ((KinematicBodyComponent) player.getComponent(ComponentNames.KINEMATIC_BODY)).setVelocity(velocity);
            if (velocity.getPosX() < 0) {
                ((RenderComponent) player.getComponent(ComponentNames.RENDER)).setFlipped(true);
            } else if (velocity.getPosX() > 0) {
                ((RenderComponent) player.getComponent(ComponentNames.RENDER)).setFlipped(false);
            }
        }
    }
}
