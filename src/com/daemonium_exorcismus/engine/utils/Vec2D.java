package com.daemonium_exorcismus.engine.utils;

public class Vec2D {
    private double posX;
    private double posY;

    public Vec2D(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Vec2D Add(Vec2D other) {
        return new Vec2D(posX + other.posX, posY + other.posY);
    }

    public Vec2D Sub(Vec2D other) {
        return new Vec2D(posX - other.posX, posY - other.posY);
    }

    public double DotProduct(Vec2D other) {
        return posX * other.posX + posY * other.posY;
    }

    public double Norm() {
        return Math.sqrt(posX * posX + posY * posY);
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}