package com.daemonium_exorcismus.ecs.components.shooting;

import com.daemonium_exorcismus.ecs.components.Component;
import com.daemonium_exorcismus.ecs.components.ComponentNames;
import com.daemonium_exorcismus.engine.core.Game;

public class ShootingComponent extends Component {

    private ShooterType type;
    private int frameReloadTimer;
    private long lastTime;

    public ShootingComponent(ShooterType type, int frameReloadTimer) {
        this.type = type;
        this.frameReloadTimer = frameReloadTimer;
        this.name = ComponentNames.SHOOTING;
        this.lastTime = 0;
    }

    public boolean canShoot(long newTime) {
        return newTime - lastTime > Game.timeFrame * frameReloadTimer;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public ShooterType getType() {
        return type;
    }

    public int getFrameReloadTimer() {
        return frameReloadTimer;
    }
}
