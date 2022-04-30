package dev.zprestige.fire.module.player.multitask

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.InteractEvent

class InteractListener(multiTask: MultiTask) : EventListener<InteractEvent, MultiTask>(InteractEvent::class.java, multiTask) {

    override fun invoke(e: Any) {
        val event = e as InteractEvent
        event.setCancelled()
    }
}