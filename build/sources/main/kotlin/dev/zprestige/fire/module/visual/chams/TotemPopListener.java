package dev.zprestige.fire.module.visual.chams;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.TotemPopEvent;
import net.minecraft.entity.player.EntityPlayer;

public class TotemPopListener extends EventListener<TotemPopEvent, Chams> {

    public TotemPopListener(final Chams chams) {
        super(TotemPopEvent.class, chams);
    }

    @Override
    public void invoke(final Object object) {
        final TotemPopEvent event = (TotemPopEvent) object;
        final EntityPlayer entityPlayer = event.getEntityPlayer();
        if (module.popChams.GetSwitch() && (!entityPlayer.equals(mc.player) || module.popSelf.GetSwitch())) {
            module.addEntity(event.getEntityPlayer());
        }
    }
}
