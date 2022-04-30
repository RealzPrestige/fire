package dev.zprestige.fire.module.movement.noslow

import dev.zprestige.fire.Main
import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.MoveEvent

class MoveListener(noSlow: NoSlow) : EventListener<MoveEvent, NoSlow>(MoveEvent::class.java, noSlow) {

    override fun invoke(e: Any) {
        if (module.webs.GetSwitch() && module.webbed() && module.mode.GetCombo().equals("Timer")) {
            Main.tickManager.setTimer(module.timer.GetSlider())
            module.timered = true
        }
    }
}