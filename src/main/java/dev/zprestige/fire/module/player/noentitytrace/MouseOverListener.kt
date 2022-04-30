package dev.zprestige.fire.module.player.noentitytrace

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.MouseOverEvent
import net.minecraft.init.Items

class MouseOverListener(noEntityTrace: NoEntityTrace) : EventListener<MouseOverEvent, NoEntityTrace>(MouseOverEvent::class.java,
    noEntityTrace
) {

    override fun invoke(e: Any) {
        val event = e as MouseOverEvent
        val item = mc.player.heldItemMainhand.getItem()
        if (item == Items.DIAMOND_PICKAXE && module!!.pickaxe.GetSwitch() || item == Items.GOLDEN_APPLE && module!!.gapple.GetSwitch()) {
            event.setCancelled()
        }
    }
}
