package dev.zprestige.fire.event.bus;

public class Event {
    protected final Stage stage;
    protected final boolean cancellable;
    protected boolean cancelled;

    public Event(final Stage stage, final boolean cancellable) {
        this.stage = stage;
        this.cancellable = cancellable;
        this.cancelled = false;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public void setCancelled() {
        if (!isCancellable()) {
            throw new IllegalArgumentException(this.getClass() + " noted as not cancellable.");
        }
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
