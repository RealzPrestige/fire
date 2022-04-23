package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;

public class FogDensityEvent extends Event {
    protected float density;

    public FogDensityEvent(final float density){
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