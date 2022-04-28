package dev.zprestige.fire.module.movement.elytrafly;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;

@Descriptor(description = "Makes flying with an elytra much easier and pleasant")
public class ElytraFly extends Module {
    public final Slider speed = Menu.Slider("Speed", 1.0f, 0.1f, 5.0f);
    public final Slider glideSpeed = Menu.Slider("Glide Speed", 0.1f, 0.0f, 10.0f);

    public ElytraFly(){
        eventListeners = new EventListener[]{
                new MoveListener(this)
        };
    }
}
