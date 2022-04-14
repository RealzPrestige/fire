package dev.zprestige.fire.module.player;

import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.InteractEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;

@Descriptor(description = "Allows multitasking")
public class MultiTask extends Module {

    @RegisterListener
    public void onInteract(final InteractEvent event) {
        event.setCancelled(true);
    }
}