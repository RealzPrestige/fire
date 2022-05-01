package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class ChorusEvent extends Event {
    protected final double x;
    protected final double y;
    protected final double z;

    public ChorusEvent(final double x, final double y, final double z) {
        super(Stage.None, false);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}