package dev.zprestige.fire.module.player.freecam

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.PacketEvent.PacketSendEvent
import net.minecraft.network.play.client.CPacketInput
import net.minecraft.network.play.client.CPacketPlayer

class PacketSendListener(freecam: Freecam) : EventListener<PacketSendEvent, Freecam>(PacketSendEvent::class.java, freecam) {

    override fun invoke(e: Any) {
        val event = e as PacketSendEvent
        if (event.packet is CPacketPlayer || event.packet is CPacketInput) {
            event.setCancelled()
        }
    }
}