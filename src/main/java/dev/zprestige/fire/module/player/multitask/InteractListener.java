package dev.zprestige.fire.module.player.multitask;

import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.InteractEvent;

public class InteractListener extends EventListener<InteractEvent, MultiTask> {

    public InteractListener(final MultiTask multiTask){
        super(InteractEvent.class, multiTask);
    }

    @Override
    public void invoke(final Object object){
        final InteractEvent event = (InteractEvent) object;
        event.setCancelled();
    }
}
