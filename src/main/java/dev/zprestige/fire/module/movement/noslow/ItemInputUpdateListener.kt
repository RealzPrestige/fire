package dev.zprestige.fire.module.movement.noslow

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.ItemInputUpdateEvent

class ItemInputUpdateListener(noSlow: NoSlow) : EventListener<ItemInputUpdateEvent, NoSlow>(ItemInputUpdateEvent::class.java, noSlow) {

    override fun invoke(e: Any) {
        if (module.slowed()) {
            val event = e as ItemInputUpdateEvent
            val movementInput = event.movementInput
            movementInput.moveForward /= 0.2f
            movementInput.moveStrafe /= 0.2f
        }
    }
}
