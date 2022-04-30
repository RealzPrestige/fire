package dev.zprestige.fire.module.player.packetmine

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.PacketEvent.PacketSendEvent
import net.minecraft.init.Items
import net.minecraft.network.play.client.CPacketHeldItemChange
import net.minecraft.network.play.client.CPacketPlayerDigging

class PacketSendListener(packetMine: PacketMine) : EventListener<PacketSendEvent, PacketMine>(
    PacketSendEvent::class.java, packetMine
) {
    override fun invoke(e: Any) {
        val event = e as PacketSendEvent
        if (!module.nullCheck()) {
            return
        }
        if (module.abortOnSwitch.GetSwitch() && module.activePos != null && module.facing != null && event.packet is CPacketHeldItemChange) {
            val packet = event.packet as CPacketHeldItemChange
            if (mc.player.inventory.getStackInSlot(packet.slotId).getItem() != Items.DIAMOND_PICKAXE) {
                module.abortWithoutEnding(module.activePos, module.facing)
                module.initiateBreaking(module.activePos, module.facing)
                module.col = floatArrayOf(module.inactiveColor.GetColor().red.toFloat(),
                    module.inactiveColor.GetColor().green.toFloat(),
                    module.inactiveColor.GetColor().blue.toFloat(),
                    module.inactiveColor.GetColor().alpha.toFloat()
                )
                module.size = 0.0f
            }
        }
        if (event.packet is CPacketPlayerDigging && module.shouldCancel) {
            val packet = event.packet as CPacketPlayerDigging
            if (packet.action == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                event.setCancelled()
            }
        }
    }
}