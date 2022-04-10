package dev.zprestige.fire.module.client;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;

@Descriptor(description = "Controls the max amount of rotations the client can send per tick")
public class RotationManager extends Module {
    public final Slider maxRotations = Menu.Slider("Max Rotations", 2.0f, 0.1f, 10.0f);

    public RotationManager(){
        enableModule();
    }
    @RegisterListener
    public void onTick(final TickEvent event) {
        Main.rotationManager.setMax((int) maxRotations.GetSlider());
    }

    @Override
    public String getData() {
        return maxRotations.GetSlider() + "";
    }
}
