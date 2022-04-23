package dev.zprestige.fire.module.player.multitask;

import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.EventListener;

@Descriptor(description = "Allows multitasking")
public class MultiTask extends Module {

    public MultiTask(){
        eventListeners = new EventListener[]{
                new InteractListener(this)
        };
    }
}