package dev.zprestige.fire.module.misc.freelook

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.module.Descriptor
import dev.zprestige.fire.module.Module

@Descriptor(description = "Allows looking around without rotating")
class Freelook : Module() {
    var yaw = 0.0f
    var pitch = 0.0f

    init {
        eventListeners = arrayOf<EventListener<*, *>>(
            CameraSetupListener(this),
            TickListener(this),
            TurnListener(this)
        )
    }

    override fun onDisable() {
        mc.gameSettings.thirdPersonView = 0
        yaw = 0.0f
        pitch = 0.0f
    }
}