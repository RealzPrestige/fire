package dev.zprestige.fire.module.movement.velocity

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module

@Descriptor(description = "Makes you not fly and go pow pow") class Velocity : Module() {

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            PacketReceiveListener(this)
        )
    }

}