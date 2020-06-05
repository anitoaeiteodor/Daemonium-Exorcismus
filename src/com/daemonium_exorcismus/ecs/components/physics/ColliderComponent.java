package com.daemonium_exorcismus.ecs.components.physics;

import com.daemonium_exorcismus.ecs.components.Component;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.awt.geom.Rectangle2D;

public class ColliderComponent extends Component {

    private boolean isCollidable;
    private Vec2D offsetFirst, offsetSecond;

    public ColliderComponent(boolean isCollidable, Vec2D offsetFirst, Vec2D offsetSecond) {
        this.isCollidable = isCollidable;
        this.name = ComponentNames.COLLIDER;
        this.offsetFirst = offsetFirst;
        this.offsetSecond = offsetSecond;
    }

    public boolean isCollidable() {
        return isCollidable;
    }

    public void setCollidable(boolean collidable) {
        isCollidable = collidable;
    }

    public Vec2D getOffsetFirst() {
        return offsetFirst;
    }

    public Vec2D getOffsetSecond() {
        return offsetSecond;
    }
}
