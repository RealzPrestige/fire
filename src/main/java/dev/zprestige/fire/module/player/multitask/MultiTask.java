package dev.zprestige.fire.module.player.multitask;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;

@Descriptor(description = "Allows multitasking")
public class MultiTask extends Module {

    public MultiTask() {
        eventListeners = new EventListener[]{
                new InteractListener(this)
        };
    }
}