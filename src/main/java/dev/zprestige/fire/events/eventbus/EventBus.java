package dev.zprestige.fire.events.eventbus;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.eventbus.event.Event;
import dev.zprestige.fire.events.eventbus.handler.Handler;
import dev.zprestige.fire.events.eventbus.handler.impl.LambdaHandler;
import dev.zprestige.fire.events.eventbus.handler.impl.ReflectHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    protected final Set<Object> subscribers = Collections.synchronizedSet(new HashSet<>());
    protected final Map<Class<?>, List<Handler>> handlerMap = new ConcurrentHashMap<>();
    protected final Class<? extends Handler> handlerType = LambdaHandler.class;

    public void register(Object subscriber) {
        if (subscriber == null || subscribers.contains(subscriber))
            return;
        subscribers.add(subscriber);
        addHandlers(subscriber);
    }

    public boolean post(Event event) {
        if (event == null) {
            return false;
        }
        List<Handler> handlers = handlerMap.get(event.getClass());
        if (handlers == null) {
            return false;
        }
        handlers.stream().filter(handler -> !event.isCancelled()).forEach(handler -> handler.invoke(event));
        return event.isCancelled();
    }

    public void unregister(Object subscriber) {
        if (subscriber == null || !subscribers.contains(subscriber))
            return;
        subscribers.remove(subscriber);
        handlerMap.values().forEach(handlers -> handlers.removeIf(handler -> handler.isSubscriber(subscriber)));
        handlerMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    protected void addHandlers(Object subscriber) {
        boolean isClass = subscriber instanceof Class;
        Arrays.stream((isClass ? (Class<?>) subscriber : subscriber.getClass()).getMethods())
                .filter(method -> method.isAnnotationPresent(RegisterListener.class))
                .filter(method -> isClass == Modifier.isStatic(method.getModifiers()))
                .forEach(method -> {
                    Class<?>[] parameters = method.getParameterTypes();
                    if (method.getReturnType() != void.class) {
                        return;
                    }
                    if (parameters.length != 1 || !Event.class.isAssignableFrom(parameters[0])) {
                        return;
                    }
                    List<Handler> handlers = handlerMap.computeIfAbsent(parameters[0], v -> new CopyOnWriteArrayList<>());
                    handlers.add(createHandler(method, subscriber));
                });
    }

    protected Handler createHandler(Method method, Object object) {
        try {
            return handlerType.getDeclaredConstructor(Method.class, Object.class).newInstance(method, object);
        } catch (Exception ignored) {
            return new ReflectHandler(method, object);
        }
    }
}
