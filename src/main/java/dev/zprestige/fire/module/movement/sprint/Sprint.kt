package dev.zprestige.fire.module.movement.sprint

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module

@Descriptor(description = "Sprints when you arent because ur a national fat fuck who cant press a key")
class Sprint : Module() {

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            TickListener(this)
        )
    }
}