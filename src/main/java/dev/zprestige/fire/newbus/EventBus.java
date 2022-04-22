package dev.zprestige.fire.newbus;

import java.util.ArrayList;
import java.util.Arrays;

public class EventBus {
    protected final ArrayList<EventListener<?>> eventListeners = new ArrayList<>();

    public void invokeEvent(final Event event){
        eventListeners.stream().filter(eventListener -> eventListener.getListener().equals(event.getClass())).forEach(eventListener -> eventListener.invoke(event));
    }

    public void registerListeners(final EventListener<?>[] listeners){
        eventListeners.addAll(Arrays.asList(listeners));
    }

    public void unregisterListeners(final EventListener<?>[] listeners){
        eventListeners.removeAll(Arrays.asList(listeners));
    }
}
