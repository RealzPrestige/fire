package dev.zprestige.fire.module.movement.antivoid

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import net.minecraft.network.play.client.CPacketPlayer

class TickListener(antiVoid: AntiVoid) : EventListener<TickEvent, AntiVoid>(TickEvent::class.java, antiVoid) {

    override fun invoke(e: Any) {
        if (mc.world != null && mc.player != null) {
            if (!module.alreadyInVoid && mc.player.posY <= 0.0f) {
                mc.player.connection.sendPacket(CPacketPlayer.Position(mc.player.posX,
                    mc.player.posY + 0.1f,
                    mc.player.posZ,
                    false
                )
                )
                module.alreadyInVoid = true
            }
            if (module.alreadyInVoid) {
                if (mc.player.posY > 0.0f) {
                    module.alreadyInVoid = false
                }
            }
        }
    }
}
