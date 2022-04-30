package dev.zprestige.fire.module.misc.freelook

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.CameraSetupEvent

class CameraSetupListener(freelook: Freelook) : EventListener<CameraSetupEvent, Freelook>(CameraSetupEvent::class.java,
    freelook
) {

    override fun invoke(e: Any) {
        val event = e as CameraSetupEvent
        event.yaw = event.yaw + module.yaw
        event.pitch = event.pitch + module.pitch
    }
}