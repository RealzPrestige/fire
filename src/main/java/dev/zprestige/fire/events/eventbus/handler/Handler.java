package dev.zprestige.fire.events.eventbus.handler;

import dev.zprestige.fire.events.eventbus.event.Event;

import java.lang.reflect.Method;

public abstract class Handler {

    protected final Object subscriber;

    public Handler(Method listener, Object subscriber) {
        listener.setAccessible(true);
        this.subscriber = subscriber;
    }

    public abstract void invoke(Event event);

    public boolean isSubscriber(Object object) {
        return this.subscriber.equals(object);
    }
}
