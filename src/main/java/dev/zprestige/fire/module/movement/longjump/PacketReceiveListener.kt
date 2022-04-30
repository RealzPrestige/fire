package dev.zprestige.fire.module.movement.longjump

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.PacketEvent.PacketReceiveEvent
import net.minecraft.network.play.server.SPacketPlayerPosLook

class PacketReceiveListener(longJump: LongJump) : EventListener<PacketReceiveEvent, LongJump>(PacketReceiveEvent::class.java, longJump) {
    
    override fun invoke(e: Any) {
        val event = e as PacketReceiveEvent
        if (event.packet is SPacketPlayerPosLook) {
            val packet = event.packet as SPacketPlayerPosLook
            if (module.disableOnLag.GetSwitch()) {
                module.disableModule()
            }
            module.motionSpeed = 0.0
            mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ())
        }
    }
}
