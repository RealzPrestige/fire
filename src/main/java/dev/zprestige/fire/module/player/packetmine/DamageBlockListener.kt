package dev.zprestige.fire.module.player.packetmine

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.BlockInteractEvent.DamageBlock
import dev.zprestige.fire.util.impl.BlockUtil
import net.minecraft.init.Blocks

class DamageBlockListener(packetMine: PacketMine) : EventListener<DamageBlock, PacketMine>(DamageBlock::class.java, packetMine) {
    
    override fun invoke(e: Any) {
        val event = e as DamageBlock
        if (module.nullCheck()) {
            if (module.activePos != null) {
                module.abort(module.activePos, module.facing)
            }
            if (BlockUtil.getState(event.pos) != Blocks.BEDROCK) {
                module.initiateBreaking(event.pos, event.facing)
                event.setCancelled()
            }
        }
    }
}