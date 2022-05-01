package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class RenderEntityNameEvent extends Event {

    public RenderEntityNameEvent() {
        super(Stage.None, true);
    }
}
