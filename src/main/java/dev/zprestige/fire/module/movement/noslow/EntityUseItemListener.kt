package dev.zprestige.fire.module.movement.noslow

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.EntityUseItemEvent
import net.minecraft.network.play.client.CPacketHeldItemChange

class EntityUseItemListener(noSlow: NoSlow) : EventListener<EntityUseItemEvent, NoSlow>(EntityUseItemEvent::class.java, noSlow) {
    
    override fun invoke(e: Any) {
        val event = e as EntityUseItemEvent
        if (event.entity == mc.player && module.slowed() && module.ncpStrict.GetSwitch()) {
            mc.player.connection.sendPacket(CPacketHeldItemChange(mc.player.inventory.currentItem))
        }
    }
}