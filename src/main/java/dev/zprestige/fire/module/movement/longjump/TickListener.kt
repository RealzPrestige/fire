package dev.zprestige.fire.module.movement.longjump

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import kotlin.math.sqrt

class TickListener(longJump: LongJump) : EventListener<TickEvent, LongJump>(TickEvent::class.java, longJump) {

    override fun invoke(e: Any) {
        module.previousDistance =
            sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ))
        if (module.useTimer.GetSwitch()) {
            Main.tickManager.setTimer(module.timerAmount.GetSlider())
        }
    }
}
