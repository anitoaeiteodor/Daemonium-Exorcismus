package com.daemonium_exorcismus.ecs.systems;

import java.util.ArrayList;
import java.util.HashMap;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.RenderComponent;
import com.daemonium_exorcismus.engine.core.RenderManager;

public class RenderSystem extends SystemBase {

    public RenderSystem() {
        this.name = SystemNames.RENDER;
        oldTime = 0;
    }

    @Override
    public void updateSystem(HashMap<String, Entity> entityList, long newTime) {
        // this system updates as many times as possible per second, no need for newTime
        ArrayList<Entity> toRender = new ArrayList<>();

        for(String key : entityList.keySet()) {
            RenderComponent component = (RenderComponent) entityList.get(key).getComponent(ComponentNames.RENDER);
            if (component != null && component.getVisibilityStatus()) {
                toRender.add(entityList.get(key));
            }
        }

        RenderManager.GetInstance().draw(toRender);
    }
}
