package com.daemonium_exorcismus.ecs.components;

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
}
