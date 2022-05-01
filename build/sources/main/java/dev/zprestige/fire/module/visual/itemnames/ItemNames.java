package dev.zprestige.fire.module.visual.itemnames;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Slider;

@Descriptor(description = "Draws names on dropped items")
public class ItemNames extends Module {
    public final Slider scale = Menu.Slider("Scale", 1.5f, 0.1f, 5.0f);

    public ItemNames(){
        eventListeners = new EventListener[]{
                new Frame3DListener(this)
        };
    }
}
