package dev.zprestige.fire.events.eventbus.handler.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import dev.zprestige.fire.events.eventbus.handler.DynamicHandler;
import dev.zprestige.fire.events.eventbus.handler.Handler;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

public class LambdaHandler extends Handler {

    protected static final ConcurrentHashMap<Method, DynamicHandler> handlerCache = new ConcurrentHashMap<>();
    protected DynamicHandler dynamicHandler;

    public LambdaHandler(Method listener, Object subscriber) {
        super(listener, subscriber);
        if (handlerCache.containsKey(listener)) dynamicHandler = handlerCache.get(listener);
        else {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                boolean isStatic = Modifier.isStatic(listener.getModifiers());
                MethodType targetSignature = MethodType.methodType(DynamicHandler.class);
                CallSite callSite = LambdaMetafactory.metafactory(lookup, "invoke", isStatic ? targetSignature : targetSignature.appendParameterTypes(subscriber.getClass()), MethodType.methodType(void.class, Event.class), lookup.unreflect(listener), MethodType.methodType(void.class, listener.getParameterTypes()[0]));
                MethodHandle target = callSite.getTarget();
                dynamicHandler = (DynamicHandler) (isStatic ? target.invoke() : target.invoke(subscriber));
                handlerCache.put(listener, dynamicHandler);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public void invoke(Event event) {
        dynamicHandler.invoke(event);
    }
}
