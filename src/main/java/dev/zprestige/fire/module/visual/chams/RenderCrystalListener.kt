package dev.zprestige.fire.module.visual.chams

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.RenderCrystalEvent

class RenderCrystalListener(chams: Chams) : EventListener<RenderCrystalEvent, Chams>(
    RenderCrystalEvent::class.java, chams
) {
    override fun invoke(e: Any) {
        if (module.crystals.GetSwitch()) {
            val event = e as RenderCrystalEvent
            event.setCancelled()
            if (module.fill.GetSwitch()) {
                module.prepareFill(module.fillColor.GetColor())
                event.render()
                module.releaseFill()
            }
            if (module.outline.GetSwitch()) {
                module.prepareOutline(module.outlineColor.GetColor(), module.outlineWidth.GetSlider())
                event.render()
                module.releaseOutline()
            }
        }
    }
}