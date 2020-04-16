package com.daemonium_exorcismus.ecs.components;

import com.daemonium_exorcismus.engine.utils.Vec2D;

public class RigidBodyComponent extends Component {
    private Vec2D pos;
    private Vec2D size;

    public RigidBodyComponent(Vec2D pos, Vec2D size) {
        this.pos = pos;
        this.size = size;
    }

    public Vec2D getPos() {
        return pos;
    }

    public Vec2D getSize() {
        return size;
    }

    public void setPos(Vec2D pos) {
        this.pos = pos;
    }

    public void setSize(Vec2D size) {
        this.size = size;
    }
}
