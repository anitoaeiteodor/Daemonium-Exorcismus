package com.daemonium_exorcismus.engine.utils;

public class Vec2D {
    private double posX;
    private double posY;

    public static final Vec2D ZERO = new Vec2D(0, 0);

    public Vec2D(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Vec2D add(Vec2D other) {
        return new Vec2D(posX + other.posX, posY + other.posY);
    }

    public Vec2D sub(Vec2D other) {
        return new Vec2D(posX - other.posX, posY - other.posY);
    }

    public double dotProduct(Vec2D other) {
        return posX * other.posX + posY * other.posY;
    }

    public double norm() {
        return Math.sqrt(posX * posX + posY * posY);
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}
