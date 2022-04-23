package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class TickEvent extends Event {

    public TickEvent() {
        super(Stage.None, false);
    }
}
