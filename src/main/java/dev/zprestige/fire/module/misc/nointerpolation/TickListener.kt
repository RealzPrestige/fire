package dev.zprestige.fire.module.misc.nointerpolation

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent

class TickListener(noInterpolation: NoInterpolation) : EventListener<TickEvent, NoInterpolation>(TickEvent::class.java, noInterpolation) {

    override fun invoke(e: Any) {
        module.i++
    }
}
