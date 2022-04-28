package dev.zprestige.fire.module.client.notifications;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TotemPopEvent;

public class TotemPopListener extends EventListener<TotemPopEvent, Notifications> {

    public TotemPopListener(final Notifications notifications) {
        super(TotemPopEvent.class, notifications);
    }

    @Override
    public void invoke(final Object object) {
        final TotemPopEvent event = (TotemPopEvent) object;
        int pops = 1;
        if (module.popMap.containsKey(event.getEntityPlayer().getName())) {
            pops = module.popMap.get(event.getEntityPlayer().getName());
            module.popMap.put(event.getEntityPlayer().getName(), ++pops);
        } else {
            module.popMap.put(event.getEntityPlayer().getName(), pops);
        }
        if (module.popMap.containsKey(event.getEntityPlayer().getName())) {
            int line = 0;
            for (char character : event.getEntityPlayer().getName().toCharArray()) {
                line += character;
                line *= 10;
            }
            if (module.totemPops.GetSwitch()) {
                Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + event.getEntityPlayer().getName() + ChatFormatting.WHITE + " has popped " + Main.chatManager.prefixColor + pops + ChatFormatting.WHITE + (pops == 1 ? " totem." : " totems."), line);
            }
        }
    }
}
