package dev.zprestige.fire.module.movement.speed

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent
import kotlin.math.max
import kotlin.math.sqrt

class TickListener(speed: Speed) : EventListener<TickEvent, Speed>(TickEvent::class.java, speed) {
    
    override fun invoke(e: Any) {
        module.previousDistance = sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ))
        if (module.useTimer.GetSwitch()) {
            Main.tickManager.setTimer(module.timerAmount.GetSlider())
        }
        val health = mc.player.health + mc.player.absorptionAmount
        var damage = 0.0f
        for (i in module.damageMap){
            val v: Float = i.value / ((System.currentTimeMillis() - i.key) / module.boostAmplifier.GetSlider())
            if (v < 0.1) {
                module.damageMap.remove(i.key)
            }
            damage += v
        }

        val dmg: Float = module.lastHealth.minus(health)
        if (dmg > 0.0f) {
            module.damageMap[System.currentTimeMillis()] = dmg
        }
        module.lastHealth = mc.player.health + mc.player.absorptionAmount
        if (module.strict.GetSwitch()) {
            module.strafeFactor.value = 0.9f
        } else {
            if (module.velocityBoost.GetSwitch()) {
                module.strafeFactor.value = max(1.0f, damage)
            }
        }
    }
}
