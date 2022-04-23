package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;

public class EntityUseItemEvent extends Event {

    public EntityUseItemEvent() {
        super(Stage.None, false);
    }
}