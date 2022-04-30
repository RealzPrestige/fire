package dev.zprestige.fire.module.player.freecam

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import dev.zprestige.fire.util.impl.EntityUtil

class TickListener(freecam: Freecam) : EventListener<TickEvent, Freecam>(TickEvent::class.java, freecam) {
    
    override fun invoke(e: Any) {
        mc.player.noClip = true
        val speed = EntityUtil.getSpeed(module.speed.GetSlider().toDouble())
        mc.player.motionX = speed[0].toDouble()
        mc.player.motionZ = speed[1].toDouble()
        mc.player.motionY = if (mc.gameSettings.keyBindJump.isKeyDown) module.speed.GetSlider()
            .toDouble() else if (mc.gameSettings.keyBindSneak.isKeyDown) (-module.speed.GetSlider()).toDouble() else 0.0f.toDouble()
    }
}
