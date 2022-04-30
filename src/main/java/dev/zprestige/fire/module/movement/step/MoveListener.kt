package dev.zprestige.fire.module.movement.step

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.MoveEvent
import dev.zprestige.fire.util.impl.EntityUtil

class MoveListener(step: Step) : EventListener<MoveEvent, Step>(MoveEvent::class.java, step) {

    override fun invoke(e: Any){
        if (module.checkRiding()) {
            when (module.mode.GetCombo()) {
                "Vanilla" -> mc.player.stepHeight = module.height.GetSlider()
                "Ncp" -> if (module.canStep()) {
                    val height = module.height.GetSlider().toInt()
                    val i = EntityUtil.getSpeed(0.1)
                    if (module.checkEmpty(height, i)) {
                        module.performStep(height, i)
                    }
                }
            }
        }
    }
}
