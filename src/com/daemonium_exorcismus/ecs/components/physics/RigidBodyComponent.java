package com.daemonium_exorcismus.ecs.components.physics;

import com.daemonium_exorcismus.ecs.components.Component;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.engine.utils.Vec2D;

/**
 * Any physics entity has this component attached. It specifies the entities coordinates and size.
 */
public class RigidBodyComponent extends Component {
    private Vec2D pos;
    private Vec2D size;

    public RigidBodyComponent(Vec2D pos, Vec2D size) {
        this.pos = pos;
        this.size = size;
        this.name = ComponentNames.RIGID_BODY;
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
