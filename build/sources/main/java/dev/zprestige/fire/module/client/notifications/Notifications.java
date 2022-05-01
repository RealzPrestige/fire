package dev.zprestige.fire.module.client.notifications;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Switch;

import java.util.HashMap;

@Descriptor(description = "Notifies you when things happen")
public class Notifications extends Module {
    public final Switch modules = Menu.Switch("Modules", true);
    public final Switch totemPops = Menu.Switch("Totem Pops", true);
    public HashMap<String, Integer> popMap = new HashMap<>();
    public Notifications() {
        eventListeners = new EventListener[]{
                new DeathListener(this),
                new ModuleDisableListener(this),
                new ModuleEnableListener(this),
                new TotemPopListener(this)
        };
    }
}
