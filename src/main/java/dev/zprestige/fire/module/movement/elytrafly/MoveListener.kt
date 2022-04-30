package dev.zprestige.fire.module.movement.elytrafly

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.MoveEvent
import dev.zprestige.fire.util.impl.EntityUtil

class MoveListener(elytraFly: ElytraFly) : EventListener<MoveEvent, ElytraFly>(MoveEvent::class.java, elytraFly) {

    override fun invoke(e: Any) {
        if (!mc.player.isElytraFlying || mc.gameSettings.keyBindJump.isKeyDown) {
            return
        }
        mc.gameSettings.keyBindSneak.pressed = false
        val event = e as MoveEvent
        val glideSpeed = -module.glideSpeed.GetSlider() / 100.0f
        val speed = EntityUtil.getSpeed(module.speed.GetSlider().toDouble())
        val x = speed[0]
        val z = speed[1]
        event.setMotion(x.toDouble(), glideSpeed.toDouble(), z.toDouble())
        mc.player.motionX = x.toDouble()
        mc.player.motionY = glideSpeed.toDouble()
        mc.player.motionZ = z.toDouble()
    }
}