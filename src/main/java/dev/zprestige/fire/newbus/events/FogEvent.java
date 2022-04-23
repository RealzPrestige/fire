package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;
import net.minecraftforge.client.event.EntityViewRenderEvent;

public class FogEvent extends Event {
    protected final EntityViewRenderEvent.FogColors fogColors;

    public FogEvent(final EntityViewRenderEvent.FogColors fogColors){
        super(Stage.None, false);
        this.fogColors = fogColors;
    }

    public EntityViewRenderEvent.FogColors getFogColors() {
        return fogColors;
    }

}