package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;

public class InteractEvent extends Event {
    public InteractEvent() {
        super(Stage.None, true);
    }
}