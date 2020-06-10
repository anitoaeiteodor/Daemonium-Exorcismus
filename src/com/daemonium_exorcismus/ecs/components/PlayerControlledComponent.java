package com.daemonium_exorcismus.ecs.components;

/**
 * Only the player entity can have this component attached.
 */
public class PlayerControlledComponent extends Component {

    private boolean isPlayerControlled;

    public PlayerControlledComponent(boolean isPlayerControlled) {
        this.name = ComponentNames.PLAYER_CNTRL;
        this.isPlayerControlled = isPlayerControlled;
    }

    public boolean isPlayerControlled() {
        return isPlayerControlled;
    }

    public void setPlayerControlled(boolean playerControlled) {
        isPlayerControlled = playerControlled;
    }
}
