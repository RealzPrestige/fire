package dev.zprestige.fire.module.player.noentitytrace;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Switch;

@Descriptor(description = "Disallows interaction with entities when holding selected items")
public class NoEntityTrace extends Module {
    public final Switch pickaxe = Menu.Switch("Pickaxe", false);
    public final Switch gapple = Menu.Switch("Gapple", false);

    public NoEntityTrace(){
        eventListeners = new EventListener[]{
                new MouseOverListener(this)
        };
    }
}
