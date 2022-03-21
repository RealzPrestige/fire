package dev.zprestige.fire.module.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.ModuleToggleEvent;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.impl.Switch;

public class Notifications extends Module {
    public final Switch modules = Menu.Switch("Modules", true);

    @RegisterListener
    public void onModuleEnable(ModuleToggleEvent.Enable event) {
        if (modules.GetSwitch()) {
            Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RESET + " has been toggled " + ChatFormatting.GREEN + "On" + ChatFormatting.RESET + ".", 1);
        }
    }

    @RegisterListener
    public void onModuleDisable(ModuleToggleEvent.Disable event) {
        if (modules.GetSwitch()) {
            Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RESET + " has been toggled " + ChatFormatting.RED + "Off" + ChatFormatting.RESET + ".", 1);
        }
    }
}
