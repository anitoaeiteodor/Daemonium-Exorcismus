package com.daemonium_exorcismus.ecs.components;

/**
 * It stores the entities health. It also provides methods for taking damage.
 */
public class HealthComponent extends Component {
    private int health;

    public HealthComponent(int health) {
        this.health = health;
        this.name = ComponentNames.HEALTH;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
