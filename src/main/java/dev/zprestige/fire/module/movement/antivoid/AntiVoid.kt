package dev.zprestige.fire.module.movement.antivoid

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module

@Descriptor(description = "Prevents you from falling into the void")
class AntiVoid : Module() {
    var alreadyInVoid = false

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            MoveListener(this),
            TickListener(this)
        )
    }
}
