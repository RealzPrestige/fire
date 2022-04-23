package dev.zprestige.fire.module.client.rotationmanager;

import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.settings.impl.Slider;
import dev.zprestige.fire.settings.impl.Switch;

@Descriptor(description = "Controls the max amount of rotations the client can send per tick")
public class RotationManager extends Module {
    public final Slider maxRotations = Menu.Slider("Max Rotations", 2.0f, 0.1f, 10.0f);
    public final Switch noForceRotations = Menu.Switch("No Force Rotations", false);

    public RotationManager() {
        eventListeners = new EventListener[]{
                new PacketReceiveListener(this)
        };
        enableModule();
    }

    @Override
    public String getData() {
        return maxRotations.GetSlider() + "";
    }
}
