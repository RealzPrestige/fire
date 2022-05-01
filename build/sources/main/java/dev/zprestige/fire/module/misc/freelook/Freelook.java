package dev.zprestige.fire.module.misc.freelook;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;

@Descriptor(description = "Allows looking around without rotating")
public class Freelook extends Module {
    protected float yaw = 0.0f, pitch = 0.0f;

    public Freelook() {
        eventListeners = new EventListener[]{
                new CameraSetupListener(this),
                new TickListener(this),
                new TurnListener(this)
        };
    }

    @Override
    public void onDisable() {
        mc.gameSettings.thirdPersonView = 0;
        yaw = 0.0f;
        pitch = 0.0f;
    }
}
