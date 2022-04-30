package dev.zprestige.fire.module.misc.autorespawn

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module

@Descriptor(description = "Respawns you when you die")
class AutoRespawn : Module() {
    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            TickListener(this)
        )
    }
}