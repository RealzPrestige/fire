package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class InteractEvent extends Event {
    public InteractEvent() {
        super(Stage.None, true);
    }
}