package dev.zprestige.fire.events.impl;

import dev.zprestige.fire.events.eventbus.event.Event;
import net.minecraftforge.client.event.EntityViewRenderEvent;

public class FogEvent extends Event {
    protected final EntityViewRenderEvent.FogColors fogColors;

    public FogEvent(final EntityViewRenderEvent.FogColors fogColors){
        this.fogColors = fogColors;
    }

    public EntityViewRenderEvent.FogColors getFogColors() {
        return fogColors;
    }
}
