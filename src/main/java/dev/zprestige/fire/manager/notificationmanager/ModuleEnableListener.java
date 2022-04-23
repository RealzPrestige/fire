package dev.zprestige.fire.manager.notificationmanager;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.ModuleToggleEvent;

public class ModuleEnableListener extends EventListener<ModuleToggleEvent.Enable, Object> {

    public ModuleEnableListener(){
        super(ModuleToggleEvent.Enable.class);
    }

    @Override
    public void invoke(final Object object){
        final ModuleToggleEvent.Enable event = (ModuleToggleEvent.Enable) object;
        Main.notificationManager.addNotifications(event.getModule().getName() + " has been toggled Off.");
    }
}
