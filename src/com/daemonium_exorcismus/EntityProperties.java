package com.daemonium_exorcismus;

import com.daemonium_exorcismus.ecs.components.shooting.ShooterType;
import com.daemonium_exorcismus.engine.utils.Vec2D;

public class EntityProperties {
    public static class Player {
        public static Vec2D COLLIDER_OFFSET_FIRST;
        public static Vec2D COLLIDER_OFFSET_SECOND;
        public static Vec2D SIZE;
        public static int HEALTH;
    }

    public static class RegularEnemy {
        public static Vec2D COLLIDER_OFFSET_FIRST;
        public static Vec2D COLLIDER_OFFSET_SECOND;
        public static Vec2D SIZE;
        public static int HEALTH;
        public static ShooterType SHOOTER_TYPE;
    }


    public static class MediumEnemy {
        public static Vec2D COLLIDER_OFFSET_FIRST;
        public static Vec2D COLLIDER_OFFSET_SECOND;
        public static Vec2D SIZE;
        public static int HEALTH;
        public static ShooterType SHOOTER_TYPE;
    }


    public static class HeavyEnemy {
        public static Vec2D COLLIDER_OFFSET_FIRST;
        public static Vec2D COLLIDER_OFFSET_SECOND;
        public static Vec2D SIZE;
        public static int HEALTH;
        public static ShooterType SHOOTER_TYPE;
    }

    public static class Crate {
        public static Vec2D COLLIDER_OFFSET_FIRST;
        public static Vec2D COLLIDER_OFFSET_SECOND;
        public static Vec2D SIZE;
    }

    public static class Column {
        public static Vec2D COLLIDER_OFFSET_FIRST;
        public static Vec2D COLLIDER_OFFSET_SECOND;
        public static Vec2D SIZE;
    }

    public static class Skull {
        public static Vec2D SIZE;
    }

    public static class Projectile {
        public static Vec2D COLLIDER_OFFSET_FIRST;
        public static Vec2D COLLIDER_OFFSET_SECOND;
        public static Vec2D SIZE;
    }
}
