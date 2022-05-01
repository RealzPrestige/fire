package dev.zprestige.fire.manager.notificationmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.ModuleToggleEvent;

public class ModuleDisableListener extends EventListener<ModuleToggleEvent.Disable, Object> {

    public ModuleDisableListener() {
        super(ModuleToggleEvent.Disable.class);
    }

    @Override
    public void invoke(final Object object) {
        final ModuleToggleEvent.Disable event = (ModuleToggleEvent.Disable) object;
        Main.notificationManager.addNotifications(event.getModule().getName() + " has been toggled Off.");
    }
}
