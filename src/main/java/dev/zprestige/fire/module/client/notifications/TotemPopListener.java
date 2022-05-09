package dev.zprestige.fire.module.client.notifications;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TotemPopEvent;
import net.minecraft.entity.player.EntityPlayer;

public class TotemPopListener extends EventListener<TotemPopEvent, Notifications> {

    public TotemPopListener(final Notifications notifications) {
        super(TotemPopEvent.class, notifications);
    }

    @Override
    public void invoke(final Object object) {
        final TotemPopEvent event = (TotemPopEvent) object;
        final EntityPlayer entityPlayer = event.getEntityPlayer();
        if (entityPlayer.equals(mc.player)){
            return;
        }
        int pops = 1;
        if (module.popMap.containsKey(entityPlayer.getName())) {
            pops = module.popMap.get(entityPlayer.getName());
            module.popMap.put(entityPlayer.getName(), ++pops);
        } else {
            module.popMap.put(entityPlayer.getName(), pops);
        }
        if (module.popMap.containsKey(entityPlayer.getName())) {
            int line = 0;
            for (char character : entityPlayer.getName().toCharArray()) {
                line += character;
                line *= 10;
            }
            if (module.totemPops.GetSwitch()) {
                Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + entityPlayer.getName() + ChatFormatting.WHITE + " has popped " + Main.chatManager.prefixColor + pops + ChatFormatting.WHITE + (pops == 1 ? " totem." : " totems."), line);
            }
        }
    }
}
