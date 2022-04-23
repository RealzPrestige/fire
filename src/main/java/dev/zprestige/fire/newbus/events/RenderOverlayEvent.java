package dev.zprestige.fire.newbus.events;

import dev.zprestige.fire.newbus.Event;
import dev.zprestige.fire.newbus.Stage;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class RenderOverlayEvent extends Event {
    protected final RenderGameOverlayEvent.ElementType type;

    public RenderOverlayEvent( final RenderGameOverlayEvent.ElementType type){
        super(Stage.None, true);
        this.type = type;
    }

    public RenderGameOverlayEvent.ElementType getType() {
        return type;
    }
}