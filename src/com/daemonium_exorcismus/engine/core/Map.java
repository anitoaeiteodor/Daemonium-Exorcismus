package com.daemonium_exorcismus.engine.core;

import com.daemonium_exorcismus.ecs.Entity;
import com.daemonium_exorcismus.ecs.EntityType;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.ecs.components.KinematicBodyComponent;
import com.daemonium_exorcismus.ecs.components.RigidBodyComponent;
import com.daemonium_exorcismus.ecs.systems.RenderSystem;
import com.daemonium_exorcismus.ecs.systems.SystemBase;
import com.daemonium_exorcismus.ecs.systems.SystemNames;
import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    private HashMap<String, Entity> entities;
    private ArrayList<SystemBase> systems;
    private GameWindow wnd;

    private boolean loadNextArea = false;
    private long oldTime;

    public Map(HashMap<String, Entity> entities, ArrayList<SystemBase> systems, GameWindow wnd) {
        this.entities = entities;
        this.systems = systems;
        this.wnd = wnd;
    }

    public void update(long newTime) {
        if (!loadNextArea) {
            oldTime = newTime;
            return;
        }

        RenderSystem rs = null;
        for (SystemBase sb : systems) {
            if (sb.getName().equals(SystemNames.RENDER)) {
                rs = (RenderSystem) sb;
            }
        }

        assert rs != null;

        rs.setFading(true);
        System.out.println(loadNextArea);
        // wait 60 frames
        if (newTime - oldTime < Game.timeFrame * 60) {
            System.out.println("WAITING");
            return;
        }

        loadNextArea = false;
        HashMap<String, Entity> remaining = new HashMap<>();
        for (Entity ent : entities.values()) {
            if (ent.getType() != EntityType.ENEMY) {
                remaining.put(ent.getId(), ent);
            }
            if (ent.getType() == EntityType.PLAYER) {
                KinematicBodyComponent kb = (KinematicBodyComponent) ent.getComponent(ComponentNames.KINEMATIC_BODY);
                kb.setPos(new Vec2D(wnd.getWndWidth() / 2., wnd.getWndHeight() / 2.));
            }
        }

        entities.clear();
        entities.putAll(remaining);

        rs.setFading(false);
    }


    public void setLoadNextArea(boolean loadNextArea) {
        this.loadNextArea = loadNextArea;
    }

    public boolean isLoadNextArea() {
        return loadNextArea;
    }
}
