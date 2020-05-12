package com.daemonium_exorcismus.ecs.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.RenderComponent;
import com.daemonium_exorcismus.engine.core.Game;
import com.daemonium_exorcismus.engine.core.RenderManager;

public class RenderSystem extends SystemBase {

    public RenderSystem() {
        this.name = SystemNames.RENDER;
        oldTime = 0;
    }

    private boolean isFading;

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        // this system updates as many times as possible per second, no need for newTime
        ArrayList<Entity> toRender = new ArrayList<>();

        for(String key : entityList.keySet()) {
            RenderComponent component = (RenderComponent) entityList.get(key).getComponent(ComponentNames.RENDER);
            if (component != null && component.isVisible()) {
                toRender.add(entityList.get(key));
            }
        }
        toRender.sort(new SortByLayer());

        RenderManager.GetInstance().setFadeToBlack(isFading);
        RenderManager.GetInstance().draw(toRender);
    }

    public void setFading(boolean fading) {
        isFading = fading;
    }

    static class SortByLayer implements Comparator<Entity> {

        @Override
        public int compare(Entity e1, Entity e2) {
            RenderComponent c1 = (RenderComponent) e1.getComponent(ComponentNames.RENDER);
            RenderComponent c2 = (RenderComponent) e2.getComponent(ComponentNames.RENDER);

            return c1.getLayer() - c2.getLayer();
        }
    }
}
