package dev.zprestige.fire.module.misc.autorespawn;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;

@Descriptor(description = "Respawns you when you die")
public class AutoRespawn extends Module {

    public AutoRespawn(){
        eventListeners = new EventListener[]{
                new TickListener(this)
        };
    }
}
