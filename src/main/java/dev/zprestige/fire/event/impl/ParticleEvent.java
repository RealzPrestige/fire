package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class ParticleEvent extends Event {
    public ParticleEvent() {
        super(Stage.None, true);
    }
}