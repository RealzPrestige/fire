package dev.zprestige.fire.module.client.notifications;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Switch;

@Descriptor(description = "Notifies you when things happen")
public class Notifications extends Module {
    public final Switch modules = Menu.Switch("Modules", true);

    public Notifications() {
        eventListeners = new EventListener[]{
                new ModuleDisableListener(this),
                new ModuleEnableListener(this)
        };
    }
}
