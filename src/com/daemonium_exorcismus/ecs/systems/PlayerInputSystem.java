package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.Constants;
import com.daemonium_exorcismus.EntityProperties;
import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.physics.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.RenderComponent;
import com.daemonium_exorcismus.ecs.factory.EntityFactory;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.engine.input.KeyboardManager;
import com.daemonium_exorcismus.engine.input.MouseManager;
import com.daemonium_exorcismus.engine.utils.Vec2D;
import com.daemonium_exorcismus.spawn.LevelSystem;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;

public class PlayerInputSystem extends SystemBase {

    private static final int SPEED = EntityProperties.Player.SPEED;
    private static final int PROJ_SPEED = Constants.PLAYER_PROJ_SPEED;
    private static final double RELOAD_SPEED = EntityProperties.Player.RELOAD_TIME * Game.timeFrame;
    private long lastReloadTime = 0;

    public PlayerInputSystem(long oldTime) {
        super(oldTime);
        name = SystemNames.PLAYER_INPUT;
    }

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        if(newTime - oldTime < Game.timeFrame)
            return;

        HashSet<Integer> input = KeyboardManager.keysPressed;

        Entity player = null;

        for (String key : entityList.keySet()) {
            if(entityList.get(key).hasComponent(ComponentNames.PLAYER_CNTRL)) {
                player = entityList.get(key);
                break;
            }
        }

        if (player == null) {
            return;
        }

        lastReloadTime += newTime - oldTime;
        if (lastReloadTime > RELOAD_SPEED) {
            Vec2D mousePos = MouseManager.mousePos;

            if (mousePos.getPosY() != -1) {
                lastReloadTime = 0;
                MouseManager.mousePos = new Vec2D(-1, -1);
                mousePos.add(new Vec2D(32, 32));
                KinematicBodyComponent playerBody = ((KinematicBodyComponent)
                        player.getComponent(ComponentNames.KINEMATIC_BODY));

                EntityFactory factory = new EntityFactory();

                Vec2D velocity = mousePos.sub(((KinematicBodyComponent)
                        player.getComponent(ComponentNames.KINEMATIC_BODY)).getPos().add(new Vec2D(32, 32)));
                velocity = velocity.scale(1. / velocity.norm());

                Entity spawn = factory.getEntity(EntityType.PLAYER_PROJ, playerBody.getPos().add(velocity.scale(30)),
                        true);

                KinematicBodyComponent kBody = (KinematicBodyComponent) spawn.getComponent(ComponentNames.KINEMATIC_BODY);
                kBody.setVelocity(velocity.scale(PROJ_SPEED));
                entityList.put(spawn.getId(), spawn);
 
                RenderComponent rc = (RenderComponent) player.getComponent(ComponentNames.RENDER);
                rc.setFlipped(false);
                if (mousePos.sub(playerBody.getPos()).getPosX() < 0) {
                    rc.setFlipped(true);
                }
            }
        } else {
            MouseManager.mousePos = new Vec2D(-1, -1);
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

        oldTime = newTime;
    }
}
