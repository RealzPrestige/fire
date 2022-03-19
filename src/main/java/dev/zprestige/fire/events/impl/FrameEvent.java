package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;

public class FrameEvent extends Event {
    protected final float partialTicks;

    public FrameEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public static class FrameEvent3D extends FrameEvent {

        public FrameEvent3D(float partialTicks){
            super(partialTicks);
        }
    }

    public static class FrameEvent2D extends FrameEvent {

        public FrameEvent2D(float partialTicks){
            super(partialTicks);
        }
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
