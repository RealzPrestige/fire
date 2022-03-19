package dev.zprestige.fire.events.eventbus.event;


public abstract class Event {
    protected boolean cancelled;
    public boolean isCancelled() {
        return this.cancelled;
    }
    public void setCancelled(boolean cancelled) {
        if (getClass().isAnnotationPresent(IsCancellable.class)) {
            this.cancelled = cancelled;
        }
    }
}
