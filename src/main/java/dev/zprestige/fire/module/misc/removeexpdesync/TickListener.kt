package dev.zprestige.fire.module.misc.removeexpdesync

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent


class TickListener(removeEXPDesync: RemoveEXPDesync) : EventListener<TickEvent, RemoveEXPDesync>(TickEvent::class.java, removeEXPDesync) {
    
    override fun invoke(e: Any) {
        if (module.started) {
            if (module.index < module.attempts.GetSlider()) {
                mc.player.setPosition(mc.player.posX, mc.player.posY - module.force.GetSlider(), mc.player.posZ)
                module.index++
                Main.tickManager.setTimer(module.timer.GetSlider())
            } else {
                Main.tickManager.syncTimer()
                module.index = 0
                module.started = false
            }
        }
        module.activeEntities = mc.world.loadedEntityList
    }
}