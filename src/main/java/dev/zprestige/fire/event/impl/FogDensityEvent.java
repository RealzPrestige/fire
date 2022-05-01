package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class FogDensityEvent extends Event {
    protected float density;

    public FogDensityEvent(final float density) {
        super(Stage.None, false);
        this.density = density;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }
}