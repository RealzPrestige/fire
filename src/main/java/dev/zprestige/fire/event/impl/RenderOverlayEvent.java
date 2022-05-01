package dev.zprestige.fire.event.impl;

import dev.zprestige.fire.event.bus.Event;
import dev.zprestige.fire.event.bus.Stage;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class RenderOverlayEvent extends Event {
    protected final RenderGameOverlayEvent.ElementType type;

    public RenderOverlayEvent(final RenderGameOverlayEvent.ElementType type) {
        super(Stage.None, true);
        this.type = type;
    }

    public RenderGameOverlayEvent.ElementType getType() {
        return type;
    }
}