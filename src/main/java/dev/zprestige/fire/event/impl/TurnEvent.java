package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class TurnEvent extends Event {
    private final float yaw;
    private final float pitch;

    public TurnEvent(float yaw, float pitch) {
        super(Stage.None, true);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
