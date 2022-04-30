package dev.zprestige.fire.module.movement.speed

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.KeyEvent

class KeyListener(speed: Speed) : EventListener<KeyEvent, Speed>(KeyEvent::class.java, speed) {
    
    override fun invoke(e: Any) {
        val event = e as KeyEvent
        if (module.switchKey.GetKey() == event.key) {
            if (module.speedMode.GetCombo() == "Strafe") {
                module.speedMode.value = "OnGround"
            } else {
                module.speedMode.value = "Strafe"
            }
            module.sendSwitchMessage()
        }
    }
}