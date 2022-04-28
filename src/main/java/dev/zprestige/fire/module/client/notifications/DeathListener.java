package dev.zprestige.fire.module.client.notifications;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.DeathEvent;

public class DeathListener extends EventListener<DeathEvent, Notifications> {

    public DeathListener(final Notifications notifications){
        super(DeathEvent.class, notifications);
    }

    @Override
    public void invoke(final Object object){
        final DeathEvent event = (DeathEvent) object;
        if (module.popMap.containsKey(event.getEntity().getName())) {
            int pops = module.popMap.get(event.getEntity().getName());
            module.popMap.remove(event.getEntity().getName());
            int line = 0;
            for (char character : event.getEntity().getName().toCharArray()) {
                line += character;
                line *= 10;
            }
            if (module.totemPops.GetSwitch()) {
                Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + event.getEntity().getName() + ChatFormatting.WHITE + " has died after popping " + Main.chatManager.prefixColor + pops + ChatFormatting.WHITE + (pops == 1 ? " totem." : " totems."), line);
            }
        }
    }


}
