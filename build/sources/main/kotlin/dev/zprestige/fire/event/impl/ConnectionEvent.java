package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class ConnectionEvent extends Event {

    public ConnectionEvent() {
        super(Stage.None, false);
    }

    public static class Join extends ConnectionEvent {
    }

    public static class Disconnect extends ConnectionEvent {

    }
}