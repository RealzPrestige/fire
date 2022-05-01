package dev.zprestige.fire.module.visual.chams

import dev.zprestige.fire.event.bus.EventListener
import dev.zprestige.fire.event.impl.RenderLivingBaseEvent
import net.minecraft.entity.player.EntityPlayer

class RenderLivingBaseListener(chams: Chams) : EventListener<RenderLivingBaseEvent, Chams>(RenderLivingBaseEvent::class.java, chams) {

    override fun invoke(`object`: Any) {
        if (module.players.GetSwitch()) {
            val event = `object` as RenderLivingBaseEvent
            if (event.entityLivingBase != mc.player && event.entityLivingBase.entityId != 696969696 && event.entityLivingBase is EntityPlayer) {
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
}