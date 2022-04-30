package dev.zprestige.fire.module.movement.blink

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.TickEvent

class TickListener(blink: Blink) : EventListener<TickEvent, Blink>(TickEvent::class.java, blink) {
    
    override fun invoke(`object`: Any) {
        if (module.mode.GetCombo() == "Pulse") {
            module.i++
        }
        if (module.i >= module.ticks.GetSlider()) {
            module.poll(true)
            module.spawnEntity()
            module.i = 0
        }
    }
}