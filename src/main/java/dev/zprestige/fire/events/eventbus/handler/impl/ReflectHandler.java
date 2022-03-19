package dev.zprestige.fire.events.eventbus.handler.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import dev.zprestige.fire.events.eventbus.handler.Handler;

import java.lang.reflect.Method;

public class ReflectHandler extends Handler {

    protected final Method listener;

    public ReflectHandler(Method listener, Object subscriber) {
        super(listener, subscriber);
        this.listener = listener;
    }

    @Override
    public void invoke(Event event) {
        try {
            listener.invoke(subscriber, event);
        } catch (Exception ignored) {
        }
    }
}
