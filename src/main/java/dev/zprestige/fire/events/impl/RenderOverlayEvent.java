package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import dev.zprestige.fire.events.eventbus.event.IsCancellable;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@IsCancellable
public class RenderOverlayEvent extends Event {
    protected final RenderGameOverlayEvent.ElementType type;

    public RenderOverlayEvent( final RenderGameOverlayEvent.ElementType type){
        this.type = type;
    }

    public RenderGameOverlayEvent.ElementType getType() {
        return type;
    }
}
