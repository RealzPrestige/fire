package dev.zprestige.fire.events.eventbus.handler;

import dev.zprestige.fire.events.eventbus.event.Event;

public interface DynamicHandler {

    void invoke(Event event);
}
