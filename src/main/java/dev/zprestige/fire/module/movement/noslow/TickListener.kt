package dev.zprestige.fire.module.movement.noslow

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import dev.zprestige.fire.util.impl.EntityUtil

class TickListener(noSlow: NoSlow) : EventListener<TickEvent, NoSlow>(TickEvent::class.java, noSlow) {

    override fun invoke(e: Any) {
        if (module.timered && module.mode.GetCombo() != "Timer") {
            Main.tickManager.syncTimer()
        }
        if (module.webs.GetSwitch()) {
            when (module.mode.GetCombo()) {
                "Vanilla" -> if (module.webbed()) {
                    mc.player.isInWeb = false
                }
                "Ncp" -> if (module.webbed()) {
                    if (!module.sneaked) {
                        mc.player.isSneaking = true
                        module.sneaked = true
                    }
                    mc.player.isInWeb = false
                } else if (module.sneaked) {
                    mc.player.isSneaking = false
                    module.sneaked = false
                }
                "Fast" -> if (module.webbed()) {
                    val factor = module.fastFactor.GetSlider()
                    mc.player.motionX *= factor.toDouble()
                    mc.player.motionY *= factor.toDouble()
                    mc.player.motionZ *= factor.toDouble()
                }
                "Factor" -> if (module.webbed()) {
                    val speed = EntityUtil.getSpeed(module.factor.GetSlider().toDouble())
                    mc.player.motionX = speed[0].toDouble()
                    mc.player.motionY = -module.verticalFactor.GetSlider().toDouble()
                    mc.player.motionZ = speed[0].toDouble()
                }
                "Timer" -> {
                    if (module.webbed() && !mc.player.onGround) {
                        Main.tickManager.setTimer(module.timer.GetSlider())
                        module.timered = true
                    }
                    if (module.timered) {
                        if (!EntityUtil.isMoving() || !module.webbed()) {
                            Main.tickManager.syncTimer()
                            module.timered = false
                        }
                    }
                }
            }
        }
    }
}