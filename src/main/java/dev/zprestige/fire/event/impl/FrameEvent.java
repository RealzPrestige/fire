package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;

public class FrameEvent extends Event {
    protected final float partialTicks;

    public FrameEvent(final float partialTicks) {
        super(Stage.None, false);
        this.partialTicks = partialTicks;
    }

    public static class FrameEvent3D extends FrameEvent {

        public FrameEvent3D(final float partialTicks) {
            super(partialTicks);
        }
    }

    public static class FrameEvent2D extends FrameEvent {

        public FrameEvent2D(final float partialTicks) {
            super(partialTicks);
        }
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}