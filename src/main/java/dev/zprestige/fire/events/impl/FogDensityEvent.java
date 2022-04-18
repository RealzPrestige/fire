package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;

public class FogDensityEvent extends Event {
    protected float density;

    public FogDensityEvent(final float density){
        this.density = density;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }
}