package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;

public class DensityEvent extends Event {
    protected float density;

    public DensityEvent(final float density){
        this.density = density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return density;
    }
}
