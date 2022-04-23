package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;

public class TickEvent extends Event {

    public TickEvent() {
        super(Stage.None, false);
    }
}
