package com.daemonium_exorcismus.ecs.systems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.physics.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.RenderComponent;
import com.daemonium_exorcismus.ecs.components.physics.RigidBodyComponent;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.engine.core.RenderManager;

/**
 * System responsible for rendering the entities. It uses the RenderManager.
 */
public class RenderSystem extends SystemBase {

    public RenderSystem(long oldTime) {
        super(oldTime);
        this.name = SystemNames.RENDER;
    }

    private boolean isFading;
    private HashMap<String, Long> flashing = new HashMap<>();
    private static final int FLASH_FRAME_COUNT = 4;

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        // this system updates as many times as possible per second
//        System.out.println("[FPS]: " + 1. / ((newTime - oldTime) / 1000000000.));
//        oldTime = newTime;
        ArrayList<Entity> toRender = new ArrayList<>();

        for (String entId : flashing.keySet()) {
            Entity ent = entityList.get(entId);
            if (ent == null) {
                continue;
            }
            if (newTime - flashing.get(entId) > Game.timeFrame * FLASH_FRAME_COUNT) {
                RenderComponent rc = (RenderComponent) ent.getComponent(ComponentNames.RENDER);
                rc.setVisible(true);
            }
        }


        for(String key : entityList.keySet()) {
            RenderComponent component = (RenderComponent) entityList.get(key).getComponent(ComponentNames.RENDER);
            if (component != null && component.isVisible()) {
                toRender.add(entityList.get(key));
            }
            if(component != null && component.isFlashing()) {
//                System.out.println("Set " + entityList.get(key).getType() + " to flash");
                flashing.put(key, newTime);
                component.setFlashing(false);
                component.setVisible(false);
            }
        }

        toRender.sort(new SortByLayer());

        RenderManager.GetInstance().setFadeToBlack(isFading);
        RenderManager.GetInstance().draw(toRender);
    }

    /**
     * Used for the fading efect.
     * @param fading sets fading
     */
    public void setFading(boolean fading) {
        isFading = fading;
    }

    /**
     * Sorting criteria for the entities based on their y level.
     * Prevents entities that are in from of other appear behind.
     */
    static class SortByLayer implements Comparator<Entity> {

        @Override
        public int compare(Entity e1, Entity e2) {
            RenderComponent c1 = (RenderComponent) e1.getComponent(ComponentNames.RENDER);
            RenderComponent c2 = (RenderComponent) e2.getComponent(ComponentNames.RENDER);

            if (c1.getLayer() == c2.getLayer()) {
                RigidBodyComponent r1 = (KinematicBodyComponent) e1.getComponent(ComponentNames.KINEMATIC_BODY);
                if (r1 == null) {
                    r1 = (RigidBodyComponent) e1.getComponent(ComponentNames.RIGID_BODY);
                }

                RigidBodyComponent r2 = (KinematicBodyComponent) e2.getComponent(ComponentNames.KINEMATIC_BODY);
                if (r2 == null) {
                    r2 = (RigidBodyComponent) e2.getComponent(ComponentNames.RIGID_BODY);
                }

                return (int)(r1.getPos().getPosY() + r1.getSize().getPosY())
                        - (int)(r2.getPos().getPosY() + r2.getSize().getPosY());
            }
            return c1.getLayer() - c2.getLayer();
        }
    }
}
