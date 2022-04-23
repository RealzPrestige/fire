package dev.zprestige.fire.module.client.notifications;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.newbus.EventListener;
import dev.zprestige.fire.newbus.events.ModuleToggleEvent;

public class ModuleDisableListener extends EventListener<ModuleToggleEvent.Disable, Notifications> {

    public ModuleDisableListener(final Notifications notifications){
        super(ModuleToggleEvent.Disable.class, notifications);
    }

    @Override
    public void invoke(final Object object) {
        final ModuleToggleEvent.Disable event = (ModuleToggleEvent.Disable) object;
        if (module.modules.GetSwitch()) {
            Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RESET + " has been toggled " + ChatFormatting.RED + "Off" + ChatFormatting.RESET + ".", 1);
        }
    }
}
