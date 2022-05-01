package dev.zprestige.fire.module.client.notifications;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.ModuleToggleEvent;

public class ModuleEnableListener extends EventListener<ModuleToggleEvent.Enable, Notifications> {

    public ModuleEnableListener(final Notifications notifications) {
        super(ModuleToggleEvent.Enable.class, notifications);
    }

    @Override
    public void invoke(final Object object) {
        final ModuleToggleEvent.Enable event = (ModuleToggleEvent.Enable) object;
        if (module.modules.GetSwitch()) {
            Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RESET + " has been toggled " + ChatFormatting.GREEN + "On" + ChatFormatting.RESET + ".", 1);
        }
    }
}
