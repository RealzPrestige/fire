package dev.zprestige.fire.module.client.holemanager;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;

@Descriptor(description = "Controls how far the client should load holes")
public class HoleManager extends Module {
    public final Slider range = Menu.Slider("Range", 20.0f, 0.1f, 50.0f);
    protected float lastRange = 0.0f;

    public HoleManager() {
        eventListeners = new EventListener[]{
                new Frame3DListener(this)
        };
        enableModule();
    }

    @Override
    public String getData() {
        return String.valueOf(range.GetSlider());
    }
}
