package com.daemonium_exorcismus.engine.utils;

/**
 * Helper structure used all throughout the game. It contains methods for
 * adding, subtracting, scaling and multiplying 2D vectors.
 */
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

    public Vec2D mul(Vec2D other) {
        return new Vec2D(posX * other.posX, posY * other.posY);
    }

    public Vec2D scale(double alpha) {
        return new Vec2D(posX * alpha, posY * alpha);
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
