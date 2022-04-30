package dev.zprestige.fire.module.movement.blink

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.PacketEvent.PacketSendEvent
import net.minecraft.network.play.client.*

class PacketSendListener(blink: Blink) : EventListener<PacketSendEvent, Blink>(PacketSendEvent::class.java, blink) {
    
    override fun invoke(e: Any) {
        val event = e as PacketSendEvent
        if (module.nullCheck() && !mc.isSingleplayer) {
            val packet = event.packet
            if (module.cPacketPlayer.value && packet is CPacketPlayer) {
                event.setCancelled()
                module.packets.add(packet)
            }
            if (!module.cPacketPlayer.value && !(packet is CPacketChatMessage || packet is CPacketConfirmTeleport || packet is CPacketKeepAlive || packet is CPacketTabComplete || packet is CPacketClientStatus)) {
                module.packets.add(packet)
                event.setCancelled()
            }
        }
    }
}
