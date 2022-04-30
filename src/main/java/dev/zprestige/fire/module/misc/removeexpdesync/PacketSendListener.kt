package dev.zprestige.fire.module.misc.removeexpdesync

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.PacketEvent.PacketSendEvent
import net.minecraft.network.play.client.CPacketPlayerTryUseItem
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock

class PacketSendListener(removeEXPDesync: RemoveEXPDesync) : EventListener<PacketSendEvent, RemoveEXPDesync>(PacketSendEvent::class.java, removeEXPDesync) {
    
    override fun invoke(e: Any) {
        val event = e as PacketSendEvent
        if (event.packet is CPacketPlayerTryUseItem) {
            val packet = event.packet as CPacketPlayerTryUseItem
            if (module.isEXP(packet.hand)) {
                module.started = true
            }
        }
        if (event.packet is CPacketPlayerTryUseItemOnBlock) {
            val packet = event.packet as CPacketPlayerTryUseItemOnBlock
            if (module.isEXP(packet.getHand())) {
                module.started = true
            }
        }
    }
}