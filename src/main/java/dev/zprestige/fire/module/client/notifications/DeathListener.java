package dev.zprestige.fire.module.client.notifications;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zprestige.fire.Main;
import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.DeathEvent;
import net.minecraft.entity.Entity;

public class DeathListener extends EventListener<DeathEvent, Notifications> {

    public DeathListener(final Notifications notifications){
        super(DeathEvent.class, notifications);
    }

    @Override
    public void invoke(final Object object){
        final DeathEvent event = (DeathEvent) object;
        final Entity entity = event.getEntity();
        if (entity.equals(mc.player)){
            return;
        }
        if (module.popMap.containsKey(entity.getName())) {
            int pops = module.popMap.get(entity.getName());
            module.popMap.remove(entity.getName());
            int line = 0;
            for (char character : entity.getName().toCharArray()) {
                line += character;
                line *= 10;
            }
            if (module.totemPops.GetSwitch()) {
                Main.chatManager.sendRemovableMessage(ChatFormatting.WHITE + "" + ChatFormatting.BOLD + entity.getName() + ChatFormatting.WHITE + " has died after popping " + Main.chatManager.prefixColor + pops + ChatFormatting.WHITE + (pops == 1 ? " totem." : " totems."), line);
            }
        }
    }


}
