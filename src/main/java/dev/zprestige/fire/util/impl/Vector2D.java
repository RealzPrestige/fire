package dev.zprestige.fire.util.impl;

import dev.zprestige.fire.util.Utils;

public class Vector2D implements Utils {
    protected final float x, y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
