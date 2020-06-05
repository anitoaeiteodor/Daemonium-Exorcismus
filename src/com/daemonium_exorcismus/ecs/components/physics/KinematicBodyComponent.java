package com.daemonium_exorcismus.ecs.components.physics;

import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.engine.utils.Vec2D;

public class KinematicBodyComponent extends RigidBodyComponent {

    private Vec2D velocity;

    public KinematicBodyComponent(Vec2D pos, Vec2D size, Vec2D velocity) {
        super(pos, size);
        this.velocity = velocity;
        this.name = ComponentNames.KINEMATIC_BODY;
    }

    public Vec2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2D velocity) {
        this.velocity = velocity;
    }
}
