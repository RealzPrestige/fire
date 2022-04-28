package dev.zprestige.fire.module.movement.noslow;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Switch;

@Descriptor(description = "Prevents slow down on items")
public class NoSlow extends Module {
    public final Switch items = Menu.Switch("Items", true);
    public final Switch ncpStrict = Menu.Switch("NCP Strict", false).visibility(z -> items.GetSwitch());

    public NoSlow(){
        eventListeners = new EventListener[]{
                new EntityUseItemListener(this),
                new ItemInputUpdateListener(this)
        };
    }

    public boolean slowed(){
        return mc.player.isHandActive() && !mc.player.isRiding() && !mc.player.isElytraFlying();
    }
}
