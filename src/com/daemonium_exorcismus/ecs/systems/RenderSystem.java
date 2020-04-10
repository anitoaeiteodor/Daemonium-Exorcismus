package com.daemonium_exorcismus.ecs.systems;

import java.util.ArrayList;
import java.util.HashMap;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.engine.core.RenderManager;

public class RenderSystem implements ISystem {
    @Override
    public void UpdateSystem(HashMap<String, Entity> entityList) {
        ArrayList<Entity> toRender = new ArrayList<>();

        for(String key : entityList.keySet()) {
            if (entityList.get(key).hasComponent(ComponentNames.RENDER)) {
                toRender.add(entityList.get(key));
            }
        }

        RenderManager.GetInstance().Draw(toRender);
    }
}
