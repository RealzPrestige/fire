package dev.zprestige.fire.module.player.refill

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import dev.zprestige.fire.util.impl.EntityUtil

class TickListener(refill: Refill) : EventListener<TickEvent, Refill>(TickEvent::class.java, refill) {

    override fun invoke(e: Any) {
        if (mc.currentScreen == null && (!module!!.strict.GetSwitch() || !EntityUtil.isMoving()) && module!!.timer.getTime(
                module!!.delay.GetSlider().toLong()
            ) && swap()
        ) {
            module!!.timer.syncTime()
        }
    }

    private fun swap(): Boolean {
        var swapped = false
        for (i in 0..9) {
            if (module!!.refillSlot(i)) {
                swapped = true
            }
        }
        return swapped
    }
}