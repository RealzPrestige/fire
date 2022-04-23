package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class EntityUseItemEvent extends Event {

    public EntityUseItemEvent() {
        super(Stage.None, false);
    }
}