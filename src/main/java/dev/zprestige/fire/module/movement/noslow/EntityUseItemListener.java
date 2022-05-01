package dev.zprestige.fire.module.movement.noslow;

import dev.zprestige.fire.event.bus.EventListener;
import dev.zprestige.fire.event.impl.EntityUseItemEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class EntityUseItemListener extends EventListener<EntityUseItemEvent, NoSlow> {

    public EntityUseItemListener(final NoSlow noSlow){
        super(EntityUseItemEvent.class, noSlow);
    }

    @Override
    public void invoke(final Object object){
        final EntityUseItemEvent event = (EntityUseItemEvent) object;
        if (event.getEntity().equals(mc.player) && module.slowed() && module.ncpStrict.GetSwitch()){
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        }
    }
}
