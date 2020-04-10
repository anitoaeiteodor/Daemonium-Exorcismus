package com.daemonium_exorcismus.ecs.systems;

import com.daemonium_exorcismus.ecs.Entity;

import java.util.HashMap;

public interface ISystem {
    void UpdateSystem(HashMap<String, Entity> entityList);
}
