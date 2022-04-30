package dev.zprestige.fire.module.player.criticals

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.PacketEvent.PacketSendEvent
import dev.zprestige.fire.util.impl.EntityUtil
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.CPacketUseEntity

class PacketSendListener(criticals: Criticals) : EventListener<PacketSendEvent, Criticals>(PacketSendEvent::class.java, criticals) {
    
    override fun invoke(e: Any) {
        val event = e as PacketSendEvent
        if (event.packet is CPacketUseEntity) {
            val packet = event.packet as CPacketUseEntity
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld(mc.world) is EntityLivingBase && mc.player.onGround && !mc.player.isInWater && !mc.player.isInLava && !mc.player.isInWeb) {
                if (!module.allowMoving.GetSwitch() && EntityUtil.isMoving()) {
                    return
                }
                module.sendPacket(module.offset.GetSlider())
                module.sendPacket(0.0f)
            }
        }
    }
}
