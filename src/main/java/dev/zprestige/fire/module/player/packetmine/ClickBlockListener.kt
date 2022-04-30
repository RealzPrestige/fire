package dev.zprestige.fire.module.player.packetmine

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.BlockInteractEvent.ClickBlock

class ClickBlockListener(packetMine: PacketMine) : EventListener<ClickBlock, PacketMine>(ClickBlock::class.java,
    packetMine
) {

    override fun invoke(e: Any) {
        if (module.nullCheck()) {
            if (mc.playerController.curBlockDamageMP > 0.0f) {
                mc.playerController.isHittingBlock = true
            }
            if (module.activePos != null && module.silentSwitch.GetCombo().equals("Clicked")) {
                module.attemptBreak(module.activePos, module.facing)
            }
        }
    }
}
