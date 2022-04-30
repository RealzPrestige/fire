package dev.zprestige.fire.module.player.multitask

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module

@Descriptor(description = "Allows multitasking")
class MultiTask : Module() {

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            InteractListener(this)
        )
    }
}