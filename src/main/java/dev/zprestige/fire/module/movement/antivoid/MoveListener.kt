package dev.zprestige.fire.module.movement.antivoid

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.MoveEvent


class MoveListener(antiVoid: AntiVoid) : EventListener<MoveEvent, AntiVoid>(MoveEvent::class.java, antiVoid) {
    
    override fun invoke(e: Any) {
        val event = e as MoveEvent
        if (!module.alreadyInVoid && mc.player.posY <= 0.0f) {
            event.setCancelled()
        }
    }
}