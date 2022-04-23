package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;

public class ConnectionEvent extends Event {

    public ConnectionEvent() {
        super(Stage.None, false);
    }

    public static class Join extends ConnectionEvent {
    }

    public static class Disconnect extends ConnectionEvent {

    }
}