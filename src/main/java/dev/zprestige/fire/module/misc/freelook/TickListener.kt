package dev.zprestige.fire.module.misc.freelook

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent

class TickListener(freelook: Freelook) : EventListener<TickEvent, Freelook>(TickEvent::class.java, freelook) {

    override fun invoke(e: Any) {
        mc.gameSettings.thirdPersonView = 1
    }
}
