package dev.zprestige.fire.module.movement.antivoid;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;

@Descriptor(description = "Prevents you from falling into the void")
public class AntiVoid extends Module {
    protected boolean alreadyInVoid;

    public AntiVoid() {
        eventListeners = new EventListener[]{
                new MoveListener(this),
                new TickListener(this)
        };
    }
}
