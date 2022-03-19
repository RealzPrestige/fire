package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;

public class ConnectionEvent extends Event {

    public static class Join extends ConnectionEvent {
    }

    public static class Disconnect extends ConnectionEvent {

    }
}
