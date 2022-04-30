package dev.zprestige.fire.module.movement.velocity

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.PacketEvent.PacketReceiveEvent
import net.minecraft.network.Packet
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketExplosion

class PacketReceiveListener(velocity: Velocity) :
    EventListener<PacketReceiveEvent, Velocity>(PacketReceiveEvent::class.java, velocity) {

    override fun invoke(e: Any) {
        val event: PacketReceiveEvent = e as PacketReceiveEvent
        val packet: Packet<*> = e.packet
        if (packet is SPacketEntityVelocity) {
            val sPacketEntityVelocity: SPacketEntityVelocity = packet
            val entityId = sPacketEntityVelocity.getEntityID()
            if (entityId == mc.player.entityId) {
                event.setCancelled()
            }
        }
        if (packet is SPacketExplosion) {
            event.setCancelled();
        }
    }
}
