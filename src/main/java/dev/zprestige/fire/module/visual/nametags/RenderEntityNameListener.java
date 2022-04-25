package dev.zprestige.fire.module.visual.nametags;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.RenderEntityNameEvent;

public class RenderEntityNameListener extends EventListener<RenderEntityNameEvent, NameTags> {

    public RenderEntityNameListener(final NameTags nameTags){
        super(RenderEntityNameEvent.class, nameTags);
    }

    @Override
    public void invoke(final Object object){
        final RenderEntityNameEvent event = (RenderEntityNameEvent) object;
        event.setCancelled();
    }
}
